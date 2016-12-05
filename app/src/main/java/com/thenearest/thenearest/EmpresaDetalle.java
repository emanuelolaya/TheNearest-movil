package com.thenearest.thenearest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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


        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("empresas/"+nit);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                System.out.println("snapshop"+dataSnapshot.getValue().toString());

                final Empresa empresa = new Empresa();

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

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),nit);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);


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
        private StorageReference mStorageRef;
        Bitmap bitmap;

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

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_slide, container, false);
            imageslide = (ImageView) rootView.findViewById(R.id.imageslide);

            if(bitmap==null){

                mStorageRef = FirebaseStorage.getInstance().getReference();
                StorageReference img1 = mStorageRef.child("fotos/empresas/"+getArguments().getInt(nit)+"/1");

                final long ONE_MEGABYTE = 1024 * 1024;
                img1.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        imageslide.setImageBitmap(bitmap);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });

            }else{
                imageslide.setImageBitmap(bitmap);
            }


            return rootView;
        }

    }

    public static class ImagenFragment2 extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String nit = "nit";
        ImageView imageslide;
        Bitmap bitmap;
        private StorageReference mStorageRef;

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

            if(bitmap==null){

                mStorageRef = FirebaseStorage.getInstance().getReference();
                StorageReference img1 = mStorageRef.child("fotos/empresas/"+getArguments().getInt(nit)+"/2");

                final long ONE_MEGABYTE = 1024 * 1024;
                img1.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        imageslide.setImageBitmap(bitmap);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });

            }else{
                imageslide.setImageBitmap(bitmap);
            }

            return rootView;
        }
    }

    public static class ImagenFragment3 extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String nit = "nit";
        ImageView imageslide;
        private StorageReference mStorageRef;
        Bitmap bitmap;


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
            //getArguments().getInt(nit)
            //imageslide.setImageResource(R.drawable.background);

            if(bitmap==null){

                mStorageRef = FirebaseStorage.getInstance().getReference();
                StorageReference img1 = mStorageRef.child("fotos/empresas/"+getArguments().getInt(nit)+"/3");

                final long ONE_MEGABYTE = 1024 * 1024;
                img1.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        imageslide.setImageBitmap(bitmap);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });

            }else{
                imageslide.setImageBitmap(bitmap);
            }

            return rootView;
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
}
