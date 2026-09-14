package br.edu.fiap.api.saude;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Expõe uma verificação simples de disponibilidade da aplicação.
 */
@RestController
@Tag(name = "Saúde")
public class SaudeController {
    /**
     * Informa que o processo Java está atendendo requisições.
     *
     * @return estado e nome do serviço
     */
    @GetMapping("/api/saude")
    @Operation(summary = "Verificar disponibilidade da API")
    @ApiResponse(responseCode = "200", description = "Aplicação disponível")
    public Map<String, String> verificar() {
        return Map.of("status", "UP", "servico", "fundamentos-api");
    }
}
