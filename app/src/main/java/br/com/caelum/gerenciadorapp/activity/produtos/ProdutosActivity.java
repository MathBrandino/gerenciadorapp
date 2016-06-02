package br.com.caelum.gerenciadorapp.activity.produtos;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.Arrays;
import java.util.List;

import br.com.caelum.gerenciadorapp.R;
import br.com.caelum.gerenciadorapp.adapter.ProdutosAdapter;
import br.com.caelum.gerenciadorapp.fragments.EstoqueFragment;
import br.com.caelum.gerenciadorapp.fragments.ProdutosFragment;
import br.com.caelum.gerenciadorapp.util.Infra;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by matheus on 24/05/16.
 */
public class ProdutosActivity extends AppCompatActivity {

    @Bind(R.id.produtos_tablayout)
    TabLayout tabLayout;

    @Bind(R.id.produtos_viewpager)
    ViewPager viewPager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produtos);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tabLayout.addTab(tabLayout.newTab().setText("Produtos"));
        tabLayout.addTab(tabLayout.newTab().setText("Estoque"));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        List<Fragment> fragments = Arrays.asList(new ProdutosFragment(), new EstoqueFragment());
        viewPager.setAdapter(new ProdutosAdapter(getSupportFragmentManager(), fragments));

        Infra.trocaFragmentCliqueNoTab(tabLayout, viewPager);

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        finish();

        return super.onOptionsItemSelected(item);
    }
}
