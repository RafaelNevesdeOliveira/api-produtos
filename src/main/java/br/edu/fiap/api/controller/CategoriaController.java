package br.edu.fiap.api.controller;

import br.edu.fiap.api.controller.dto.CategoriaRequest;
import br.edu.fiap.api.controller.dto.CategoriaResponse;
import br.edu.fiap.api.entity.Categoria;
import br.edu.fiap.api.service.CategoriaService;

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
 * Camada web que expõe o contrato HTTP de categorias.
 *
 * <p>O controller valida e converte dados de entrada e saída, delegando os
 * casos de uso à camada de aplicação.</p>
 */
@RestController 
@RequestMapping ("/api/categorias")
@Tag (name = "Categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    /**
     * Cria o controller com o serviço de aplicação.
     *
     * @param categoriaService casos de uso de categoria
     */
    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    /**
     * Lista as categorias.
     *
     * @return representações das categorias cadastradas
     */
    @GetMapping 
    @Operation (summary = "Listar categorias", description = "Retorna todas as categorias cadastradas.")
    @ApiResponse (responseCode = "200", description = "Lista recuperada com sucesso")
    public List<CategoriaResponse> listar() {
        return categoriaService.listar().stream().map(CategoriaResponse::de).toList();
    }

    /**
     * Busca uma categoria.
     *
     * @param id identificador da categoria
     * @return representação da categoria encontrada
     */
    @GetMapping("/{id}")
    @Operation (summary = "Buscar categoria por ID", description = "Retorna a categoria correspondente ao identificador informado.")
    @ApiResponses ({
        @ApiResponse (responseCode = "200", description = "Categoria encontrada com sucesso"),
        @ApiResponse (responseCode = "404", description = "Categoria não encontrada")
    })
    public CategoriaResponse buscar(
        @Parameter (description = "Identificador da categoria", example = "1")
        @PathVariable Long id) {
        
        return CategoriaResponse.de(categoriaService.buscar(id));
    }


    /**
     * Cria uma categoria.
     *
     * @param request dados da categoria a ser criada
     * @return representação da categoria criada
     */
    @PostMapping
    @Operation (summary = "Criar categoria", description = "Cria uma nova categoria e retorna sua representação.")
    @ApiResponses ({
        @ApiResponse (responseCode = "201", description = "Categoria criada com sucesso"),
        @ApiResponse (responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<CategoriaResponse> criar(
        @Valid @RequestBody CategoriaRequest request) {
        Categoria categoriaSalva = categoriaService.criar(request.nome(), request.descricao());
        URI localizacao = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(categoriaSalva.getId())
                .toUri();
        return ResponseEntity.created(localizacao).body(CategoriaResponse.de(categoriaSalva));
    }

    /**
     * Atualiza uma categoria.
     *
     * @param id identificador da categoria a ser atualizada
     * @param request dados da categoria a ser atualizada
     * @return representação da categoria atualizada
     */
    @PutMapping ("/{id}")
    @Operation (summary = "Atualizar categoria", description = "Atualiza uma categoria existente")
    @ApiResponses ({
        @ApiResponse (responseCode = "200", description = "Categoria atualizada com sucesso"),
        @ApiResponse (responseCode = "400", description = "Dados inválidos"),
        @ApiResponse (responseCode = "404", description = "Categoria não encontrada")
    })
    public CategoriaResponse atualizar(
        @Parameter (description = "Identificador da categoria", example = "1")
        @PathVariable Long id,
        @Valid @RequestBody CategoriaRequest request) {

        return CategoriaResponse.de(
            categoriaService.atualizar(id, request.nome(), request.descricao()));
    }


    /**
     * Exclui uma categoria.
     *
     * @param id identificador da categoria recebido na URI
     * @return resposta 204 sem corpo
     */
    @DeleteMapping ("/{id}")
    @Operation (summary = "Excluir categoria", description = "Exclui uma categoria existente")
    @ApiResponses ({
        @ApiResponse (responseCode = "204", description = "Categoria excluída com sucesso"),
        @ApiResponse (responseCode = "404", description = "Categoria não encontrada")
    })
    public ResponseEntity<Void> excluir(
        @Parameter (description = "Identificador da categoria", example = "1")
        @PathVariable Long id) {
        categoriaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
    
}
