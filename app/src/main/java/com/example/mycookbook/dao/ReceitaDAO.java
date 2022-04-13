package com.example.mycookbook.dao;

import android.util.Log;

import androidx.annotation.Nullable;

import com.example.mycookbook.DataBase.ReceitasDBHelper;
import com.example.mycookbook.model.Receita;

import java.util.ArrayList;
import java.util.List;

public class ReceitaDAO{

    private final static List<Receita> receitas = new ArrayList<>();

    public void salva(Receita receita, ReceitasDBHelper db){
        receitas.add(receita);
        db.insereReceitaDB(receita);
    }



    public void edita(Receita receita, ReceitasDBHelper db){
        Receita receitaEncontrada = buscaReceitaPeloId(receita);
        if(receitaEncontrada != null){ // Se for encontrado o id da receita é porque ela esta na lista
            int posicaoReceita = receitas.indexOf(receitaEncontrada); // Pega a posição da receita na lista
            receitas.set(posicaoReceita, receita); // Faz a troca da receita antiga para a receita editada
            int idReceita = receita.getId();
            Log.i("Testes", "id da receita editada:" + idReceita);
            db.setReceitaPorID(idReceita, receita);

        }
    }

    public void exclui(Receita receita, ReceitasDBHelper db){
        Receita receitaEncontrada = buscaReceitaPeloId(receita);
        if (receitaEncontrada != null){
            int posicaoReceita = receitas.indexOf(receitaEncontrada);
            receitas.remove(posicaoReceita);
            db.deletaReceitaDB(receita.getId());

        }
    }

    @Nullable
    private Receita buscaReceitaPeloId(Receita receita) {
        Receita receitaEncontrada = null;
        for(Receita a: receitas){ // Para a na lista de receitas faça
            if (a.getId() == receita.getId()){ // Se o id de "a" for o mesmo id da receita enviada como parametro
                return a; //Retorne "a"
            }
        }


        return null;
    }

    public List<Receita> todas(ReceitasDBHelper db){
        insereTodasAsReceitasDoDB(db);
        return new ArrayList<>(receitas);
    }

    public void insereTodasAsReceitasDoDB(ReceitasDBHelper db) {
        receitas.clear();
        receitas.addAll(db.todasReceitas());

}
}
