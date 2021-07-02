package com.fesi.funda;

import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

public class ActividadInicial extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private FirebaseDatabase database;
    private DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_ingresar);

    }

    public void botonatras() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
