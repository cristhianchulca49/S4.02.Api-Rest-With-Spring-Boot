package cat.itacademy.fruitapimysql.controller;

import cat.itacademy.fruitapimysql.dto.supplier.SupplierDtoRequest;
import cat.itacademy.fruitapimysql.dto.supplier.SupplierDtoResponse;
import cat.itacademy.fruitapimysql.service.SupplierService;
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
    ResponseEntity<SupplierDtoResponse> update(@PathVariable Long id, @Valid @RequestBody SupplierDtoRequest supplierDtoRequest) {
        return ResponseEntity.ok(supplierService.update(id, supplierDtoRequest));
    }

    @DeleteMapping("{id}")
    ResponseEntity<Void> delete(@PathVariable Long id) {
        supplierService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
