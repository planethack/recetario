package com.example.crudfirebase;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.example.crudfirebase.models.RecetaModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DetalleActivity extends AppCompatActivity {

    private FloatingActionButton fab_eliminar;
    private FloatingActionButton fab_editar;
    private TextView txTitulo;
    private TextView txTiempo;
    private TextView txReceta;
    private ImageView imagen;
    private String text_reference = "receta";
    private String id;
    private RecetaModel model;
    private FirebaseDatabase dbReceta = FirebaseDatabase.getInstance();
    private DatabaseReference dbReferencia = dbReceta.getReference(text_reference);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);
        Toolbar toolbar = findViewById(R.id.tb_detalle);
        setSupportActionBar(toolbar);


        txTitulo = findViewById(R.id.txTitulo);
        txTiempo = findViewById(R.id.txTiempo);
        txReceta = findViewById(R.id.txReceta);
        imagen = findViewById(R.id.imgReceta);
        fab_eliminar = findViewById(R.id.fab_eliminar);
        fab_editar = findViewById(R.id.fab_editar);

        model = new RecetaModel();
        id = getIntent().getStringExtra("id");
        if(id != null && !id.equals(""))
        {
            dbReferencia.child(id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    model = dataSnapshot.getValue(RecetaModel.class);
                    if(model != null)
                    {
                        txTitulo.setText(model.getTitulo().toString());
                        txTiempo.setText(model.getTiempo().toString() + " Minutos");
                        txReceta.setText(model.getReceta().toString());
                        String imagen64 = model.getImagen().toString();
                        if(!imagen64.equals("")) {
                            byte[] decodedString = Base64.decode(imagen64, Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                            imagen.setImageBitmap(decodedByte);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(DetalleActivity.this, "Error con FireBase", Toast.LENGTH_LONG).show();
                }
            });
        }


        fab_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editar = new Intent(DetalleActivity.this, EditarActivity.class);
                    editar.putExtra("id", model.getId());
                    startActivity(editar);
            }
        });

        fab_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(id != null && !id.equals(""))
                {
                    new AlertDialog.Builder(DetalleActivity.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle(R.string.title_borrar)
                            .setMessage(R.string.message_borrar)
                            .setPositiveButton(R.string.Si, new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dbReferencia.child(id).removeValue();
                                    Toast.makeText(DetalleActivity.this, "Elemento eliminado satisfactoriamente", Toast.LENGTH_LONG).show();
                                    Intent lista = new Intent(DetalleActivity.this, MainActivity.class);
                                    startActivity(lista);
                                    finish();
                                }
                            })
                            .setNegativeButton(R.string.No, null)
                            .show();





                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
