package br.edu.fiap.api.service;

import br.edu.fiap.api.entity.Categoria;
import br.edu.fiap.api.exception.CategoriaNaoEncontradaException;
import br.edu.fiap.api.repository.CategoriaRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CategoriaService {
    private final CategoriaRepository repository;

    public CategoriaService(CategoriaRepository repository) {
        this.repository = repository;
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
        repository.delete(buscar(id));
    }
}
