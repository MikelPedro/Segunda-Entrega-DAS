package com.example.primera_entrega_das;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;


import java.util.ArrayList;

public class Temas extends AppCompatActivity implements RecyclerViewInterface{

    ArrayList<ModeloTemas> modeloTemas = new ArrayList<>();

    int[] imagenes = {R.drawable.baseline_computer_24,R.drawable.baseline_brush_24,R.drawable.baseline_sports_football_24,
                        R.drawable.baseline_map_24, R.drawable.baseline_science_24};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temas);

        RecyclerView recyclerView = findViewById(R.id.miRecyclerView);
        crearModeloTemas();
        TemasAdaptadorRecycler adapter = new TemasAdaptadorRecycler(this,modeloTemas,this);
        recyclerView.setAdapter(adapter);
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

    @Override
    public void onItemClick(int posicion) {

        Intent intent = new Intent(Temas.this,IntermedioJugarCrear.class);
        //pasar el nombre del tema a la otra actividad
        intent.putExtra("Tema",modeloTemas.get(posicion).getTitulo());
        startActivity(intent);
        finish();
    }
}