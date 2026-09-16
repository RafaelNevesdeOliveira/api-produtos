package br.edu.fiap.api.repository;

import br.edu.fiap.api.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Porta de persistência dos produtos.
 *
 * <p>O Spring Data JPA cria a implementação em tempo de execução.</p>
 */
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
