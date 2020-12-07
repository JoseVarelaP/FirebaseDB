package com.jv.firebasedb;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;

    RecyclerView lv;

    private FirebaseRecyclerOptions<Usuario> options;
    private FirebaseRecyclerAdapter<Usuario, MyViewHolder> adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), RegistroUsuario.class);
                startActivityForResult(myIntent, 0);
            }
        });

        lv = findViewById (R.id.rvList);
        lv.setHasFixedSize(true);
        lv.setLayoutManager (new LinearLayoutManager (this));
        lv.addItemDecoration (new DividerItemDecoration (getBaseContext (), DividerItemDecoration.VERTICAL));

        cargarListado();
    }

    void cargarListado(){
        Vector<Usuario> vUsers = new Vector<>();

        auth = FirebaseAuth.getInstance ();
        iniciarSesion ();

        DatabaseReference songs = FirebaseDatabase.getInstance().getReference().child("usuarios");

        FirebaseStorage storage = FirebaseStorage.getInstance ();

        options = new FirebaseRecyclerOptions.Builder<Usuario>().setQuery( songs, Usuario.class ).build();
        adapter = new FirebaseRecyclerAdapter<Usuario, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Usuario model) {

                // Configura la imagen.
                Utilidades u = new Utilidades();
                StorageReference folder = storage.getReference ("images/");
                StorageReference imageFile = folder.child ( u.md5( model.getNombre() ) + ".png");

                final long SIZE_BUFFER = 1024 * 1024;
                imageFile.getBytes (SIZE_BUFFER)
                        .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess (byte[] bytes) {
                                Bitmap bitmap = BitmapFactory.decodeByteArray (bytes, 0, bytes.length);
                                holder.img.setImageBitmap (bitmap);
                                Log.i("Image","Loaded image! ("+ model.getNombre() + ")" );
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure (@NonNull Exception e) {
                        holder.img.setImageResource( R.drawable.default_picture );
                        // Toast.makeText (getBaseContext (), "Error: " + e.getMessage (), Toast.LENGTH_LONG).show();
                    }
                });

                // Configura lo demás.
                holder.tvTitle.setText (model.getNombre());
                holder.tvComposer.setText (model.getApellidos());
                holder.tvAlbum.setText (model.getDireccion());
                holder.tvYear.setText (""+model.getEdad());
                holder.tvCompany.setText (model.getTelefono());
            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem,parent,false);
                return new MyViewHolder(v);
            }
        };

        adapter.startListening();
        lv.setAdapter(adapter);
    }

    void iniciarSesion () {
        auth.signInAnonymously ()
                .addOnFailureListener(e -> Log.e ("FBDB", "Fail on anonymous auth", e));
    }
}