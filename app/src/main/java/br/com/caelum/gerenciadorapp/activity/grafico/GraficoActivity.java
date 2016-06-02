package br.com.caelum.gerenciadorapp.activity.grafico;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import br.com.caelum.gerenciadorapp.R;
import br.com.caelum.gerenciadorapp.adapter.GraficoAdapter;
import br.com.caelum.gerenciadorapp.fragments.GraficoClientesFragment;
import br.com.caelum.gerenciadorapp.fragments.GraficoProdutosFragment;
import br.com.caelum.gerenciadorapp.fragments.GraficoVendasFragment;
import br.com.caelum.gerenciadorapp.util.Infra;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by matheus on 02/06/16.
 */
public class GraficoActivity extends AppCompatActivity {

    @Bind(R.id.grafico_viewpager)
    ViewPager viewPager;


    @Bind(R.id.grafico_tablayout)
    TabLayout tabLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafica);
        ButterKnife.bind(this);
        setTitle("Nome");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tabLayout.addTab(tabLayout.newTab().setText("Vendas"));
        tabLayout.addTab(tabLayout.newTab().setText("Produtos"));
        tabLayout.addTab(tabLayout.newTab().setText("Clientes"));

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new GraficoVendasFragment());
        fragments.add(new GraficoProdutosFragment());
        fragments.add(new GraficoClientesFragment());

        viewPager.setAdapter(new GraficoAdapter(getSupportFragmentManager(), fragments));

        Infra.trocaFragmentCliqueNoTab(tabLayout, viewPager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();

            default:
                return true;
        }

    }
}
