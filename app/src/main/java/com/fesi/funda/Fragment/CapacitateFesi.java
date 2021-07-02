package com.fesi.funda.Fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fesi.funda.R;
import com.fesi.funda.adapter.AdapterCarreras;
import com.fesi.funda.adapter.AdapterFacultades;
import com.fesi.funda.src.Carreras;
import com.fesi.funda.src.CarrerasInscritas;
import com.fesi.funda.src.Facultades;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

public class CapacitateFesi extends Fragment {

    private View view;

    private RecyclerView nombreFacultad;
    private RecyclerView carreras;
    private BottomSheetDialog mBottomSheetDialog;
    private TextView titulo, descripcion;
    //Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private StorageReference mStorage;
    private DatabaseReference facultadRef;
    private DatabaseReference carreraRef;
    private DatabaseReference carrerInscritaRef;
    private FirebaseUser currentuser;
    private StorageReference storageReference;
    public ProgressDialog progressDialog;

    //Adapters
    private AdapterFacultades adapterFacultades;
    private AdapterCarreras adapterCarreras;

    //ArrayList<>
    ArrayList<Facultades> nombreFacultadArray = new ArrayList<Facultades>();
    ArrayList<Carreras> carrerasArray = new ArrayList<Carreras>();
    ArrayList<CarrerasInscritas> carrerasInscritasArray = new ArrayList<CarrerasInscritas>();

    //Obejetos
    private Carreras carreraInscrita;

    public CapacitateFesi() {
        carrerasArray.clear();
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
        view = inflater.inflate(R.layout.activity_cursos, container, false);
        vistas(view);
        carrerasInscritas();

        return view;
    }


    private void vistas(View view) {
        LinearLayoutManager layoutFacultades = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        nombreFacultad = view.findViewById(R.id.recyclerViewFacultades);
        nombreFacultad.setLayoutManager(layoutFacultades);
        facultades();

        GridLayoutManager layoutCarreras = new GridLayoutManager(getActivity(), 2);
        carreras = view.findViewById(R.id.recyclerViewCarreras);
        carreras.setLayoutManager(layoutCarreras);
        carrerasAll();
    }


