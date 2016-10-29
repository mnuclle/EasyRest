package com.example.manu.myapplication;

import android.app.Activity;
import android.app.ListFragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CategoriaFragment extends ListFragment implements AdapterView.OnItemClickListener{

    private InterfazCategorias listener;
    private TextView textoMenus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_categoria, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.Categorias, R.layout.element_list_categoria);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);

        textoMenus = (TextView) getActivity().findViewById(R.id.textoMenus);

        Typeface type = Typeface.createFromAsset(getActivity().getAssets(),"segoeui.ttf");
        textoMenus.setTypeface(type);


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


}
