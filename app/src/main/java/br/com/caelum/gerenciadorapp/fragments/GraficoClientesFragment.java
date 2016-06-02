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
import com.db.chart.view.HorizontalBarChartView;

import java.util.List;

import br.com.caelum.gerenciadorapp.R;
import br.com.caelum.gerenciadorapp.dao.ClienteDao;
import br.com.caelum.gerenciadorapp.dao.VendasDao;
import br.com.caelum.gerenciadorapp.modelo.Cliente;
import br.com.caelum.gerenciadorapp.modelo.ClienteQtd;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by matheus on 02/06/16.
 */
public class GraficoClientesFragment extends Fragment {

    @Bind(R.id.grafico_clientes)
    HorizontalBarChartView grafico;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_grafico_clientes, container, false);

        ButterKnife.bind(this, view);

        BarSet barSet = new BarSet();

        VendasDao dao = new VendasDao(getContext());
        List<ClienteQtd> clienteQtds = dao.listarClienteQtdVenda();
        dao.close();

        int contador = 1;

        for (ClienteQtd clienteQtd : clienteQtds) {

            ClienteDao clienteDao = new ClienteDao(getContext());
            Cliente cliente = clienteDao.recuperaCliente(clienteQtd.getId());
            clienteDao.close();

            Bar bar = new Bar(cliente.getNome(), clienteQtd.getQtd());
            bar.setColor(contador % 2 == 0 ? Color.YELLOW : Color.RED);
            contador++;

            barSet.addBar(bar);
        }

        grafico.addData(barSet);

        grafico.show();
        return view;
    }
}
