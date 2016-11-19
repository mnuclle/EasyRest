package com.example.manu.myapplication.Cliente;

import android.app.Activity;
import android.app.Dialog;
import android.app.ListFragment;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manu.myapplication.Entidades.DetallePedido;
import com.example.manu.myapplication.Entidades.Images;
import com.example.manu.myapplication.Entidades.Menus;
import com.example.manu.myapplication.Entidades.TodasImages;
import com.example.manu.myapplication.InterfazListadoMenus;
import com.example.manu.myapplication.R;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;


public class ClienteMenuFragment extends ListFragment implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener{
    private MenusAdapter adapter;
    private int categ = 0;
    private TextView menus;
    private InterfazListadoMenus listener;
    private String URLGlobal;
    private TextView cantidadMenus,textoMenus;
    private ArrayList<DetallePedido> listadoDetallePedido;

    public static ClienteMenuFragment newInstance(int idCategoria, String url, ArrayList<DetallePedido> listadoDetallePedido) {
        ClienteMenuFragment fragment = new ClienteMenuFragment();
        Bundle args = new Bundle();
        args.putInt("idCategoria", idCategoria);
        args.putString("URLGlobal",url);
        args.putSerializable("LISTADODETALLEPEDIDO",listadoDetallePedido);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cliente_menu, container, false);
        if (v != null) {
            //listaMenus = (ListView) v.findViewById(R.id.list);
            menus = (TextView) v.findViewById(R.id.textoMenusCliente);
            cantidadMenus = (TextView) v.findViewById(R.id.cantidadDeMenus);



            Typeface type = Typeface.createFromAsset(getActivity().getAssets(),"segoeui.ttf");
            menus.setTypeface(type);
        }
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        menus.setText("Menus");
        if (getArguments() != null) {
            categ = getArguments().getInt("idCategoria", 8);
            URLGlobal = getArguments().getString("URLGlobal");
            listadoDetallePedido = (ArrayList<DetallePedido>) getArguments().getSerializable("LISTADODETALLEPEDIDO");
            String categoria = "";
            switch (categ){
                case 0 :
                    categoria = "Minutas";
                    break;
                case 1 :
                    categoria = "Lomitos";
                    break;
                case 2 :
                    categoria = "Pastas";
                    break;
                case 3 :
                    categoria = "Tablas";
                    break;
                case 4 :
                    categoria = "Bebidas";
                    break;
                case 5 :
                    categoria = "Pizzas";
                    break;
                case 6 :
                    categoria = "Postres";
                    break;
                case 7 :
                    categoria = "Desayuno";
                    break;
                default:
                    categoria = "NO SELECCCIONÓ CATEGORÍA.";
                    break;
            }

            menus.setText(categoria);
        }

        adapter = new MenusAdapter();
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
        getListView().setOnItemLongClickListener(this);

