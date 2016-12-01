package com.thenearest.thenearest;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ListarEmpresas extends AppCompatActivity {


    ListView lista;
    List<Empresa> listaempresas;
    EmpresaListAdapter adapter;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference empresasref = database.getReference("empresas");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listar_empresas);

        lista = (ListView) findViewById(R.id.list);
        //bitmap = getBitmapFromURL("https://firebasestorage.googleapis.com/v0/b/prueba-24692.appspot.com/o/fotos%2Fempresas%2F123456789%2F1?alt=media&token=e0ee738e-8a75-4aa1-862f-5131242ab1ef");
        //imageView.setImageBitmap(bitmap);

        Bundle b = getIntent().getExtras();
        String filtro = b.getString("filtro");
        String valor = b.getString("valor");

        HiloConsulta hiloConsulta = new HiloConsulta();
        hiloConsulta.execute(valor);
    }

    public class HiloConsulta extends AsyncTask<String,Integer,List<Empresa>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<Empresa> empresas) {
            adapter = new EmpresaListAdapter(getApplicationContext(),empresas);
            lista.setAdapter(adapter);

            lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(getApplicationContext(),EmpresaDetalle.class);
                    Bundle b = new Bundle();
                    b.putString("nit","" + view.getTag());
                    i.putExtras(b);
                    Toast.makeText(getApplicationContext(), "nit =" + view.getTag(), Toast.LENGTH_SHORT).show();
                    startActivity(i);
                }
            });
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(List<Empresa> aVoid) {
            super.onCancelled(aVoid);
        }

        @Override
        protected List<Empresa> doInBackground(String... strings) {

            String valor = strings[0];
            System.out.println("claveeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee "+valor);
            final List<Empresa> listaempresas= new ArrayList<>();

            empresasref.orderByChild("servicios").equalTo(valor).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {



                    Empresa empresa = new Empresa();
                    empresa.setNombre(dataSnapshot.child("nombre").getValue().toString());
                    empresa.setNit(Integer.parseInt(dataSnapshot.child("nit").getValue().toString()));
                    empresa.setDireccion(dataSnapshot.child("direccion").getValue().toString());
                    empresa.setWeb(dataSnapshot.child("web").getValue().toString());
                    empresa.setTelefono(Integer.parseInt(dataSnapshot.child("telefono").getValue().toString()));
                    empresa.setDomicilios(dataSnapshot.child("domicilios").getValue().toString());
                    empresa.setLatitud(Double.parseDouble(dataSnapshot.child("latitud").getValue().toString()));
                    empresa.setLongitud(Double.parseDouble(dataSnapshot.child("longitud").getValue().toString()));
                    listaempresas.add(empresa);

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }


            });

            return listaempresas;
        }
    }
}
