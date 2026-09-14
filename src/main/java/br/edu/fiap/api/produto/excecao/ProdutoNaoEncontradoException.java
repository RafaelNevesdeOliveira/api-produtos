package br.edu.fiap.api.produto.excecao;

/**
 * Indica que não existe produto com o identificador informado.
 */
public class ProdutoNaoEncontradoException extends RuntimeException {

    /**
     * Cria a exceção com uma mensagem adequada para o contrato HTTP.
     *
     * @param id identificador procurado
     */
    public ProdutoNaoEncontradoException(Long id) {
        super("Produto nao encontrado: " + id);
    }
}
