package com.example.crudfirebase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.example.crudfirebase.models.RecetaModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Base64;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class NuevoActivity extends AppCompatActivity {

    private String id;
    private EditText txTitulo;
    private EditText txTiempo;
    private EditText txReceta;
    private ImageView iv;
    private FloatingActionButton fab_nuevo;
    private String text_reference = "receta";
    private RecetaModel model;
    private FirebaseDatabase dbReceta = FirebaseDatabase.getInstance();
    private DatabaseReference dbReferencia = dbReceta.getReference(text_reference);

    Bitmap bitmap;
    int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo);
        Toolbar toolbar = findViewById(R.id.tb_nuevo);
        setSupportActionBar(toolbar);

        fab_nuevo = findViewById(R.id.fab_nuevo);
        txTitulo = findViewById(R.id.txTitulo);
        txTiempo = findViewById(R.id.txTiempo);
        txReceta = findViewById(R.id.txReceta);

        iv = findViewById(R.id.imagen);

        fab_nuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String titulo = txTitulo.getText().toString();
                String tiempo = txTiempo.getText().toString();
                String receta = txReceta.getText().toString();
                String imagen = "";
                if(bitmap != null)
                    imagen = getStringImagen(bitmap);

                if(!titulo.equals("") && !tiempo.equals("") && !receta.equals("")) {
                     id = dbReferencia.push().getKey();
                    if (id != null) {
                        model = new RecetaModel(id, titulo, tiempo, receta, imagen);
                        dbReferencia.child(id).setValue(model)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        Toast.makeText(NuevoActivity.this, "Elemento guardado satisfactoriamente", Toast.LENGTH_LONG).show();
                                        Intent lista = new Intent(NuevoActivity.this, MainActivity.class);
                                        startActivity(lista);
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(NuevoActivity.this, "Debe completar todos los campos", Toast.LENGTH_LONG).show();
                                    }
                                });
                    } else {
                        Toast.makeText(NuevoActivity.this, "Debe completar todos los campos", Toast.LENGTH_LONG).show();
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
