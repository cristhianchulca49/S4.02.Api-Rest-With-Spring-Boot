package cat.itacademy.fruitapimysql.service;

import cat.itacademy.fruitapimysql.dto.FruitDto;
import cat.itacademy.fruitapimysql.exception.ResourceAlreadyExistsException;
import cat.itacademy.fruitapimysql.exception.ResourceNotFoundException;
import cat.itacademy.fruitapimysql.mapper.FruitMapper;
import cat.itacademy.fruitapimysql.model.Fruit;
import cat.itacademy.fruitapimysql.repository.FruitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FruitService {
    private final FruitRepository fruitRepository;

    public FruitService(FruitRepository fruitRepository) {
        this.fruitRepository = fruitRepository;
    }

    @Transactional
    public FruitDto createFruit(FruitDto fruitDto) {
        if (fruitRepository.existsByName(fruitDto.name())) {
            throw new ResourceAlreadyExistsException("Fruit", fruitDto.name());
        }

        Fruit fruitSaved = fruitRepository.save(FruitMapper.toEntity(fruitDto));
        return FruitMapper.toDto(fruitSaved);
    }

    public List<FruitDto> getAll() {
        return fruitRepository.findAll().stream()
                .map(FruitMapper::toDto)
                .toList();
    }

    public FruitDto getById(Long id) {
        return fruitRepository.findById(id)
                .map(FruitMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public FruitDto update(Long id, FruitDto fruitDto) {
        Fruit fruit = fruitRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        fruit.setName(fruitDto.name());
        fruit.setWeightKg(fruitDto.weightKg());
        return FruitMapper.toDto(fruitRepository.save(fruit));
    }

    public void delete(Long id) {
        fruitRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        fruitRepository.deleteById(id);
    }
}
