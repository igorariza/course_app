package com.fesi.funda.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.fesi.funda.R;
import com.fesi.funda.src.Carreras;
import com.fesi.funda.src.CircleTransform;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


public class AdapterCarreras
        extends RecyclerView.Adapter<AdapterCarreras.ViewHolder>
        implements View.OnClickListener{

    Context context;
    private ArrayList<Carreras> carreras = new ArrayList<>();
    private View.OnClickListener listener;

    public AdapterCarreras(Context c, ArrayList<Carreras> p) {
        context = c;
        carreras = p;
    }

    @Override
    public AdapterCarreras.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carrera, parent, false);
        view.setOnClickListener(this);
        return new AdapterCarreras.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterCarreras.ViewHolder holder, final int position) {
        //Log.d(TAG, "onBindViewHolder: called.");
        Picasso.get()
                .load(carreras.get(position).getImgCarrera())
                .placeholder(R.drawable.fesi_logo_bonton_1)
                .centerCrop()
                .error(R.drawable.fesi_logo_bonton_1)
                .fit()
                .priority(Picasso.Priority.HIGH)
                .transform(new CircleTransform()).into(holder.image);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.name.setText(Html.fromHtml(String.valueOf(Html.fromHtml(carreras.get(position).getNombreCarrera())), Html.FROM_HTML_MODE_COMPACT));
            holder.cant.setText(Html.fromHtml(String.valueOf(Html.fromHtml(carreras.get(position).getCantidadCursos() + " Cursos")), Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.name.setText(Html.fromHtml(String.valueOf(Html.fromHtml(String.valueOf(Html.fromHtml(carreras.get(position).getNombreCarrera()))))));
            holder.cant.setText(Html.fromHtml(String.valueOf(Html.fromHtml(String.valueOf(Html.fromHtml(carreras.get(position).getCantidadCursos() + " Cursos"))))));
        }

    }

    @Override
    public int getItemCount() {
        return carreras.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;

    }

    @Override
    public void onClick(View v) {
        if(listener != null){
            listener.onClick(v);

        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView name;
        TextView cant;
        CardView cardCarrera;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.img_carrera);
            name = itemView.findViewById(R.id.primary_text);
            cant = itemView.findViewById(R.id.sub_text);
            cardCarrera = itemView.findViewById(R.id.cardCarrera);
        }
    }
}