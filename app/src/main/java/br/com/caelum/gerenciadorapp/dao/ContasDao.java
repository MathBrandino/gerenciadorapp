package br.com.caelum.gerenciadorapp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;

import br.com.caelum.gerenciadorapp.modelo.Conta;

/**
 * Created by matheus on 17/05/16.
 */
public class ContasDao implements Closeable {


    private static final String CONTAS = "Contas";
    private static final String ID = "id";
    private static final String IDVENDAS = "idVenda";
    private HelperDao dao;
    private Context context;


    public ContasDao(Context context) {
        this.context = context;
        this.dao = new HelperDao(context);
    }


    public void insere(Long idVenda) {

        ContentValues values = new ContentValues();
        values.put(IDVENDAS, idVenda);

        dao.getWritableDatabase().insert(CONTAS, null, values);
    }

    public List<Conta> listar() {

        List<Conta> contas = new ArrayList<>();

        Cursor cursor = dao.getReadableDatabase().rawQuery("select * from " + CONTAS, null);

        while (cursor.moveToNext())
            contas.add(criaConta(cursor));


        cursor.close();
        return contas;
    }

    private Conta criaConta(Cursor cursor) {

        Conta conta = new Conta();

        conta.setId(cursor.getLong(cursor.getColumnIndex(ID)));
        conta.setIdVenda(cursor.getLong(cursor.getColumnIndex(IDVENDAS)));

        PagamentoDao pagamentoDao = new PagamentoDao(context);
        conta.setPagamentos(pagamentoDao.listarPelo(conta.getId()));
        pagamentoDao.close();

        return conta;
    }

    @Override
    public void close() {
        dao.close();
    }
}
