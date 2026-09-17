package br.edu.fiap.api.exception;

public class CategoriaNaoEncontradaException extends RuntimeException {

    /**
     * Cria a exceção com uma mensagem adequada para o contrato HTTP.
     *
     * @param id identificador procurado
     */
    public CategoriaNaoEncontradaException(Long id) {
        super("Categoria nao encontrada: " + id);
    }
    
}
