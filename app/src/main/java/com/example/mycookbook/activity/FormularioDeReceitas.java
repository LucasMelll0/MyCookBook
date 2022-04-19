package com.example.mycookbook.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import com.example.mycookbook.dataBase.ReceitasDBHelper;
import com.example.mycookbook.R;
import com.example.mycookbook.customViews.TextGradient;
import com.example.mycookbook.dao.ReceitaDAO;
import com.example.mycookbook.model.Receita;

import java.util.ArrayList;

public class FormularioDeReceitas extends AppCompatActivity {

    private EditText campoNome;
    private EditText campoIngredientes;
    private EditText campoDescricao;
    private EditText campoPorcao;
    private TextGradient toolbarText;
    private LinearLayout linearLayout;
    private int campoCategoria;
    private Spinner spinner;
    private String categoria;
    private final ReceitaDAO dao = new ReceitaDAO();
    private Receita receita;
    private ReceitasDBHelper db = new ReceitasDBHelper(this);
    private ArrayList<String> ingredientes = new ArrayList<>();
    private int idIngrediente = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_de_receitas);
        inicializadorDosCampos();
        configuraBotaoSalvar();
        configuraBotaoExcluir();
        configuraToolBar();
        configuraSpinner();
        carregaReceita();

    }

    private void configuraSpinner() {
        spinner = findViewById(R.id.spinner_categoria);

        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(this, R.array.categorias_array, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        adapterSpinner.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSpinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                campoCategoria = position;
                String[] categorias = getResources().getStringArray(R.array.categorias_array);
                categoria = categorias[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                campoCategoria = 0;
            }
        });

    }

    private void carregaReceita() {
        Intent dados = getIntent();
        if (dados.hasExtra("receita")) {
            toolbarText.setText("Editando Receita");
            receita = (Receita) dados.getSerializableExtra("receita");
            Log.i("Veio os Extras?", "Sim " + receita.getId());
            campoNome.setText(receita.getNome());
            campoDescricao.setText(receita.getModoDePreparo());
            campoPorcao.setText(receita.getPorcao());
            carregaIngredientes();
            String[] categorias = getResources().getStringArray(R.array.categorias_array);
            for (int i = 0; i < categorias.length; i++) {
                if (receita.getCategoria().equals(categorias[i])) {
                    spinner.setSelection(i);
                    break;
                }
            }
        } else{
            Log.i("Veio os Extras?", "Não");
        receita = new Receita();
    }
    }

    private void carregaIngredientes() {
        for (int i = 0; i < receita.getIngredientes().size(); i++) {
            String ingrediente = receita.getIngredientes().get(i);

            configuraAdicaoRemocaoDeIngredientes(ingrediente);
        }
    }

    private void configuraAdicaoRemocaoDeIngredientes(String ingrediente) {
        LinearLayout horizontal = new LinearLayout(this);
        linearLayout.addView(horizontal);
        horizontal.setOrientation(LinearLayout.HORIZONTAL);
        horizontal.setId(idIngrediente);
        ImageView seta = new ImageView(this);
        seta.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_right));
        seta.setBackground(getResources().getDrawable(R.drawable.button_effect));
        seta.setId(idIngrediente);
        TextView textIngrediente = new TextView(this);
        textIngrediente.setId(idIngrediente);
        textIngrediente.setText(ingrediente);
        textIngrediente.setTextSize(25);
        textIngrediente.setTextColor(getColor(R.color.black));
        ImageButton buttonExcluirIngrediente = new ImageButton(this);
        buttonExcluirIngrediente.setId(idIngrediente);
        buttonExcluirIngrediente.setImageDrawable(getResources().getDrawable(R.drawable.ic_remove));
        buttonExcluirIngrediente.setBackground(getResources().getDrawable(R.drawable.button_effect));
        horizontal.addView(seta);
        horizontal.addView(textIngrediente);
        horizontal.addView(buttonExcluirIngrediente);
        idIngrediente++;
        ingredientes.add(ingrediente);
        buttonExcluirIngrediente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int ingredientePosicao = ingredientes.indexOf(ingrediente);
                horizontal.removeView(seta);
                horizontal.removeView(textIngrediente);
                horizontal.removeView(view);
                ingredientes.remove(ingredientePosicao);
                idIngrediente--;
                Log.i("Testes", "Igrediente removido: " + ingredientes + " ID ATUAL: " + idIngrediente);
            }
        });
        campoIngredientes.setText("");
    }


    private void configuraToolBar() {
        toolbarText = findViewById(R.id.TextView_toolBar);
        toolbarText.setText("Nova Receita");

    }

    private void configuraBotaoSalvar() {
        Button botaoSalvar = findViewById(R.id.button_salvar_receita);
        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalizaFormulario();
            }
        });
    }

    private void configuraBotaoExcluir() {
        AppCompatImageButton botaoExcluir = findViewById(R.id.button_excluir_receita);
        botaoExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                configuraAlertDialogParaRemocao();
            }
        });
    }

    private void configuraAlertDialogParaRemocao() {
        AlertDialog.Builder confirmacaoDeRemocao = new AlertDialog.Builder(FormularioDeReceitas.this);
        confirmacaoDeRemocao.setTitle("Excluir Receita");
        confirmacaoDeRemocao.setMessage("Deseja realmente excluir a receita permanentemente?");
        confirmacaoDeRemocao.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (receita.temIdValido()) {
                    dao.exclui(receita, db);
                    finish();
                } else {
                    finish();
                }
            }
        });
        confirmacaoDeRemocao.setNegativeButton("Cancelar", null);
        confirmacaoDeRemocao.show();
    }


    private void inicializadorDosCampos() {
        campoNome = findViewById(R.id.edittext_nome_da_receita);
        campoIngredientes = findViewById(R.id.edittext_ingredientes_da_receitas);
        campoDescricao = findViewById(R.id.edittext_descricao_da_receitas);
        campoPorcao = findViewById(R.id.edittext_porcao_da_receita);
        configuraBotaoNovoIngrediente();
    }



    private void configuraBotaoNovoIngrediente() {
        AppCompatImageButton botaoAdicionar = findViewById(R.id.button_adiciona_ingrediente);
        linearLayout = findViewById(R.id.view_ingredientes);
        LinearLayout linearHorizontal = new LinearLayout(this);
        linearHorizontal.setOrientation(LinearLayout.HORIZONTAL);

        botaoAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                configuraNovoIngrediente();

                Log.i("Testes", "Receita adicionada: " + ingredientes+ " ID ATUAL: " + idIngrediente);
            }
        });
    }

    private void configuraNovoIngrediente() {
       String ingrediente = campoIngredientes.getText().toString();
       configuraAdicaoRemocaoDeIngredientes(ingrediente);
        }

    private void finalizaFormulario() {
        preencheReceita();
        if (receita.temIdValido()) {
            dao.edita(receita, db);
            Intent voltaParaMostraReceita = new Intent(FormularioDeReceitas.this, MostraReceita.class);
            voltaParaMostraReceita.putExtra("receita", receita);
            startActivity(voltaParaMostraReceita);
            finish();
        } else {

            dao.salva(receita, db);

        }
        finish();

    }

    private void preencheReceita() {
        String nome = campoNome.getText().toString();
        String descricao = campoDescricao.getText().toString();
        String porcao = campoPorcao.getText().toString();

        receita.setNome(nome);
        receita.setIngredientes(ingredientes);
        receita.setModoDePreparo(descricao);
        receita.setPorcao(porcao);
        receita.setCategoria(categoria);
        receita.setPosicaoCategoria(campoCategoria);

    }

}
