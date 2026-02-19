package cat.itacademy.fruitapih2.service;

import cat.itacademy.fruitapih2.dto.FruitDto;
import cat.itacademy.fruitapih2.exception.FruitAlreadyExistsException;
import cat.itacademy.fruitapih2.exception.FruitNotFoundException;
import cat.itacademy.fruitapih2.mapper.FruitMapper;
import cat.itacademy.fruitapih2.model.Fruit;
import cat.itacademy.fruitapih2.repository.FruitRepository;
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
            throw new FruitAlreadyExistsException(fruitDto.name());
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
                .orElseThrow(() -> new FruitNotFoundException(id));
    }

    public FruitDto update(Long id, FruitDto fruitDto) {
        Fruit fruit = fruitRepository.findById(id).orElseThrow(() -> new FruitNotFoundException(id));
        fruit.setName(fruitDto.name());
        fruit.setWeightKg(fruitDto.weightKg());
        return FruitMapper.toDto(fruitRepository.save(fruit));
    }

    public void delete(Long id) {
        fruitRepository.findById(id).orElseThrow(() -> new FruitNotFoundException(id));
        fruitRepository.deleteById(id);
    }
}
