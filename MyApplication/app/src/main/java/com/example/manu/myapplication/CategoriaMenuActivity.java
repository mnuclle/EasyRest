package com.example.manu.myapplication;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.example.manu.myapplication.Entidades.DetallePedido;
import com.example.manu.myapplication.Entidades.Menus;
import java.util.ArrayList;
import java.util.Iterator;

public class CategoriaMenuActivity extends FragmentActivity implements InterfazCategorias,InterfazListadoMenus{
    private int NroCliente;
    private ArrayList<Menus> listadoMenus = new ArrayList<Menus>();
    private ArrayList<DetallePedido> listadoDetalleAConfirmar;
    private Button btnAceptar;
    private String URLGlobal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria_menu);
        btnAceptar = (Button) findViewById(R.id.btnAceptarPedido);
        final Intent intent = getIntent();
        NroCliente=(int) intent.getExtras().get("IDCLIENTE");
        URLGlobal = intent.getExtras().get("URLGlobal").toString();
        Bundle bundle = intent.getExtras();
        listadoDetalleAConfirmar = (ArrayList<DetallePedido>) bundle.getSerializable("LISTAACONDFIRMAR");

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                ArrayList<DetallePedido> listadoMenusFinal;
                listadoMenusFinal = loadListadoMenus(listadoMenus);
                Intent intent1 = new Intent();
                intent1.putExtra("IDCLIENTE",NroCliente);
                intent1.putExtra("URLGlobal",URLGlobal);
                Bundle bundle = new Bundle();
                bundle.putSerializable("listadoMenus",listadoMenusFinal);
                intent1.putExtras(bundle);

                //intent1.putExtra("listadoMenus",listadoMenus);

                setResult(Activity.RESULT_OK,intent1);
                finish();
            }

        });



        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();

        CategoriaFragment fragment1 = new CategoriaFragment();
        MenusFragment fragment2 = MenusFragment.newInstance(1,URLGlobal,listadoDetalleAConfirmar);

        fragmentTransaction.add(R.id.FragmentContainer1, fragment1);
        fragmentTransaction.add(R.id.FragmentContainer2, fragment2);
        fragmentTransaction.commit();


        }

    private ArrayList<DetallePedido> loadListadoMenus(ArrayList<Menus> listado)
    {
        ArrayList<DetallePedido> listadoMenusFinal;

        if (listadoDetalleAConfirmar != null )
            listadoMenusFinal = listadoDetalleAConfirmar;
        else
            listadoMenusFinal = new ArrayList<>();

        boolean agrego = false;
        for(Iterator<Menus> it = listado.iterator(); it.hasNext();)
        {
            Menus menu = it.next();
            if(listadoMenusFinal.size() > 0 ) {
                for (Iterator<DetallePedido> it2 = listadoMenusFinal.iterator(); it2.hasNext(); ) {
                    DetallePedido auxMenu = it2.next();
                    if(auxMenu.getNombreMenu().trim().equals(menu.getNombreMenu().trim()))
                    {
                        it2.remove();
                        int cantidad = auxMenu.getCantidad();
                        cantidad ++;
                        double total = auxMenu.getPrecio() * cantidad;
                        DetallePedido detalleAAgregar = new DetallePedido();
                        detalleAAgregar.setIdMenu(menu.getIdMenu());
                        detalleAAgregar.setCantidad(cantidad);
                        detalleAAgregar.setIdEstado(0); //estado = 0 significa que esta tomado el detalle.
                        detalleAAgregar.setIdCategoria(menu.getIdCategoria());
                        detalleAAgregar.setIdInsumo(menu.getIdInsumo());
                        detalleAAgregar.setNombreMenu(menu.getNombreMenu());
                        detalleAAgregar.setPrecio(menu.getPrecio());
                        detalleAAgregar.setTotalDetalle(total);
                        detalleAAgregar.setDescripcion(menu.getDescripcion());
                        listadoMenusFinal.add(detalleAAgregar);
                        agrego = true;
                        break;
                    }

                }
                if(!agrego) {
                    DetallePedido detalleAAgregar = new DetallePedido();
                    detalleAAgregar.setIdMenu(menu.getIdMenu());
                    detalleAAgregar.setCantidad(1);
                    detalleAAgregar.setIdEstado(0); //estado = 0 significa que esta tomado el detalle.
                    detalleAAgregar.setIdCategoria(menu.getIdCategoria());
                    detalleAAgregar.setIdInsumo(menu.getIdInsumo());
                    detalleAAgregar.setNombreMenu(menu.getNombreMenu());
                    detalleAAgregar.setPrecio(menu.getPrecio());
                    detalleAAgregar.setTotalDetalle(menu.getPrecio());
                    detalleAAgregar.setDescripcion(menu.getDescripcion());

                    listadoMenusFinal.add(detalleAAgregar);
                }
                agrego = false;
            }
            else
            {
                DetallePedido detalleAAgregar = new DetallePedido();
                detalleAAgregar.setIdMenu(menu.getIdMenu());
                detalleAAgregar.setCantidad(1);
                detalleAAgregar.setIdEstado(0); //estado = 0 significa que esta tomado el detalle.
                detalleAAgregar.setIdCategoria(menu.getIdCategoria());
                detalleAAgregar.setIdInsumo(menu.getIdInsumo());
                detalleAAgregar.setNombreMenu(menu.getNombreMenu());
                detalleAAgregar.setPrecio(menu.getPrecio());
                detalleAAgregar.setTotalDetalle(menu.getPrecio());
                detalleAAgregar.setDescripcion(menu.getDescripcion());
                listadoMenusFinal.add(detalleAAgregar);
            }
        }

        return  listadoMenusFinal;
    }


    @Override
    public void onCategoriaSelect(int idCategoria) {


        MenusFragment frag2 = MenusFragment.newInstance(idCategoria,URLGlobal,listadoDetalleAConfirmar);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        fragmentTransaction.replace(R.id.FragmentContainer2,frag2);
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();

    }

    @Override
    public void onMenuSelect(Menus menu) {
        listadoMenus.add(menu);
    }

}
