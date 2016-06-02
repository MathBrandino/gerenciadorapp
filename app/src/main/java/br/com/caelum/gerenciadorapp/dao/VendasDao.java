package br.com.caelum.gerenciadorapp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;

import br.com.caelum.gerenciadorapp.modelo.ClienteQtd;
import br.com.caelum.gerenciadorapp.modelo.ItemVenda;
import br.com.caelum.gerenciadorapp.modelo.Venda;

/**
 * Created by matheus on 05/05/16.
 */
public class VendasDao implements Closeable {

    protected static final String VENDAS = "Vendas";
    protected static final String ID_CLIENTE = "idCliente";
    protected static final String DATA = "data";
    protected static final String ID = "id";
    private HelperDao dao;
    private Context context;

    public VendasDao(Context context) {
        this.context = context;
        dao = new HelperDao(context);
    }


    public long insere(Venda venda) {

        ContentValues values = new ContentValues();

        values.put(ID_CLIENTE, venda.getIdCliente());
        values.put(DATA, venda.getDataVenda());

        long idVenda = dao.getWritableDatabase().insert(VENDAS, null, values);

        VendasProdutoDao vendasProdutoDao = new VendasProdutoDao(context);
        ProdutosEstoqueDao produtosEstoqueDao = new ProdutosEstoqueDao(context);

        for (ItemVenda item : venda.getItens()) {
            vendasProdutoDao.insere(item, idVenda);
            Integer quantidadeAnterior = produtosEstoqueDao.quantidadeParaProduto(item.getProduto());
            produtosEstoqueDao.atualiza(item.getProduto().getId(), quantidadeAnterior - item.getQuantidade());
        }

        produtosEstoqueDao.close();
        vendasProdutoDao.close();

        ContasDao contasDao = new ContasDao(context);
        contasDao.insere(idVenda);
        contasDao.close();

        return idVenda;

    }

    public List<Venda> listar() {

        List<Venda> vendas = new ArrayList<>();

        Cursor cursor = dao.getReadableDatabase().rawQuery("select * from " + VENDAS, null);

        while (cursor.moveToNext())
            vendas.add(criarVenda(cursor));

        cursor.close();
        return vendas;
    }

    private Venda criarVenda(Cursor cursor) {

        Venda venda = new Venda();

        venda.setId(cursor.getLong(cursor.getColumnIndex(ID)));
        venda.setIdCliente(cursor.getLong(cursor.getColumnIndex(ID_CLIENTE)));
        venda.setDataVenda(cursor.getString(cursor.getColumnIndex(DATA)));

        VendasProdutoDao dao = new VendasProdutoDao(context);
        List<ItemVenda> itensDaVenda = dao.getItensDaVenda(venda.getId());

        venda.setItens(itensDaVenda);

        return venda;
    }

    public Venda buscaVendaPelo(Long id) {

        String sql = "Select * from " + VENDAS + " where id = ?";

        Cursor cursor = dao.getReadableDatabase().rawQuery(sql, new String[]{id.toString()});
        cursor.moveToNext();

        Venda venda = criarVenda(cursor);

        cursor.close();
        return venda;
    }


    @Override
    public void close() {

        dao.close();
    }

    public List<ClienteQtd> listarClienteQtdVenda() {

        List<ClienteQtd> list = new ArrayList<>();

        Cursor cursor = dao.getReadableDatabase().rawQuery("Select " + ID_CLIENTE + ", count(" + ID_CLIENTE + ") as qtd from " + VENDAS + " group by " + ID_CLIENTE, null);

        while (cursor.moveToNext()) {
            ClienteQtd clienteQtd = new ClienteQtd();
            clienteQtd.setId(cursor.getLong(cursor.getColumnIndex(ID_CLIENTE)));
            clienteQtd.setQtd(cursor.getLong(cursor.getColumnIndex("qtd")));
            list.add(clienteQtd);
        }

        cursor.close();

        return list;
    }
}
