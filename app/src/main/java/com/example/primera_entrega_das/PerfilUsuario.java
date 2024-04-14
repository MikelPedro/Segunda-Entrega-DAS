package com.example.primera_entrega_das;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.content.Intent;
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

public class PerfilUsuario extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 10;
    private String nomUsuario = "";
    private ImageView imgPerfil;

    private ActivityResultLauncher<Intent> takePictureLauncher =
            registerForActivityResult(new
                    ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK &&
                        result.getData()!= null) {
                    Bundle bundle = result.getData().getExtras();
                    ImageView elImageView = findViewById(R.id.imageView);
                    Bitmap laminiatura = (Bitmap) bundle.get("data");
                    elImageView.setImageBitmap(laminiatura);
                } else {
                    Log.d("TakenPicture", "No se ha tomado una foto");
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        TextView tvNombre = findViewById(R.id.textViewNombre);
        imgPerfil = findViewById(R.id.imagePerfil);

        //Traer por medio del intent, el nombre de usuario y sus puntos
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            nomUsuario = extras.getString("nombreUsu");
            tvNombre.setText("Nombre: " + nomUsuario);
        }

        //cargar la foto (si tiene el usuario)
        obtenerImagenPerfil();

    }



    public void OnClickVolverMain(View v){
        //Al darle click te lleva a la MainActivity
        Intent volver = new Intent(this,MainActivity.class);
        this.startActivity(volver);
        finish();
    }

    public void onClickSubirFoto(View v){
        subirImagenPerfil();
    }


    //Sacar una foot y subirla a la BD remota
    private void subirImagenPerfil(){

        //Para mandar los datos a la bd
        Data datosSubir = new Data.Builder()
                .putString("nom",nomUsuario)
                .putString("accion","subir")
                .build();

        OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(conexionBDRemota.class)
                .setInputData(datosSubir)
                .build();

        WorkManager.getInstance(this).enqueue(otwr);
    }


    //si no tiene foto de perfil que se hace ?
    private void obtenerImagenPerfil() {

        //Para mandar los datos a la bd
        Data datosObtener = new Data.Builder()
                .putString("nom",nomUsuario)
                .putString("accion","obtener")
                .build();

        OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(conexionBDRemota.class)
                .setInputData(datosObtener)
                .build();

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(otwr.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if(workInfo != null && workInfo.getState() == WorkInfo.State.SUCCEEDED){

                            byte[] decodedString = Base64.decode(workInfo.getOutputData().toString(), Base64.DEFAULT);
                            Bitmap loadedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            imgPerfil.setImageBitmap(loadedBitmap);
                        }else {
                            Log.d("IMAGEN", "No se ha cargado la imagen");
                            Toast.makeText(PerfilUsuario.this,"Error al cargar imagen", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        WorkManager.getInstance(this).enqueue(otwr);

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


}