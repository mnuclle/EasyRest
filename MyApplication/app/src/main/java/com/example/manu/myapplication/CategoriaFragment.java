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
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class CategoriaFragment extends ListFragment implements AdapterView.OnItemClickListener{

    private InterfazCategorias listener;
    private TextView textoMenus;
    private CategoriasAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_categoria, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textoMenus = (TextView) getActivity().findViewById(R.id.textoMenus);

        Typeface type = Typeface.createFromAsset(getActivity().getAssets(),"segoeui.ttf");
        textoMenus.setTypeface(type);

        adapter = new CategoriasAdapter();
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



    class CategoriasAdapter extends BaseAdapter {
        private ArrayList<String> listaCategorias;
        private LayoutInflater inflater;

        public CategoriasAdapter() {
            listaCategorias = new ArrayList<>();
            inflater = LayoutInflater.from(getActivity());


            listaCategorias.add("Minutas");
            listaCategorias.add("Lomitos");
            listaCategorias.add("Pastas");
            listaCategorias.add("Tablas");
            listaCategorias.add("Bebidas");
            listaCategorias.add("Pizzas");
            listaCategorias.add("Postres");
            listaCategorias.add("Desayuno");

        }

        public void addCategorias(String info) {
            if (info != null) {
                listaCategorias.add(info);
            }
        }

        public void clear() {
            listaCategorias = new ArrayList<>();
        }
        @Override
        public int getCount() {
            return listaCategorias.size();
        }

        @Override
        public Object getItem(int position) {
            return listaCategorias.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        class Holder {
            private TextView txtNombreCategoria;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup arg2) {

            Holder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.element_list_categoria, null);
                holder = new Holder();
                holder.txtNombreCategoria = (TextView) convertView
                        .findViewById(R.id.text1);

                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            String info = (String) getItem(position);

            holder.txtNombreCategoria.setText(info);

            Typeface type = Typeface.createFromAsset(convertView.getContext().getAssets(),"segoeui.ttf");
            holder.txtNombreCategoria.setTypeface(type);
            return convertView;
        }

    }

}
