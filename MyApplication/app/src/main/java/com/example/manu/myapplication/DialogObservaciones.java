package com.example.manu.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.manu.myapplication.Entidades.DetallePedido;

/**
 * Created by Manu on 20/06/2016.
 */

public class DialogObservaciones extends DialogFragment {
    private EditText txtObservaciones;
    private DetallePedido item;

    public DetallePedido getItem() {
        return item;
    }

    public void setItem(DetallePedido item) {
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
        Typeface type = Typeface.createFromAsset(getActivity().getAssets(),"segoeui.ttf");
        txtObservaciones.setTypeface(type);
        Button dialogObservacionesButtonAceptar = (Button) myView.findViewById(R.id.dialogObservacionesButtonAceptar);
        dialogObservacionesButtonAceptar.setTypeface(type);
        builder.setTitle("OBSERVACIONES");

        // if button is clicked, close the custom dialog
        dialogObservacionesButtonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setObservacion(txtObservaciones.getText().toString());
                mListener.onDialogPositiveClick(DialogObservaciones.this);
                DialogObservaciones.this.getDialog().cancel();
            }
        });


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
