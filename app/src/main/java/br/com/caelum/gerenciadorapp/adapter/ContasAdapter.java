package br.com.caelum.gerenciadorapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.caelum.gerenciadorapp.R;
import br.com.caelum.gerenciadorapp.activity.contas.CadastroPagamentoActivity;
import br.com.caelum.gerenciadorapp.dao.PagamentoDao;
import br.com.caelum.gerenciadorapp.dao.VendasDao;
import br.com.caelum.gerenciadorapp.modelo.Conta;
import br.com.caelum.gerenciadorapp.modelo.ItemVenda;
import br.com.caelum.gerenciadorapp.modelo.Pagamento;
import br.com.caelum.gerenciadorapp.modelo.Venda;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by matheus on 17/05/16.
 */
public class ContasAdapter extends RecyclerView.Adapter {


    private List<Conta> contas;
    private Context context;

    public ContasAdapter(List<Conta> contas, Context context) {
        this.contas = contas;

        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_conta, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        ViewHolder holder = (ViewHolder) viewHolder;
        Conta conta = contas.get(position);

        Venda venda = pegaVenda(conta);

        double valorTotal = calculaValorTotal(venda);

        double valorPago = calculaValorPago(conta);

        double valorRecebido = calculaValorRecebido(conta);

        holder.itemVenda.setText(conta.getIdVenda().toString());
        holder.valorTotal.setText("R$ " + valorTotal);
        holder.valorPago.setText("R$ " + valorPago);
        holder.dataVenda.setText(venda.getDataVenda());
        holder.valorRecebido.setText("R$ " + valorRecebido);

        holder.valorRecebido.setTextColor(ColorStateList.valueOf(Color.GREEN));
        holder.valorPago.setTextColor(ColorStateList.valueOf(Color.GREEN));
        if (valorPago != valorTotal)
            holder.valorPago.setTextColor(ColorStateList.valueOf(Color.RED));

        if (valorTotal != valorRecebido)
            holder.valorRecebido.setTextColor(ColorStateList.valueOf(Color.RED));

    }

    private double calculaValorRecebido(Conta conta) {

        PagamentoDao dao = new PagamentoDao(context);
        List<Pagamento> pagamentos = dao.listarPelo(conta.getId());
        dao.close();

        double valorRecebido = 0;
        for (Pagamento pagamento : pagamentos) {
            if (pagamento.getPago())
                valorRecebido += pagamento.getValor();
        }
        return valorRecebido;
    }

    private Venda pegaVenda(Conta conta) {
        VendasDao dao = new VendasDao(context);
        Venda venda = dao.buscaVendaPelo(conta.getIdVenda());
        dao.close();
        return venda;
    }

    private double calculaValorTotal(Venda venda) {


        List<ItemVenda> itens = venda.getItens();
        double valorTotal = 0;

        for (ItemVenda item : itens) {
            valorTotal += item.getValorUnitarioVendido() * item.getQuantidade();
        }
        return valorTotal;
    }

    private double calculaValorPago(Conta conta) {
        PagamentoDao pagamentoDao = new PagamentoDao(context);
        List<Pagamento> pagamentos = pagamentoDao.listarPelo(conta.getId());
        pagamentoDao.close();

        double valorPago = 0;

        for (Pagamento pagamento : pagamentos) {
            valorPago += pagamento.getValor();
        }
        return valorPago;
    }

    @Override
    public int getItemCount() {
        return contas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.item_conta_id_venda)
        TextView itemVenda;

        @Bind(R.id.item_conta_valor_total)
        TextView valorTotal;

        @Bind(R.id.item_conta_valor_pago)
        TextView valorPago;

        @Bind(R.id.item_conta_data_venda)
        TextView dataVenda;

        @Bind(R.id.item_conta_valor_recebido)
        TextView valorRecebido;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        @OnClick(R.id.item_conta)
        public void clickItem() {
            Conta conta = contas.get(getAdapterPosition());
            Intent intent = new Intent(context, CadastroPagamentoActivity.class);
            intent.putExtra("conta", conta);
            context.startActivity(intent);
        }
    }
}
