package cat.itacademy.fruitorderapimongo.service;

import cat.itacademy.fruitorderapimongo.infrastructure.dto.fruit.FruitDtoRequest;
import cat.itacademy.fruitorderapimongo.infrastructure.dto.fruit.FruitDtoResponse;
import cat.itacademy.fruitorderapimongo.domain.model.valueobject.Name;
import cat.itacademy.fruitorderapimongo.domain.model.valueobject.PricePerKg;
import cat.itacademy.fruitorderapimongo.infrastructure.exception.ResourceAlreadyExistsException;
import cat.itacademy.fruitorderapimongo.infrastructure.exception.ResourceNotFoundException;
import cat.itacademy.fruitorderapimongo.infrastructure.mapper.FruitMapper;
import cat.itacademy.fruitorderapimongo.domain.model.Fruit;
import cat.itacademy.fruitorderapimongo.domain.model.Supplier;
import cat.itacademy.fruitorderapimongo.infrastructure.adapter.out.persistence.FruitRepository;
import cat.itacademy.fruitorderapimongo.infrastructure.adapter.out.persistence.SupplierRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class FruitService {
    private final FruitRepository fruitRepository;
    private final SupplierRepository supplierRepository;

    public FruitService(FruitRepository fruitRepository, SupplierRepository supplierRepository) {
        this.fruitRepository = fruitRepository;
        this.supplierRepository = supplierRepository;
    }

    @Transactional
    public FruitDtoResponse createFruit(FruitDtoRequest fruitDtoRequest) {
        Name fruitName = Name.of(fruitDtoRequest.name());
        if (fruitRepository.existsByName(fruitName)) {
            throw new ResourceAlreadyExistsException("Fruit", fruitDtoRequest.name());
        }

        Supplier supplier = validateIfSupplierExists(fruitDtoRequest.supplierId());

        Fruit fruitSaved = fruitRepository.save(FruitMapper.toEntity(fruitDtoRequest, supplier));
        return FruitMapper.toDto(fruitSaved);
    }

    public List<FruitDtoResponse> getAll() {
        return fruitRepository.findAll().stream()
                .map(FruitMapper::toDto)
                .toList();
    }

    public FruitDtoResponse getById(String id) {
        return FruitMapper.toDto(validateIfFruitExists(id));
    }

    @Transactional
    public FruitDtoResponse update(String id, FruitDtoRequest fruitDtoRequest) {
        Fruit fruit = validateIfFruitExists(id);
        fruit.changeName(Name.of(fruitDtoRequest.name()));
        fruit.changePrice(PricePerKg.of(fruitDtoRequest.weightKg()));
        return FruitMapper.toDto(fruitRepository.save(fruit));
    }

    @Transactional
    public void delete(String id) {
        Fruit fruit = validateIfFruitExists(id);
        fruitRepository.delete(fruit);
    }

    private Fruit validateIfFruitExists(String id){
        return fruitRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Fruit", id));
    }

    private Supplier validateIfSupplierExists(String id){
        return supplierRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Supplier", id));
    }

    public List<FruitDtoResponse> getBySupplierId(String id) {
        Supplier supplier = validateIfSupplierExists(id);
        return fruitRepository.findAllBySupplier(supplier).stream()
                .map(FruitMapper::toDto)
                .toList();
    }
}
