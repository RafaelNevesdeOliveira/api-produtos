package br.edu.fiap.api.service;

import br.edu.fiap.api.entity.Categoria;
import br.edu.fiap.api.exception.CategoriaComProdutosException;
import br.edu.fiap.api.exception.CategoriaNaoEncontradaException;
import br.edu.fiap.api.repository.CategoriaRepository;
import br.edu.fiap.api.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CategoriaService {
    private final CategoriaRepository repository;
    private final ProdutoRepository produtoRepository;


    public CategoriaService(CategoriaRepository repository, ProdutoRepository produtoRepository) {
        this.repository = repository;
        this.produtoRepository = produtoRepository;
    }


    public List<Categoria> listar() {
        return repository.findAll();
    }


    public Categoria buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new CategoriaNaoEncontradaException(id));
    }


    @Transactional
    public Categoria criar(String nome, String descricao) {
        return repository.save(new Categoria(nome, descricao));
    }


    @Transactional
    public Categoria atualizar(Long id, String nome, String descricao) {
        Categoria categoria = buscar(id);
        categoria.atualizar(nome, descricao);
        return repository.save(categoria);
    }

    @Transactional
    public void excluir(Long id) {
        Categoria categoria = buscar(id);
        if (!produtoRepository.findByCategoriaId(id).isEmpty()) {
            throw new CategoriaComProdutosException(id);
        }
        repository.delete(categoria);
    }

}
