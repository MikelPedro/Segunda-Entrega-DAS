package com.example.primera_entrega_das;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.preference.PreferenceManager;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class PerfilUsuario extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 10;
    private String nomUsuario = "";
    private ImageView imgPerfil;
    private TextView tvNombre;
    private Bitmap laminiatura;



    private ActivityResultLauncher<Intent> takePictureLauncher =
            registerForActivityResult(new
                    ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK &&
                        result.getData()!= null) {
                    Bundle bundle = result.getData().getExtras();
                    ImageView elImageView = findViewById(R.id.imagePerfil);
                    laminiatura = (Bitmap) bundle.get("data");
                    Log.d("IMAGEN", "LA MINIATURA: " + laminiatura);
                    elImageView.setImageBitmap(laminiatura);
                    Log.d("IMAGEN","llegga aqui");

                } else {
                    Log.d("TakenPicture", "No se ha tomado una foto");
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        tvNombre = findViewById(R.id.textViewNombre);
        imgPerfil = findViewById(R.id.imagePerfil);

        //Obtener de las preferencias el nombre de usuario que ha iniciado sesion
        obtenerInsertarDatosUsuario();
        //cargar la foto (si tiene el usuario)
        obtenerImagenPerfil();

    }

    public void obtenerInsertarDatosUsuario(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        nomUsuario = preferences.getString("nombreUsu", "");
        tvNombre.setText("Nombre: " + nomUsuario);
    }

    public void OnClickAbrirCamara(View v){
        // Verificar si se tienen los permisos necesarios
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Si los permisos no están otorgados, solicitarlos al usuario
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            // Si los permisos ya están otorgados, abrir la cámara
            camara();
        }
    }

    public void OnClickVolverMain(View v){
        //Al darle click te lleva a la MainActivity
        Intent volver = new Intent(this,MainActivity.class);
        this.startActivity(volver);
        finish();
    }

    public void onClickSubirFoto(View v){
        Log.d("IMAGEN","pulso boton de subir");
        subirImagenPerfil();
    }


    //Sacar una foot y subirla a la BD remota
    private void subirImagenPerfil(){

        if (laminiatura != null){

            String foto64 = this.transformarBitMapAString(laminiatura);
            Log.d("IMAGEN","La foto en subir" + foto64);
            //Para mandar los datos a la bd
            Data datosSubir = new Data.Builder()
                    .putString("nom",nomUsuario)
                    .putString("imagen", foto64)
                    .putString("reg","subir") //accion que se va a realizar
                    .build();

            OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(conexionBDRemota.class)
                    .setInputData(datosSubir)
                    .build();

            WorkManager.getInstance(this).enqueue(otwr);
            //Toast para indicar al usuario que se ha cargado la foto en la BD
            Toast.makeText(this, "Se ha cargado la foto en la BD", Toast.LENGTH_SHORT).show();
        }

    }


    //si no tiene foto de perfil que se hace ?
    private void obtenerImagenPerfil() {

        //Para mandar los datos a la bd
        Data datosObtener = new Data.Builder()
                .putString("nom",nomUsuario)
                .putString("reg","obtener")
                .build();

        //Log.d("IMAGEN", "Construye el DATA en obtener imagen perfil");
        OneTimeWorkRequest otwr1 = new OneTimeWorkRequest.Builder(conexionBDRemota.class)
                .setInputData(datosObtener)
                .build();

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(otwr1.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if(workInfo != null && workInfo.getState().isFinished()){

                            // Obtener la salida de la tarea de WorkManager (en este caso, la imagen de perfil)
                            Bitmap imagenPerfil = RecogerImagen.getInstance().getFoto();
                            if (imagenPerfil != null) {
                                // Si se cargó la imagen desde la base de datos, establecerla en el ImageView
                                imgPerfil.setImageBitmap(imagenPerfil);
                            } else {
                                // Si no se cargó la imagen desde la base de datos, establecer la imagen predeterminada
                                imgPerfil.setImageResource(R.drawable.baseline_person_24_red);
                            }
                        }else {
                            Log.d("IMAGEN", "No se ha cargado la imagen");
                            //Toast.makeText(PerfilUsuario.this,"No se ha cargado imagen", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        WorkManager.getInstance(this).enqueue(otwr1);

    }

    private void camara() {
        //Metodo para abrir la aplicación de la camara
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureLauncher.launch(intent);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //Metodo para pedir permisos al usuario
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            // Verificar si los permisos fueron concedidos
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Si se aceptan los permisos de la camara, se abre la aplicación
                camara();
            } else {
                // Si  se rechazan los permisos de la camara, se muestra un mensaje
                Toast.makeText(this, "Permiso de la cámara denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Metodo para transformar bitmap a string
    private String transformarBitMapAString(Bitmap imagenBitMap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        imagenBitMap.compress(Bitmap.CompressFormat.JPEG, 30, stream);
        byte[] fototransformada = stream.toByteArray();
        String fotoen64 = Base64.encodeToString(fototransformada,Base64.DEFAULT);
        return fotoen64;
    }


}