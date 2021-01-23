package com.fesi.funda.Fragment;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fesi.funda.MainUsuario;
import com.fesi.funda.R;
import com.fesi.funda.adapter.AdapterCarreras;
import com.fesi.funda.src.Carreras;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TusCursos extends Fragment {

    private FloatingActionButton btn_Agregar;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser currentuser;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private View view;
    private RecyclerView carreras;
    private DatabaseReference carrerInscritaRef;
    private AdapterCarreras adapterCarreras;
    private TextView titulo, descripcion;
    private Carreras carreraInscrita;
    private BottomSheetDialog mBottomSheetDialog;

    ArrayList<Carreras> carrerasArray = new ArrayList<Carreras>();

    public TusCursos() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        currentuser = FirebaseAuth.getInstance().getCurrentUser();
        authlistener();
        mDatabase = FirebaseDatabase.getInstance().getReference("users").child(firebaseAuth.getUid());
        mStorage = FirebaseStorage.getInstance().getReference();
    }

    private void authlistener() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuthh) {
                currentuser = firebaseAuth.getCurrentUser();
            }
        };

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tuscursos, container, false);
        vistas(view);


        return view;
    }
    private void vistas(View view) {

        GridLayoutManager layoutCarreras = new GridLayoutManager(getActivity(), 2);
        carreras = view.findViewById(R.id.recyclerViewTusCursos);
        carreras.setLayoutManager(layoutCarreras);
        carrerasInscritas();
    }

    private void carrerasInscritas() {

        carrerInscritaRef = mDatabase.child("carreras_inscritas");
        carrerInscritaRef.orderByChild("_idCarrera").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                carrerasArray.clear();
                if (dataSnapshot.exists()) {
                    for (final DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                        final Carreras carreras = new Carreras();
                        carreras.setNombreCarrera(dataSnapshot1.getValue(Carreras.class).getNombreCarrera());
                        carreras.set_idCarrera(dataSnapshot1.getValue(Carreras.class).get_idCarrera());
                        carreras.setCantidadCursos(dataSnapshot1.getValue(Carreras.class).getCantidadCursos());
                        carreras.setImgCarrera(dataSnapshot1.getValue(Carreras.class).getImgCarrera());
                        carreras.setDescripcionCarrera(dataSnapshot1.getValue(Carreras.class).getDescripcionCarrera());
                        String[] nombreArchivo = new String[1];
//                        nombreArchivo = carreras.getImgCarrera().split(".");

                        carrerasArray.add(carreras);

                    }

                }
                adapterCarreras = new AdapterCarreras(getActivity(), carrerasArray);
                adapterCarreras.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Toast.makeText(getContext(),
                                    "Seleccion: " + carrerasArray.get(carreras.getChildAdapterPosition(v)).getNombreCarrera(),
                                    Toast.LENGTH_LONG).show();
                    }
                });
                carreras.setAdapter(adapterCarreras);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
    }
    @Override
    public void onStart() {
        firebaseAuth.addAuthStateListener(mAuthListener);
        currentuser = ((MainUsuario) getActivity()).getFirebaseUser();
        super.onStart();
    }

    @Override
    public void onStop() {
        if (mAuthListener != null) {
            firebaseAuth.removeAuthStateListener(mAuthListener);
        }
        // Toast.makeText(getActivity(), "onStop ", Toast.LENGTH_LONG).show();
        super.onStop();
    }

    @Override
    public void onResume() {
        firebaseAuth.addAuthStateListener(mAuthListener);
        currentuser = ((MainUsuario) getActivity()).getFirebaseUser();
        //Toast.makeText(getActivity(), "onResume " + currentuser.getUid(), Toast.LENGTH_LONG).show();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        if (mAuthListener != null) {
            firebaseAuth.removeAuthStateListener(mAuthListener);
        }
        firebaseAuth.signOut();
        super.onDestroy();
    }

}