package com.example.crudfirebase;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import com.example.crudfirebase.models.RecetaModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class EditarActivity extends AppCompatActivity {
    private EditText txTitulo;
    private EditText txTiempo;
    private EditText txReceta;
    private ImageView iv;
    private String text_reference = "receta";
    private String id;
    private RecetaModel model;
    private FirebaseDatabase dbReceta = FirebaseDatabase.getInstance();
    private DatabaseReference dbReferencia = dbReceta.getReference(text_reference);

    Bitmap bitmap;
    int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txTitulo = findViewById(R.id.txTitulo);
        txTiempo = findViewById(R.id.txTiempo);
        txReceta = findViewById(R.id.txReceta);
        iv = findViewById(R.id.imgReceta);

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
                        txTiempo.setText(model.getTiempo().toString());
                        txReceta.setText(model.getReceta().toString());
                        String imagen64 = model.getImagen().toString();
                        if(!imagen64.equals("")) {
                            byte[] decodedString = Base64.decode(imagen64, Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                            iv.setImageBitmap(decodedByte);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(EditarActivity.this, "Error con FireBase", Toast.LENGTH_LONG).show();
                }
            });
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String titulo = txTitulo.getText().toString();
                String tiempo = txTiempo.getText().toString();
                String receta = txReceta.getText().toString();
                String imagen = "";
                if(bitmap != null)
                    imagen = getStringImagen(bitmap);

                if(!titulo.equals("") && !tiempo.equals("") && !receta.equals("")) {
                    if (id != null) {
                        model = new RecetaModel(id, titulo, tiempo, receta, imagen);

                        dbReferencia.child(id).setValue(model)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(EditarActivity.this, "Elemento guardado satisfactoriamente", Toast.LENGTH_LONG).show();
                                        Intent lista = new Intent(EditarActivity.this, MainActivity.class);
                                        startActivity(lista);
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(EditarActivity.this, "Debe completar todos los campos", Toast.LENGTH_LONG).show();
                                    }
                                });
                    } else {
                        Toast.makeText(EditarActivity.this, "Debe completar todos los campos", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
    }

    public String getStringImagen(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleciona imagen"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Cómo obtener el mapa de bits de la Galería
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Configuración del mapa de bits en ImageView
                iv.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
