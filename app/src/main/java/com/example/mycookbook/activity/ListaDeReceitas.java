package com.example.mycookbook.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mycookbook.R;
import com.example.mycookbook.adapterView.AdapterListaDeReceitas;
import com.example.mycookbook.dao.ReceitaDAO;
import com.example.mycookbook.dataBase.ReceitasDBHelper;
import com.example.mycookbook.model.Receita;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ListaDeReceitas extends AppCompatActivity {

    private final ReceitaDAO dao = new ReceitaDAO();
    private AdapterListaDeReceitas adapter;
    private RecyclerView listaDeReceitas;
    private View semItemNaLista;
    public ReceitasDBHelper db = new ReceitasDBHelper(this);
    private Boolean layout = true;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_de_receitas_main);
        configuraFabNovaReceita();
        configuraLista();
        configuraBotaoMudaLayout();
        configuraBotaoLogar();
        Toast.makeText(this, "Usuário atual: " + firebaseAuth.getCurrentUser(), Toast.LENGTH_SHORT).show();
    }

    private void configuraBotaoLogar() {
        AppCompatTextView logar = findViewById(R.id.textview_login_lista_de_receitas);
        if (firebaseAuth.getCurrentUser() == null) {
            logar.setText("LOGIN");
            logar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent vaiParaTelaDeLogin = new Intent(ListaDeReceitas.this, LoginActiivty.class);
                    startActivity(vaiParaTelaDeLogin);
                }
            });
        } else {
            logar.setText("LOGOUT");
            logar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ExecutorService executorService = Executors.newSingleThreadExecutor();
                    progressBar.setVisibility(View.VISIBLE);
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            firebaseAuth.signOut();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setVisibility(View.GONE);
                                    configuraBotaoLogar();
                                    Toast.makeText(ListaDeReceitas.this, "Usuário Deslogado!!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
            });
        }
    }

    private void configuraBotaoMudaLayout() {
        AppCompatImageView alternaLayout = findViewById(R.id.image_alterna_layout);


        alternaLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (layout) {
                    RecyclerView.LayoutManager layoutReceitas = new GridLayoutManager(getApplicationContext(), 2);
                    listaDeReceitas.setLayoutManager(layoutReceitas);
                    alternaLayout.setImageDrawable(getResources().getDrawable(R.drawable.ic_linear));
                    layout = false;
                } else {
                    RecyclerView.LayoutManager layoutReceitas = new LinearLayoutManager(getApplicationContext());
                    listaDeReceitas.setLayoutManager(layoutReceitas);
                    alternaLayout.setImageDrawable(getResources().getDrawable(R.drawable.ic_grid));
                    layout = true;
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

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View inflater = getLayoutInflater().inflate(R.layout.dialog_excluir_receita, null);
        builder.setView(inflater);
        builder.
                setPositiveButton("Sim", confirmaExclusao(posicao, receitaEscolhida))
                .setNegativeButton("Cancelar", null);
        AppCompatImageView imagemDialog = inflater.findViewById(R.id.imageview_receita_dialog);

        if (receitaEscolhida.getImagemReceita() != null) {
            Glide.with(this)
                    .load(receitaEscolhida.getImagemReceita())
                    .apply(RequestOptions.circleCropTransform())
                    .into(imagemDialog);
        } else {
            Glide.with(this)
                    .load(getResources().getDrawable(R.drawable.receita_sem_imagem))
                    .apply(RequestOptions.circleCropTransform())
                    .into(imagemDialog);
        }

        builder.show();
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
        progressBar = findViewById(R.id.progressbar_lista_de_receitas);
        progressBar.setVisibility(View.VISIBLE);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                adapter.atualiza(dao.todas(db), semItemNaLista);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                    }
                });
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
