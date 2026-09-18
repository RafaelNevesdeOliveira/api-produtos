package br.edu.fiap.api.repository;

import br.edu.fiap.api.entity.Produto;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Porta de persistência dos produtos.
 *
 * <p>O Spring Data JPA cria a implementação em tempo de execução.</p>
 */
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    /**
     * @return produtos ja acompanhados da sua categoria
     * */
    @Override
    @EntityGraph(attributePaths = "categoria")
    List<Produto> findAll();

    /**
     * @return produto ja acompanhado da sua categoria
     * */
    @Override
    @EntityGraph(attributePaths = "categoria")
    Optional<Produto> findById(Long id);


    /**
     * @return produtos daquela categoria especifica
     * */
    @EntityGraph(attributePaths = "categoria")
    List<Produto> findByCategoriaId(Long categoriaId);
}
