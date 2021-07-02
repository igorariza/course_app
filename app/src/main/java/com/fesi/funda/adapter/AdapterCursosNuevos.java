package com.fesi.funda.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fesi.funda.R;
import com.fesi.funda.src.CircleTransform;
import com.fesi.funda.src.Cursos;
import com.fesi.funda.src.Facultades;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterCursosNuevos extends RecyclerView.Adapter<AdapterCursosNuevos.MyViewHolder> {

    Context context;
    ArrayList<Cursos> profiles;

    public AdapterCursosNuevos(Context c, ArrayList<Cursos> p) {
        context = c;
        profiles = p;
    }

    @NonNull
    @Override
    public AdapterCursosNuevos.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterCursosNuevos.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_cursos_nuevos, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCursosNuevos.MyViewHolder holder, final int position) {
        holder.name.setText(profiles.get(position).getNombreCurso());
        Picasso.get()
                .load(profiles.get(position).getImagenCurso())
                .placeholder(R.drawable.fesi_logo_bonton_1)
                .centerCrop()
                .error(R.drawable.fesi_logo_bonton_1)
                .fit()
                .priority(Picasso.Priority.HIGH)
                .transform(new CircleTransform()).into(holder.imagenCurso);
    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        CircleImageView imagenCurso;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.txt_nombreCursoNuevo);
            imagenCurso = (CircleImageView) itemView.findViewById(R.id.ivImageCursoNuevo);
        }

        public void onClick(final View.OnClickListener position) {
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, " is clickedd", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
