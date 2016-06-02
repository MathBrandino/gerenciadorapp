package br.com.caelum.gerenciadorapp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.caelum.gerenciadorapp.R;
import br.com.caelum.gerenciadorapp.activity.produtos.FormularioProdutosActivity;
import br.com.caelum.gerenciadorapp.dao.ProdutoDao;
import br.com.caelum.gerenciadorapp.modelo.Produto;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

/**
 * Created by matheus on 03/05/16.
 */
public class ListaProdutosAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<Produto> produtos;

    public ListaProdutosAdapter(Context context, List<Produto> produtos) {

        this.context = context;
        this.produtos = produtos;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_produto, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        ViewHolder holder = (ViewHolder) viewHolder;

        Produto produto = produtos.get(position);

        holder.descricao.setText(produto.getDescricao());
        holder.nome.setText(produto.getNome());
        holder.preco.setText("R$ " + produto.getPreco().toString());

    }

    @Override
    public int getItemCount() {
        return produtos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {


        @Bind(R.id.item_produto_nome)
        TextView nome;

        @Bind(R.id.item_produto_descricao)
        TextView descricao;

        @Bind(R.id.item_produto_preco)
        TextView preco;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


        @OnClick(R.id.item_produto)
        public void onClick() {
            Produto produto = produtos.get(getAdapterPosition());

            Intent intent = new Intent(context, FormularioProdutosActivity.class);
            intent.putExtra("produto", produto);
            context.startActivity(intent);

        }

        @OnLongClick(R.id.item_produto)
        public boolean onLongClick() {
            final Produto produto = produtos.get(getAdapterPosition());

            new AlertDialog.Builder(context)
                    .setTitle(produto.getNome())
                    .setMessage("Deseja excluir ?")
                    .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            ProdutoDao dao = new ProdutoDao(context);
                            dao.deleta(produto);
                            dao.close();

                            produtos.remove(produto);

                            notifyDataSetChanged();

                        }
                    })
                    .setNegativeButton("Não", null)
                    .show();

            return true;
        }
    }
}
