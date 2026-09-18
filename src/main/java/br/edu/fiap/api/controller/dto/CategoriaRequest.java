package br.edu.fiap.api.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoriaRequest(
        @Schema(example = "Perifericos")
        @NotBlank @Size(max = 80) String nome,
        @Schema(example = "Acessorios para computadores")
        @Size(max = 255) String descricao
) {
}
