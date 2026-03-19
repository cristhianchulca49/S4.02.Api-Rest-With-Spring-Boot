package cat.itacademy.fruitorderapimongo.infrastructure.adapter.in.rest;

import cat.itacademy.fruitorderapimongo.infrastructure.dto.fruit.FruitDtoRequest;
import cat.itacademy.fruitorderapimongo.infrastructure.dto.fruit.FruitDtoResponse;
import cat.itacademy.fruitorderapimongo.service.FruitService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/fruits")
public class FruitController {
    private final FruitService fruitService;

    public FruitController(FruitService fruitService) {
        this.fruitService = fruitService;
    }

    @PostMapping
    ResponseEntity<FruitDtoResponse> create(@Valid @RequestBody FruitDtoRequest fruitDtoRequest) {
        FruitDtoResponse createdFruit = fruitService.createFruit(fruitDtoRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdFruit.id()).toUri();

        return ResponseEntity.created(location).body(createdFruit);
    }

    @GetMapping
    ResponseEntity<List<FruitDtoResponse>> getAll(@RequestParam(required = false) String supplierId) {
        if(supplierId != null) {
            return ResponseEntity.ok(fruitService.getBySupplierId(supplierId));
        }
        return ResponseEntity.ok(fruitService.getAll());
    }

    @GetMapping("/{id}")
    ResponseEntity<FruitDtoResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(fruitService.getById(id));
    }

    @PutMapping("/{id}")
    ResponseEntity<FruitDtoResponse> update(@PathVariable String id, @Valid @RequestBody FruitDtoRequest fruitDtoRequest) {
        return ResponseEntity.ok(fruitService.update(id, fruitDtoRequest));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable String id){
        fruitService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
