package ui.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycookbook.R;
import com.example.mycookbook.activity.model.Receita;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import adapterView.AdapterListaDeReceitas;
import dao.ReceitaDAO;

public class ListaDeReceitas extends AppCompatActivity {

    private final ReceitaDAO dao = new ReceitaDAO();
    private AdapterListaDeReceitas adapter;
    private RecyclerView listaDeReceitas;
    private View semItemNaLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_de_receitas_main);
        configuraFabNovaReceita();
        configuraLista();

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
        confirmacaoDeRemocao.setMessage("A receita sera excluida permanentemente, Deseja mesmo excluir?");
        confirmacaoDeRemocao.setPositiveButton("Sim", confirmaExclusao(posicao, receitaEscolhida))
                .setNegativeButton("Cancelar", null);
        confirmacaoDeRemocao.show();
    }

    @NonNull
    private DialogInterface.OnClickListener confirmaExclusao(int posicao, Receita receitaEscolhida) {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dao.exclui(receitaEscolhida); // Exclui do DAO
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

    private void atualizaReceitas() {
        adapter.atualiza(dao.todas(), semItemNaLista);
        adapter.notifyDataSetChanged();
    }


    private void configuraLista() {
        listaDeReceitas = findViewById(R.id.listview_lista_de_receitas);
        semItemNaLista = findViewById(R.id.view_sem_item_na_lista);

        RecyclerView.LayoutManager layoutReceitas = new LinearLayoutManager(getApplicationContext());
        listaDeReceitas.setLayoutManager(layoutReceitas);
        listaDeReceitas.setHasFixedSize(true);
        listaDeReceitas.addItemDecoration(new DividerItemDecoration(this, LinearLayout.HORIZONTAL));
        configuraAdapter(listaDeReceitas);
        registerForContextMenu(listaDeReceitas);
    }


    private void configuraAdapter(RecyclerView listaDeReceitas) {
        adapter = new AdapterListaDeReceitas(dao.todas());
        listaDeReceitas.setAdapter(adapter);

    }


}
