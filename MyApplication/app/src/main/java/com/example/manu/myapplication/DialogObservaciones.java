package com.example.manu.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Manu on 20/06/2016.
 */

public class DialogObservaciones extends DialogFragment {
    private EditText txtObservaciones;
    private com.example.manu.myapplication.Entidades.DetallePedido item;

    public com.example.manu.myapplication.Entidades.DetallePedido getItem() {
        return item;
    }

    public void setItem(com.example.manu.myapplication.Entidades.DetallePedido item) {
        this.item = item;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View myView = inflater.inflate(R.layout.dialog_observaciones,null);

        builder.setView(myView);
        txtObservaciones = (EditText) myView.findViewById(R.id.txtObservacionesDetalle);
        txtObservaciones.setText(item.getObservacion());

        builder
                // Add action buttons
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {


                        item.setObservacion(txtObservaciones.getText().toString());
                        mListener.onDialogPositiveClick(DialogObservaciones.this);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        mListener.onDialogNegativeClick(DialogObservaciones.this);
                        DialogObservaciones.this.getDialog().cancel();
                    }
                }
                )
                .setTitle("Observaciones");



        return builder.create();
    }

    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }


    NoticeDialogListener mListener;

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }
}
