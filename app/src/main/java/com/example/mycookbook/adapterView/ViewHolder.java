package com.example.mycookbook.adapterView;

import android.view.ContextMenu;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycookbook.R;

public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
    final AppCompatTextView nome;
    final AppCompatTextView categoria;
    final LinearLayout base;


    public ViewHolder(View view) {
        super(view);
        base = view.findViewById(R.id.item_list_raiz);
        nome = view.findViewById(R.id.textview_nome_da_receita);
        categoria = view.findViewById(R.id.textview_categoria_da_receita);
        view.setOnCreateContextMenuListener(this);

    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        contextMenu.add(0, R.id.activity_lista_receitas_menu_editar, 0, "Editar"); // Cria a opção editar
        contextMenu.add(0, R.id.activity_lista_receitas_menu_excluir, 0, "Excluir"); // Cria a opção excluir
    }



    public LinearLayout getBase() {
        return base;
    }


    public AppCompatTextView getNome() {
        return nome;
    }

    public AppCompatTextView getCategoria() {
        return categoria;
    }

}
