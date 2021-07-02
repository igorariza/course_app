package com.fesi.funda;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fesi.funda.Fragment.FragmentoPaginado;
import com.fesi.funda.Fragment.MiCuenta;
import com.fesi.funda.Fragment.Multimedia;
import com.fesi.funda.src.CircleTransform;
import com.fesi.funda.src.User2;
import com.fesi.funda.src.Usuario;
import com.fesi.funda.vistas.SlidesActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainUsuario extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth firebaseAuth1;
    private FirebaseUser user;
    private DatabaseReference myRef;
    private TextView correo, nombre_usuario;
    private CircleImageView img_user;
    private FirebaseUser userSesion;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseCursos;
    private User2 datosUsuario;
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mNamesCarreras = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private ArrayList<String> mCantidad = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);
        userSesion = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        mDatabaseCursos = FirebaseDatabase.getInstance().getReference("facultades");
        myRef = FirebaseDatabase.getInstance().getReference();
        firebaseAuth1 = FirebaseAuth.getInstance();
        datosUsuario = User2.getInstance();
        infoUsuario();
        authlistener();
        agregarToolbar();

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);

        View hView = navigationView.getHeaderView(0);
        correo = hView.findViewById(R.id.email);
        nombre_usuario = hView.findViewById(R.id.username);
        img_user = hView.findViewById(R.id.circle_image);
        headerInfo();


        if (navigationView != null) {
            prepararDrawer(navigationView);
            // Seleccionar item por defecto
            seleccionarItem(navigationView.getMenu().getItem(0));
        }
    }

    private void headerInfo(){
        mDatabase.child(userSesion.getUid()).child("perfil").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario perfilCuenta = dataSnapshot.getValue(Usuario.class);
                //Obtenemos los valores que queres
                String nombreUsuario = perfilCuenta.getNameUser();
                if(nombreUsuario != null){
                    nombreUsuario = perfilCuenta.getNameUser();

                }else{
                    nombreUsuario = "FESI";
                }
                String emailUsuario = perfilCuenta.getEmailUser();
                correo.setText(emailUsuario);
                nombre_usuario.setText(nombreUsuario);
                String urlPhoto;
                if(perfilCuenta.getPhotoUser().isEmpty()){
                    urlPhoto = "gs://fundafesi-541fd.appspot.com/S.png";
                }else{
                    urlPhoto = perfilCuenta.getPhotoUser().toString();
                }

                Picasso.get()
                        .load(urlPhoto)
                        .placeholder(R.drawable.fesi_logo_bonton_1)
                        .centerCrop()
                        .error(R.drawable.fesi_logo_bonton_1)
                        .fit()
                        .priority(Picasso.Priority.HIGH)
                        .transform(new CircleTransform())
                        .into(img_user);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Toast.makeText(getApplicationContext(), "Algo salió mal " + error.toException(), Toast.LENGTH_SHORT).show();
                //Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }


    private void infoUsuario() {
        final String uidUser = userSesion.getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users/" + uidUser);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    guardarDatosGlobal();
                } else {
                    Toast.makeText(MainUsuario.this, "no existe el usuario",
                            Toast.LENGTH_SHORT).show();
                    addDocumentToCollectionFB(userSesion);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addDocumentToCollectionFB(FirebaseUser userfb) {
        String userId = userfb.getUid();
        String userDi = "";
        String userNacimiento = "";
        String userGenero = "";
        String userDireccion = "";
        String emailFacebook = userfb.getEmail();
        correo.setText(emailFacebook);
        String userNameFacebook = userfb.getDisplayName();
        nombre_usuario.setText(userNameFacebook);
        String userPhotoFacebook = String.valueOf(userfb.getPhotoUrl());
        Picasso.get()
                .load(userPhotoFacebook)
                .placeholder(R.drawable.fesi_logo_bonton_1)
                .centerCrop()
                .error(R.drawable.fesi_logo_bonton_1)
                .fit()
                .priority(Picasso.Priority.HIGH)
                .transform(new CircleTransform())
                .into(img_user);
        String userPhoneFacebook = "";
        final Usuario user = new Usuario(userId, userDi, userNacimiento, userGenero, emailFacebook, userNameFacebook, userPhotoFacebook, userPhoneFacebook, userDireccion);

        DatabaseReference usrID = myRef.child("users").child(userId);
        usrID.child("perfil").setValue(user);
    }

    private void datosCursos(){



    }
    private void guardarDatosGlobal() {
        final String uidUser = userSesion.getUid();
        mDatabase.child(uidUser).child("perfil").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario perfilCuenta = dataSnapshot.getValue(Usuario.class);
                //Obtenemos los valores que queres
                String nombreUsuario = perfilCuenta.getNameUser();
                String documentoUsuario = perfilCuenta.getDiUser();
                String emailUsuario = perfilCuenta.getEmailUser();
                String nacimientoUsuario = perfilCuenta.getNacimientoUser();
                String phoneUsuario = perfilCuenta.getPhoneUser();
                String direccionUsuario = perfilCuenta.getDireccionUser();
                String generoUsuario = perfilCuenta.getGeneroUser();
                correo.setText(emailUsuario);
                nombre_usuario.setText(nombreUsuario);
                String urlPhoto;
                if(perfilCuenta.getPhotoUser().isEmpty()){
                    urlPhoto = "gs://fundafesi-541fd.appspot.com/S.png";
                }else{
                    urlPhoto = perfilCuenta.getPhotoUser().toString();
                }

                Picasso.get()
                        .load(urlPhoto)
                        .placeholder(R.drawable.fesi_logo_bonton_1)
                        .centerCrop()
                        .error(R.drawable.fesi_logo_bonton_1)
                        .fit()
                        .priority(Picasso.Priority.HIGH)
                        .transform(new CircleTransform())
                        .into(img_user);
                datosUsuario.setNameUser(nombreUsuario);
                datosUsuario.setEmailUser(emailUsuario);
                datosUsuario.setDiUser(documentoUsuario);
                datosUsuario.setNacimientoUser(nacimientoUsuario);
                datosUsuario.setPhoneUser(phoneUsuario);
                datosUsuario.setDireccionUser(direccionUsuario);
                datosUsuario.setGeneroUser(generoUsuario);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Toast.makeText(getApplicationContext(), "Algo salió mal " + error.toException(), Toast.LENGTH_SHORT).show();
                //Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void authlistener() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuthh) {
                user = firebaseAuthh.getCurrentUser();
            }
        };

    }
    public FirebaseUser getFirebaseUser() {
        return user;
    }

    private void agregarToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            // Poner ícono del drawer toggle
            ab.setHomeAsUpIndicator(R.drawable.drawer_toggle);
            ab.setDisplayHomeAsUpEnabled(true);
        }

    }

    private void prepararDrawer(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        seleccionarItem(menuItem);
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });

    }

    @SuppressLint("ResourceAsColor")
    private void seleccionarItem(MenuItem itemDrawer) {
        Fragment fragmentoGenerico = null;
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (itemDrawer.getItemId()) {
            case R.id.nav_cursos:
                fragmentoGenerico = new FragmentoPaginado();
                toolbar.setSubtitleTextColor(R.color.colorPrimaryDark);
                toolbar.setSubtitleTextColor(R.color.white);
                toolbar.setSubtitle("Información Cursos");
                break;
            case R.id.nav_perfil:
                fragmentoGenerico = new MiCuenta();
                toolbar.setSubtitle("Información Sobre tu cuenta");
                break;
            case R.id.nav_diploma:
                fragmentoGenerico = new Multimedia();
                toolbar.setSubtitle("Cursos finalizados");
                break;
            case R.id.nav_bolsa:
                fragmentoGenerico = new Multimedia();
                toolbar.setSubtitle("Oportunidades Laborales");
                break;
            case R.id.nav_log_out:
                startActivity(new Intent(this, SlidesActivity.class));
                finish();
                overridePendingTransition(R.anim.zoom_forward_in, R.anim.zoom_forward_out);
                break;

        }
        if (fragmentoGenerico != null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.contenedor_principal, fragmentoGenerico)
                    .commit();
        }

        // Setear título actual
        setTitle(itemDrawer.getTitle());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        firebaseAuth1.addAuthStateListener(mAuthListener);
        super.onStart();

    }

    @Override
    public void onStop() {
        if (mAuthListener != null) {
            firebaseAuth1.removeAuthStateListener(mAuthListener);
        }
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        firebaseAuth1.signOut();
        super.onDestroy();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}

