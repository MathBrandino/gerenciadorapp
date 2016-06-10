package br.com.caelum.gerenciadorapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import br.com.caelum.gerenciadorapp.R;
import br.com.caelum.gerenciadorapp.activity.cliente.ListaClienteActivity;
import br.com.caelum.gerenciadorapp.activity.contas.ContasActivity;
import br.com.caelum.gerenciadorapp.activity.grafico.GraficoActivity;
import br.com.caelum.gerenciadorapp.activity.produtos.ProdutosActivity;
import br.com.caelum.gerenciadorapp.activity.venda.ListaVendasActivity;
import br.com.caelum.gerenciadorapp.permissao.Permissao;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Permissao.verifica(permissions, grantResults, this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Permissao.fazPermissao(this);
    }

    @OnClick(R.id.botao_produto)
    public void onClickProduto() {
        Intent intent = new Intent(this, ProdutosActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.botao_cliente)
    public void onClickCliente() {

        Intent intent = new Intent(this, ListaClienteActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.botao_vendas)
    public void onClickVendas() {
        Intent intent = new Intent(this, ListaVendasActivity.class);
        startActivity(intent);
    }


    @OnClick(R.id.botao_contas)
    public void onClickContas() {
        Intent intent = new Intent(this, ContasActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.botao_grafico)
    public void onClickGraficos() {
        Intent intent = new Intent(this, GraficoActivity.class);
        startActivity(intent);
    }
}
