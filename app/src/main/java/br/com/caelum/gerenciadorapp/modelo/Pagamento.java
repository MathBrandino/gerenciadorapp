package br.com.caelum.gerenciadorapp.modelo;

import java.io.Serializable;

/**
 * Created by matheus on 17/05/16.
 */
public class Pagamento implements Serializable {

    public static final int PAGO = 1;


    private Long id;
    private Long idConta;
    private String data;
    private Boolean pago = new Boolean(false);
    private Double valor;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Boolean getPago() {
        return pago;
    }

    public void setPago(Boolean pago) {
        this.pago = pago;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Long getIdConta() {
        return idConta;
    }

    public void setIdConta(Long idConta) {
        this.idConta = idConta;
    }

    @Override
    public String toString() {
        String pago = this.pago ? "Sim" : "Não";
        return "R$ " + valor + "  Data : " + data + "\nRecebido : " + pago;
    }
}
