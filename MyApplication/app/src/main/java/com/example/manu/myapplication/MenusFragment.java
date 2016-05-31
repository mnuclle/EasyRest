package com.example.manu.myapplication;

import android.app.Activity;
import android.app.ListFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.manu.myapplication.Entidades.Menus;
import com.example.manu.myapplication.Entidades.TodasImages;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


public class MenusFragment extends ListFragment implements AdapterView.OnItemClickListener{
    private MenusAdapter adapter;
    private int categ = 0;
    private TextView menus;
    private InterfazListadoMenus listener;
    private String URLGlobal;

    public static MenusFragment newInstance(int idCategoria,String url) {
        MenusFragment fragment = new MenusFragment();
        Bundle args = new Bundle();
        args.putInt("idCategoria", idCategoria);
        args.putString("URLGlobal",url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menus, container, false);
        if (v != null) {
            //listaMenus = (ListView) v.findViewById(R.id.list);
            menus = (TextView) v.findViewById(R.id.textoMenus);
        }
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        menus.setText("Menus");
        if (getArguments() != null) {
            categ = getArguments().getInt("idCategoria", 0);
            URLGlobal = getArguments().getString("URLGlobal");
            menus.setText("Categoria Seleccionada" + categ);
        }

        adapter = new MenusAdapter();
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);

        Object[] obj = new Object[2];
        obj[0] = categ;
        obj[1] = URLGlobal;
        new GetTask().execute(obj);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        /* Toast.makeText(getActivity(), "Ha pulsado menu " + position + " " + adapter.listaMenus.get(position).getNombreMenu(),
                Toast.LENGTH_SHORT).show();*/
         if(listener!=null)
        {
            listener.onMenuSelect(adapter.listaMenus.get(position));
        }
    }

    private void loadMenus(ArrayList<Menus> listaMenus)
    {
        loadMenusData(listaMenus);
    }
    private void loadMenusData(ArrayList<Menus> listaMenus) {


        for (Menus cm : listaMenus
                ) {

            adapter.addMenus(cm);
        }
        if(listaMenus.size() !=0 )
            adapter.notifyDataSetChanged();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            listener = (InterfazListadoMenus) activity;
        }catch(ClassCastException ex){
            Log.e("ExampleFragment", "El Activity debe implementar la interfaz FragmentIterationListener");
        }
    }

    class MenusAdapter extends BaseAdapter {
        private ArrayList<Menus> listaMenus;
        private LayoutInflater inflater;
        private TodasImages ti;

        public MenusAdapter() {
            listaMenus = new ArrayList<>();
            inflater = LayoutInflater.from(getActivity());
            ti = new TodasImages();
        }

        public void addMenus(Menus info) {
            if (info != null) {
                listaMenus.add(info);
            }
        }

        @Override
        public int getCount() {
            return listaMenus.size();
        }

        @Override
        public Object getItem(int position) {
            return listaMenus.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        class Holder {
            private TextView txtNombreMenu;
            private TextView txtPrecio;
            private ImageView imageMenus;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup arg2) {

            Holder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.elemento_lista_menus, null);
                holder = new Holder();
                holder.imageMenus = (ImageView) convertView
                        .findViewById(R.id.imagenMenus);
                holder.txtNombreMenu = (TextView) convertView
                        .findViewById(R.id.nombreMenus);
                holder.txtPrecio = (TextView) convertView
                        .findViewById(R.id.precioMenus);

                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            Menus info = (Menus) getItem(position);
           /* if (info.isEsMenu()) {
                holder.imageMenus.setImageResource((ti.obtenerImagen(info.getIdMenu(), true)).getIdImagen());
            } else {
                holder.imageMenus.setImageResource((ti.obtenerImagen(info.getIdInsumo(), false)).getIdImagen());
            }*/
            holder.imageMenus.setImageResource(R.drawable.agua);
            holder.txtNombreMenu.setText(info.getNombreMenu());
            holder.txtPrecio.setText("$" + info.getPrecio());

            return convertView;
        }

    }

    class GetTask extends AsyncTask<Object, String, String> {

        private Exception exception;
        private ArrayList<Menus> listaMenus = new ArrayList<>();
        private Menus menus = new Menus();
        private String URLGlobal;

        protected String doInBackground(Object... params) {
            URLGlobal = params[1].toString();
            try {
                String response = "No se conecto";
                    HttpURLConnection urlConn;
                    StringBuilder result = new StringBuilder();
                    URL url = new URL(URLGlobal+"menu/menusXCateg/" + (((int) params[0])+1)); //obtengo los menus de las categorias por params[0]

                    urlConn = (HttpURLConnection) url.openConnection();

                    InputStream in = new BufferedInputStream(urlConn.getInputStream());

                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    try {
                        JsonReader reader1 = new JsonReader(new InputStreamReader(new ByteArrayInputStream(result.toString().getBytes(StandardCharsets.UTF_8))));
                        response = "GET: ";

                        reader1.beginArray();
                        while (reader1.hasNext()) {
                            int idMenu = -1;
                            int idInsumo = -1;
                            int idCategoria = -1;
                            String nombre = "";
                            boolean esMenu = false;
                            double precio = 0;
                            reader1.beginObject();
                            while (reader1.hasNext()) {
                                String name = reader1.nextName();
                                switch (name) {
                                    case "idMenu":
                                        if (reader1.peek() == JsonToken.NULL)
                                        {
                                            reader1.skipValue();
                                        } else {
                                            idMenu = reader1.nextInt();
                                        }
                                        break;
                                    case "idInsumo":
                                        if (reader1.peek() == JsonToken.NULL)
                                        {
                                            reader1.skipValue();
                                        } else {
                                            idInsumo = reader1.nextInt();
                                        }
                                        break;
                                    case "nombre":
                                        nombre = reader1.nextString();
                                        break;
                                    case "idCategoria":
                                        idCategoria = reader1.nextInt();
                                        break;
                                    case "esMenu":
                                        String esMenuAux = "S";
                                        if (reader1.nextString() == esMenuAux)
                                            esMenu = true;
                                        break;
                                    case "precio":
                                        precio = reader1.nextDouble();
                                        break;
                                    default:
                                        reader1.skipValue();
                                        break;
                                }
                            }
                            reader1.endObject();
                            menus = new Menus();
                            if (idInsumo == -1)
                                menus.setIdMenu(idMenu);
                            else
                                menus.setIdInsumo(idInsumo);
                            menus.setEsMenu(esMenu);
                            menus.setPrecio(precio);
                            menus.setNombreMenu(nombre);
                            menus.setIdCategoria(idCategoria);
                            listaMenus.add(menus);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        urlConn.disconnect();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                {}
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            loadMenus(listaMenus);
        }
    }
}
