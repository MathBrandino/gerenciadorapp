package br.com.caelum.gerenciadorapp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by matheus on 03/05/16.
 */
public class ClienteTelefoneDao implements Closeable {

    protected static final String ID_CLIENTE = "idCliente";
    protected static final String TELEFONE = "telefone";
    protected static final String CLIENTE_TELEFONE = "ClienteTelefone";
    private HelperDao dao;

    public ClienteTelefoneDao(Context context) {
        this.dao = new HelperDao(context);
    }


    public void insere(Long idCliente, String telefone) {

        ContentValues values = new ContentValues();

        values.put(ID_CLIENTE, idCliente);
        values.put(TELEFONE, telefone);

        dao.getWritableDatabase().insert(CLIENTE_TELEFONE, null, values);

    }

    public void close() {
        dao.close();
    }

    public List<String> getTelefones(Long id) {
        ArrayList<String> telefones = new ArrayList<>();

        Cursor cursor = dao.getReadableDatabase().rawQuery("select * from " + CLIENTE_TELEFONE + " where " + ID_CLIENTE + " = ?", new String[]{id.toString()});

        while (cursor.moveToNext())
            telefones.add(cursor.getString(cursor.getColumnIndex(TELEFONE)));

        cursor.close();

        return telefones;
    }

    public void deleta(String telefone) {
        dao.getWritableDatabase().delete(CLIENTE_TELEFONE, TELEFONE + " = ?", new String[]{telefone});
    }

    public boolean hasTelefone(Long id, String telefone) {
        Cursor cursor = dao.getReadableDatabase().rawQuery("select * from " + CLIENTE_TELEFONE + " where " + ID_CLIENTE + " = ? and " + TELEFONE + " = ?", new String[]{id.toString(), telefone});

        boolean retorno = cursor.moveToNext();
        cursor.close();

        return retorno;
    }
}
