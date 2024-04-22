package com.example.primera_entrega_das;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


import java.util.ArrayList;

public class Temas extends AppCompatActivity implements RecyclerViewInterface{

    ArrayList<ModeloTemas> modeloTemas = new ArrayList<>(); //arraylist de objetos modeloTemas

    //imagenes que se usan en los CardViews
    int[] imagenes = {R.drawable.baseline_computer_24,R.drawable.baseline_brush_24,R.drawable.baseline_sports_football_24,
                        R.drawable.baseline_map_24, R.drawable.baseline_science_24, R.drawable.baseline_public_24};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temas);

        //encontrar el recycler view en el xml
        RecyclerView recyclerView = findViewById(R.id.miRecyclerView);
        crearModeloTemas(); //guardar todos los elementos de los temas
        //Crear adaptador para el recycler view.
        TemasAdaptadorRecycler adapter = new TemasAdaptadorRecycler(this,modeloTemas,this);
        //Establece el adaptador
        recyclerView.setAdapter(adapter);
        //Se asegura que los elementos se muestren en una lista verticalmente desplazable
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    //metodo para añadir a la ArrayList los datos de strings.xml de manera ordenanda
    private void crearModeloTemas(){

        String[] titulosTemas = getResources().getStringArray(R.array.titulos_temas);
        String[] dcpTemas = getResources().getStringArray(R.array.descripcion_tema);

        for(int i = 0; i < titulosTemas.length; i++){
            modeloTemas.add(new ModeloTemas(titulosTemas[i], dcpTemas[i], imagenes[i]));
        }
    }

    //Metodo el cual se va a llamar al darle click en un tema del recycler view
    @Override
    public void onItemClick(int posicion) {
        if (posicion==5){
            Log.d("MAIN","ENTRA");
            Intent mapas = new Intent(Temas.this, IntermedioMapaGeo.class);
            mapas.putExtra("Tema", modeloTemas.get(posicion).getTitulo());
            mapas.putExtra("Imagen", modeloTemas.get(posicion).getImagenId());
            this.startActivity(mapas);
            finish();
        }else {
            Intent intent = new Intent(Temas.this, IntermedioJugarCrear.class);
            //guardar en el intent el nombre del tema e id de la imagen
            intent.putExtra("Tema", modeloTemas.get(posicion).getTitulo());
            intent.putExtra("Imagen", modeloTemas.get(posicion).getImagenId());
            this.startActivity(intent);
            finish();
        }
    }
}