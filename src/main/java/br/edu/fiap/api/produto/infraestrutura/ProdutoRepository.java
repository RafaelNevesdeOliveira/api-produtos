package br.edu.fiap.api.produto.infraestrutura;

import br.edu.fiap.api.produto.dominio.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Porta de persistência dos produtos.
 *
 * <p>O Spring Data JPA cria a implementação em tempo de execução.</p>
 */
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
