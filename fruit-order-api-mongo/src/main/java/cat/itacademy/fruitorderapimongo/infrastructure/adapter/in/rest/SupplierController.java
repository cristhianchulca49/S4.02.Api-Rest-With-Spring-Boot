package cat.itacademy.fruitorderapimongo.infrastructure.adapter.in.rest;

import cat.itacademy.fruitorderapimongo.infrastructure.dto.supplier.SupplierDtoRequest;
import cat.itacademy.fruitorderapimongo.infrastructure.dto.supplier.SupplierDtoResponse;
import cat.itacademy.fruitorderapimongo.service.SupplierService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/suppliers")
public class SupplierController {
    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @PostMapping
    public ResponseEntity<SupplierDtoResponse> create(@Valid @RequestBody SupplierDtoRequest supplierDtoRequest) {
        SupplierDtoResponse createdSupplier = supplierService.create(supplierDtoRequest);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdSupplier.id())
                .toUri();
        return ResponseEntity.created(location).body(createdSupplier);
    }

    @PutMapping("{id}")
    ResponseEntity<SupplierDtoResponse> update(@PathVariable String id, @Valid @RequestBody SupplierDtoRequest supplierDtoRequest) {
        return ResponseEntity.ok(supplierService.update(id, supplierDtoRequest));
    }

    @DeleteMapping("{id}")
    ResponseEntity<Void> delete(@PathVariable String id) {
        supplierService.delete(id);
        return ResponseEntity.noContent().build();
    }
}