package com.thenearest.thenearest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;


public class EmpresaListAdapter extends BaseAdapter {

    Context context;
    List<Empresa> listaempresas;
    TextView nombre, direccion, web, domicilios;
    Button detalle, mapa;

    public EmpresaListAdapter(Context context, List<Empresa> listaempresas) {
        this.context = context;
        this.listaempresas = listaempresas;
    }

    @Override
    public int getCount() {
        return listaempresas.size();
    }

    @Override
    public Object getItem(int i) {
        return listaempresas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.item_empresa_list, null);
        nombre = (TextView)v.findViewById(R.id.nombre);
        direccion = (TextView)v.findViewById(R.id.direccion);
        web = (TextView)v.findViewById(R.id.web);
        domicilios = (TextView) v.findViewById(R.id.domicilios);
        detalle = (Button) v.findViewById(R.id.btdetalles);
        mapa = (Button) v.findViewById(R.id.btmapa);
        nombre.setText(listaempresas.get(i).getNombre());
        direccion.setText(listaempresas.get(i).getDireccion());
        web.setText(listaempresas.get(i).getWeb());
        domicilios.setText(domicilios.getText().toString()+listaempresas.get(i).getDomicilios());
        detalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                b.putInt("nit", listaempresas.get(i).getNit());
                Intent i = new Intent(context,EmpresaDetalle.class);
                i.putExtras(b);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });
        mapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                b.putString("nombre", listaempresas.get(i).getNombre());
                b.putDouble("latitud", listaempresas.get(i).getLatitud());
                b.putDouble("longitud", listaempresas.get(i).getLongitud());
                Intent i = new Intent(context,EmpresaMapa.class);
                i.putExtras(b);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(i);
            }
        });
        v.setTag(listaempresas.get(i).getNit());
        return v;
    }
}
