package br.com.caelum.gerenciadorapp.modelo;

/**
 * Created by matheus on 02/06/16.
 */
public class ClienteQtd {
    private long id;
    private long qtd;

    public long getQtd() {
        return qtd;
    }

    public long getId() {

        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setQtd(long qtd) {
        this.qtd = qtd;
    }
}
