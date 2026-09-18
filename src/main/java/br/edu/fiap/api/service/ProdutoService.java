package br.edu.fiap.api.service;

import br.edu.fiap.api.entity.Categoria;
import br.edu.fiap.api.entity.Produto;
import br.edu.fiap.api.exception.CategoriaNaoEncontradaException;
import br.edu.fiap.api.exception.ProdutoNaoEncontradoException;
import br.edu.fiap.api.repository.CategoriaRepository;
import br.edu.fiap.api.repository.ProdutoRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
public class ProdutoService {
    private final ProdutoRepository repository;
    private final CategoriaRepository categoriaRepository;

    public ProdutoService(ProdutoRepository repository, CategoriaRepository categoriaRepository) {
        this.repository = repository;
        this.categoriaRepository = categoriaRepository;
    }


    public List<Produto> listar() {
        return repository.findAll();
    }


    public Produto buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(id));
    }

    public List<Produto> listarPorCategoria(Long categoriaId){
        buscarCategoria(categoriaId);
        return repository.findByCategoriaId(categoriaId);
    }


    @Transactional
    public Produto criar(String nome, BigDecimal preco, boolean ativo, Long categoriaId) {
        return repository.save(new Produto(nome, preco, ativo, buscarCategoriaOpcional(categoriaId)));
    }


    @Transactional
    public Produto atualizar(Long id, String nome, BigDecimal preco, boolean ativo, Long categoriaId) {
        Produto produto = buscar(id);
        produto.atualizar(nome, preco, ativo, buscarCategoriaOpcional(categoriaId));
        return repository.save(produto);
    }

    @Transactional
    public void excluir(Long id) {
        repository.delete(buscar(id));
    }

    private Categoria buscarCategoriaOpcional(Long categoriaId){
        return categoriaId == null ? null : buscarCategoria(categoriaId);
    }

    private Categoria buscarCategoria(Long categoriaId){
        return categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new CategoriaNaoEncontradaException(categoriaId));
    }
}
