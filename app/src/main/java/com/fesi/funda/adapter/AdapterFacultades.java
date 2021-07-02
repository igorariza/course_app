package com.fesi.funda.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fesi.funda.R;
import com.fesi.funda.src.Facultades;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


public class AdapterFacultades extends RecyclerView.Adapter<AdapterFacultades.MyViewHolder> {

    Context context;
    ArrayList<Facultades> profiles;

    public AdapterFacultades(Context c, ArrayList<Facultades> p) {
        context = c;
        profiles = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_nombrefacultad, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.name.setText(profiles.get(position).getNombreFacultad());
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, " is card" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        CardView card;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.txt_nombreCarera);
            card = (CardView) itemView.findViewById(R.id.carNombreCarrera);
        }

    }
}