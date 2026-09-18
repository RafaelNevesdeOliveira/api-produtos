package br.edu.fiap.api.controller;

import br.edu.fiap.api.controller.dto.ProdutoRequest;
import br.edu.fiap.api.controller.dto.ProdutoResponse;
import br.edu.fiap.api.entity.Produto;
import br.edu.fiap.api.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


@RestController
@RequestMapping("/api/produtos")
@Tag(name = "Produtos")
public class ProdutoController {
    private final ProdutoService service;

    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar produtos", description = "Retorna todos os produtos cadastrados.")
    @ApiResponse(responseCode = "200", description = "Lista recuperada com sucesso")
    public List<ProdutoResponse> listar() {
        return service.listar().stream().map(ProdutoResponse::de).toList();
    }


    @GetMapping("/{id}")
    @Operation(summary = "Buscar produto por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto encontrado"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ProdutoResponse buscar(
            @Parameter(description = "Identificador do produto", example = "1")
            @PathVariable Long id) {
        return ProdutoResponse.de(service.buscar(id));
    }

    @GetMapping("/categoria/{categoriaId}")
    @Operation(summary = "Listar produtos por categoria")
    public List<ProdutoResponse> listarPorCategoria(@PathVariable long categoriaId){
        return service.listarPorCategoria(categoriaId).stream().map(ProdutoResponse::de).toList();
    }


    @PostMapping
    @Operation(summary = "Criar produto")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Produto criado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<ProdutoResponse> criar(
            @Valid @RequestBody ProdutoRequest request) {
        Produto salvo = service.criar(request.nome(), request.preco(), request.ativo(), request.categoriaId());
        URI localizacao = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(salvo.getId())
                .toUri();
        return ResponseEntity.created(localizacao).body(ProdutoResponse.de(salvo));
    }


    @PutMapping("/{id}")
    @Operation(summary = "Atualizar produto")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto atualizado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ProdutoResponse atualizar(
            @Parameter(description = "Identificador do produto", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody ProdutoRequest request) {
        return ProdutoResponse.de(
                service.atualizar(id, request.nome(), request.preco(), request.ativo(), request.categoriaId()));
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir produto")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Produto excluído"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<Void> excluir(
            @Parameter(description = "Identificador do produto", example = "1")
            @PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
