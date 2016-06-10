package br.com.caelum.gerenciadorapp.activity.venda;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.caelum.gerenciadorapp.R;
import br.com.caelum.gerenciadorapp.dao.ClienteDao;
import br.com.caelum.gerenciadorapp.dao.ProdutoDao;
import br.com.caelum.gerenciadorapp.dao.ProdutosEstoqueDao;
import br.com.caelum.gerenciadorapp.dao.VendasDao;
import br.com.caelum.gerenciadorapp.modelo.Cliente;
import br.com.caelum.gerenciadorapp.modelo.ItemVenda;
import br.com.caelum.gerenciadorapp.modelo.Produto;
import br.com.caelum.gerenciadorapp.modelo.Venda;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

public class FormularioVendaActivity extends AppCompatActivity {

    @Bind(R.id.formulario_vendas_lista_clientes)
    Spinner listaClientes;

    @Bind(R.id.formulario_vendas_lista_produtos)
    Spinner listaProdutos;

    @Bind(R.id.formulario_vendas_nome_cliente)
    TextView nomeCliente;

    @Bind(R.id.formulario_vendas_nome_produto)
    TextView nomeProduto;

    @Bind(R.id.formulario_vendas_quantidade_estoque)
    TextView qtdEstoque;

    @Bind(R.id.formulario_vendas_quantidade_produto)
    EditText qtdVendida;

    @Bind(R.id.formulario_vendas_preco_produto)
    TextView valorProduto;

    @Bind(R.id.formulario_vendas_preco_venda)
    EditText valorVendido;

    @Bind(R.id.formulario_vendas_lista_produtos_vendidos)
    ListView listaProdutosVendidos;

    private Cliente cliente;
    private Produto produto;
    private Venda venda;

    private List<ItemVenda> itens = new ArrayList<>();
    private ArrayAdapter<ItemVenda> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_venda);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        ClienteDao clienteDao = new ClienteDao(this);
        List<Cliente> clientes = clienteDao.listar();
        clienteDao.close();

        ProdutoDao produtoDao = new ProdutoDao(this);
        List<Produto> produtos = produtoDao.listar();
        produtoDao.close();


        listaClientes.setAdapter(new ArrayAdapter<Cliente>(this, android.R.layout.simple_list_item_1, clientes));
        listaProdutos.setAdapter(new ArrayAdapter<Produto>(this, android.R.layout.simple_list_item_1, produtos));

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itens);
        listaProdutosVendidos.setAdapter(adapter);


    }

    @OnItemSelected(R.id.formulario_vendas_lista_clientes)
    public void selecionaCliente(int position) {

        cliente = (Cliente) listaClientes.getItemAtPosition(position);

        nomeCliente.setText(cliente.getNome());


    }


    @OnItemSelected(R.id.formulario_vendas_lista_produtos)
    public void selecionaProduto(int position) {

        produto = (Produto) listaProdutos.getItemAtPosition(position);

        nomeProduto.setText(produto.getNome());

        ProdutosEstoqueDao produtosEstoqueDao = new ProdutosEstoqueDao(this);
        Integer quantidadeEmEstoque = produtosEstoqueDao.quantidadeParaProduto(produto);
        produtosEstoqueDao.close();

        qtdEstoque.setText(quantidadeEmEstoque.toString());

        valorProduto.setText("R$ " + produto.getPreco() + "  ");

    }


    @OnClick(R.id.formulario_vendas_botao_add_produto)
    public void adicionaProdutoNaLista() {

        try {
            Integer quantidadeVendida = Integer.valueOf(qtdVendida.getText().toString());
            Double precoDoProdutoVendido = Double.valueOf(valorVendido.getText().toString());

            if (validaQtdVendida(quantidadeVendida)) {


                ItemVenda itemVenda = new ItemVenda(produto, quantidadeVendida, precoDoProdutoVendido);

                itens.add(itemVenda);
                adapter.notifyDataSetChanged();

                valorVendido.setText("");
                qtdVendida.setText("");

            } else {
                Toast.makeText(this, "Valor em estoque inferior ao vendido", Toast.LENGTH_LONG).show();
            }

        } catch (NumberFormatException e) {
            Log.d("NumberFormatException", e.toString());
            Toast.makeText(this, "Informe valores antes de adicionar na lista", Toast.LENGTH_LONG).show();
        }

    }

    private boolean validaQtdVendida(Integer quantidadeVendida) {
        return !(quantidadeVendida > Integer.valueOf(qtdEstoque.getText().toString()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.menu_formulario, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                return true;

            case R.id.menu_salvar:

                if (validaCampos()) {
                    venda = criaVenda();

                    VendasDao vendasDao = new VendasDao(this);
                    vendasDao.insere(venda);
                    vendasDao.close();

                    finish();
                }

                return true;

            default:
                return false;
        }

    }

    private boolean validaCampos() {

        if (cliente == null) {
            Toast.makeText(this, "Você precisa de um cliente ", Toast.LENGTH_LONG).show();
            return false;
        }

        if (itens.isEmpty()) {
            Toast.makeText(this, "Nenhum produto foi adicionado", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;

    }

    private Venda criaVenda() {

        venda = new Venda();

        venda.setItens(itens);
        String data = new SimpleDateFormat("dd/MM/yyyy").format(new Date(System.currentTimeMillis()));
        venda.setDataVenda(data);
        venda.setIdCliente(cliente.getId());

        return venda;
    }

}

