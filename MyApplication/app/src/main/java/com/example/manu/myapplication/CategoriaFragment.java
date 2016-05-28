package com.example.manu.myapplication;

import android.app.ListFragment;
import android.os.Bundle;
<<<<<<< HEAD
import android.util.Log;
=======
>>>>>>> origin/master
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
<<<<<<< HEAD
=======
import android.widget.Toast;
>>>>>>> origin/master

import java.util.ArrayList;

<<<<<<< HEAD
public class CategoriaFragment extends ListFragment implements AdapterView.OnItemClickListener{

    private InterfazCategorias listener;

=======

public class CategoriaFragment extends ListFragment implements AdapterView.OnItemClickListener {

    private ArrayList<String> list = new ArrayList<>();

    private String[] categorias = { "MINUTAS", "LOMITOS", "PASTAS", "TABLAS",
            "BEBIDAS", "PIZZAS", "POSTRES", "DESAYUNO"};


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setListAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.activity_list_item, categorias));

    }
>>>>>>> origin/master

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_categoria, container, false);
    }

<<<<<<< HEAD
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.Categorias, android.R.layout.simple_list_item_1);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);

    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(listener!=null)
        {
            listener.onCategoriaSelect(position);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            listener = (InterfazCategorias) activity;
        }catch(ClassCastException ex){
            Log.e("ExampleFragment", "El Activity debe implementar la interfaz FragmentIterationListener");
        }
    }

=======
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        list.add("MINUTAS");
        list.add("LOMITOS");
        list.add("PASTAS");
        list.add("TABLAS");
        list.add("BEBIDAS");
        list.add("PIZZAS");
        list.add("POSTRES");
        list.add("DESAYUNO");


        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.Categorias, android.R.layout.simple_list_item_1);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getActivity(), "Ha pulsado " + list.get(position),
                Toast.LENGTH_SHORT).show();
    }
>>>>>>> origin/master
}
