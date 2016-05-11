package com.example.manu.myapplication.Entidades;

import android.widget.ImageView;

import com.example.manu.myapplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Manu on 07/05/2016.
 */
public class TodasImages {

    private ArrayList<Images> listaImagen = new ArrayList<>();


    public TodasImages() {

        Images imagen_1 = new Images(1, R.drawable.agua, false, 20);
        Images imagen_2 = new Images(2, R.drawable.coca, false, 21);
        Images imagen_3 = new Images(3, R.drawable.desayuno, false, 22);
        Images imagen_4 = new Images(4, R.drawable.hamburguesa, true, 2);
        Images imagen_5 = new Images(5, R.drawable.lomito, true, 3);
        Images imagen_6 = new Images(6, R.drawable.parrilla, true, 4);

        listaImagen.add(imagen_1);
        listaImagen.add(imagen_2);
        listaImagen.add(imagen_3);
        listaImagen.add(imagen_4);
        listaImagen.add(imagen_5);
        listaImagen.add(imagen_6);
       
    }

    public Images obtenerImagen(int id, boolean esMenu)
    {
        Images image = null;
        for (Images im :
                listaImagen) {
            if (im.getId() == id && im.isEsMenu() == esMenu) {
                image = im;
                break;
            }
        }
        return image;

    }



}
