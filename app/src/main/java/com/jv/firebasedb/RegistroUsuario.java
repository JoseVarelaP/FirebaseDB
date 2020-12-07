package com.jv.firebasedb;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class RegistroUsuario extends AppCompatActivity {

    private ImageView imageView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_user);

        Button b = findViewById(R.id.BotonEnviar);
        b.setOnClickListener(view -> {
            EnviarInformacion();
        });

        Button a = findViewById(R.id.CargarImagen);
        a.setOnClickListener(view -> {
            SelectImage();
        });

        imageView = findViewById(R.id.ImagenView);
    }

    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 22;

    private void SelectImage()
    {
        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    private void SubirImagen()
    {
        Utilidades u = new Utilidades();
        FirebaseStorage storage = FirebaseStorage.getInstance ();

        StorageReference imagesFolder = storage.getReference ("images/");
        EditText TNombre = findViewById(R.id.Nombre);
        String nm = u.md5( TNombre.getText().toString() );
        // Guarda la imagen basada en el nombre del usuario.
        StorageReference  image = imagesFolder.child ( nm + ".png");

        ByteArrayOutputStream bos = new ByteArrayOutputStream ();
        Bitmap bitmap = getBitmapFromDrawable (imageView.getDrawable ());
        bitmap.compress (Bitmap.CompressFormat.PNG, 100, bos);
        byte [] buffer = bos.toByteArray ();

        image.putBytes (buffer)
                .addOnFailureListener (e -> {
                    Toast.makeText (getBaseContext (), "Error uploading file: " + e.getMessage (), Toast.LENGTH_LONG).show ();
                    Log.e ("TYAM", "Error uploading file: " + e.getMessage ());
                })
                .addOnCompleteListener (task -> {
                    if (task.isComplete ()) {
                        Task<Uri> getUriTask = image.getDownloadUrl ();

                        getUriTask.addOnCompleteListener (t -> {
                            Uri uri = t.getResult ();
                            if (uri == null) return;

                            Toast.makeText (getBaseContext (), "Download URL " + uri.toString (), Toast.LENGTH_LONG).show ();
                            Log.i ("TYAM", "Download URL " + uri.toString ());
                        });
                    }
                });
    }

    // Override onActivityResult method
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                                getContentResolver(),
                                filePath);
                imageView.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    public void EnviarInformacion(){
        // Obten los objetos.
        EditText TNombre, TApellido, TDireccion, TEdad, TTelefono;

        TNombre = findViewById(R.id.Nombre);
        TApellido = findViewById(R.id.Apellido);
        TDireccion = findViewById(R.id.Direccion);
        TEdad = findViewById(R.id.Edad);
        TTelefono = findViewById(R.id.Telefono);

        Usuario usr = new Usuario();
        usr.setNombre( TNombre.getText().toString() );
        usr.setApellidos( TApellido.getText().toString() );
        usr.setDireccion( TDireccion.getText().toString() );
        usr.setEdad( Integer.parseInt( TEdad.getText().toString() ) );
        usr.setTelefono( TTelefono.getText().toString() );

        // La implementacion del objeto a la base de datos será en un hash MD5.
        Utilidades u = new Utilidades();
        String NuevoMD5 = u.md5( usr.getNombre() );

        FirebaseDatabase database = FirebaseDatabase.getInstance ();
        DatabaseReference songs = database.getReference ("usuarios");

        HashMap<String, Object> node = new HashMap<>();
        node.put (NuevoMD5 , usr);

        // Si no hay imagen, entonces no hagas nada en subirla.
        if( imageView.getDrawable() != null )
            SubirImagen();

        songs.updateChildren (node)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText (getBaseContext (), "Entrada añadida exitosamente.", Toast.LENGTH_LONG).show ();
                    // Devuelve el usuario a la pantalla principal.
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText (getBaseContext (), "Node add failed: " + e.getMessage (), Toast.LENGTH_LONG).show ());
    }

    /**
     * Obtiene un objeto de mapa de bits a partir del objeto Drawable (canvas) recibido.
     *
     * @param drble Drawable que contiene la imagen deseada.
     * @return objeto de mapa de bits con la estructura de la imagen.
     */
    private Bitmap getBitmapFromDrawable (Drawable drble) {
        // debido a la forma que el sistema dibuja una imagen en un el sistema gráfico
        // es necearios realzar comprobaciones para saber del tipo de objeto Drawable
        // con que se está trabajando.
        //
        // si el objeto recibido es del tipo BitmapDrawable no se requieren más conversiones
        if (drble instanceof BitmapDrawable) {
            return  ((BitmapDrawable) drble).getBitmap ();
        }

        // en caso contrario, se crea un nuevo objeto Bitmap a partir del contenido
        // del objeto Drawable
        Bitmap bitmap = Bitmap.createBitmap (drble.getIntrinsicWidth (), drble.getIntrinsicHeight (), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drble.setBounds (0, 0, canvas.getWidth (), canvas.getHeight ());
        drble.draw (canvas);

        return bitmap;
    }
}
