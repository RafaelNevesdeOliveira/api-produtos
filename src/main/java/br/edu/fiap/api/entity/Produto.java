package br.edu.fiap.api.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;

/**
 * Entidade de domínio que representa um produto persistido no PostgreSQL.
 *
 * <p>A classe concentra o estado e as mudanças permitidas no produto. As
 * anotações JPA descrevem apenas como esse estado é armazenado.</p>
 */
@Entity
@Table(name = "produtos")
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private BigDecimal preco;
    private boolean ativo;

    /**
     * Construtor exigido pelo JPA.
     */
    protected Produto() {
    }

    /**
     * Cria um produto ainda não persistido.
     *
     * @param nome nome apresentado pela API
     * @param preco preço de venda
     * @param ativo indica se o produto está ativo
     */
    public Produto(String nome, BigDecimal preco, boolean ativo) {
        this.nome = nome;
        this.preco = preco;
        this.ativo = ativo;
    }

    /**
     * Substitui os dados editáveis do produto.
     *
     * @param nome novo nome
     * @param preco novo preço
     * @param ativo novo estado de ativação
     */
    public void atualizar(String nome, BigDecimal preco, boolean ativo) {
        this.nome = nome;
        this.preco = preco;
        this.ativo = ativo;
    }

    /**
     * Obtém o identificador atribuído pelo banco.
     *
     * @return identificador atribuído pelo banco
     */
    public Long getId() {
        return id;
    }

    /**
     * Obtém o nome apresentado pela API.
     *
     * @return nome do produto
     */
    public String getNome() {
        return nome;
    }

    /**
     * Obtém o preço de venda do produto.
     *
     * @return preço do produto
     */
    public BigDecimal getPreco() {
        return preco;
    }

    /**
     * Informa se o produto está ativo.
     *
     * @return {@code true} quando o produto está ativo
     */
    public boolean isAtivo() {
        return ativo;
    }
}
