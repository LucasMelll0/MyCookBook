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
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;

import com.example.mycookbook.DataBase.ReceitasDBHelper;
import com.example.mycookbook.R;
import com.example.mycookbook.dao.ReceitaDAO;
import com.example.mycookbook.model.Receita;

public class FormularioDeReceitas extends AppCompatActivity {

    private EditText campoNome;
    private EditText campoIngredientes;
    private EditText campoDescricao;
    private EditText campoPorcao;
    private int campoCategoria;
    private Toolbar toolbar;
    private Spinner spinner;
    private String categoria;
    private final ReceitaDAO dao = new ReceitaDAO();
    private Receita receita;
    private ReceitasDBHelper db = new ReceitasDBHelper(this);


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
            toolbar.setTitle("Editando Receita");
            receita = (Receita) dados.getSerializableExtra("receita");
            Log.i("Veio os Extras?", "Sim" + receita.getId());
            campoNome.setText(receita.getNome());
            campoIngredientes.setText(receita.getIngredientes());
            campoDescricao.setText(receita.getModoDePreparo());
            campoPorcao.setText(receita.getPorcao());
            String[] categorias = getResources().getStringArray(R.array.categorias_array);
            for (int i = 0; i < categorias.length; i++) {
                if (receita.getCategoria().equals(categorias[i])) {
                    spinner.setSelection(i);
                    break;
                } else if(i == categorias.length) {
                    spinner.setSelection(0);
                }
            }
        } else{
            Log.i("Veio os Extras?", "Não");
        receita = new Receita();
    }
    }



    private void configuraToolBar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Nova Receita");

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
        String ingredientes = campoIngredientes.getText().toString();
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
