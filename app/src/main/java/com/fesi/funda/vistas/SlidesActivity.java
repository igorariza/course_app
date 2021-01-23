package com.fesi.funda.vistas;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.InflateException;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.fesi.funda.ActividadIngreso;
import com.fesi.funda.R;
import com.fesi.funda.adapter.SliderAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

public class SlidesActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
    private String[] PERMISOS = {
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.INTERNET,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private List<String> lottieView;
    private List<String> txtSlider;
    private AppCompatButton registro;
    private AppCompatButton inicio;
    private FirebaseAuth firebaseAuth;
    ViewPager viewPager;
    TabLayout indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        permisos();
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setTheme(R.style.HomeTheme);
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        try{
            setContentView(R.layout.activity_sliders);
        }catch (InflateException e){
            Log.e("Igor", e.getMessage());

        }

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();
        LoginManager.getInstance().logOut();
        vistas();

        viewPager.setAdapter(new SliderAdapter(this, lottieView, txtSlider));
        indicator.setupWithViewPager(viewPager, true);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);
    }

    private void permisos() {
        int leer0 = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        int leer1 = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS);
        int leer2 = ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        int leer3 = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int leer4 = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int leer5 = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int leer6 = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int leer7 = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        if (leer0 == PackageManager.PERMISSION_DENIED || leer1 == PackageManager.PERMISSION_DENIED || leer2 == PackageManager.PERMISSION_DENIED || leer3 == PackageManager.PERMISSION_DENIED ||
                leer4 == PackageManager.PERMISSION_DENIED || leer5 == PackageManager.PERMISSION_DENIED || leer6 == PackageManager.PERMISSION_DENIED || leer7 == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(SlidesActivity.this, PERMISOS, REQUEST_CODE);
        }
    }

    private void setButtonListeners() {
        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SlidesActivity.this, ActividadIngreso.class);
                intent.putExtra("accionUser", "¡Únete a FESI!");
                intent.putExtra("txtaccionUser", "Regístrate con tu email");
                intent.putExtra("btnaccionUser", "Regístrate");
                startActivity(intent);
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
            }
        });

        inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SlidesActivity.this, ActividadIngreso.class);
                intent.putExtra("accionUser", "Inicia Sesión");
                intent.putExtra("txtaccionUser", "Ingresa con tu email");
                intent.putExtra("btnaccionUser", "Ingresa");
                startActivity(intent);
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
            }
        });
    }


    public void vistas() {
        lottieView = new ArrayList<>();
        lottieView.add("heart.json");
        lottieView.add("moonlighter.json");
        lottieView.add("designvare.json");

        txtSlider = new ArrayList<>();
        txtSlider.add("Clases Hechas con Amor!!");
        txtSlider.add("Aprende en cualquier Momento!!");
        txtSlider.add("Ahorra dinero y tiempo para Aprender!");

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(2);
        indicator = (TabLayout) findViewById(R.id.indicator);
        registro = (AppCompatButton) findViewById(R.id.btn_registro);
        inicio = (AppCompatButton) findViewById(R.id.btn_inicio);

        setButtonListeners();
    }

    private class SliderTimer extends TimerTask {

        @Override
        public void run() {
            SlidesActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (viewPager.getCurrentItem() < lottieView.size() - 1) {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    } else {
                        viewPager.setCurrentItem(0);
                    }
                }
            });
        }
    }
}