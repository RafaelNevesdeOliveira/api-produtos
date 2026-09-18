package br.edu.fiap.api.controller.dto;

import br.edu.fiap.api.entity.Categoria;
import io.swagger.v3.oas.annotations.media.Schema;

public record CategoriaResponse(
        @Schema(example = "1") Long id,
        @Schema(example = "Perifericos") String nome,
        @Schema(example = "Acessorios para computadores") String descricao
) {
    public static CategoriaResponse de(Categoria categoria) {
        return new CategoriaResponse(categoria.getId(), categoria.getNome(), categoria.getDescricao());
    }
}
