package br.edu.fiap.api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração central dos metadados publicados no documento OpenAPI.
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Fundamentos de API com Java",
                version = "1.0.0",
                description = "API didática multicamadas com Spring Boot e PostgreSQL.",
                contact = @Contact(name = "FIAP - Aula de Fundamentos de API"),
                license = @License(name = "Uso didático")
        ),
        tags = {
                @Tag(name = "Produtos", description = "Operações CRUD de produtos"),
                @Tag(name = "Saúde", description = "Verificação de disponibilidade")
        }
)
public class OpenApiConfig {
    /** Cria a configuração baseada nas anotações OpenAPI desta classe. */
    public OpenApiConfig() {
    }
}
