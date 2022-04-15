package com.example.mycookbook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mycookbook.R;
import com.example.mycookbook.customViews.TextGradient;
import com.example.mycookbook.model.Receita;

import java.util.ArrayList;

public class MostraReceita extends AppCompatActivity {

    private TextView campoNome;
    private LinearLayout campoIngredientes;
    private TextView campoDescricao;
    private TextView campoPorcao;
    private TextView campoCategoria;
    private TextGradient toolbar;
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
        configuraCategoria();

    }

    private void configuraCategoria() {
        campoCategoria.setText(receita.getCategoria());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void inicializadorDosCampos() {
        campoNome = findViewById(R.id.textview_mostra_nome_receita);
        campoIngredientes = findViewById(R.id.linearlayout_mostra_ingredientes_receita);
        campoDescricao = findViewById(R.id.textview_mostra_descricao_receita);
        campoPorcao = findViewById(R.id.textview_mostra_porcao);
        campoCategoria = findViewById(R.id.textview_mostra_categoria);
    }

    private void carregaReceita() {
        Intent dados = getIntent();
        receita = (Receita) dados.getSerializableExtra("receita");
        carregaIngredientes();
        campoNome.setText(receita.getNome());
        campoDescricao.setText(receita.getModoDePreparo());
        campoPorcao.setText(receita.getPorcao());
        campoCategoria.setText(receita.getCategoria());
    }

    private void carregaIngredientes() {
        ArrayList<String> ingredientes = receita.getIngredientes();
        Log.i("Testes", "carregaIngredientes: " + ingredientes);

        for (int i = 0; i < ingredientes.size(); i++) {
            LinearLayout linearHorizontal = new LinearLayout(this);
            linearHorizontal.setOrientation(LinearLayout.HORIZONTAL);
            linearHorizontal.setId(i);

            TextView ingrediente = new TextView(this);
            ImageView seta = new ImageView(this);
            seta.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_right));
            seta.setBackground(getResources().getDrawable(R.drawable.button_effect));
            ingrediente.setId(i);
            ingrediente.setText(ingredientes.get(i));
            ingrediente.setTextSize(24);
            campoIngredientes.addView(linearHorizontal);
            linearHorizontal.addView(seta);
            linearHorizontal.addView(ingrediente);

        }
    }

    private void mudaToolBar() {
        toolbar = findViewById(R.id.TextView_toolBar);
        toolbar.setText("");


    }

    private void configuraBotaoVoltar() {

        Button botaoVoltar = findViewById(R.id.button_voltar_lista_de_receitas);

        botaoVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent voltaListareceita = new Intent(MostraReceita.this, ListaDeReceitas.class);
                startActivity(voltaListareceita);
            }
        });

    }

    private void configuraBotaoEditar() {

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
