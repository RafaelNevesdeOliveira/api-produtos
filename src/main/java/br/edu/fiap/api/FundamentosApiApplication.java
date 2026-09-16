package br.edu.fiap.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Ponto de entrada da API didática de fundamentos.
 */
@SpringBootApplication
public class FundamentosApiApplication {
    /** Classe utilitária de inicialização; não deve ser instanciada. */
    private FundamentosApiApplication() {
    }

    /**
     * Inicializa o Spring Boot e o servidor HTTP incorporado.
     *
     * @param args argumentos opcionais de inicialização
     */
    public static void main(String[] args) {
        SpringApplication.run(FundamentosApiApplication.class, args);
    }
}
