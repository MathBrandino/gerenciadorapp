package br.com.caelum.gerenciadorapp.modelo;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by matheus on 04/05/16.
 */
public class Venda implements Serializable {

    private Long id;
    private Long idCliente;
    private List<ItemVenda> itens;
    private String dataVenda;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public String getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(String dataVenda) {
        this.dataVenda = dataVenda;
    }

    public List<ItemVenda> getItens() {
        return itens;
    }

    public void setItens(List<ItemVenda> itens) {
        this.itens = itens;
    }

    public boolean isMesmoDia(Calendar dia) {

        Calendar data = geraDia();

        if (data.get(Calendar.DAY_OF_YEAR) == dia.get(Calendar.DAY_OF_YEAR) && data.get(Calendar.YEAR) == dia.get(Calendar.YEAR))
            return true;
        return false;
    }

    private Calendar geraDia() {
        Calendar data = Calendar.getInstance();

        Date date = null;
        try {
            date = new SimpleDateFormat("dd/MM/yyyy").parse(dataVenda);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long time = date.getTime();
        data.setTimeInMillis(time);
        return data;
    }
}
