package br.edu.fiap.api.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


/**
 * Dados aceitos nas operações de criação e atualização.
 *
 * @param nome nome obrigatório com até 120 caracteres
 * @param descricao descrição obrigatória com até 255 caracteres
 */
public record CategoriaRequest (
    @Schema(description = "Nome da categoria", example = "Eletrônicos")
    @NotBlank @Size(max = 120) String nome,
    @Schema(description = "Descrição da categoria", example = "Produtos eletrônicos e acessórios")
    @NotBlank @Size(max = 255) String descricao
){   
}
