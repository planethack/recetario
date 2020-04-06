package com.example.crudfirebase.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.crudfirebase.R;
import com.example.crudfirebase.models.RecetaModel;

import java.util.ArrayList;

public class RecetaAdapter extends BaseAdapter {
    private final Context context;
    private RecetaModel model;
    private ArrayList<RecetaModel> list;

    public RecetaAdapter(Context context, ArrayList<RecetaModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View itemView = view;
        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(R.layout.item_receta, viewGroup, false);
        }
        TextView txTitulo = itemView.findViewById(R.id.txTitulo);
        TextView txTiempo = itemView.findViewById(R.id.txTiempo);
        ImageView imagen = itemView.findViewById(R.id.imgReceta);
        model = list.get(i);
        txTitulo.setText(model.getTitulo().toString());
        txTiempo.setText(model.getTiempo().toString() + " Minutos");
        String imagen64 = model.getImagen().toString();
        if(!imagen64.equals("")) {
            byte[] decodedString = Base64.decode(imagen64, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            imagen.setImageBitmap(decodedByte);
        }
        return itemView;
    }
}
