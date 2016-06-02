package br.com.caelum.gerenciadorapp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;

import br.com.caelum.gerenciadorapp.modelo.Cliente;

/**
 * Created by matheus on 02/05/16.
 */
public class ClienteDao implements Closeable {

    protected static final String CLIENTES = "Clientes";
    protected static final String NOME = "nome";
    protected static final String ID = "id";

    private HelperDao dao;
    private Context context;

    public ClienteDao(Context context) {
        this.context = context;
        this.dao = new HelperDao(context);
    }


    public void insere(Cliente cliente) {

        ContentValues values = criaContentValues(cliente);

        long idInserido = dao.getWritableDatabase().insert(CLIENTES, null, values);
        ClienteTelefoneDao ctDao = new ClienteTelefoneDao(context);

        for (String telefone : cliente.getTelefones()) {
            if (!ctDao.hasTelefone(idInserido, telefone)) {
                ctDao.insere(idInserido, telefone);
            }
        }


        ctDao.close();

    }

    public void altera(Cliente cliente) {

        ContentValues contentValues = criaContentValues(cliente);

        dao.getWritableDatabase().update(CLIENTES, contentValues, "id = ? ", new String[]{cliente.getId().toString()});

        for (String telefone : cliente.getTelefones()) {
            ClienteTelefoneDao dao = new ClienteTelefoneDao(context);
            if (!dao.hasTelefone(cliente.getId(), telefone)) {
                dao.insere(cliente.getId(), telefone);
            }
            dao.close();
        }
    }

    public void deleta(Cliente cliente) {

        dao.getWritableDatabase().delete(CLIENTES, "id = ?", new String[]{cliente.getId().toString()});
    }


    @NonNull
    private ContentValues criaContentValues(Cliente cliente) {
        ContentValues values = new ContentValues();

        values.put(NOME, cliente.getNome());
        return values;
    }


    public List<Cliente> listar() {

        List<Cliente> clientes = new ArrayList<>();

        Cursor cursor = dao.getReadableDatabase().rawQuery("Select * from " + CLIENTES, null);

        while (cursor.moveToNext())
            clientes.add(criaCliente(cursor));

        cursor.close();

        return clientes;
    }

    private Cliente criaCliente(Cursor cursor) {

        Cliente cliente = new Cliente();

        cliente.setId(cursor.getLong(cursor.getColumnIndex(ID)));
        cliente.setNome(cursor.getString(cursor.getColumnIndex(NOME)));

        ClienteTelefoneDao ctDao = new ClienteTelefoneDao(context);
        cliente.setTelefones(ctDao.getTelefones(cliente.getId()));
        ctDao.close();


        return cliente;
    }

    @Override
    public void close() {

        dao.close();
    }


    public Cliente recuperaCliente(Long idCliente) {

        Cursor cursor = dao.getReadableDatabase().rawQuery("Select * from " + CLIENTES + " where " + ID + " = ?", new String[]{idCliente.toString()});
        cursor.moveToNext();
        Cliente cliente = criaCliente(cursor);


        cursor.close();
        return cliente;
    }
}