        Object[] obj = new Object[3];
        obj[0] = categ;
        obj[1] = URLGlobal;
        obj[2] = listadoDetallePedido;
        new GetTask().execute(obj);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        /* Toast.makeText(getActivity(), "Ha pulsado menu " + position + " " + adapter.listaMenus.get(position).getNombreMenu(),
                Toast.LENGTH_SHORT).show();*/
        if(listener!=null)
        {

            Menus menu = adapter.listaMenus.get(position);
            if (menu.getCantidad() == menu.getStock() && !menu.isEsMenu())
            {
                Toast.makeText(getActivity(), "No hay mas menú en stock.",
                        Toast.LENGTH_SHORT).show();
            }
            else {
                listener.onMenuSelect(adapter.listaMenus.get(position));
                loadMenusSumarCantidad(menu);
            }
        }
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        if(listener!=null) {

            Menus menu = (Menus) adapter.getItem(position);
            if (menu.getCantidad() > 0) {
                final Dialog dialog = new Dialog(view.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_restar_ver);
                Button dialogButtonVerMenu = (Button) dialog.findViewById(R.id.dialogButtonVerMenu);
                Button dialogButtonRestarMenu = (Button) dialog.findViewById(R.id.dialogButtonRestarMenu);
                TextView txtTituloDialog = (TextView) dialog.findViewById(R.id.txtTituloDialog) ;
                dialog.setTitle("ACCIONES");

                Typeface type = Typeface.createFromAsset(view.getContext().getAssets(),"segoeui.ttf");
                dialogButtonVerMenu.setTypeface(type);
                dialogButtonRestarMenu.setTypeface(type);
                txtTituloDialog.setTypeface(type);

                // if button is clicked, close the custom dialog
                dialogButtonVerMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        VerDetalle(position);
                        dialog.cancel();
                    }
                });
                dialogButtonRestarMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        restarUno(position);
                        dialog.cancel();
                    }
                });

                dialog.show();
            } else {
                final Dialog dialog = new Dialog(view.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_ver);
                Button dialogButtonVer = (Button) dialog.findViewById(R.id.dialogButtonVer);
                TextView txtTituloDialog = (TextView) dialog.findViewById(R.id.txtTituloDialog) ;

                Typeface type = Typeface.createFromAsset(view.getContext().getAssets(),"segoeui.ttf");
                dialogButtonVer.setTypeface(type);
                txtTituloDialog.setTypeface(type);


                dialog.setTitle("ACCIONES");

                // if button is clicked, close the custom dialog
                dialogButtonVer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        VerDetalle(position);
                        dialog.cancel();
                    }
                });

                dialog.show();
            }
        }
        return true;
    }


    public void VerDetalle(int position)
    {
        TodasImages ti = new TodasImages();

        Menus menu = (Menus) adapter.getItem(position);

        final Dialog dialog = new Dialog(getView().getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_ver_descripcion_menu);

        ImageView imagen = (ImageView) dialog.findViewById(R.id.imagenMenuDetalle);
        TextView txtNombreMenu = (TextView) dialog.findViewById(R.id.txtNombreMenuDescripcion);
        TextView txtDescripcionMenu = (TextView) dialog.findViewById(R.id.txtDescripcionMenu);
        TextView txtTituloDialog = (TextView) dialog.findViewById(R.id.txtTituloDialog) ;

        txtNombreMenu.setText(menu.getNombreMenu());
        txtDescripcionMenu.setText(menu.getDescripcion());

        Typeface type = Typeface.createFromAsset(getView().getContext().getAssets(),"segoeui.ttf");
        txtNombreMenu.setTypeface(type);
        txtDescripcionMenu.setTypeface(type);
        txtTituloDialog.setTypeface(type);

        if (menu.isEsMenu()) {
            imagen.setImageResource((ti.obtenerImagen(menu.getIdMenu(), true)).getIdImagen());
        } else {
            imagen.setImageResource((ti.obtenerImagen(menu.getIdInsumo(), false)).getIdImagen());
        }
        dialog.setTitle("DESCRIPCIÓN");
        dialog.show();
    }

    public void restarUno(int position)
    {

        Menus menu = (Menus) adapter.getItem(position);
        int cantidad = menu.getCantidad();

        menu.setCantidad(cantidad-1);
        if (!menu.isEsMenu()) {
            int stock = menu.getStock();
            menu.setStock(stock+1);
        }
        listener.onMenuLongSelect(menu);
        adapter.notifyDataSetChanged();
    }

    private void loadMenusSumarCantidad(Menus menu)
    {
        ArrayList<Menus> listadoMenus = adapter.listaMenus;
        adapter.clear();
        for (Menus men: listadoMenus
                ) {
            if (men.getNombreMenu().equals(menu.getNombreMenu()))
            {
                int cantidad = men.getCantidad() + 1;
                men.setCantidad(cantidad);
            }
            adapter.addMenus(men);
        }
        adapter.notifyDataSetChanged();
    }
    private void loadMenus(ArrayList<Menus> listaMenus, ArrayList<DetallePedido> listadoDetallePedido)
    {
        for (Menus m : listaMenus
                ) {
            for (DetallePedido det : listadoDetallePedido
                    ) {
                if (m.getNombreMenu().equals(det.getNombreMenu()))
                {
                    m.setCantidad(det.getCantidad());
                    break;
                }
            }
        }
        loadMenusData(listaMenus);
    }
    private void loadMenusData(ArrayList<Menus> listaMenus) {

        adapter.clear();
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
        private Images im;

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

        public void clear() {
            listaMenus = new ArrayList<>();
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
            private TextView cantidad;
            private TextView txtDescripcion;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup arg2) {

            MenusAdapter.Holder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.elemento_lista_menus, null);
                holder = new MenusAdapter.Holder();
                holder.imageMenus = (ImageView) convertView
                        .findViewById(R.id.imagenMenus);
                holder.txtNombreMenu = (TextView) convertView
                        .findViewById(R.id.nombreMenus);
                holder.txtPrecio = (TextView) convertView
                        .findViewById(R.id.precioMenus);
                holder.cantidad = (TextView) convertView
                        .findViewById(R.id.cantidadDeMenus);
                holder.txtDescripcion = (TextView) convertView
                        .findViewById(R.id.observacionesMenu);

                convertView.setTag(holder);
            } else {
                holder = (MenusAdapter.Holder) convertView.getTag();
            }

            Menus info = (Menus) getItem(position);
            if (ti.equals(null))
                ti = new TodasImages();

            if (info.isEsMenu()) {
                im = (ti.obtenerImagen(info.getIdMenu(), true));
                holder.imageMenus.setImageBitmap(decodeSampledBitmapFromResource(getResources(), im.getIdImagen(),100,100));
              //  holder.imageMenus.setImageResource((ti.obtenerImagen(info.getIdMenu(), true)).getIdImagen());
            } else {
                im = (ti.obtenerImagen(info.getIdInsumo(), false));
                holder.imageMenus.setImageBitmap(decodeSampledBitmapFromResource(getResources(), im.getIdImagen(),100,100));
                //holder.imageMenus.setImageResource((ti.obtenerImagen(info.getIdInsumo(), false)).getIdImagen());
            }
            int color;
            if (info.getCantidad() == 0)
                color = Color.WHITE;
            else
                color = Color.parseColor("#F38129");

            holder.cantidad.setTextColor(color);
            if(info.getCantidad() == 0)
                holder.cantidad.setText("0");
            else
                holder.cantidad.setText(""+info.getCantidad());

            if (info.getCantidad() == info.getStock() && !info.isEsMenu())
                color = Color.GRAY;
            else
                color = Color.WHITE;


            holder.txtNombreMenu.setTextColor(color);
            holder.txtPrecio.setTextColor(color);
            holder.txtDescripcion.setTextColor(color);

            //holder.imageMenus.setImageResource(R.drawable.agua);
            int stock = info.getStock() - info.getCantidad();

            if (info.isEsMenu())
                holder.txtNombreMenu.setText(info.getNombreMenu());
            else
                holder.txtNombreMenu.setText(info.getNombreMenu());
            // + " (Stock: " + stock + ")"

            holder.txtPrecio.setText("$" + String.format("%.2f",info.getPrecio()));
            holder.txtDescripcion.setText(info.getDescripcion());

            Typeface type = Typeface.createFromAsset(convertView.getContext().getAssets(),"segoeui.ttf");
            holder.txtNombreMenu.setTypeface(type);
            holder.txtPrecio.setTypeface(type);
            holder.cantidad.setTypeface(type);
            holder.txtDescripcion.setTypeface(type);
            return convertView;
        }

    }

    class GetTask extends AsyncTask<Object, String, String> {

        private Exception exception;

        private ArrayList<Menus> listaMenus = new ArrayList<>();
        private ArrayList<DetallePedido> listadoDetallePedido;
        private Menus menus = new Menus();
        private String URLGlobal;

        protected String doInBackground(Object... params) {
            URLGlobal = params[1].toString();
            listadoDetallePedido = (ArrayList<DetallePedido>) params[2];
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
                    JsonReader reader1 = new JsonReader(new InputStreamReader(new ByteArrayInputStream(result.toString().getBytes(Charset.forName("UTF-8")))));
                    response = "GET: ";

                    reader1.beginArray();
                    while (reader1.hasNext()) {
                        int idMenu = -1;
                        int idInsumo = -1;
                        int idCategoria = -1;
                        String nombre = "";
                        boolean esMenu = false;
                        double precio = 0;
                        String descripcion ="";
                        int stock = 0;
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
                                    String esmenu = reader1.nextString();
                                    if (esmenu.toString().equals(esMenuAux.toString()))
                                        esMenu = true;
                                    break;
                                case "precio":
                                    precio = reader1.nextDouble();
                                    break;
                                case "descripcion":
                                    descripcion = reader1.nextString();
                                    break;
                                case "stock":
                                    if (reader1.peek() == JsonToken.NULL)
                                    {
                                        stock = 0;
                                    }
                                    else
                                    {
                                        stock = reader1.nextInt();
                                    }
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
                        menus.setDescripcion(descripcion);
                        menus.setStock(stock);
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
            loadMenus(listaMenus,listadoDetallePedido);
        }
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }
}
