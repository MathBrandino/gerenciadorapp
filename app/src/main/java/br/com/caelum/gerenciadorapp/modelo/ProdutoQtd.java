package br.com.caelum.gerenciadorapp.modelo;

import java.io.Serializable;

/**
 * Created by matheus on 02/06/16.
 */
public class ProdutoQtd implements Serializable {


    private Long idProduto;
    private Long qtd;


    public Long getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Long idProduto) {
        this.idProduto = idProduto;
    }

    public Long getQtd() {
        return qtd;
    }

    public void setQtd(Long qtd) {
        this.qtd = qtd;
    }
}
