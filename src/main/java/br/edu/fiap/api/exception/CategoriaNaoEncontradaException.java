package br.edu.fiap.api.exception;

public class CategoriaNaoEncontradaException extends RuntimeException {
    public CategoriaNaoEncontradaException(Long id) {
        super("Categoria nao encontrada: " + id);
    }
}
