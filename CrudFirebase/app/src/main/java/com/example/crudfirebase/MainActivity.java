package com.example.crudfirebase;

import android.content.Intent;
import android.os.Bundle;

import com.example.crudfirebase.adapters.RecetaAdapter;
import com.example.crudfirebase.models.RecetaModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView lv_recetas;
    private SwipeRefreshLayout swiperefresh;
    private FloatingActionButton fab_nuevo;
    private ArrayList<RecetaModel> list;
    private String text_reference = "receta";
    private RecetaModel model;
    private FirebaseDatabase dbReceta = FirebaseDatabase.getInstance();
    private DatabaseReference dbReferencia = dbReceta.getReference(text_reference);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.tb_main);
        setSupportActionBar(toolbar);

        lv_recetas = findViewById(R.id.lvRecetas);
        swiperefresh = findViewById(R.id.swiperefresh);
        fab_nuevo = findViewById(R.id.fab);
        list = new ArrayList<>();
        model = new RecetaModel();

        dbReferencia.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                for(DataSnapshot child : dataSnapshot.getChildren())
                {
                    model = child.getValue(RecetaModel.class);
                    list.add(model);
                }
                lv_recetas.setAdapter(new RecetaAdapter(MainActivity.this, list));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Error con FireBase", Toast.LENGTH_LONG).show();
            }
        });

        fab_nuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nuevo = new Intent(MainActivity.this, NuevoActivity.class);
                startActivity(nuevo);
            }
        });

        lv_recetas.setOnItemClickListener(new AdapterView.OnItemClickListener() {

             @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                model = (RecetaModel)adapterView.getItemAtPosition(i);
                if (model.getId() != null && !model.getId().equals(""))
                {
                    Intent detalle = new Intent(MainActivity.this, DetalleActivity.class);
                    detalle.putExtra("id", model.getId());
                    startActivity(detalle);
                }
            }
        });

        swiperefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        dbReferencia.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                list = new ArrayList<>();
                                for(DataSnapshot child : dataSnapshot.getChildren())
                                {
                                    model = child.getValue(RecetaModel.class);
                                    list.add(model);
                                }
                                lv_recetas.setAdapter(new RecetaAdapter(MainActivity.this, list));
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(MainActivity.this, "Error con FireBase", Toast.LENGTH_LONG).show();
                            }
                        });
                        swiperefresh.setRefreshing(false);
                    }
                }
        );
    }

}
