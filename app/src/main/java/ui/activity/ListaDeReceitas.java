package ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.mycookbook.R;
import com.example.mycookbook.activity.model.Receita;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import dao.ReceitaDAO;

public class ListaDeReceitas extends AppCompatActivity{

    private final ReceitaDAO dao = new ReceitaDAO();
    private ArrayAdapter<Receita> adapter;
    private View confirmacaoDeRemocao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_de_receitas_main);
        configuraFabNovaReceita();
        configuraLista();
        dao.salva(new Receita("Bolo", "ovo e leite", "bata tudo no liquidificador", "2","Bolo"));
        dao.salva(new Receita("Torta", "ovo e leite", "bata tudo no liquidificador", "2","Torta"));
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.activity_lista_receitas_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();//atribui as informações do item seleciodado do adapterView(ListView) a uma variavel(menuInfo)
        int posicao = menuInfo.position; //pega a posição do item selecionado
        Receita receitaEscolhida = adapter.getItem(posicao); //pega o item (objeto do tipo receita) a partir da posição

        int itemId = item.getItemId(); //pega o ID do item selecionado

        if(itemId == R.id.activity_lista_receitas_menu_editar){ // Se a opção no menu de contexto for "editar" abre o formulario no modo editar
            Intent abreFormularioModoEdita = new Intent(ListaDeReceitas.this, FormularioDeReceitas.class);
            abreFormularioModoEdita.putExtra("receita", receitaEscolhida);
            startActivity(abreFormularioModoEdita);
        }else if(itemId == R.id.activity_lista_receitas_menu_excluir){ // Se for a opção excluir, exclui o item da lista
            confirmacaoDeRemocao = findViewById(R.id.view_confirma_remocao_lista_activity);
            confirmacaoDeRemocao.setVisibility(View.VISIBLE);
            confirguraConfirmacaoDeRemocao(receitaEscolhida);

        }


        return super.onContextItemSelected(item);
    }

    private void confirguraConfirmacaoDeRemocao(Receita receitaEscolhida) {
        AppCompatButton botaoNega = findViewById(R.id.button_nega_confirmacao);
        AppCompatButton botaoAceita = findViewById(R.id.button_aceita_confirmacao);

        botaoNega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmacaoDeRemocao.setVisibility(View.GONE);
            }
        });
        botaoAceita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dao.exclui(receitaEscolhida); // Exclui do DAO
                adapter.remove(receitaEscolhida); // Exclui do Adapter
                confirmacaoDeRemocao.setVisibility(View.GONE);
            }
        });
    }

    private void configuraFabNovaReceita() {
        FloatingActionButton botaoNovaReceita = findViewById(R.id.fab_nova_receita);
        botaoNovaReceita.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListaDeReceitas.this, FormularioDeReceitas.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        atualizaReceitas();
    }

    private void atualizaReceitas() {
        adapter.clear();
        adapter.addAll(dao.todas());
    }

    private void configuraLista(){
        ListView listaDeReceitas = findViewById(R.id.listview_lista_de_receitas);
        configuraAdapter(listaDeReceitas);
        configuraListenerDeCliquePorItem(listaDeReceitas);
        registerForContextMenu(listaDeReceitas);
    }

    private void configuraListenerDeCliquePorItem(ListView listaDeReceitas){
        listaDeReceitas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int posicao, long id) {
                Receita receitaEscolhida = (Receita) adapterView.getItemAtPosition(posicao);
                abreReceitaSelecionada(receitaEscolhida);
            }
        });
    }

    private void abreReceitaSelecionada(Receita receitaEscolhida) {
        Intent mostraReceitaActivity = new Intent(ListaDeReceitas.this, MostraReceita.class);
        mostraReceitaActivity.putExtra("receita", receitaEscolhida);
        startActivity(mostraReceitaActivity);
    }

    private void configuraAdapter(ListView listaDeReceitas){
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listaDeReceitas.setAdapter(adapter);

    }


}
