package cat.itacademy.fruitorderapimongo.infrastructure.adapter.in.rest;

import cat.itacademy.fruitorderapimongo.domain.model.Supplier;
import cat.itacademy.fruitorderapimongo.application.usecase.supplier.CreateSupplierUseCase;
import cat.itacademy.fruitorderapimongo.application.usecase.supplier.DeleteSupplierUseCase;
import cat.itacademy.fruitorderapimongo.application.usecase.supplier.GetAllSuppliersUseCase;
import cat.itacademy.fruitorderapimongo.application.usecase.supplier.GetSupplierByIdUseCase;
import cat.itacademy.fruitorderapimongo.application.usecase.supplier.UpdateSupplierUseCase;
import cat.itacademy.fruitorderapimongo.application.dto.supplier.SupplierDtoRequest;
import cat.itacademy.fruitorderapimongo.application.dto.supplier.SupplierDtoResponse;
import cat.itacademy.fruitorderapimongo.application.mapper.SupplierMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/suppliers")
public class SupplierController {
    private final CreateSupplierUseCase createSupplierUseCase;
    private final GetAllSuppliersUseCase getAllSuppliersUseCase;
    private final GetSupplierByIdUseCase getSupplierByIdUseCase;
    private final UpdateSupplierUseCase updateSupplierUseCase;
    private final DeleteSupplierUseCase deleteSupplierUseCase;

    public SupplierController(CreateSupplierUseCase createSupplierUseCase,
                              GetAllSuppliersUseCase getAllSuppliersUseCase,
                              GetSupplierByIdUseCase getSupplierByIdUseCase,
                              UpdateSupplierUseCase updateSupplierUseCase,
                              DeleteSupplierUseCase deleteSupplierUseCase) {
        this.createSupplierUseCase = createSupplierUseCase;
        this.getAllSuppliersUseCase = getAllSuppliersUseCase;
        this.getSupplierByIdUseCase = getSupplierByIdUseCase;
        this.updateSupplierUseCase = updateSupplierUseCase;
        this.deleteSupplierUseCase = deleteSupplierUseCase;
    }

    @PostMapping
    public ResponseEntity<SupplierDtoResponse> create(@Valid @RequestBody SupplierDtoRequest supplierDtoRequest) {
        Supplier createdSupplier = createSupplierUseCase.execute(supplierDtoRequest.name(), supplierDtoRequest.country());
        SupplierDtoResponse response = SupplierMapper.toDto(createdSupplier);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    ResponseEntity<List<SupplierDtoResponse>> getAll() {
        return ResponseEntity.ok(getAllSuppliersUseCase.execute().stream()
                .map(SupplierMapper::toDto)
                .toList());
    }

    @GetMapping("/{id}")
    ResponseEntity<SupplierDtoResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(SupplierMapper.toDto(getSupplierByIdUseCase.execute(id)));
    }

    @PutMapping("{id}")
    ResponseEntity<SupplierDtoResponse> update(@PathVariable String id, @Valid @RequestBody SupplierDtoRequest supplierDtoRequest) {
        return ResponseEntity.ok(SupplierMapper.toDto(updateSupplierUseCase.execute(id, supplierDtoRequest.name(), supplierDtoRequest.country())));
    }

    @DeleteMapping("{id}")
    ResponseEntity<Void> delete(@PathVariable String id) {
        deleteSupplierUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
