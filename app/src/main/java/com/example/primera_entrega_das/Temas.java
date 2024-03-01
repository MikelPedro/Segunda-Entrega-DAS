package com.example.primera_entrega_das;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import java.util.ArrayList;

public class Temas extends AppCompatActivity {

    ArrayList<ModeloTemas> modeloTemas = new ArrayList<>();

    int[] imagenes = {R.drawable.baseline_computer_24,R.drawable.baseline_brush_24,R.drawable.baseline_sports_football_24,
                        R.drawable.baseline_map_24, R.drawable.baseline_science_24};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temas);

        RecyclerView recyclerView = findViewById(R.id.miRecyclerView);
        crearModeloTemas();
        TemasAdaptadorRecycler adapter = new TemasAdaptadorRecycler(this,modeloTemas);
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
}