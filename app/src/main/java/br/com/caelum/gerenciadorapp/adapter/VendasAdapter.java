package br.com.caelum.gerenciadorapp.adapter;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import br.com.caelum.gerenciadorapp.R;
import br.com.caelum.gerenciadorapp.dao.ClienteDao;
import br.com.caelum.gerenciadorapp.modelo.Cliente;
import br.com.caelum.gerenciadorapp.modelo.ItemVenda;
import br.com.caelum.gerenciadorapp.modelo.Venda;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnLongClick;

/**
 * Created by matheus on 04/05/16.
 */
public class VendasAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Venda> vendas;

    public VendasAdapter(Context context, List<Venda> vendas) {
        this.context = context;
        this.vendas = vendas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_venda, parent, false);
        ViewHolder holder = new ViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        ViewHolder holder = (ViewHolder) viewHolder;

        Venda venda = vendas.get(position);

        ClienteDao dao = new ClienteDao(context);
        Cliente cliente = dao.recuperaCliente(venda.getIdCliente());
        dao.close();


        holder.cliente.setText(cliente.getNome());
        holder.identificador.setText("Venda nº " + venda.getId());
        holder.data.setText(venda.getDataVenda());

        Double valorTotal = 0.0;

        for (ItemVenda itemVenda : venda.getItens()) {

            valorTotal += itemVenda.getValorUnitarioVendido() * itemVenda.getQuantidade();

        }

        holder.valorTotal.setText("R$ " + valorTotal.toString());


    }


    @Override
    public int getItemCount() {
        return vendas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.item_venda_identificador)
        TextView identificador;

        @Bind(R.id.item_venda_cliente)
        TextView cliente;

        @Bind(R.id.item_venda_data)
        TextView data;

        @Bind(R.id.item_venda_valor_total)
        TextView valorTotal;


        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }


        @OnLongClick(R.id.item_venda)
        public boolean onLongClickItem() {

            Venda venda = vendas.get(getAdapterPosition());
            List<ItemVenda> itens = venda.getItens();

            ListView lista = new ListView(context);
            lista.setAdapter(new ArrayAdapter<ItemVenda>(context, android.R.layout.simple_list_item_1, itens));

            new AlertDialog.Builder(context)
                    .setView(lista)
                    .setMessage("Produtos Vendidos")
                    .show();


            return true;
        }
    }
}