    private void carrerasInscritas() {
        carrerInscritaRef = mDatabase.child("carreras_inscritas");
        carrerInscritaRef.orderByChild("_idCarrera").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (final DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                        final CarrerasInscritas carreras = new CarrerasInscritas();
                        carreras.setNombreCarrera(dataSnapshot1.getValue(CarrerasInscritas.class).getNombreCarrera());
                        carreras.set_idCarrera(dataSnapshot1.getValue(CarrerasInscritas.class).get_idCarrera());
                        carreras.setCantidadCursos(dataSnapshot1.getValue(CarrerasInscritas.class).getCantidadCursos());

                        /*String [] nombreArchivo = new String[1];
                        nombreArchivo =  carreras.getImgCarrera().split(".");*/

                        carrerasInscritasArray.add(carreras);

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void carrerasAll() {
        carreraRef = FirebaseDatabase.getInstance().getReference().child("carreras");
        carreraRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (final DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                        final Carreras carreras = new Carreras();
                        carreras.setNombreCarrera(dataSnapshot1.getValue(Carreras.class).getNombreCarrera());
                        carreras.set_idCarrera(dataSnapshot1.getValue(Carreras.class).get_idCarrera());
                        carreras.setCantidadCursos(dataSnapshot1.getValue(Carreras.class).getCursos().size());
                        carreras.setImgCarrera(dataSnapshot1.getValue(Carreras.class).getImgCarrera());
                        carreras.setDescripcionCarrera(dataSnapshot1.getValue(Carreras.class).getDescripcionCarrera());
                        String[] nombreArchivo = new String[1];
                        nombreArchivo = carreras.getImgCarrera().split(".");

                        carrerasArray.add(carreras);

                    }

                }
                adapterCarreras = new AdapterCarreras(getActivity(), carrerasArray);
                adapterCarreras.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final View bottomSheetLayout = getLayoutInflater().inflate(R.layout.dialogo_carrera, null);
                        titulo = bottomSheetLayout.findViewById(R.id.tv_title);
                        descripcion = bottomSheetLayout.findViewById(R.id.tv_detail);
                        carreraInscrita = new Carreras();
                        carreraInscrita.set_idCarrera(carrerasArray.get(carreras.getChildAdapterPosition(v)).get_idCarrera());
                        carreraInscrita.setNombreCarrera(carrerasArray.get(carreras.getChildAdapterPosition(v)).getNombreCarrera());
                        //carreraInscrita.setImgCarrera(carrerasArray.get(carreras.getChildAdapterPosition(v)).getImgCarrera());
//                        carreraInscrita.setCantidadCursos(carrerasArray.get(carreras.getChildAdapterPosition(v)).getCursos().size());
                        //titulo.setText(Html.fromHtml(carrerasArray.get(carreras.getChildAdapterPosition(v)).getNombreCarrera()));

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            titulo.setText(Html.fromHtml(String.valueOf(Html.fromHtml(carrerasArray.get(carreras.getChildAdapterPosition(v)).getNombreCarrera())), Html.FROM_HTML_MODE_COMPACT));
                            descripcion.setText(Html.fromHtml(String.valueOf(Html.fromHtml(carrerasArray.get(carreras.getChildAdapterPosition(v)).getDescripcionCarrera())), Html.FROM_HTML_MODE_COMPACT));
                        } else {
                            titulo.setText(Html.fromHtml(String.valueOf(Html.fromHtml(String.valueOf(Html.fromHtml(carrerasArray.get(carreras.getChildAdapterPosition(v)).getNombreCarrera()))))));
                            descripcion.setText(Html.fromHtml(String.valueOf(Html.fromHtml(String.valueOf(Html.fromHtml(carrerasArray.get(carreras.getChildAdapterPosition(v)).getDescripcionCarrera()))))));
                        }
                        (bottomSheetLayout.findViewById(R.id.button_close)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mBottomSheetDialog.dismiss();
                            }
                        });
                        (bottomSheetLayout.findViewById(R.id.button_ok)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showProgressDialog();
                                confirmarInscripcion(carreraInscrita);


                            }
                        });

                        mBottomSheetDialog = new BottomSheetDialog(getContext());
                        mBottomSheetDialog.setContentView(bottomSheetLayout);
                        mBottomSheetDialog.show();
                        //Toast.makeText(getContext(),
                        //      "Seleccion: " + carrerasArray.get(carreras.getChildAdapterPosition(v)).getDescripcionCarrera(),
                        //    Toast.LENGTH_LONG).show();

                    }
                });
                carreras.setAdapter(adapterCarreras);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void confirmarInscripcion(final Carreras carreraInscrita){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());//R.style.Theme_Dialog_Translucent
        LayoutInflater inflater = this.getLayoutInflater();

        //View dialogView= inflater.inflate(R.layout.alert_label_editor, null);

        // set title

        alertDialogBuilder.setTitle("This is title");
        alertDialogBuilder.setIcon(R.drawable.ic_account);

        // set dialog message
        alertDialogBuilder
                .setMessage("Mensaje")
                .setCancelable(false)
                .setPositiveButton(R.string.yes,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        inscribirCurso(carreraInscrita);

                    }
                })
                .setNegativeButton(R.string.no,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        hideProgressDialog();
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();



        // show it
        alertDialog.show();

    }
    public AlertDialog createCustomDialog() {
        final AlertDialog alertDialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();
        // Inflar y establecer el layout para el dialogo
        // Pasar nulo como vista principal porque va en el diseño del diálogo
        View v = inflater.inflate(R.layout.dialog_inscribirse, null);
        //builder.setView(inflater.inflate(R.layout.dialog_signin, null))
        Button btnFire = (Button) v.findViewById(R.id.btn_fire);
        Button btnCancel = (Button) v.findViewById(R.id.btn_cancel);
        builder.setView(v);
        alertDialog = builder.create();
        // Add action buttons
        btnFire.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Aceptar
                        alertDialog.dismiss();
                    }
                }
        );
        btnCancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                }
        );
        return alertDialog;
    }

    private void inscribirCurso(final Carreras carreraInscrita) {
       final DatabaseReference usrID = mDatabase.child("carreras_inscritas");
        String idCarrera = carreraInscrita.get_idCarrera();
        usrID.orderByChild("_idCarrera").equalTo(idCarrera).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    mBottomSheetDialog.dismiss();
                    hideProgressDialog();
                    Toast.makeText(getApplicationContext(), "Te has inscrito al curso Exitos!! te enviaremos los pasos a seguir #DecisionesConAcciones!!", Toast.LENGTH_SHORT).show();

                }else{
                    DatabaseReference usrIDs = usrID.push();
                    usrIDs.setValue(carreraInscrita);
                    hideProgressDialog();
                    mBottomSheetDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Te has inscrito al curso Exitos!! te enviaremos los pasos a seguir #DecisionesConAcciones!!", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                hideProgressDialog();

            }

        });

                hideProgressDialog();
    }

    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
        }
        progressDialog.show();
        progressDialog.setContentView(R.layout.custom_progressdialog);
    }

    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void facultades() {

        facultadRef = FirebaseDatabase.getInstance().getReference().child("facultades");
        facultadRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                        Facultades facultad = new Facultades();
                        facultad.setNombreFacultad(dataSnapshot1.getValue(Facultades.class).getNombreFacultad());
                        facultad.set_idFacultad(dataSnapshot1.getValue(Facultades.class).get_idFacultad());
                        nombreFacultadArray.add(facultad);

                    }
                }
                adapterFacultades = new AdapterFacultades(getActivity(), nombreFacultadArray);
                nombreFacultad.setAdapter(adapterFacultades);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onStart() {
        firebaseAuth.addAuthStateListener(mAuthListener);
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
        super.onResume();
    }


}
