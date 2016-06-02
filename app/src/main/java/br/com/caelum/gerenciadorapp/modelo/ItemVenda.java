package br.com.caelum.gerenciadorapp.modelo;

import java.io.Serializable;

/**
 * Created by matheus on 05/05/16.
 */
public class ItemVenda implements Serializable {

    private Long id;
    private Produto produto;
    private Integer quantidade;
    private Double valorUnitarioVendido;

    public ItemVenda(Produto produto, Integer quantidade, Double valorUnitarioVendido) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.valorUnitarioVendido = valorUnitarioVendido;
    }

    public Produto getProduto() {
        return produto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public Double getValorUnitarioVendido() {
        return valorUnitarioVendido;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return quantidade + " - " + produto.getNome() + " - R$ " + valorUnitarioVendido;
    }
}
