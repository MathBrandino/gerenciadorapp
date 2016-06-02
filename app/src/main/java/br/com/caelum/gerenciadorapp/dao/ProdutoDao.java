package br.com.caelum.gerenciadorapp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;

import br.com.caelum.gerenciadorapp.modelo.Produto;

/**
 * Created by matheus on 02/05/16.
 */
public class ProdutoDao implements Closeable {

    protected static final String PRODUTOS = "Produtos";
    protected static final String NOME = "nome";
    protected static final String DESCRICAO = "descricao";
    protected static final String PRECO = "preco";
    protected static final String ID = "id";

    private HelperDao dao;
    private Context context;

    public ProdutoDao(Context context) {
        this.context = context;
        dao = new HelperDao(context);
    }


    public void adiciona(Produto produto) {

        ContentValues values = criaContentValues(produto);

        long id = dao.getWritableDatabase().insert(PRODUTOS, null, values);

        ProdutosEstoqueDao estoqueDao = new ProdutosEstoqueDao(context);
        estoqueDao.insere(id);
        estoqueDao.close();

    }

    public void altera(Produto produto) {
        ContentValues contentValues = criaContentValues(produto);

        dao.getWritableDatabase().update(PRODUTOS, contentValues, "id = ?", new String[]{produto.getId().toString()});

    }

    public List<Produto> listar() {

        List<Produto> produtos = new ArrayList<>();

        Cursor cursor = dao.getReadableDatabase().rawQuery("select * from " + PRODUTOS, null);

        while (cursor.moveToNext())
            produtos.add(criaProdutoCursor(cursor));

        cursor.close();

        return produtos;
    }

    public void deleta(Produto produto) {

        dao.getWritableDatabase().delete(PRODUTOS, "id = ?", new String[]{produto.getId().toString()});
    }

    @NonNull
    private ContentValues criaContentValues(Produto produto) {

        ContentValues values = new ContentValues();

        values.put(NOME, produto.getNome());
        values.put(DESCRICAO, produto.getDescricao());
        values.put(PRECO, produto.getPreco());

        return values;
    }

    private Produto criaProdutoCursor(Cursor cursor) {

        Produto produto = new Produto();

        produto.setId(cursor.getLong(cursor.getColumnIndex(ID)));
        produto.setNome(cursor.getString(cursor.getColumnIndex(NOME)));
        produto.setDescricao(cursor.getString(cursor.getColumnIndex(DESCRICAO)));
        produto.setPreco(cursor.getDouble(cursor.getColumnIndex(PRECO)));

        return produto;
    }


    public Produto recuperaProduto(Long id) {

        Cursor cursor = dao.getReadableDatabase().rawQuery("Select * from " + PRODUTOS + " where " + ID + " = ?", new String[]{id.toString()});

        cursor.moveToNext();
        Produto produto = criaProdutoCursor(cursor);


        cursor.close();
        return produto;
    }

    @Override
    public void close() {
        dao.close();
    }
}
