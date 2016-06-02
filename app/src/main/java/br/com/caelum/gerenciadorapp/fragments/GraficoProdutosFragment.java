package br.com.caelum.gerenciadorapp.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.db.chart.model.Bar;
import com.db.chart.model.BarSet;
import com.db.chart.view.BarChartView;

import java.util.List;

import br.com.caelum.gerenciadorapp.R;
import br.com.caelum.gerenciadorapp.dao.ProdutoDao;
import br.com.caelum.gerenciadorapp.dao.VendasProdutoDao;
import br.com.caelum.gerenciadorapp.modelo.Produto;
import br.com.caelum.gerenciadorapp.modelo.ProdutoQtd;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by matheus on 02/06/16.
 */
public class GraficoProdutosFragment extends Fragment {

    @Bind(R.id.grafico_produtos)
    BarChartView grafico;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grafico_produtos, container, false);
        ButterKnife.bind(this, view);

        BarSet barSet = new BarSet();
        int contador = 1;

        VendasProdutoDao dao = new VendasProdutoDao(getContext());
        List<ProdutoQtd> qtds = dao.listar();
        dao.close();


        for (ProdutoQtd qtd : qtds) {
            ProdutoDao produtoDao = new ProdutoDao(getContext());
            Produto produto = produtoDao.recuperaProduto(qtd.getIdProduto());
            produtoDao.close();


            Bar bar = new Bar(produto.getNome(), qtd.getQtd());
            bar.setColor(contador % 2 == 0 ? Color.BLUE : Color.GREEN);
            barSet.addBar(bar);
            contador++;
        }

        grafico.addData(barSet);
        grafico.show();

        return view;
    }
}
