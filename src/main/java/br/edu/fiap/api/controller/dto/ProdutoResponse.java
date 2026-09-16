package br.edu.fiap.api.controller.dto;

import br.edu.fiap.api.entity.Produto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

/**
 * Representação pública devolvida pela API.
 *
 * @param id identificador do produto
 * @param nome nome do produto
 * @param preco preço do produto
 * @param ativo estado de ativação
 */
public record ProdutoResponse(
        @Schema(example = "1") Long id,
        @Schema(example = "Teclado mecânico") String nome,
        @Schema(example = "299.90") BigDecimal preco,
        @Schema(example = "true") boolean ativo) {

    /**
     * Converte a entidade de domínio para o contrato HTTP.
     *
     * @param produto entidade a converter
     * @return representação segura para o consumidor
     */
    public static ProdutoResponse de(Produto produto) {
        return new ProdutoResponse(
                produto.getId(),
                produto.getNome(),
                produto.getPreco(),
                produto.isAtivo());
    }
}
