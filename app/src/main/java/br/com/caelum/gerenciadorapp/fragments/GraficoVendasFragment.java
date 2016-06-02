package br.com.caelum.gerenciadorapp.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.db.chart.model.LineSet;
import com.db.chart.model.Point;
import com.db.chart.view.LineChartView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.caelum.gerenciadorapp.R;
import br.com.caelum.gerenciadorapp.dao.VendasDao;
import br.com.caelum.gerenciadorapp.modelo.Venda;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by matheus on 31/05/16.
 */
public class GraficoVendasFragment extends Fragment {

    @Bind(R.id.grafico_vendas)
    LineChartView grafico;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grafico_vendas, container, false);

        ButterKnife.bind(this, view);

        VendasDao dao = new VendasDao(getContext());
        List<Venda> vendas = dao.listar();
        dao.close();

        if (vendas.size() > 0) {
            List<Venda> doDia = new ArrayList<>();


            Calendar dia = geraData(vendas.get(0));

            LineSet dataSet = new LineSet();

            for (Venda venda : vendas) {

                if (venda.isMesmoDia(dia)) {
                    doDia.add(venda);
                } else {
                    dia = geraData(venda);

                    dataSet.addPoint(new Point(doDia.get(0).getDataVenda(), doDia.size()));

                    doDia = new ArrayList<>();
                    doDia.add(venda);
                }

            }

            dataSet.addPoint(new Point(doDia.get(0).getDataVenda(), doDia.size()));
            dataSet.setColor(Color.BLUE);
            grafico.addData(dataSet);

            grafico.setBackgroundColor(Color.TRANSPARENT);
            grafico.show();
        }

        return view;
    }


    @NonNull
    private Calendar geraData(Venda venda) {
        String dataVenda = venda.getDataVenda();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = simpleDateFormat.parse(dataVenda);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long time = date.getTime();

        Calendar data = Calendar.getInstance();
        data.setTimeInMillis(time);
        return data;
    }
}
