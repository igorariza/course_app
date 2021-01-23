package com.fesi.funda;

import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.fesi.funda.Fragment.QuienesSomos;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class ActivityHome extends AppCompatActivity {

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
    private ImageView btnfacebook, btninstagram, btnyoutube,
            btnwhatsapp, btnplaystore, btnbuzon;
    private TextView txtEducacionCultura, txtSalud;
    private Button btnRegistrate, btnCertificate, btnIngresar, btnQueEs;
    private LinearLayout linearEducacionCultura, linearCapacitate, linearSalud, linearAnimalista, linearTestimonio;
    private Context mContext = this;
    private FirebaseDatabase database;
    private DatabaseReference myRefVideoFesi;
    private String videoInstitucional;
    private TextView mensajeHomeHead; //Mensaje de pagina ppal
    private DatabaseReference mDatabaseMensaje;//Mensaje de perfil de cuenta
    private FirebaseAuth firebaseAuth;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int leer0 = ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS);
        int leer1 = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS);
        int leer2 = ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        int leer3 = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int leer4 = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int leer5 = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int leer6 = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int leer7 = ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_SMS);

        if(leer0== PackageManager.PERMISSION_DENIED || leer1 == PackageManager.PERMISSION_DENIED || leer2==PackageManager.PERMISSION_DENIED || leer3 ==PackageManager.PERMISSION_DENIED ||
                leer4== PackageManager.PERMISSION_DENIED || leer5 == PackageManager.PERMISSION_DENIED || leer6==PackageManager.PERMISSION_DENIED || leer7 ==PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, PERMISOS, REQUEST_CODE);

        }
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setTheme(R.style.HomeTheme);
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_home);
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabaseMensaje = FirebaseDatabase.getInstance().getReference("mensajePpal");
        firebaseAuth.signOut();
        LoginManager.getInstance().logOut();
        agregarVistas();
        agregarEventos();
        cargarMensaje();
    }

    public void cargarMensaje() {
        mDatabaseMensaje.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String mensajeDeCuenta = dataSnapshot.getValue().toString();
//                mensajeHomeHead.setText(mensajeDeCuenta);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Toast.makeText(getApplicationContext(), "Algo salió mal " + error.toException(), Toast.LENGTH_SHORT).show();
                //Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }


    private void accessContacts() {

        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        int rawContactInsertIndex = ops.size();

        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, "Profe Igor Ariza") // Name of the person
                .build());
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(
                        ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, "+573104617019") // Number of the person
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build()); // Type of mobile number

        //< Email >
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)// rawContact_NewID)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Email.DATA, "igorariza@gmail.com")
                .build());
//</ Email >

        //----< check result >----
//< get result >
        ContentProviderResult[] res = new ContentProviderResult[0];
        try {
            res = getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (RemoteException e) {
//e.printStackTrace();
            //Log.e("getContentResolver()",e.getMessage());
        } catch (OperationApplicationException e) {
//e.printStackTrace();
//Log.getStackTraceString(e)
            //Log.e("getContentResolver()",e.toString());
        } catch (Exception e) {
//e.printStackTrace();
//Log.getStackTraceString(e)
            //Log.e("getContentResolver()",e.toString());
        } finally {
//ok.. Log.d("getContentResolver.","..finally");
        }
//</ get result >

//--< check result >--
        if (res != null && res[0] != null) {
//< ok >
            //Uri newContactUri = res[0].uri;
            //Log.d("AddContact", "URI added contact:" + newContactUri);
//</ ok >
        } else {
//< error >
// Log.e("AddContact", "Contact not added.");
//</ error >
        }
//--</ check result >--
//----</ check result >----

    }

    public void agregarVistas() {
        mensajeHomeHead = (TextView) findViewById(R.id.mensajeTitulo);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        btnRegistrate = (Button) findViewById(R.id.btn_registrate);
        btnCertificate = (Button) findViewById(R.id.btn_certificate);
        btnIngresar = (Button) findViewById(R.id.btn_Ingresar);
        btnQueEs = (Button) findViewById(R.id.btn_quees);

        btnfacebook = (ImageView) findViewById(R.id.btn_facebook);
        btninstagram = (ImageView) findViewById(R.id.btn_instagram);
        btnyoutube = (ImageView) findViewById(R.id.btn_youtube);
        btnwhatsapp = (ImageView) findViewById(R.id.btn_whatsapp);
        btnplaystore = (ImageView) findViewById(R.id.btn_playstore);
        btnbuzon = (ImageView) findViewById(R.id.btn_buzon);

        database = FirebaseDatabase.getInstance();
        myRefVideoFesi = database.getReference().child("videosinstitucionales");

        myRefVideoFesi.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String valor = dataSnapshot.child("fesi").getValue().toString();
                videoInstitucional = valor;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void agregarEventos() {
        //bottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_recents:
                        Toast.makeText(ActivityHome.this, "Recents", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_favorites:
                        Toast.makeText(ActivityHome.this, "Favorites", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_nearby:
                        Toast.makeText(ActivityHome.this, "Nearby", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
        //Registrarse
        btnRegistrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // accessContacts();
                Intent intent = new Intent(ActivityHome.this, ActivityRegistro.class);
                intent.putExtra("accionUser", "¡Únete a FESI!");
                intent.putExtra("txtaccionUser", "Regístrate con tu email");
                intent.putExtra("btnaccionUser", "Regístrate");
                startActivity(intent);
                overridePendingTransition(R.anim.left_in, R.anim.left_out);

            }
        });
        //Ingresar
        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //accessContacts();
                Intent intent = new Intent(ActivityHome.this, ActivityRegistro.class);
                intent.putExtra("accionUser", "Inicia Sesión");
                intent.putExtra("txtaccionUser", "Ingresa con tu email");
                intent.putExtra("btnaccionUser", "Ingresa");
                startActivity(intent);
                overridePendingTransition(R.anim.left_in, R.anim.left_out);

            }
        });
        //Certificate
        btnCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accessContacts();
                Toast.makeText(ActivityHome.this, "Certificate en Construcción", Toast.LENGTH_SHORT).show();
            }
        });
        //Que es FESI
        btnQueEs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accessContacts();
                Intent intent = new Intent(ActivityHome.this, QuienesSomos.class);
                intent.putExtra("urlVideo", videoInstitucional);
                startActivity(intent);
                overridePendingTransition(R.anim.left_in, R.anim.left_out);

            }
        });
    }

    private boolean isIntentAvailable(Context ctx, Intent intent) {
        final PackageManager packageManager = ctx.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    @Override
    public void onDestroy() {
        finish();
        super.onDestroy();
    }

}