package cat.itacademy.fruitapimysql.service;

import cat.itacademy.fruitapimysql.dto.fruit.FruitDtoRequest;
import cat.itacademy.fruitapimysql.dto.fruit.FruitDtoResponse;
import cat.itacademy.fruitapimysql.exception.ResourceAlreadyExistsException;
import cat.itacademy.fruitapimysql.exception.ResourceNotFoundException;
import cat.itacademy.fruitapimysql.mapper.FruitMapper;
import cat.itacademy.fruitapimysql.model.Fruit;
import cat.itacademy.fruitapimysql.repository.FruitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class FruitService {
    private final FruitRepository fruitRepository;

    public FruitService(FruitRepository fruitRepository) {
        this.fruitRepository = fruitRepository;
    }

    @Transactional
    public FruitDtoResponse createFruit(FruitDtoRequest fruitDtoRequest) {
        if (fruitRepository.existsByName(fruitDtoRequest.name())) {
            throw new ResourceAlreadyExistsException("Fruit", fruitDtoRequest.name());
        }

        Fruit fruitSaved = fruitRepository.save(FruitMapper.toEntity(fruitDtoRequest));
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
