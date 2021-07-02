package com.fesi.funda.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.fesi.funda.src.Cursos;
import com.fesi.funda.R;
import com.fesi.funda.src.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class AdapterCursos extends RecyclerView.Adapter<AdapterCursos.ViewHolder> {

    //vars
    private ArrayList<Cursos> cursoArray = new ArrayList<>();
    private Context mContext;

    public AdapterCursos(Context context, ArrayList<Cursos> cursos) {
        cursoArray = cursos;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carrera, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        //Log.d(TAG, "onBindViewHolder: called.");
        Picasso.get()
                .load(cursoArray.get(position).getImagenCurso())
                .placeholder(R.drawable.fesi_logo_bonton_1)
                .centerCrop()
                .error(R.drawable.fesi_logo_bonton_1)
                .fit()
                .priority(Picasso.Priority.HIGH)
                .transform(new CircleTransform()).into(holder.image);


        holder.name.setText(cursoArray.get(position).getNombreCurso());


    }

    @Override
    public int getItemCount() {
        return cursoArray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView name;
        TextView cant;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.img_carrera);
            name = itemView.findViewById(R.id.primary_text);
            cant = itemView.findViewById(R.id.sub_text);
        }
    }
}
