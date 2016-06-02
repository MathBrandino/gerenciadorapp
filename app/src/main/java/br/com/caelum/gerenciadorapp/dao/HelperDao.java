package br.com.caelum.gerenciadorapp.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by matheus on 02/05/16.
 */
class HelperDao extends SQLiteOpenHelper {

    private static final String NOME = "GerenciadorApp";
    private static final int VERSION = 1;

    public HelperDao(Context context) {
        super(context, NOME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sqlCliente = "Create table Clientes (" +
                "id integer primary key," +
                "nome text not null);";

        String sqlProduto = "Create table Produtos (" +
                "id integer primary key," +
                "nome text not null, " +
                "descricao text not null, " +
                "preco real not null );";


        String sqlClienteTelefone = " Create table ClienteTelefone( " +
                "id integer primary key," +
                "telefone text not null," +
                "idCliente integer not null," +
                "FOREIGN KEY(idCliente) REFERENCES Clientes (id));";

        String sqlProdutoEstoque = "Create table ProdutosEstoque ( " +
                "id integer primary key," +
                "quantidade integer not null, " +
                "idProduto integer not null, " +
                "FOREIGN KEY(IdProduto) REFERENCES Produtos(id) ) ;";


        String sqlVendas = "Create table Vendas (" +
                " id integer primary key," +
                " idCliente integer not null, " +
                " data text not null, " +
                "FOREIGN KEY (idCliente) REFERENCES Clientes (id) );";

        String sqlVendasProduto = "Create table VendasProduto (" +
                " id integer primary key," +
                " idVenda integer not null, " +
                " idProduto integer not null, " +
                " quantidade integer not null, " +
                " valorUnitarioVendido real not null, " +
                " FOREIGN KEY (idVenda) REFERENCES Vendas(id)," +
                " FOREIGN KEY (idProduto) REFERENCES Produtos(id) ) ;";


        String sqlContas = "Create table Contas( " +
                "id integer primary key," +
                "idVenda integer not null," +
                "FOREIGN KEY (idVenda) references Vendas(id) );";

        String sqlPagamento = "Create table Pagamentos ( " +
                "id integer primary key," +
                "idConta integer not null," +
                "data text not null," +
                "valor real," +
                "pago integer," +
                "FOREIGN KEY (idConta) REFERENCES Contas(id) );";

        db.execSQL(sqlCliente);
        db.execSQL(sqlProduto);
        db.execSQL(sqlClienteTelefone);
        db.execSQL(sqlProdutoEstoque);
        db.execSQL(sqlVendas);
        db.execSQL(sqlVendasProduto);
        db.execSQL(sqlContas);
        db.execSQL(sqlPagamento);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
