package com.thenearest.thenearest;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class SplashScreenActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("servicios");
    CountDownTimer countDownTimer;
    Tiempo tiempo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set portrait orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Hide title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash_screen);

        tiempo = new Tiempo();
        tiempo.Contar();

        if(checkConection(getBaseContext())){
            HilosServicio hilosServicio = new HilosServicio();
            hilosServicio.execute();
        }else{
            Toast.makeText(getBaseContext(),"NO HOY CONEXION A INTERNET", Toast.LENGTH_SHORT).show();
            esperar(3000,true,null);

        }

    }

    public void esperar(int time, final boolean close, final String extras){
        countDownTimer =  new CountDownTimer(time, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                if(close){
                    finish();
                }else{
                    Bundle b = new Bundle();
                    b.putString("servicios",extras);
                    Intent i = new Intent(getBaseContext(),Main.class);
                    i.putExtras(b);
                    startActivity(i);
                    finish();
                }

            }
        }.start();
    }

    public static boolean checkConection(Context context) {

        boolean connected = false;

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Recupera todas las redes (tanto móviles como wifi)
        NetworkInfo[] redes = connectivityManager.getAllNetworkInfo();

        for (int i = 0; i < redes.length; i++) {
            // Si alguna red tiene conexión, se devuelve true
            if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
                connected = true;
            }
        }
        return connected;
    }

    public class HilosServicio extends AsyncTask<Void,Integer,List<String>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            super.onPostExecute(strings);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(List<String> strings) {
            super.onCancelled(strings);
        }

        @Override
        protected List<String> doInBackground(Void... voids) {

            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {
                    };
                    List<String> data = dataSnapshot.getValue(t);

                    if (data == null) {
                        Toast.makeText(getBaseContext(),"A OCURRIDO UN ERROR", Toast.LENGTH_SHORT).show();
                        esperar(3000,true,null);

                    } else {
                        String servicios = TextUtils.join(", ", data);
                        int transcurrido = tiempo.getSegundos();
                        esperar(4000-transcurrido,false,servicios);



                    }

                }


                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("error", "Failed to read value.", error.toException());
                }
            });

            // Read from the database
            return null;
        }
    }
}
