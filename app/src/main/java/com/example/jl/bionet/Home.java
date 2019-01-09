package com.example.jl.bionet;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawer;

    Dialog cerrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        cerrar = new Dialog(this);

        Toolbar toolbar = findViewById(R.id.toolbar);

        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.nav_clientes:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new Fragment_clientes()).commit();
                    break;

                case R.id.nav_inventario:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new Fragment_inventarios()).commit();
                    break;

                case R.id.nav_cerrar_sesion:
                        cerrar.setContentView(R.layout.pop_up_cerrarsesion);
                        cerrar.show();
                    break;
            }

            drawer.closeDrawer(GravityCompat.START);
            return true;

    }

    public void replaceFragments(Class fragmentClass) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
                .commit();
    }

    public void ShowDireccionFiscal(View view) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.pop_up_direccion_fiscal);
        dialog.setTitle("Dirección fiscal");
        dialog.show();
    }

    public void Aceptar(View view){
        Intent intent = new Intent(Home.this, MainActivity.class);
        startActivity(intent);
    }

    public void Cancelar(View view){
        cerrar.dismiss();
    }




}