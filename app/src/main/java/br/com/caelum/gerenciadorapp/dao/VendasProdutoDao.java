package br.com.caelum.gerenciadorapp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;

import br.com.caelum.gerenciadorapp.modelo.ItemVenda;
import br.com.caelum.gerenciadorapp.modelo.Produto;
import br.com.caelum.gerenciadorapp.modelo.ProdutoQtd;

/**
 * Created by matheus on 05/05/16.
 */
public class VendasProdutoDao implements Closeable {

    protected static final String VENDAS_PRODUTO = "VendasProduto";
    protected static final String ID_VENDA = "idVenda";
    protected static final String ID_PRODUTO = "idProduto";
    protected static final String QUANTIDADE = "quantidade";
    protected static final String VALOR_UNITARIO_VENDIDO = "valorUnitarioVendido";
    protected static final String ID = "id";
    private HelperDao dao;
    private Context context;

    public VendasProdutoDao(Context context) {
        this.context = context;
        dao = new HelperDao(context);
    }

    public void insere(ItemVenda item, Long idVenda) {

        ContentValues values = new ContentValues();

        values.put(ID_VENDA, idVenda);
        values.put(ID_PRODUTO, item.getProduto().getId());
        values.put(QUANTIDADE, item.getQuantidade());
        values.put(VALOR_UNITARIO_VENDIDO, item.getValorUnitarioVendido());

        dao.getWritableDatabase().insert(VENDAS_PRODUTO, null, values);
    }


    public List<ItemVenda> getItensDaVenda(Long idVenda) {

        List<ItemVenda> itensVenda = new ArrayList<>();

        Cursor cursor = dao.getReadableDatabase().rawQuery("Select * from " + VENDAS_PRODUTO + " where " + ID_VENDA + " = ?", new String[]{idVenda.toString()});

        while (cursor.moveToNext())
            itensVenda.add(criaItemVenda(cursor));

        cursor.close();

        return itensVenda;
    }

    private ItemVenda criaItemVenda(Cursor cursor) {

        ProdutoDao dao = new ProdutoDao(context);
        Produto produto = dao.recuperaProduto(cursor.getLong(cursor.getColumnIndex(ID_PRODUTO)));
        dao.close();

        Integer quantidade = cursor.getInt(cursor.getColumnIndex(QUANTIDADE));

        Double valorUnitarioVendido = cursor.getDouble(cursor.getColumnIndex(VALOR_UNITARIO_VENDIDO));

        ItemVenda item = new ItemVenda(produto, quantidade, valorUnitarioVendido);
        item.setId(cursor.getLong(cursor.getColumnIndex(ID)));


        return item;
    }

    @Override
    public void close() {
        dao.close();
    }

    public List<ProdutoQtd> listar() {

        List<ProdutoQtd> produtoQtds = new ArrayList<>();

        Cursor cursor = dao.getReadableDatabase().rawQuery("select " + ID_PRODUTO + ",  sum(" + QUANTIDADE + ") as qtd from " + VENDAS_PRODUTO + " group by " + ID_PRODUTO, null);

        while (cursor.moveToNext()) {
            ProdutoQtd produtoQtd = new ProdutoQtd();
            produtoQtd.setIdProduto(cursor.getLong(cursor.getColumnIndex(ID_PRODUTO)));
            produtoQtd.setQtd(cursor.getLong(cursor.getColumnIndex("qtd")));

            produtoQtds.add(produtoQtd);
        }
        cursor.close();

        return produtoQtds;
    }
}
