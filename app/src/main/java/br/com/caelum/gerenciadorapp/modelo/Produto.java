package br.com.caelum.gerenciadorapp.modelo;

import java.io.Serializable;

/**
 * Created by matheus on 02/05/16.
 */
public class Produto implements Serializable {


    private Long id;
    private String nome;
    private String descricao;
    private Double preco;


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return nome;
    }
}
