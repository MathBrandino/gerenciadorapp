package br.com.caelum.gerenciadorapp.activity.contas;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.caelum.gerenciadorapp.R;
import br.com.caelum.gerenciadorapp.dao.PagamentoDao;
import br.com.caelum.gerenciadorapp.dao.VendasProdutoDao;
import br.com.caelum.gerenciadorapp.modelo.Conta;
import br.com.caelum.gerenciadorapp.modelo.ItemVenda;
import br.com.caelum.gerenciadorapp.modelo.Pagamento;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class CadastroPagamentoActivity extends AppCompatActivity {

    @Bind(R.id.cadastro_pagamento_data_pagamento)
    TextView dataPagamento;

    @Bind(R.id.cadastro_pagamento_id_venda)
    TextView idVenda;

    @Bind(R.id.cadastro_pagamento_valor_recebido)
    TextView valorRecibido;

    @Bind(R.id.cadastro_pagamento_valor_pago)
    TextView valorPago;

    @Bind(R.id.cadastro_pagamento_valor_total)
    TextView valorTotal;

    @Bind(R.id.cadastro_pagamento_valor_restante)
    TextView valorRestante;

    @Bind(R.id.cadastro_pagamento_valor_pagamento_entrada)
    TextInputEditText valorPagamento;

    @Bind(R.id.cadastro_pagamento_lista_pagamento)
    ListView listaPagamentos;

    @Bind(R.id.cadastro_pagamento_botao_add_lista)
    Button botaoAddLista;

    @Bind(R.id.fab_cadastro_pagamento)
    FloatingActionButton fabCadastro;

    private Conta conta;

    private List<Pagamento> pagamentos = new ArrayList<>();

    private double precoTotal;
    private double precoPago;
    private double precoRecibo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_pagamento);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        conta = (Conta) getIntent().getSerializableExtra("conta");

        populaDados();

    }

    private void regraDeInsercao() {
        if (precoTotal == precoRecibo || precoTotal == precoPago) {
            ((TextInputLayout) valorPagamento.getParent()).setVisibility(View.GONE);
            dataPagamento.setVisibility(View.GONE);
            botaoAddLista.setVisibility(View.GONE);
            fabCadastro.setVisibility(View.GONE);
        } else {
            ((TextInputLayout) valorPagamento.getParent()).setVisibility(View.VISIBLE);
            dataPagamento.setVisibility(View.VISIBLE);
            botaoAddLista.setVisibility(View.VISIBLE);
            fabCadastro.setVisibility(View.VISIBLE);

        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        carregaLista();
        regraDeInsercao();
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

                PagamentoDao dao = new PagamentoDao(this);

                for (Pagamento pagamento : pagamentos) {

                    if (pagamento.getId() == null) {
                        dao.insere(pagamento);
                    } else {
                        dao.altera(pagamento);
                    }
                }
                dao.close();

                finish();

                return true;


            default:
                return true;
        }
    }


    private void populaDados() {
        Long idVenda = conta.getIdVenda();
        this.idVenda.setText(idVenda.toString());

        VendasProdutoDao dao = new VendasProdutoDao(this);
        List<ItemVenda> itensDaVenda = dao.getItensDaVenda(idVenda);
        dao.close();


        for (ItemVenda itemVenda : itensDaVenda) {
            precoTotal += itemVenda.getValorUnitarioVendido() * itemVenda.getQuantidade();
        }

        valorTotal.setText("R$ " + precoTotal);

        PagamentoDao pagamentoDao = new PagamentoDao(this);
        pagamentos = pagamentoDao.listarPelo(conta.getId());
        pagamentoDao.close();

        carregaLista();

        atualizaPrecos();


    }

    private void atualizaPrecos() {
        atualizaPrecoRecebido();

        atualizaPrecoPago();

        atualizaValorRestante();
    }

    private void atualizaPrecoPago() {

        precoPago = 0;

        for (Pagamento pagamento : pagamentos) {
            precoPago += pagamento.getValor();
        }

        valorPago.setText("R$ " + precoPago);

    }

    private void atualizaPrecoRecebido() {

        precoRecibo = 0;
        for (Pagamento pagamento : pagamentos) {
            if (pagamento.getPago())
                precoRecibo += pagamento.getValor();
        }

        valorRecibido.setText("R$ " + precoRecibo);
    }

    private void atualizaValorRestante() {
        valorRestante.setText("R$ " + calculaValorRestante());
    }

    private double calculaValorRestante() {
        return precoTotal - precoPago;
    }

    @OnClick(R.id.fab_cadastro_pagamento)
    public void onClickFAB() {
        final DatePicker picker = new DatePicker(this);

        new AlertDialog.Builder(this).setView(picker).setPositiveButton("Pronto", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int day = picker.getDayOfMonth();
                int month = picker.getMonth();
                int year = picker.getYear();

                String data = day + "/" + month + "/" + year;

                dataPagamento.setText(data);
            }
        }).show();
    }


    @OnClick(R.id.cadastro_pagamento_botao_add_lista)
    public void clickBotaoAddLista() {

        if (validaCampos()) {
            pagamentos.add(criaPagamento());

            carregaLista();
            atualizaPrecos();

            limpaCampos();

            regraDeInsercao();

        } else {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_LONG).show();
        }
    }

    private boolean validaCampos() {
        boolean hasValor = !valorPagamento.getText().toString().trim().isEmpty();
        boolean hasDataValida = dataPagamento.getText().toString().contains("/");

        return hasValor && hasDataValida;
    }

    private void limpaCampos() {

        valorPagamento.setText("");
        dataPagamento.setText("Escolha a data do pagamento");

    }


    @OnItemClick(R.id.cadastro_pagamento_lista_pagamento)
    public void onClickItem(int position) {

        final Pagamento pagamento = (Pagamento) listaPagamentos.getItemAtPosition(position);

        if (!pagamento.getPago()) {
            new AlertDialog.Builder(this)
                    .setTitle("Pagamento")
                    .setMessage("O que deseja fazer ?")
                    .setNegativeButton("Deletar pagamento", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            pagamentos.remove(pagamento);

                            if (pagamento.getId() != null) {
                                PagamentoDao dao = new PagamentoDao(CadastroPagamentoActivity.this);
                                dao.deleta(pagamento);
                                dao.close();
                            }


                            carregaLista();
                            atualizaPrecos();
                            regraDeInsercao();
                        }
                    })
                    .setPositiveButton("Marcar como pago", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            pagamento.setPago(true);

                            ArrayAdapter adapter = (ArrayAdapter) listaPagamentos.getAdapter();
                            adapter.notifyDataSetChanged();

                            atualizaPrecos();
                        }
                    }).show();

        }

    }


    private void carregaLista() {
        listaPagamentos.setAdapter(new ArrayAdapter<Pagamento>(this, android.R.layout.simple_list_item_1, pagamentos));
    }

    public Pagamento criaPagamento() {

        Pagamento pagamento = new Pagamento();

        pagamento.setData(dataPagamento.getText().toString());
        pagamento.setIdConta(conta.getId());
        pagamento.setValor(Double.valueOf(valorPagamento.getText().toString()));

        return pagamento;
    }
}
