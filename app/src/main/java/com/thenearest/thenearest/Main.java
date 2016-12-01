package com.thenearest.thenearest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main extends AppCompatActivity {

    AutoCompleteTextView busqueda;
    ArrayAdapter<String> adapter;
    RadioGroup rbgroup;
    Button btbuscar;
    String filtro;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        busqueda = (AutoCompleteTextView) findViewById(R.id.busqueda);
        rbgroup = (RadioGroup) findViewById(R.id.rbgroup);
        btbuscar = (Button) findViewById(R.id.btnbuscar);

        btbuscar.setEnabled(false);

        Bundle b = getIntent().getExtras();
        String[] servicios = b.getString("servicios").split(",");
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, servicios);
        busqueda.setAdapter(adapter);
        busqueda.setThreshold(0);

        rbgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if (checkedId == R.id.rbnombre){
                    filtro = "nombre";
                    btbuscar.setEnabled(true);
                }else if (checkedId == R.id.rbservicios){
                    filtro = "servicios";
                    btbuscar.setEnabled(true);
                }else{
                    Toast.makeText(getBaseContext(),"no se a seleccionado ningun filtro",Toast.LENGTH_SHORT).show();
                }

            }

        });

    }

    public void buscar(View view){
        Bundle b = new Bundle();
        b.putString("filtro",filtro);
        b.putString("valor", busqueda.getText().toString().trim());
        Intent i = new Intent(getBaseContext(),ListarEmpresas.class);
        i.putExtras(b);
        startActivity(i);
    }

}
