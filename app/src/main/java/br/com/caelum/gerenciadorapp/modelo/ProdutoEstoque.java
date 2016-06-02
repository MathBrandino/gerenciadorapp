package br.com.caelum.gerenciadorapp.modelo;

import java.io.Serializable;

/**
 * Created by matheus on 04/05/16.
 */
public class ProdutoEstoque implements Serializable {

    private Integer quantidade;
    private Long id;
    private Long idProduto;

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Long idProduto) {
        this.idProduto = idProduto;
    }
}
