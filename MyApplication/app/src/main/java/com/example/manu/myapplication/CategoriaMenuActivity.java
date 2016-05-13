package com.example.manu.myapplication;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.WindowManager;

public class CategoriaMenuActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria_menu);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();

        WindowManager wm = getWindowManager();
        Display d = wm.getDefaultDisplay();
        CategoriaFragment fragment1 = new CategoriaFragment();
        MenusFragment fragment2 = new MenusFragment();

        fragmentTransaction.add(R.id.FragmentContainer1, fragment1);
        fragmentTransaction.add(R.id.FragmentContainer2, fragment2);
        fragmentTransaction.commit();
        }



}
