package com.example.organizzeclone.adapter;

import android.content.Context;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.organizzeclone.R;
import com.example.organizzeclone.model.Movimentacao;

import java.util.List;

public class AdapterMovimentacao extends RecyclerView.Adapter<AdapterMovimentacao.MyViewHolder> implements View.OnClickListener, View.OnLongClickListener {

    List<Movimentacao> movimentacoes;
    Context context;
    CardView card;

    public AdapterMovimentacao(List<Movimentacao> movimentacoes, Context context) {
        this.movimentacoes = movimentacoes;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_movimentacao, parent, false);
        return new MyViewHolder(itemLista);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Movimentacao movimentacao = movimentacoes.get(position);

        holder.titulo.setText(movimentacao.getDescricao());
        holder.valor.setText(String.valueOf(movimentacao.getValor()));
        holder.categoria.setText(movimentacao.getCategoria());
        holder.valor.setTextColor(context.getResources().getColor(R.color.purple_500R));
        card.setOnClickListener(this); //Fernando
        card.setOnLongClickListener(this);

        if (movimentacao.getTipo().equals("D")) {
            holder.valor.setTextColor(context.getResources().getColor(R.color.purple_500D));//Contexto de cores de outra activity
            holder.valor.setText("-" + movimentacao.getValor());
        }
    }


    @Override
    public int getItemCount() {
        return movimentacoes.size();
    }

    @Override
    public void onClick(View view) {
        try {
            if (view.equals(card)){
                Toast.makeText(context, "Click curto!", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){

        }
    }

    @Override
    public boolean onLongClick(View view) {
        try {
            if (view.equals(card)){
                Toast.makeText(context, "Click longo!", Toast.LENGTH_SHORT).show();
                return true;
            }
        }catch (Exception e){

        }
        return false;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView titulo, valor, categoria;


        public MyViewHolder(View itemView) {
            super(itemView);

            titulo = itemView.findViewById(R.id.textAdapterTitulo);
            valor = itemView.findViewById(R.id.textAdapterValor);
            categoria = itemView.findViewById(R.id.textAdapterCategoria);
            card = itemView.findViewById(R.id.card_movimentacao);
        }

    }

}
