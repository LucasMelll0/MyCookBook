package adapterView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycookbook.R;
import com.example.mycookbook.activity.model.Receita;

import java.io.Serializable;
import java.util.List;

import ui.activity.MostraReceita;

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


    public void atualiza(List<Receita> receitas){
        this.receitas.clear();
        this.receitas.addAll(receitas);
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


