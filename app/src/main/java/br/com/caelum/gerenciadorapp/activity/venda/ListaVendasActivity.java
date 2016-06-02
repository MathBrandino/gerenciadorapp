package br.com.caelum.gerenciadorapp.activity.venda;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import java.util.List;

import br.com.caelum.gerenciadorapp.R;
import br.com.caelum.gerenciadorapp.adapter.VendasAdapter;
import br.com.caelum.gerenciadorapp.dao.VendasDao;
import br.com.caelum.gerenciadorapp.modelo.Venda;
import br.com.caelum.gerenciadorapp.util.Infra;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ListaVendasActivity extends AppCompatActivity {

    @Bind(R.id.recycler_vendas_activity)
    RecyclerView recyclerView;

    @Bind(R.id.fab_vendas_activity)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_vendas);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Infra.escondeFABNoScroll(recyclerView, fab);

    }


    @Override
    protected void onResume() {
        super.onResume();

        VendasDao dao = new VendasDao(this);
        List<Venda> vendas = dao.listar();
        dao.close();

        recyclerView.setAdapter(new VendasAdapter(this, vendas));

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.fab_vendas_activity)
    public void onClickFab() {
        startActivity(new Intent(this, FormularioVendaActivity.class));
    }


}
