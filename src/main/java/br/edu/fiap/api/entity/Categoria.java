package br.edu.fiap.api.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidade que agrupa produtos por uma classificação de negócio.
 *
 * <p><strong>Camada:</strong> {@code entity}. Uma entidade JPA é o objeto
 * Java que mapeia uma tabela; esta classe representa {@code categorias} e
 * guarda o estado persistido de uma categoria.</p>
 *
 * <p>O relacionamento com {@code Produto} é armazenado no lado N, isto é,
 * na coluna {@code produtos.categoria_id}. Por isso esta classe não precisa
 * manter uma coleção de produtos para realizar o CRUD de categorias.</p>
 */
@Entity
@Table(name = "categorias")
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String descricao;

    /** Construtor exigido pelo JPA. */
    protected Categoria() {
    }

    /**
     * Cria uma categoria ainda não persistida.
     *
     * @param nome nome apresentado aos consumidores da API
     * @param descricao explicação opcional da categoria
     */
    public Categoria(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    /**
     * Atualiza os campos editáveis da categoria.
     *
     * @param nome novo nome
     * @param descricao nova descrição
     */
    public void atualizar(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    /**
     * Obtém o identificador atribuído pelo banco.
     *
     * @return identificador da categoria
     */
    public Long getId() {
        return id;
    }

    /**
     * Obtém o nome da categoria.
     *
     * @return nome da categoria
     */
    public String getNome() {
        return nome;
    }

    /**
     * Obtém a descrição opcional.
     *
     * @return descrição da categoria
     */
    public String getDescricao() {
        return descricao;
    }
}
