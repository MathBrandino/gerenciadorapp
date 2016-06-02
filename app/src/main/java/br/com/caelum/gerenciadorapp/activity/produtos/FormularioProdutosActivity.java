package br.com.caelum.gerenciadorapp.activity.produtos;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import br.com.caelum.gerenciadorapp.R;
import br.com.caelum.gerenciadorapp.dao.ProdutoDao;
import br.com.caelum.gerenciadorapp.modelo.Produto;
import butterknife.Bind;
import butterknife.ButterKnife;

public class FormularioProdutosActivity extends AppCompatActivity {


    @Bind(R.id.formulario_produto_nome)
    TextInputEditText nome;

    @Bind(R.id.formulario_produto_descricao)
    TextInputEditText descricao;

    @Bind(R.id.formulario_produto_preco)
    TextInputEditText preco;

    private Produto produto = new Produto();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_produtos);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().hasExtra("produto")) {
            produto = (Produto) getIntent().getSerializableExtra("produto");
            populaFormulario();
        }

    }

    private void populaFormulario() {
        nome.setText(produto.getNome());
        descricao.setText(produto.getDescricao());
        preco.setText(produto.getPreco().toString());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_formulario, menu);

        return super.onCreateOptionsMenu(menu);
    }


    private boolean validaFormulario() {

        boolean hasNome = hasNome();
        if (!hasNome) {
            mostraErroNome();
        }

        boolean hasDescricao = hasDescricao();

        if (!hasDescricao) {
            mostraErroDescricao();
        }

        boolean hasPreco = hasPreco();

        if (!hasPreco) {
            mostraErroPreco();
        }

        return hasNome && hasDescricao && hasPreco;
    }

    private void mostraErroPreco() {
        TextInputLayout parent = (TextInputLayout) preco.getParent();
        parent.setError("Preço inválido ");
    }

    private void mostraErroDescricao() {
        TextInputLayout parent = (TextInputLayout) descricao.getParent();
        parent.setError("Descrição inválida ");
    }

    private void mostraErroNome() {
        TextInputLayout parent = (TextInputLayout) nome.getParent();
        parent.setError("Nome inválido ");
    }

    private boolean hasPreco() {
        return !preco.getText().toString().trim().isEmpty();
    }

    private boolean hasDescricao() {
        return !descricao.getText().toString().trim().isEmpty();
    }

    private boolean hasNome() {
        return !nome.getText().toString().trim().isEmpty();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:

                finish();

                return true;

            case R.id.menu_salvar:

                if (validaFormulario()) {

                    produto = geraProduto();

                    ProdutoDao dao = new ProdutoDao(this);

                    if (produto.getId() == null) {
                        dao.adiciona(produto);
                    } else {
                        dao.altera(produto);
                    }

                    dao.close();
                    finish();
                }

                return true;
            default:
                return true;
        }

    }

    private Produto geraProduto() {

        produto.setNome(nome.getText().toString().trim());
        produto.setDescricao(descricao.getText().toString().trim());
        produto.setPreco(Double.valueOf(preco.getText().toString()));

        return produto;
    }
}
