package com.example.manu.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

/**
 * Created by Manu on 20/06/2016.
 */

public class DialogSalir extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View myView = inflater.inflate(R.layout.dialog_salir,null);

        builder.setView(myView);

        Button dialogSalirButtonAceptar = (Button) myView.findViewById(R.id.dialogSalirButtonAceptar);
        Button dialogSalirButtonCancelar = (Button) myView.findViewById(R.id.dialogSalirButtonCancelar);
        builder.setTitle("ATENCIÓN");

        // if button is clicked, close the custom dialog
        dialogSalirButtonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDialogPositiveClick(DialogSalir.this);
                DialogSalir.this.getDialog().cancel();
                getActivity().finish();

            }
        });

        dialogSalirButtonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDialogNegativeClick(DialogSalir.this);
                DialogSalir.this.getDialog().cancel();
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
