package com.guzman.realidadvirtual.ACTIVITIES;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.guzman.realidadvirtual.ADAPTER.ImagenAdapter;
import com.guzman.realidadvirtual.CLASS.ImagenClass;
import com.guzman.realidadvirtual.R;
import com.guzman.realidadvirtual.UTILS.PARAMETROSGLOBALES;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    ImagenAdapter imagenAdapter;
    RecyclerView rvImagenes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Iniciar();
    }

    private void Iniciar(){

        rvImagenes = findViewById(R.id.rv_imagenes_activity_menu);
        //rvImagenes.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
         rvImagenes.setLayoutManager(new GridLayoutManager(this,2));
         rvImagenes.setHasFixedSize(true);
        //rvImagenes.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
       // rvImagenes.setLayoutManager();
        CargarImagenes();

    }


    private void CargarImagenes(){

        List<ImagenClass> imagenClasses = new ArrayList();

        imagenClasses.add(new ImagenClass(1,"Personaje mujer con uniforme"));
        imagenClasses.add(new ImagenClass(2,"Personaje hombre con uniforme"));
        imagenClasses.add(new ImagenClass(3,"Personaje mujer con vestido"));
        //imagenClasses.add(new ImagenClass(4,"Personaje de anime mujer 02" ));

        //PARAMETROSGLOBALES.imagenClasses = imagenClasses;

        imagenAdapter = new ImagenAdapter(this,imagenClasses);
        rvImagenes.setAdapter(imagenAdapter);
        imagenAdapter.notifyDataSetChanged();



    }


}
