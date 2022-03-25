package dao;

import androidx.annotation.Nullable;

import com.example.mycookbook.activity.model.Receita;

import java.util.ArrayList;
import java.util.List;

public class ReceitaDAO {

    private final static List<Receita> receitas = new ArrayList<>();
    private static int contadorDeIds = 1;

    public void salva(Receita receita){
        receita.setId(contadorDeIds);
        receitas.add(receita);
        contadorDeIds++; //atualiza ids
    }

    public void edita(Receita receita){
        Receita receitaEncontrada = buscaReceitaPeloId(receita);
        if(receitaEncontrada != null){ // Se for encontrado o id da receita é porque ela esta na lista
            int posicaoReceita = receitas.indexOf(receitaEncontrada); // Pega a posição da receita na lista
            receitas.set(posicaoReceita, receita); // Faz a troca da receita antiga para a receita editada
        }
    }

    public void exclui(Receita receita){
        Receita receitaEncontrada = buscaReceitaPeloId(receita);
        if (receitaEncontrada != null){
            int posicaoReceita = receitas.indexOf(receitaEncontrada);
            receitas.remove(posicaoReceita);
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

    public List<Receita> todas(){
        return new ArrayList<>(receitas);
    }

}
