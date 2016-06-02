package br.com.caelum.gerenciadorapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.caelum.gerenciadorapp.R;
import br.com.caelum.gerenciadorapp.activity.produtos.FormularioProdutosActivity;
import br.com.caelum.gerenciadorapp.adapter.ListaProdutosAdapter;
import br.com.caelum.gerenciadorapp.dao.ProdutoDao;
import br.com.caelum.gerenciadorapp.modelo.Produto;
import br.com.caelum.gerenciadorapp.util.Infra;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by matheus on 24/05/16.
 */
public class ProdutosFragment extends Fragment {

    @Bind(R.id.recycler_produto_activity)
    RecyclerView recyclerView;

    @Bind(R.id.fab_produto_activity)
    FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.lista_produtos_fragment, container, false);
        ButterKnife.bind(this, view);


        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        Infra.escondeFABNoScroll(recyclerView, fab);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        ProdutoDao dao = new ProdutoDao(getContext());
        List<Produto> produtos = dao.listar();
        dao.close();

        recyclerView.setAdapter(null);
        ListaProdutosAdapter adapter = new ListaProdutosAdapter(getContext(), produtos);

        recyclerView.setAdapter(adapter);

    }


    @OnClick(R.id.fab_produto_activity)
    public void onClickFab() {

        Intent intent = new Intent(getContext(), FormularioProdutosActivity.class);
        startActivity(intent);

    }
}

