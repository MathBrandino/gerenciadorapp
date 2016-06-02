package br.com.caelum.gerenciadorapp.activity.cliente;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import br.com.caelum.gerenciadorapp.R;
import br.com.caelum.gerenciadorapp.adapter.ListaClienteAdapter;
import br.com.caelum.gerenciadorapp.dao.ClienteDao;
import br.com.caelum.gerenciadorapp.modelo.Cliente;
import br.com.caelum.gerenciadorapp.util.Infra;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by matheus on 02/05/16.
 */
public class ListaClienteActivity extends AppCompatActivity {

    @Bind(R.id.fab_cliente_activity)
    FloatingActionButton fab;

    @Bind(R.id.recycler_cliente_activity)
    RecyclerView recyclerView;

    private List<Cliente> clientes = new ArrayList<>();
    private ListaClienteAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_cliente_activity);

        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        carregaLista();

        Infra.escondeFABNoScroll(recyclerView, fab);
    }

    @Override
    public void onResume() {
        super.onResume();

        carregaLista();

    }

    private void carregaLista() {

        ClienteDao dao = new ClienteDao(this);
        clientes = dao.listar();
        dao.close();

        adapter = new ListaClienteAdapter(this, clientes);
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.fab_cliente_activity)
    public void clickFAB() {


        Intent intent = new Intent(this, FormularioClienteActivity.class);

        startActivity(intent);
    }


}
