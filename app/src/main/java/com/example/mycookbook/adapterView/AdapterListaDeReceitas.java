package com.example.mycookbook.adapterView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycookbook.R;


import java.io.Serializable;
import java.util.List;

import com.example.mycookbook.activity.MostraReceita;
import com.example.mycookbook.model.Receita;

public class AdapterListaDeReceitas extends RecyclerView.Adapter<ViewHolder> implements Serializable {


    private List<Receita> receitas;
    private int position;

    public AdapterListaDeReceitas(List<Receita> receitas) {
        this.receitas = receitas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_de_receitas, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.getNome().setText(receitas.get(position).getNome());
        holder.getCategoria().setText(receitas.get(position).getCategoria());
        holder.getBase().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Receita receita = receitas.get(position);
                Log.i("Id da receita", "" + receita.getId());
                Log.i("Testes", "onClick: " + receita.getImagemReceita());
                abreReceitaEscolhida(receita, view);
            }
        });
        holder.getNome().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Receita receita = receitas.get(position);
                abreReceitaEscolhida(receita, view);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                setPosition(holder.getPosition()); // Atribui ao atributo position a posição do item clicado longamente
                return false;
            }
        });


    }

    private void abreReceitaEscolhida(Receita receita, View view) {
        Intent mostraReceitaActivity = new Intent(view.getContext(), MostraReceita.class);
        mostraReceitaActivity.putExtra("receita", receita);
        view.getContext().startActivity(mostraReceitaActivity);
    }

    @Override
    public int getItemCount() {
        return receitas.size();
    }


    public void atualiza(List<Receita> receitas, View view_sem_items){
        this.receitas.clear();
        this.receitas.addAll(receitas);
        this.verificaSeContemItemsNaLista(view_sem_items);
    }

    public void verificaSeContemItemsNaLista(View view) {
        if (this.receitas.size() < 1) {
            view.setVisibility(View.VISIBLE);
        }else{
            view.setVisibility(View.GONE);
        }
    }

    public void remove(Receita receitaEscolhida) {
        receitas.remove(receitaEscolhida);
    }

    public Receita getItem(int posicao) {
        Receita receitaSolicitada = receitas.get(posicao);
        return receitaSolicitada;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }



}


