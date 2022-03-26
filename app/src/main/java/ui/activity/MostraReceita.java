package ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.mycookbook.R;
import com.example.mycookbook.activity.model.Receita;

public class MostraReceita extends AppCompatActivity {

    private TextView campoNome;
    private TextView campoIngredientes;
    private TextView campoDescricao;
    private TextView campoPorcao;
    private TextView campoCategoria;
    private Toolbar toolbar;
    private Receita receita;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostra_receita);
        inicializadorDosCampos();
        carregaReceita();
        mudaToolBar();
        configuraBotaoVoltar();
        configuraBotaoEditar();

    }

    @Override
    protected void onResume() {
        super.onResume();
        carregaReceita();
    }

    private void inicializadorDosCampos(){
        campoNome = findViewById(R.id.textview_mostra_nome_receita);
        campoIngredientes = findViewById(R.id.textview_mostra_ingredientes_receita);
        campoDescricao = findViewById(R.id.textview_mostra_descricao_receita);
        campoPorcao = findViewById(R.id.textview_mostra_porcao);
        campoCategoria = findViewById(R.id.textview_mostra_categoria);
    }

    private void carregaReceita(){
        Intent dados = getIntent();

        receita = (Receita) dados.getSerializableExtra("receita");

        campoNome.setText(receita.getNome());
        campoIngredientes.setText(receita.getIngredientes());
        campoDescricao.setText(receita.getModoDePreparo());
        campoPorcao.setText(receita.getPorcao());
        campoCategoria.setText(receita.getCategoria());
    }

    private void mudaToolBar(){
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");

    }

    private void configuraBotaoVoltar(){

        Button botaoVoltar = findViewById(R.id.button_voltar_lista_de_receitas);

        botaoVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent voltaListareceita = new Intent(MostraReceita.this, ListaDeReceitas.class);
                startActivity(voltaListareceita);
            }
        });

    }

    private void configuraBotaoEditar(){

        Button botaoEditar = findViewById(R.id.button_editar_receita);

        botaoEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent abreFormularioModoEditaReceita = new Intent(MostraReceita.this, FormularioDeReceitas.class);
                Intent dados = getIntent();
                Receita receitaParaEditar = (Receita) dados.getSerializableExtra("receita");
                abreFormularioModoEditaReceita.putExtra("receita", receitaParaEditar);
                startActivity(abreFormularioModoEditaReceita);
                finish();
            }
        });
    }

}
