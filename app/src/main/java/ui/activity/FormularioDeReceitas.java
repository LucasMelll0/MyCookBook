package ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.widget.AppCompatImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.mycookbook.R;
import com.example.mycookbook.activity.model.Receita;

import dao.ReceitaDAO;

public class FormularioDeReceitas extends AppCompatActivity {

    private EditText campoNome;
    private EditText campoIngredientes;
    private EditText campoDescricao;
    private EditText campoPorcao;
    private Toolbar toolbar;
    private final ReceitaDAO dao = new ReceitaDAO();
    private Receita receita;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_de_receitas);
        inicializadorDosCampos();
        configuraBotaoSalvar();
        configuraBotaoExcluir();
        configuraToolBar();
        carregaReceita();
    }

    private void carregaReceita(){
        Intent dados = getIntent();
        if (dados.hasExtra("receita")){
            toolbar.setTitle("Editando Receita");
            receita = (Receita) dados.getSerializableExtra("receita");
            campoNome.setText(receita.getNome());
            campoIngredientes.setText(receita.getIngredientes());
            campoDescricao.setText(receita.getModoDePreparo());
            campoPorcao.setText(receita.getPorcao());
        }else{
            receita = new Receita();
        }
    }

    private void configuraToolBar(){
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Nova Receita");

    }

    private void configuraBotaoSalvar(){
        Button botaoSalvar = findViewById(R.id.button_salvar_receita);
        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalizaFormulario();
            }
        });
    }

    private void configuraBotaoExcluir(){
        AppCompatImageButton botaoExcluir = findViewById(R.id.button_excluir_receita);
        botaoExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(receita.temIdValido()){
                    dao.exclui(receita);
                    finish();
                }else{
                    finish();
                }
            }
        });
    }

    private void inicializadorDosCampos(){
        campoNome = findViewById(R.id.edittext_nome_da_receita);
        campoIngredientes = findViewById(R.id.edittext_ingredientes_da_receitas);
        campoDescricao = findViewById(R.id.edittext_descricao_da_receitas);
        campoPorcao = findViewById(R.id.edittext_porcao_da_receita);
    }

    private void finalizaFormulario() {
        preencheReceita();
        if(receita.temIdValido()){
            dao.edita(receita);
            Intent voltaParaMostraReceita = new Intent(FormularioDeReceitas.this, MostraReceita.class);
            voltaParaMostraReceita.putExtra("receita", receita);
            startActivity(voltaParaMostraReceita);
        }else{
            dao.salva(receita);
        }
        finish();

    }

    private void preencheReceita(){
        String nome = campoNome.getText().toString();
        String ingredientes = campoIngredientes.getText().toString();
        String descricao = campoDescricao.getText().toString();
        String porcao = campoPorcao.getText().toString();

        receita.setNome(nome);
        receita.setIngredientes(ingredientes);
        receita.setModoDePreparo(descricao);
        receita.setPorcao(porcao);

    }

}
