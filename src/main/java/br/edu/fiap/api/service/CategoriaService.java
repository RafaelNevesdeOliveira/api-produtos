package br.edu.fiap.api.service;

import java.util.List;
import org.springframework.stereotype.Service;
import br.edu.fiap.api.entity.Categoria;
import br.edu.fiap.api.exception.CategoriaNaoEncontradaException;
import br.edu.fiap.api.repository.CategoriaRepository;
import org.springframework.transaction.annotation.Transactional;

@Service 
@Transactional(readOnly = true) 
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    /**
     * Cria o serviço com sua dependência de persistência.
     *
     * @param categoriaRepository repositório de categorias
     */
    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    /**
     * Lista todas as categorias cadastradas.
     *
     * @return categorias encontradas
     */
    @Transactional
    public List<Categoria> listar() {
        return categoriaRepository.findAll();
    }

    /**
     * Busca uma categoria pelo identificador.
     *
     * @param id identificador da categoria
     * @return categoria encontrada
     * @throws CategoriaNaoEncontradaException quando o identificador não existe
     */
    @Transactional 
    public Categoria buscar(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new CategoriaNaoEncontradaException(id));
    }

    /**
     * Cria e persiste uma categoria.
     *
     * @param nome nome da categoria
     * @param descricao descrição da categoria
     * @return categoria persistida, com identificador
     */
    @Transactional
    public Categoria criar(String nome, String descricao) {
        return categoriaRepository.save(new Categoria(nome, descricao));
    }

    /**
     * Atualiza uma categoria existente.
     *
     * @param id identificador da categoria
     * @param nome novo nome da categoria
     * @param descricao nova descrição da categoria
     * @return categoria atualizada
     * @throws CategoriaNaoEncontradaException quando o identificador não existe
     */
    @Transactional
    public Categoria atualizar(Long id, String nome, String descricao) {
        Categoria categoria = buscar(id);
        categoria.atualizar(nome, descricao);
        return categoriaRepository.save(categoria);
    }

    /**
     * Remove uma categoria existente.
     *
     * @param id identificador da categoria
     * @throws CategoriaNaoEncontradaException quando o identificador não existe
     */
    @Transactional
    public void excluir(Long id) {
        categoriaRepository.delete(buscar(id));
    }

    
}
