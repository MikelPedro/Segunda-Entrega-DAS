package com.example.primera_entrega_das;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TemasAdaptadorRecycler extends RecyclerView.Adapter<TemasAdaptadorRecycler.ElViewHolder> {

    private final RecyclerViewInterface recyclerViewInterface;
    Context contexto;
    ArrayList<ModeloTemas> modeloTemas;

    //Constructor
    public TemasAdaptadorRecycler(Context contexto, ArrayList<ModeloTemas> modeloTemas,RecyclerViewInterface recyclerViewInterface){
        this.contexto = contexto;
        this.modeloTemas = modeloTemas;
        this.recyclerViewInterface = recyclerViewInterface;
    }
    // Infla el layout para cada uno de los elementos
    @NonNull
    @Override
    public TemasAdaptadorRecycler.ElViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.mi_recycler_view_fila,parent,false);
        return new ElViewHolder(view);
    }

    // Asigna a los atributos del ViewHolder los valores a mostrar para una posición concreta
    @Override
    public void onBindViewHolder(@NonNull TemasAdaptadorRecycler.ElViewHolder holder, int position) {
        holder.titulo.setText(modeloTemas.get(position).getTitulo());
        holder.descp.setText(modeloTemas.get(position).getDescripcion());
        holder.laImagen.setImageResource(modeloTemas.get(position).getImagenId());
    }

    // Devuelve la cantidad de elementos mostrar
    @Override
    public int getItemCount() {
        return modeloTemas.size();
    }

    public class ElViewHolder extends RecyclerView.ViewHolder{

        ImageView laImagen;
        TextView titulo, descp;

        public ElViewHolder(@NonNull View itemView) {
            super(itemView);

            laImagen = itemView.findViewById(R.id.imageView2);
            titulo = itemView.findViewById(R.id.tituloView);
            descp = itemView.findViewById(R.id.descpView);

            //Listener al darle click al elemento
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recyclerViewInterface != null){
                        int pos = getAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(pos); //llamada al metodo de la interfaz con la posicion del card view que hemos seleccionado
                        }
                    }
                }
            });
        }
    }

}
