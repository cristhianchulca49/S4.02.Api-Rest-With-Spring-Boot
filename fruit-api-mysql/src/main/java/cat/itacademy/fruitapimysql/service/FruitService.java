package cat.itacademy.fruitapimysql.service;

import cat.itacademy.fruitapimysql.dto.fruit.FruitDtoRequest;
import cat.itacademy.fruitapimysql.dto.fruit.FruitDtoResponse;
import cat.itacademy.fruitapimysql.exception.ResourceAlreadyExistsException;
import cat.itacademy.fruitapimysql.exception.ResourceNotFoundException;
import cat.itacademy.fruitapimysql.mapper.FruitMapper;
import cat.itacademy.fruitapimysql.model.Fruit;
import cat.itacademy.fruitapimysql.model.Supplier;
import cat.itacademy.fruitapimysql.repository.FruitRepository;
import cat.itacademy.fruitapimysql.repository.SupplierRepository;
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
        if (fruitRepository.existsByName(fruitDtoRequest.name())) {
            throw new ResourceAlreadyExistsException("Fruit", fruitDtoRequest.name());
        }

        Supplier supplier = supplierRepository.findById(fruitDtoRequest.supplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", fruitDtoRequest.supplierId()));

        Fruit fruitSaved = fruitRepository.save(FruitMapper.toEntity(fruitDtoRequest, supplier));
        return FruitMapper.toDto(fruitSaved);
    }

    public List<FruitDtoResponse> getAll() {
        return fruitRepository.findAll().stream()
                .map(FruitMapper::toDto)
                .toList();
    }

    public FruitDtoResponse getById(Long id) {
        return FruitMapper.toDto(validateIfFruitExists(id));
    }

    @Transactional
    public FruitDtoResponse update(Long id, FruitDtoRequest fruitDtoRequest) {
        Fruit fruit = validateIfFruitExists(id);
        fruit.setName(fruitDtoRequest.name());
        fruit.setWeightKg(fruitDtoRequest.weightKg());
        return FruitMapper.toDto(fruitRepository.save(fruit));
    }

    @Transactional
    public void delete(Long id) {
        validateIfFruitExists(id);
        fruitRepository.deleteById(id);
    }

    private Fruit validateIfFruitExists(Long id){
        return fruitRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Fruit", id));
    }
}
