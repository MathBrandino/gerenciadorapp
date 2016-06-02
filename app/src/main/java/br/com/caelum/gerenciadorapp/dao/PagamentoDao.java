package br.com.caelum.gerenciadorapp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;

import br.com.caelum.gerenciadorapp.modelo.Pagamento;

/**
 * Created by matheus on 17/05/16.
 */
public class PagamentoDao implements Closeable {

    protected static final String PAGAMENTOS = "Pagamentos";
    protected static final String ID = "id";
    protected static final String ID_CONTA = "idConta";
    protected static final String DATA = "data";
    protected static final String VALOR = "valor";
    protected static final String PAGO = "pago";
    private HelperDao dao;
    private Context context;

    public PagamentoDao(Context context) {
        this.context = context;

        dao = new HelperDao(context);
    }

    public List<Pagamento> listarPelo(Long id) {
        List<Pagamento> pagamentos = new ArrayList<>();

        Cursor cursor = dao.getReadableDatabase().rawQuery("Select * from " + PAGAMENTOS + " where " + ID_CONTA + " = ?", new String[]{id.toString()});
        while (cursor.moveToNext()) {
            pagamentos.add(criaPagamento(cursor));
        }

        cursor.close();
        return pagamentos;
    }

    public void insere(Pagamento pagamento) {

        ContentValues values = new ContentValues();

        values.put(DATA, pagamento.getData());
        values.put(ID_CONTA, pagamento.getIdConta());
        values.put(PAGO, pagamento.getPago() ? 1 : 0);
        values.put(VALOR, pagamento.getValor());


        dao.getWritableDatabase().insert(PAGAMENTOS, null, values);
    }

    public void altera(Pagamento pagamento) {

        ContentValues values = new ContentValues();

        values.put(DATA, pagamento.getData());
        values.put(ID_CONTA, pagamento.getIdConta());
        values.put(PAGO, pagamento.getPago() ? 1 : 0);
        values.put(VALOR, pagamento.getValor());


        dao.getWritableDatabase().update(PAGAMENTOS, values, "id = ?", new String[]{pagamento.getId().toString()});
    }

    private Pagamento criaPagamento(Cursor cursor) {

        Pagamento pagamento = new Pagamento();

        pagamento.setId(cursor.getLong(cursor.getColumnIndex(ID)));
        pagamento.setIdConta(cursor.getLong(cursor.getColumnIndex(ID_CONTA)));
        pagamento.setData(cursor.getString(cursor.getColumnIndex(DATA)));
        pagamento.setValor(cursor.getDouble(cursor.getColumnIndex(VALOR)));

        int hasPago = cursor.getInt(cursor.getColumnIndex(PAGO));
        pagamento.setPago(hasPago == Pagamento.PAGO);

        return pagamento;
    }

    @Override
    public void close() {

        dao.close();
    }

    public void deleta(Pagamento pagamento) {
        dao.getWritableDatabase().delete(PAGAMENTOS, "id = ?", new String[]{pagamento.getId().toString()});
    }
}
