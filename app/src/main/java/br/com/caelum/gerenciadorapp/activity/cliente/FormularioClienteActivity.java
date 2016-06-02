package br.com.caelum.gerenciadorapp.activity.cliente;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import br.com.caelum.gerenciadorapp.R;
import br.com.caelum.gerenciadorapp.dao.ClienteDao;
import br.com.caelum.gerenciadorapp.dao.ClienteTelefoneDao;
import br.com.caelum.gerenciadorapp.modelo.Cliente;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemLongClick;

/**
 * Created by matheus on 02/05/16.
 */
public class FormularioClienteActivity extends AppCompatActivity {

    @Bind(R.id.formulario_cliente_nome)
    TextInputEditText nome;
    @Bind(R.id.formulario_cliente_telefone)
    TextInputEditText telefone;
    @Bind(R.id.formulario_cliente_lista_telefones)
    ListView listaTelefones;
    List<String> telefones = new ArrayList<>();
    ArrayAdapter<String> adapter;
    private Cliente cliente = new Cliente();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formulario_cliente_activity);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if (getIntent().hasExtra("cliente")) {
            cliente = (Cliente) getIntent().getSerializableExtra("cliente");
            populaDados();
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, telefones);
        listaTelefones.setAdapter(this.adapter);


    }

    private void populaDados() {
        nome.setText(cliente.getNome());
        telefones = cliente.getTelefones();

    }


    @OnClick(R.id.formulario_cliente_botao_add_numero)
    public void clickBotao() {

        if (validaNumero()) {
            String numero = this.telefone.getText().toString().trim();
            telefones.add(numero);
            adapter.notifyDataSetChanged();
            telefone.setText("");
        } else {
            mostaErroTelefone();
        }


    }


    private boolean validaNome() {
        boolean isEmpty = nome.getText().toString().trim().isEmpty();

        return !isEmpty;

    }

    private boolean validaNumero() {
        boolean isEmpty = telefone.getText().toString().trim().isEmpty();

        return !isEmpty;

    }

    private void mostaErroNome() {
        TextInputLayout parent = (TextInputLayout) nome.getParent();
        parent.setError("Nome inválido");
    }

    private void mostaErroTelefone() {
        TextInputLayout parent = (TextInputLayout) telefone.getParent();
        parent.setError("Telefone inválido");
    }

    @OnItemLongClick(R.id.formulario_cliente_lista_telefones)
    public boolean clickLista(int position) {

        final String telefone = (String) listaTelefones.getItemAtPosition(position);

        new AlertDialog.Builder(this).setTitle("Deletar : " + telefone + " ?")
                .setMessage("Você irá perder este número, tem certeza ?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ClienteTelefoneDao dao = new ClienteTelefoneDao(FormularioClienteActivity.this);
                        dao.deleta(telefone);
                        dao.close();

                        telefones.remove(telefone);

                        adapter.notifyDataSetChanged();
                    }
                }).setNegativeButton("Não", null)
                .show();


        return true;

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_formulario, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_salvar:

                if (validaNome()) {

                    Cliente cliente = geraCliente();
                    ClienteDao dao = new ClienteDao(this);


                    if (cliente.getId() == null)
                        dao.insere(cliente);
                    else
                        dao.altera(cliente);

                    dao.close();

                    finish();
                } else {
                    mostaErroNome();
                }
                return true;


            case android.R.id.home:
                finish();
            default:
                return false;
        }

    }


    private Cliente geraCliente() {

        cliente.setTelefones(telefones);
        cliente.setNome(nome.getText().toString().trim());

        return cliente;

    }
}
