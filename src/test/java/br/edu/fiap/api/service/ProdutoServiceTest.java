package br.edu.fiap.api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import br.edu.fiap.api.entity.Produto;
import br.edu.fiap.api.exception.ProdutoNaoEncontradoException;
import br.edu.fiap.api.repository.ProdutoRepository;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Testes unitários da camada de aplicação, sem iniciar Spring ou PostgreSQL.
 */
class ProdutoServiceTest {
    private RepositorioFalso repositorioFalso;
    private ProdutoService service;

    @BeforeEach
    void preparar() {
        repositorioFalso = new RepositorioFalso();
        ProdutoRepository repository = (ProdutoRepository) Proxy.newProxyInstance(
                ProdutoRepository.class.getClassLoader(),
                new Class<?>[] {ProdutoRepository.class},
                repositorioFalso);
        service = new ProdutoService(repository);
    }

    @Test
    void deveListarProdutosDoRepositorio() {
        Produto produto = produto();
        repositorioFalso.todos = List.of(produto);

        List<Produto> encontrados = service.listar();

        assertThat(encontrados).containsExactly(produto);
    }

    @Test
    void deveCriarProduto() {
        Produto produto = produto();
        repositorioFalso.salvo = produto;

        Produto criado = service.criar("Teclado", new BigDecimal("299.90"), true);

        assertThat(criado).isSameAs(produto);
    }

    @Test
    void deveFalharAoBuscarIdInexistente() {
        assertThatThrownBy(() -> service.buscar(42L))
                .isInstanceOf(ProdutoNaoEncontradoException.class)
                .hasMessageContaining("42");
    }

    @Test
    void deveExcluirProdutoEncontrado() {
        Produto produto = produto();
        repositorioFalso.encontrado = Optional.of(produto);

        service.excluir(1L);

        assertThat(repositorioFalso.excluido).isSameAs(produto);
    }

    private Produto produto() {
        return new Produto("Teclado", new BigDecimal("299.90"), true);
    }

    /**
     * Implementação mínima do repository por proxy para manter o teste
     * independente de agentes de instrumentação da JVM.
     */
    private static final class RepositorioFalso implements InvocationHandler {
        private List<Produto> todos = List.of();
        private Optional<Produto> encontrado = Optional.empty();
        private Produto salvo;
        private Produto excluido;

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) {
            return switch (method.getName()) {
                case "findAll" -> todos;
                case "findById" -> encontrado;
                case "save" -> salvo;
                case "delete" -> {
                    excluido = (Produto) args[0];
                    yield null;
                }
                case "toString" -> "RepositorioFalso";
                default -> throw new UnsupportedOperationException(
                        "Operacao nao implementada no teste: " + method.getName());
            };
        }
    }
}
