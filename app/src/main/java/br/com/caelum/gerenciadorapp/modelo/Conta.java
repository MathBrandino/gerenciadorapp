package br.com.caelum.gerenciadorapp.modelo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by matheus on 17/05/16.
 */
public class Conta implements Serializable {

    private Long id;
    private Long idVenda;
    private List<Pagamento> pagamentos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdVenda() {
        return idVenda;
    }

    public void setIdVenda(Long idVenda) {
        this.idVenda = idVenda;
    }

    public List<Pagamento> getPagamentos() {
        return pagamentos;
    }

    public void setPagamentos(List<Pagamento> pagamentos) {
        this.pagamentos = pagamentos;
    }
}
