package com.example.manu.myapplication;

import android.app.Activity;
import android.app.ListFragment;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class CategoriaFragment extends ListFragment {



    private String[] categorias = { "MINUTAS", "LOMITOS", "PASTAS", "TABLAS",
            "BEBIDAS", "PIZZAS", "POSTRES", "DESAYUNO"};


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setListAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.activity_list_item, categorias));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_categoria, container, false);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);

        // Mostramos un mensaje con el elemento pulsado
        Toast.makeText(getActivity(), "Ha pulsado " + categorias[position],
                Toast.LENGTH_SHORT).show();
    }

}
