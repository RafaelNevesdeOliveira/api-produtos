package br.edu.fiap.api.controller;

import br.edu.fiap.api.controller.dto.CategoriaRequest;
import br.edu.fiap.api.controller.dto.CategoriaResponse;
import br.edu.fiap.api.controller.dto.CategoriaResumoResponse;
import br.edu.fiap.api.entity.Categoria;
import br.edu.fiap.api.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/api/categorias")
@Tag(name = "Categorias")
public class CategoriaController {
    private final CategoriaService service;


    public CategoriaController(CategoriaService service) {
        this.service = service;
    }


    @GetMapping
    @Operation(summary = "Listar categorias")
    public List<CategoriaResponse> listar() {
        return service.listar().stream().map(CategoriaResponse::de).toList();
    }


    @GetMapping("/{id}")
    @Operation(summary = "Buscar categoria por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoria encontrada"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    })
    public CategoriaResponse buscar(
            @Parameter(example = "1") @PathVariable Long id) {
        return CategoriaResponse.de(service.buscar(id));
    }


    @PostMapping
    @Operation(summary = "Criar categoria")
    public ResponseEntity<CategoriaResponse> criar(@Valid @RequestBody CategoriaRequest request) {
        Categoria salvo = service.criar(request.nome(), request.descricao());
        URI localizacao = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(salvo.getId())
                .toUri();
        return ResponseEntity.created(localizacao).body(CategoriaResponse.de(salvo));
    }


    @PutMapping("/{id}")
    @Operation(summary = "Atualizar categoria")
    public CategoriaResponse atualizar(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaRequest request) {
        return CategoriaResponse.de(service.atualizar(id, request.nome(), request.descricao()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir categoria sem produtos")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Categoria excluída"),
            @ApiResponse(responseCode = "409", description = "Categoria possui produtos")
    })
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
