package com.example.manu.myapplication;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class CategoriaMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria_menu);

        if (findViewById(R.id.viewer) != null) return;
        if (savedInstanceState == null) {
            CategoriaFragment bf2 = CategoriaFragment.
                    newInstance("String1", "String2");
            MenusFragment bf1 = MenusFragment.
                    newInstance("String1", "String2");
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.list, bf2, "frag2")
                    .detach(bf2)
                    .add(R.id.viewer, bf1, "frag1")
                    .commit();
        }
        TextView tv = (TextView) findViewById(R.id.fragTxt);
        View.OnClickListener Listener =
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MenusFragment bf1 = (MenusFragment)
                                getFragmentManager().findFragmentByTag("frag1");
                        CategoriaFragment bf2 = (CategoriaFragment)
                                getFragmentManager().findFragmentByTag("frag2");
                        FragmentTransaction ft =
                                getFragmentManager().beginTransaction();
                        if (bf2.isDetached()) {
                            ft.detach(bf1).attach(bf2);
                        }
                        if (bf1.isDetached()) {
                            ft.detach(bf2).attach(bf1);
                        }
                        ft.commit();
                    }
                };
        tv.setOnClickListener(Listener);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_categoria_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
