package br.com.caelum.gerenciadorapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.caelum.gerenciadorapp.R;
import br.com.caelum.gerenciadorapp.adapter.EstoqueAdapter;
import br.com.caelum.gerenciadorapp.dao.ProdutosEstoqueDao;
import br.com.caelum.gerenciadorapp.modelo.ProdutoEstoque;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by matheus on 24/05/16.
 */
public class EstoqueFragment extends Fragment {


    @Bind(R.id.recycler_estoque_activity)
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.estoque_fragment, container, false);
        ButterKnife.bind(this, view);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        ProdutosEstoqueDao dao = new ProdutosEstoqueDao(getContext());
        List<ProdutoEstoque> produtosEstoque = dao.listar();
        dao.close();

        recyclerView.setAdapter(new EstoqueAdapter(getContext(), produtosEstoque));

    }
}
