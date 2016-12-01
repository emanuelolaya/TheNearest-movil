package com.thenearest.thenearest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.LruCache;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class EmpresaDetalle extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    TextView nombre, direccion, telefono, web, domicilios, email, horarios;
    int nit;
    Button mapa;
    private LruCache<String, Bitmap> mMemoryCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empresa_detalle);

        nombre = (TextView)findViewById(R.id.txtnombre);
        direccion = (TextView)findViewById(R.id.txtdireccion);
        telefono = (TextView)findViewById(R.id.txttelefono);
        web = (TextView)findViewById(R.id.txtweb);
        domicilios = (TextView)findViewById(R.id.txtdomicilios);
        email = (TextView)findViewById(R.id.txtemail);
        horarios = (TextView)findViewById(R.id.txthorarios);
        mapa = (Button) findViewById(R.id.mapa);
        Bundle b = new Bundle();
        b = getIntent().getExtras();
        nit= b.getInt("nit");

        System.out.println("999999999"+nit);

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };




        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),nit);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        HiloDetalle hiloDetalle = new HiloDetalle();
        hiloDetalle.execute(b.getInt("nit"));

    }

    public static Bitmap getBitmapFromURL(String src){

        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;
        } catch (java.io.IOException e) {
            e.printStackTrace();
            System.out.println("Errorrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr");
            return null;
        }


    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {

        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_empresa_detalle, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class ImagenFragment1 extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String nit = "nit";
        ImageView imageslide;
        private LruCache<String, Bitmap> mMemoryCache;

        public ImagenFragment1() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static ImagenFragment1 newInstance(int sectionNumber) {
            ImagenFragment1 fragment = new ImagenFragment1();
            Bundle args = new Bundle();
            args.putInt(nit, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public void addBitmapToMemoryCache(String key, Bitmap bitmap) {

            if (getBitmapFromMemCache(key) == null) {
                mMemoryCache.put(key, bitmap);
            }
        }

        public Bitmap getBitmapFromMemCache(String key) {
            return mMemoryCache.get(key);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_slide, container, false);
            imageslide = (ImageView) rootView.findViewById(R.id.imageslide);
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
            final int cacheSize = maxMemory / 8;

            mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    // The cache size will be measured in kilobytes rather than
                    // number of items.
                    return bitmap.getByteCount() / 1024;
                }
            };
            Hiloimagenes hiloimagenes = new Hiloimagenes();
            hiloimagenes.execute(getArguments().getInt(nit));
            //imageslide.setImageResource(R.drawable.background);
            return rootView;
        }
        public  class Hiloimagenes extends AsyncTask<Integer,Integer,Bitmap>{
            @Override
            protected void onPostExecute(Bitmap bitmap) {

                    imageslide.setImageBitmap(getBitmapFromMemCache("imagen1"));

            }
            @Override
            protected Bitmap doInBackground(Integer... nit) {
                final String[] url = new String[1];
                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("empresas/"+nit[0]+"/imagenes/0");
                System.out.println("referencia"+myRef.toString());
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        System.out.println("snapshop"+dataSnapshot.getValue().toString());
                        url[0] = dataSnapshot.getValue().toString();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.i("999999999", "Failed to read valueeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee.", error.toException());
                    }
                });
                final Bitmap[] bitmap = new Bitmap[1];
                System.out.println("urrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr "+url[0]);
                if(getBitmapFromMemCache("imagen1")==null) {
                    try {
                        URL urll = new URL(url[0]);
                        System.out.println("urlllllllllllllllllllllllllllllllllllllllllllll" + urll.toString());
                        HttpURLConnection connection = (HttpURLConnection) urll.openConnection();
                        connection.setDoInput(true);
                        connection.connect();
                        InputStream input = connection.getInputStream();
                        bitmap[0] = BitmapFactory.decodeStream(input);
                        addBitmapToMemoryCache("imagen1", bitmap[0]);

                        return bitmap[0];

                    } catch (java.io.IOException e) {
                        e.printStackTrace();
                        System.out.println("Errorrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr" + e);
                    }
                }
                return null;
            }
        }
    }

    public static class ImagenFragment2 extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String nit = "nit";
        ImageView imageslide;

        public ImagenFragment2() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static ImagenFragment2 newInstance(int sectionNumber) {
            ImagenFragment2 fragment = new ImagenFragment2();
            Bundle args = new Bundle();
            args.putInt(nit, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_slide, container, false);
            imageslide = (ImageView) rootView.findViewById(R.id.imageslide);
            Hiloimagenes hiloimagenes = new Hiloimagenes();
            hiloimagenes.execute(getArguments().getInt(nit));
            //imageslide.setImageResource(R.drawable.background);
            return rootView;
        }
        public  class Hiloimagenes extends AsyncTask<Integer,Integer,Bitmap>{
            @Override
            protected void onPostExecute(Bitmap bitmap) {
                imageslide.setImageBitmap(bitmap);
            }
            @Override
            protected Bitmap doInBackground(Integer... nit) {
                final String[] url = new String[1];
                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("empresas/"+nit[0]+"/imagenes/1");
                System.out.println("referencia"+myRef.toString());
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        System.out.println("snapshop"+dataSnapshot.getValue().toString());
                        url[0] = dataSnapshot.getValue().toString();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.i("999999999", "Failed to read valueeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee.", error.toException());
                    }
                });
                final Bitmap[] bitmap = new Bitmap[1];
                System.out.println("urrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr "+url[0]);
                try {
                    URL urll = new URL(url[0]);
                    System.out.println("urlllllllllllllllllllllllllllllllllllllllllllll"+urll.toString());
                    HttpURLConnection connection = (HttpURLConnection) urll.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    bitmap[0] = BitmapFactory.decodeStream(input);
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                    System.out.println("Errorrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr" + e);
                }
                return bitmap[0];
            }
        }
    }

    public static class ImagenFragment3 extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String nit = "nit";
        ImageView imageslide;


        public ImagenFragment3() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static ImagenFragment3 newInstance(int sectionNumber) {
            ImagenFragment3 fragment = new ImagenFragment3();
            Bundle args = new Bundle();
            args.putInt(nit, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_slide, container, false);
            imageslide = (ImageView) rootView.findViewById(R.id.imageslide);


            // Use 1/8th of the available memory for this memory cache.

            Hiloimagenes hiloimagenes = new Hiloimagenes();
            hiloimagenes.execute(getArguments().getInt(nit));
            //imageslide.setImageResource(R.drawable.background);
            return rootView;
        }
        public  class Hiloimagenes extends AsyncTask<Integer,Integer,Bitmap>{
            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if(imageslide.getImageMatrix()==null){
                    System.out.println("vaciooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo si");
                }else{
                    System.out.println("vaciooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo no");
                }
                imageslide.setImageBitmap(bitmap);
            }
            @Override
            protected Bitmap doInBackground(Integer... nit) {
                final String[] url = new String[1];
                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("empresas/"+nit[0]+"/imagenes/2");
                System.out.println("referencia"+myRef.toString());
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        System.out.println("snapshop"+dataSnapshot.getValue().toString());
                        url[0] = dataSnapshot.getValue().toString();
                        System.out.println("urrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr "+url[0]);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.i("999999999", "Failed to read valueeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee.", error.toException());
                    }
                });
                final Bitmap[] bitmap = new Bitmap[1];
                System.out.println("urrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr "+url[0]);

                try {
                    URL urll = new URL(url[0]);
                    System.out.println("urlllllllllllllllllllllllllllllllllllllllllllll"+urll.toString());
                    HttpURLConnection connection = (HttpURLConnection) urll.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    bitmap[0] = BitmapFactory.decodeStream(input);
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                    System.out.println("Errorrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr" + e);
                }

                return bitmap[0];
            }
        }
    }



    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        int nit;

        public SectionsPagerAdapter(FragmentManager fm, int nit) {
            super(fm);
            this.nit = nit;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if(position == 0){
                return ImagenFragment1.newInstance(nit);
            }
            else if(position == 1){
                return ImagenFragment2.newInstance(nit);
            }
            else if(position == 2){
                return ImagenFragment3.newInstance(nit);
            }
            else {
                return null;
            }

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "imagen 1";
                case 1:
                    return "imagen 2";
                case 2:
                    return "imagen 3";
            }
            return null;
        }
    }

    public class HiloDetalle extends AsyncTask<Integer,Void,Bitmap>{
        @Override
        protected void onPostExecute(Bitmap bitmap) {

        }

        @Override
        protected void onCancelled(Bitmap empresa) {
            super.onCancelled(empresa);
        }

        @Override
        protected Bitmap doInBackground(Integer... nit) {
            final Empresa empresa =new Empresa();
            final Bitmap[] bitmap = new Bitmap[1];

            System.out.println("999999999"+nit[0]);

            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("empresas/"+nit[0]);

            System.out.println("999999999 "+myRef.toString());



            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(final DataSnapshot dataSnapshot) {
                    System.out.println("snapshop"+dataSnapshot.getValue().toString());

                    empresa.setNombre(dataSnapshot.child("nombre").getValue().toString());
                    empresa.setDireccion(dataSnapshot.child("direccion").getValue().toString());
                    empresa.setTelefono(Integer.parseInt(dataSnapshot.child("telefono").getValue().toString()));
                    empresa.setWeb(dataSnapshot.child("web").getValue().toString());
                    empresa.setDomicilios(dataSnapshot.child("domicilios").getValue().toString());
                    empresa.setEmail(dataSnapshot.child("email").getValue().toString());
                    empresa.setHorario1(dataSnapshot.child("horario1/desde").getValue().toString()+" - "+dataSnapshot.child("horario1/hasta").getValue().toString());
                    empresa.setHorario2(dataSnapshot.child("horario2/desde").getValue().toString()+" - "+dataSnapshot.child("horario2/hasta").getValue().toString());
                    System.out.println("999999999" + empresa.getHorario2());

                    nombre.setText("Nombre: "+empresa.getNombre());
                    direccion.setText("Direccion: "+empresa.getDireccion());
                    telefono.setText("Telefono: "+empresa.getTelefono());
                    web.setText("Pagina Web: "+empresa.getWeb());
                    domicilios.setText("Domicilios: "+empresa.getDomicilios());
                    email.setText("Email: "+empresa.getEmail());
                    if(empresa.getHorario2().length()>5){
                        horarios.setText("Horarios: "+empresa.getHorario1()+" / "+empresa.getHorario2());
                    }else{
                        horarios.setText("Horario: "+empresa.getHorario1());
                    }
                    mapa.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Bundle b = new Bundle();
                            b.putString("nombre", empresa.getNombre());
                            b.putDouble("latitud", Double.parseDouble(dataSnapshot.child("latitud").getValue().toString()));
                            b.putDouble("longitud", Double.parseDouble(dataSnapshot.child("longitud").getValue().toString()));
                            Intent i = new Intent(getBaseContext(),EmpresaMapa.class);
                            i.putExtras(b);
                            startActivity(i);
                        }
                    });

                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.i("999999999", "Failed to read valueeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee.", error.toException());
                }
            });



            return bitmap[0];
        }
    }


}