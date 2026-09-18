package br.edu.fiap.api.exception;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Traduz exceções da aplicação para respostas HTTP padronizadas.
 */
@RestControllerAdvice
public class ApiExceptionHandler {
    /** Cria o tradutor de exceções usado por todos os controllers. */
    public ApiExceptionHandler() {
    }

    /**
     * Converte a ausência de um produto em {@code 404 Not Found}.
     *
     * @param erro exceção lançada pela camada de aplicação
     * @return corpo de erro padronizado
     */
    @ExceptionHandler(ProdutoNaoEncontradoException.class)
    ResponseEntity<Map<String, Object>> naoEncontrado(ProdutoNaoEncontradoException erro) {
        return resposta(HttpStatus.NOT_FOUND, erro.getMessage());
    }

    @ExceptionHandler(CategoriaNaoEncontradaException.class)
    ResponseEntity<Map<String, Object>> categoriaNaoEncontrada(CategoriaNaoEncontradaException erro) {
        return resposta(HttpStatus.NOT_FOUND, erro.getMessage());
    }

    /**
     * Converte falhas de Bean Validation em {@code 400 Bad Request}.
     *
     * @param erro detalhes da validação
     * @return corpo de erro padronizado
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<Map<String, Object>> dadosInvalidos(MethodArgumentNotValidException erro) {
        String detalhe = erro.getBindingResult().getFieldErrors().stream()
                .map(campo -> campo.getField() + ": " + campo.getDefaultMessage())
                .findFirst().orElse("Dados invalidos");
        return resposta(HttpStatus.BAD_REQUEST, detalhe);
    }

    private ResponseEntity<Map<String, Object>> resposta(HttpStatus status, String detalhe) {
        Map<String, Object> corpo = new LinkedHashMap<>();
        corpo.put("timestamp", Instant.now());
        corpo.put("status", status.value());
        corpo.put("erro", status.getReasonPhrase());
        corpo.put("detalhe", detalhe);
        return ResponseEntity.status(status).body(corpo);
    }
}
