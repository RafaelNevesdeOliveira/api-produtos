package br.edu.fiap.api.produto.web;

import br.edu.fiap.api.produto.aplicacao.ProdutoService;
import br.edu.fiap.api.produto.dominio.Produto;
import br.edu.fiap.api.produto.web.dto.ProdutoRequest;
import br.edu.fiap.api.produto.web.dto.ProdutoResponse;
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

/**
 * Camada web que expõe o contrato HTTP de produtos.
 *
 * <p>O controller valida e converte dados de entrada e saída, delegando os
 * casos de uso à camada de aplicação.</p>
 */
@RestController
@RequestMapping("/api/produtos")
@Tag(name = "Produtos")
public class ProdutoController {
    private final ProdutoService service;

    /**
     * Cria o controller com o serviço de aplicação.
     *
     * @param service casos de uso de produto
     */
    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    /**
     * Lista os produtos.
     *
     * @return representações dos produtos cadastrados
     */
    @GetMapping
    @Operation(summary = "Listar produtos", description = "Retorna todos os produtos cadastrados.")
    @ApiResponse(responseCode = "200", description = "Lista recuperada com sucesso")
    public List<ProdutoResponse> listar() {
        return service.listar().stream().map(ProdutoResponse::de).toList();
    }

    /**
     * Busca um produto.
     *
     * @param id identificador recebido na URI
     * @return representação do produto
     */
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

    /**
     * Cria um produto e informa sua URI no cabeçalho {@code Location}.
     *
     * @param request corpo JSON validado
     * @return resposta 201 com o produto criado
     */
    @PostMapping
    @Operation(summary = "Criar produto")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Produto criado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<ProdutoResponse> criar(
            @Valid @RequestBody ProdutoRequest request) {
        Produto salvo = service.criar(request.nome(), request.preco(), request.ativo());
        URI localizacao = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(salvo.getId())
                .toUri();
        return ResponseEntity.created(localizacao).body(ProdutoResponse.de(salvo));
    }

    /**
     * Atualiza integralmente os dados editáveis de um produto.
     *
     * @param id identificador recebido na URI
     * @param request novo estado validado
     * @return representação atualizada
     */
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
                service.atualizar(id, request.nome(), request.preco(), request.ativo()));
    }

    /**
     * Exclui um produto.
     *
     * @param id identificador recebido na URI
     * @return resposta 204 sem corpo
     */
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
