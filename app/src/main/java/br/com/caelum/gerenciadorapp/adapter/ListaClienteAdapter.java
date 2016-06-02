package br.com.caelum.gerenciadorapp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import br.com.caelum.gerenciadorapp.R;
import br.com.caelum.gerenciadorapp.activity.cliente.FormularioClienteActivity;
import br.com.caelum.gerenciadorapp.dao.ClienteDao;
import br.com.caelum.gerenciadorapp.modelo.Cliente;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

/**
 * Created by matheus on 02/05/16.
 */
public class ListaClienteAdapter extends RecyclerView.Adapter {


    private Context context;
    private List<Cliente> clientes;

    public ListaClienteAdapter(Context context, List<Cliente> clientes) {

        this.context = context;
        this.clientes = clientes;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_cliente, parent, false);

        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        ViewHolder holder = (ViewHolder) viewHolder;
        Cliente cliente = clientes.get(position);

        holder.nome.setText(cliente.getNome());


    }

    @Override
    public int getItemCount() {
        return clientes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {


        @Bind(R.id.item_cliente_nome)
        TextView nome;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        @OnClick(R.id.item_cliente)
        public void cliqueCard() {

            Intent intent = new Intent(context, FormularioClienteActivity.class);
            intent.putExtra("cliente", clientes.get(getAdapterPosition()));

            context.startActivity(intent);


        }

        @OnLongClick(R.id.item_cliente)
        public boolean clickCard() {

            final Cliente cliente = clientes.get(getAdapterPosition());

            new AlertDialog.Builder(context)
                    .setTitle("Cliente : " + cliente.getNome())
                    .setMessage("O que você deseja fazer ?")
                    .setNegativeButton("Excluir", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            ClienteDao dao = new ClienteDao(context);
                            dao.deleta(cliente);
                            dao.close();

                            clientes.remove(cliente);

                            notifyDataSetChanged();
                        }
                    })
                    .setPositiveButton("Ligar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            ListView view = new ListView(context);
                            view.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, cliente.getTelefones()));

                            view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent intent = new Intent(Intent.ACTION_CALL);
                                    intent.setData(Uri.parse("tel:" + cliente.getTelefones().get(position)));
                                    context.startActivity(intent);
                                }
                            });

                            new AlertDialog.Builder(context)
                                    .setTitle("Para qual numero deseja ligar ?")
                                    .setView(view)
                                    .show();


                        }
                    })
                    .show();


            return true;
        }
    }

}
