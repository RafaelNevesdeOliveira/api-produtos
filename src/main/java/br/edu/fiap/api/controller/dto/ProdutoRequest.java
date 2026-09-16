package br.edu.fiap.api.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * Dados aceitos nas operações de criação e atualização.
 *
 * @param nome nome obrigatório com até 120 caracteres
 * @param preco preço mínimo de 0,01
 * @param ativo estado de ativação
 */
public record ProdutoRequest(
        @Schema(description = "Nome do produto", example = "Teclado mecânico")
        @NotBlank @Size(max = 120) String nome,
        @Schema(description = "Preço de venda", example = "299.90")
        @NotNull @DecimalMin(value = "0.01") BigDecimal preco,
        @Schema(description = "Indica se o produto está disponível", example = "true")
        boolean ativo
) {
}
