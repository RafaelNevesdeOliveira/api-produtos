package br.edu.fiap.api.service;

import br.edu.fiap.api.entity.Produto;
import br.edu.fiap.api.exception.ProdutoNaoEncontradoException;
import br.edu.fiap.api.repository.ProdutoRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Camada de aplicação responsável pelos casos de uso de produto.
 *
 * <p>O serviço delimita transações e impede que o controller conheça detalhes
 * de persistência. Regras de negócio e coordenação entre repositórios devem
 * ficar nesta camada.</p>
 */
@Service
@Transactional(readOnly = true)
public class ProdutoService {
    private final ProdutoRepository repository;

    /**
     * Cria o serviço com sua dependência de persistência.
     *
     * @param repository repositório de produtos
     */
    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    /**
     * Lista todos os produtos cadastrados.
     *
     * @return produtos encontrados
     */
    public List<Produto> listar() {
        return repository.findAll();
    }

    /**
     * Busca um produto pelo identificador.
     *
     * @param id identificador do produto
     * @return produto encontrado
     * @throws ProdutoNaoEncontradoException quando o identificador não existe
     */
    public Produto buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(id));
    }

    /**
     * Cria e persiste um produto.
     *
     * @param nome nome do produto
     * @param preco preço do produto
     * @param ativo estado de ativação
     * @return produto persistido, com identificador
     */
    @Transactional
    public Produto criar(String nome, BigDecimal preco, boolean ativo) {
        return repository.save(new Produto(nome, preco, ativo));
    }

    /**
     * Atualiza integralmente os campos editáveis de um produto.
     *
     * @param id identificador do produto
     * @param nome novo nome
     * @param preco novo preço
     * @param ativo novo estado de ativação
     * @return produto atualizado
     * @throws ProdutoNaoEncontradoException quando o identificador não existe
     */
    @Transactional
    public Produto atualizar(Long id, String nome, BigDecimal preco, boolean ativo) {
        Produto produto = buscar(id);
        produto.atualizar(nome, preco, ativo);
        return repository.save(produto);
    }

    /**
     * Exclui um produto.
     *
     * @param id identificador do produto
     * @throws ProdutoNaoEncontradoException quando o identificador não existe
     */
    @Transactional
    public void excluir(Long id) {
        repository.delete(buscar(id));
    }
}
