package com.jv.firebasedb;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegistroUsuario extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_user);

        Button b = findViewById(R.id.BotonEnviar);
        b.setOnClickListener(view -> {
            EnviarInformacion();
        });
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

        songs.updateChildren (node)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText (getBaseContext (), "Entrada añadida exitosamente.", Toast.LENGTH_LONG).show ();
                    // Devuelve el usuario a la pantalla principal.
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText (getBaseContext (), "Node add failed: " + e.getMessage (), Toast.LENGTH_LONG).show ());
    }
}
