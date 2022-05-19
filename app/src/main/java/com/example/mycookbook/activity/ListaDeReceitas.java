package com.example.mycookbook.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycookbook.R;
import com.example.mycookbook.adapterView.AdapterListaDeReceitas;
import com.example.mycookbook.dao.ReceitaDAO;
import com.example.mycookbook.dataBase.ReceitasDBHelper;
import com.example.mycookbook.model.Receita;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ListaDeReceitas extends AppCompatActivity {

    private final ReceitaDAO dao = new ReceitaDAO();
    private AdapterListaDeReceitas adapter;
    private RecyclerView listaDeReceitas;
    private View semItemNaLista;
    public ReceitasDBHelper db = new ReceitasDBHelper(this);
    private Boolean alternaLayoutBol = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_de_receitas_main);
        configuraFabNovaReceita();
        configuraLista();
        configuraBotaoMudaLayout();
    }

    private void configuraBotaoMudaLayout() {
        AppCompatImageView alternaLayout = findViewById(R.id.image_alterna_layout);

        alternaLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (alternaLayoutBol == false) {
                    RecyclerView.LayoutManager layoutReceitas = new GridLayoutManager(getApplicationContext(), 2);
                    listaDeReceitas.setLayoutManager(layoutReceitas);
                    alternaLayout.setImageDrawable(getResources().getDrawable(R.drawable.ic_linear));
                    alternaLayoutBol = true;
                } else {
                    RecyclerView.LayoutManager layoutReceitas = new LinearLayoutManager(getApplicationContext());
                    listaDeReceitas.setLayoutManager(layoutReceitas);
                    alternaLayout.setImageDrawable(getResources().getDrawable(R.drawable.ic_grid));
                    alternaLayoutBol = false;
                }
            }
        });
    }


    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int posicao = adapter.getPosition(); //pega a posição do item selecionado
        Receita receitaEscolhida = adapter.getItem(posicao); //pega o item (objeto do tipo receita) a partir da posição

        int itemId = item.getItemId(); //pega o ID do item selecionado

        if (itemId == R.id.activity_lista_receitas_menu_editar) { // Se a opção no menu de contexto for "editar" abre o formulario no modo editar
            Intent abreFormularioModoEdita = new Intent(ListaDeReceitas.this, FormularioDeReceitas.class);
            abreFormularioModoEdita.putExtra("receita", receitaEscolhida);
            startActivity(abreFormularioModoEdita);
        } else if (itemId == R.id.activity_lista_receitas_menu_excluir) { // Se for a opção excluir, exclui o item da lista
            configuraAlertDialogParaRemocao(posicao, receitaEscolhida);

        }


        return super.onContextItemSelected(item);
    }

    private void configuraAlertDialogParaRemocao(int posicao, Receita receitaEscolhida) {
        AlertDialog.Builder confirmacaoDeRemocao = new AlertDialog.Builder(this);
        confirmacaoDeRemocao.setTitle("Excluir Receita");
        confirmacaoDeRemocao.setMessage("Deseja realmente excluir a receita permanentemente?");
        confirmacaoDeRemocao.setPositiveButton("Sim", confirmaExclusao(posicao, receitaEscolhida))
                .setNegativeButton("Cancelar", null);
        confirmacaoDeRemocao.show();
    }

    @NonNull
    private DialogInterface.OnClickListener confirmaExclusao(int posicao, Receita receitaEscolhida) {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dao.exclui(receitaEscolhida, db); // Exclui do DAO
                adapter.remove(receitaEscolhida); // Exclui do Adapter
                adapter.notifyItemRemoved(posicao);
                adapter.verificaSeContemItemsNaLista(semItemNaLista);
            }
        };
    }


    private void configuraFabNovaReceita() {
        ExtendedFloatingActionButton botaoNovaReceita = findViewById(R.id.fab_nova_receita);
        botaoNovaReceita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent novaReceita = new Intent(ListaDeReceitas.this, FormularioDeReceitas.class);
                startActivity(novaReceita);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        atualizaReceitas();

    }

    public void atualizaReceitas() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        ProgressBar progressBar = findViewById(R.id.progressbar_lista_de_receitas);

        executor.execute(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
                adapter.atualiza(dao.todas(db), semItemNaLista);
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

        });


    }

    private void configuraLista() {
        listaDeReceitas = findViewById(R.id.recyclerview_lista_de_receitas);
        semItemNaLista = findViewById(R.id.view_sem_item_na_lista);

        RecyclerView.LayoutManager layoutReceitas = new LinearLayoutManager(getApplicationContext());
        listaDeReceitas.setLayoutManager(layoutReceitas);
        listaDeReceitas.setHasFixedSize(true);
        configuraAdapter(listaDeReceitas);
        registerForContextMenu(listaDeReceitas);
    }


    private void configuraAdapter(RecyclerView listaDeReceitas) {
        adapter = new AdapterListaDeReceitas(dao.todas(db));
        listaDeReceitas.setAdapter(adapter);

    }


}
