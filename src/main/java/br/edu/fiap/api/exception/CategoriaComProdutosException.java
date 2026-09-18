package br.edu.fiap.api.exception;

public class CategoriaComProdutosException extends RuntimeException {

    public CategoriaComProdutosException(Long id) {
        super("Categoria possui produtos relacionados: " + id);
    }
}
