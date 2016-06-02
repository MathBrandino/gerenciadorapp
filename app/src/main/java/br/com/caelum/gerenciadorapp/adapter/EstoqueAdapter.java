package br.com.caelum.gerenciadorapp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import br.com.caelum.gerenciadorapp.R;
import br.com.caelum.gerenciadorapp.dao.ProdutoDao;
import br.com.caelum.gerenciadorapp.dao.ProdutosEstoqueDao;
import br.com.caelum.gerenciadorapp.modelo.Produto;
import br.com.caelum.gerenciadorapp.modelo.ProdutoEstoque;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by matheus on 04/05/16.
 */
public class EstoqueAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<ProdutoEstoque> produtosEstoque;

    public EstoqueAdapter(Context context, List<ProdutoEstoque> produtosEstoque) {
        this.context = context;
        this.produtosEstoque = produtosEstoque;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_estoque, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        ViewHolder holder = (ViewHolder) viewHolder;

        ProdutoEstoque estoque = produtosEstoque.get(position);

        ProdutoDao dao = new ProdutoDao(context);
        Produto produto = dao.recuperaProduto(estoque.getIdProduto());
        dao.close();

        holder.nomeProduto.setText(produto.getNome());
        holder.quantidade.setText(estoque.getQuantidade().toString());
        holder.descricaoProduto.setText(produto.getDescricao());
    }

    @Override
    public int getItemCount() {
        return produtosEstoque.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.item_estoque_quantidade)
        TextView quantidade;

        @Bind(R.id.item_estoque_nome_produto)
        TextView nomeProduto;

        @Bind(R.id.item_estoque_descricao)
        TextView descricaoProduto;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }


        @OnClick(R.id.item_estoque)
        public void onClickCard() {

            final EditText view = new EditText(context);
            view.setHint("Quantidade");
            view.setInputType(InputType.TYPE_CLASS_NUMBER);
            new AlertDialog.Builder(context)
                    .setMessage("Qual é a quantidade que você quer adicionar?")
                    .setView(view)
                    .setNegativeButton("Cancelar", null)
                    .setPositiveButton("Atualizar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Integer entrada = Integer.valueOf(view.getText().toString());

                            ProdutoEstoque estoque = produtosEstoque.get(getAdapterPosition());
                            estoque.setQuantidade(estoque.getQuantidade() + entrada);

                            ProdutosEstoqueDao dao = new ProdutosEstoqueDao(context);
                            dao.atualiza(estoque.getIdProduto(), estoque.getQuantidade());
                            dao.close();

                            notifyDataSetChanged();
                        }

                    })
                    .show();
        }

    }
}
