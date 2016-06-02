package br.com.caelum.gerenciadorapp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;

import br.com.caelum.gerenciadorapp.modelo.Produto;
import br.com.caelum.gerenciadorapp.modelo.ProdutoEstoque;

/**
 * Created by matheus on 04/05/16.
 */
public class ProdutosEstoqueDao implements Closeable {

    protected static final String PRODUTOS_ESTOQUE = "ProdutosEstoque";
    protected static final String ID = "id";
    protected static final String ID_PRODUTO = ID + "Produto";
    protected static final String QUANTIDADE = "quant" + ID + "ade";
    private HelperDao dao;

    public ProdutosEstoqueDao(Context context) {
        this.dao = new HelperDao(context);
    }


    public void insere(Long idProduto) {

        ContentValues values = new ContentValues();

        values.put(ID_PRODUTO, idProduto);
        values.put(QUANTIDADE, 0);

        dao.getWritableDatabase().insert(PRODUTOS_ESTOQUE, null, values);
    }


    public void atualiza(Long idProduto, Integer quantidade) {

        ContentValues values = new ContentValues();
        values.put(QUANTIDADE, quantidade);

        dao.getWritableDatabase().update(PRODUTOS_ESTOQUE, values, ID_PRODUTO + " = ?", new String[]{idProduto.toString()});
    }


    public List<ProdutoEstoque> listar() {

        List<ProdutoEstoque> estoque = new ArrayList<>();

        String sql = "select * from " + PRODUTOS_ESTOQUE;
        Cursor cursor = dao.getReadableDatabase().rawQuery(sql, null);

        while (cursor.moveToNext()) {
            estoque.add(criaProdutoEstoque(cursor));
        }

        cursor.close();

        return estoque;
    }

    private ProdutoEstoque criaProdutoEstoque(Cursor cursor) {

        ProdutoEstoque produtoEstoque = new ProdutoEstoque();

        produtoEstoque.setId(cursor.getLong(cursor.getColumnIndex(ID)));
        produtoEstoque.setIdProduto(cursor.getLong(cursor.getColumnIndex(ID_PRODUTO)));
        produtoEstoque.setQuantidade(cursor.getInt(cursor.getColumnIndex(QUANTIDADE)));

        return produtoEstoque;
    }

    @Override
    public void close() {
        dao.close();
    }

    public Integer quantidadeParaProduto(Produto produto) {

        String sql = "select * from " + PRODUTOS_ESTOQUE + " where " + ID_PRODUTO + " = ?";
        Cursor cursor = dao.getReadableDatabase().rawQuery(sql, new String[]{produto.getId().toString()});

        cursor.moveToNext();
        ProdutoEstoque produtoEstoque = criaProdutoEstoque(cursor);
        return produtoEstoque.getQuantidade();

    }
}
