package cat.itacademy.fruitapimysql.controller;

import cat.itacademy.fruitapimysql.dto.FruitDto;
import cat.itacademy.fruitapimysql.service.FruitService;
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

    @PutMapping("/{id}")
    ResponseEntity<FruitDto> update(@PathVariable("id") Long id, @Valid @RequestBody FruitDto fruitDto) {
        return ResponseEntity.ok(fruitService.update(id, fruitDto));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable("id") Long id){
        fruitService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
