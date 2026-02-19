package cat.itacademy.fruitapih2.controller;

import cat.itacademy.fruitapih2.dto.FruitDto;
import cat.itacademy.fruitapih2.service.FruitService;
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
    ResponseEntity<FruitDto> create(@Valid @RequestBody FruitDto fruitDto) {
        FruitDto createdFruit = fruitService.createFruit(fruitDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdFruit.id())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(createdFruit);
    }

    @GetMapping
    ResponseEntity<List<FruitDto>> getAll(){
        return ResponseEntity.ok(fruitService.getAll());
    }

    @GetMapping("/{id}")
    ResponseEntity<FruitDto> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(fruitService.getById(id));
    }
}
