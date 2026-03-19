package cat.itacademy.fruitorderapimongo.infrastructure.adapter.in.rest;

import cat.itacademy.fruitorderapimongo.domain.model.Fruit;
import cat.itacademy.fruitorderapimongo.domain.usecase.fruit.CreateFruitUseCase;
import cat.itacademy.fruitorderapimongo.domain.usecase.fruit.DeleteFruitUseCase;
import cat.itacademy.fruitorderapimongo.domain.usecase.fruit.GetAllFruitsUseCase;
import cat.itacademy.fruitorderapimongo.domain.usecase.fruit.GetFruitByIdUseCase;
import cat.itacademy.fruitorderapimongo.domain.usecase.fruit.UpdateFruitUseCase;
import cat.itacademy.fruitorderapimongo.infrastructure.dto.fruit.FruitDtoRequest;
import cat.itacademy.fruitorderapimongo.infrastructure.dto.fruit.FruitDtoResponse;
import cat.itacademy.fruitorderapimongo.infrastructure.mapper.FruitMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/fruits")
public class FruitController {
    private final CreateFruitUseCase createFruitUseCase;
    private final GetAllFruitsUseCase getAllFruitsUseCase;
    private final GetFruitByIdUseCase getFruitByIdUseCase;
    private final UpdateFruitUseCase updateFruitUseCase;
    private final DeleteFruitUseCase deleteFruitUseCase;

    public FruitController(CreateFruitUseCase createFruitUseCase,
                           GetAllFruitsUseCase getAllFruitsUseCase,
                           GetFruitByIdUseCase getFruitByIdUseCase,
                           UpdateFruitUseCase updateFruitUseCase,
                           DeleteFruitUseCase deleteFruitUseCase) {
        this.createFruitUseCase = createFruitUseCase;
        this.getAllFruitsUseCase = getAllFruitsUseCase;
        this.getFruitByIdUseCase = getFruitByIdUseCase;
        this.updateFruitUseCase = updateFruitUseCase;
        this.deleteFruitUseCase = deleteFruitUseCase;
    }

    @PostMapping
    ResponseEntity<FruitDtoResponse> create(@Valid @RequestBody FruitDtoRequest fruitDtoRequest) {
        Fruit createdFruit = createFruitUseCase.execute(fruitDtoRequest);
        FruitDtoResponse response = FruitMapper.toDto(createdFruit);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    ResponseEntity<List<FruitDtoResponse>> getAll(@RequestParam(required = false) String supplierId) {
        return ResponseEntity.ok(getAllFruitsUseCase.execute(supplierId).stream()
                .map(FruitMapper::toDto)
                .toList());
    }

    @GetMapping("/{id}")
    ResponseEntity<FruitDtoResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(FruitMapper.toDto(getFruitByIdUseCase.execute(id)));
    }

    @PutMapping("/{id}")
    ResponseEntity<FruitDtoResponse> update(@PathVariable String id, @Valid @RequestBody FruitDtoRequest fruitDtoRequest) {
        return ResponseEntity.ok(FruitMapper.toDto(updateFruitUseCase.execute(id, fruitDtoRequest)));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable String id) {
        deleteFruitUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
