package br.com.caelum.gerenciadorapp.activity.contas;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import java.util.List;

import br.com.caelum.gerenciadorapp.R;
import br.com.caelum.gerenciadorapp.adapter.ContasAdapter;
import br.com.caelum.gerenciadorapp.dao.ContasDao;
import br.com.caelum.gerenciadorapp.modelo.Conta;
import butterknife.Bind;
import butterknife.ButterKnife;

public class ContasActivity extends AppCompatActivity {


    @Bind(R.id.recycler_contas_activity)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contas);

        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onResume() {
        super.onResume();

        ContasDao dao = new ContasDao(this);
        List<Conta> contas = dao.listar();
        dao.close();

        recyclerView.setAdapter(new ContasAdapter(contas, this));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }
}
