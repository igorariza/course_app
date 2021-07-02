package com.fesi.funda.Fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.fesi.funda.R;
import com.fesi.funda.src.CircleTransform;
import com.fesi.funda.src.User;
import com.fesi.funda.src.User2;
import com.fesi.funda.src.Usuario;
import com.fesi.funda.vistas.SlidesActivity;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import de.hdodenhof.circleimageview.CircleImageView;
import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

public class MiCuenta extends Fragment {
    private GoogleApiClient googleApiClient;
    private CircleImageView img_User;
    private TextView title_name_User;
    private TextView title_mensaje_User;
    private EditText nombre_User;
    private EditText di_User;
    private EditText email_User;
    private EditText nacimiento_User;
    private EditText telefono_User;
    private EditText direccion_User;
    private RadioButton radio_M;
    private RadioButton radio_Femenino;
    private AppCompatButton btn_CerrarSesion;
    private AppCompatButton btn_GuardarCambios;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private View view;
    private FirebaseUser currentuser;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseMensaje;//Mensaje de perfil de cuenta
    private String userGenero;
    private final int SELECT_PICTURE = 300;
    private final int PHOTO_CODE = 200;
    private String mPath;
    private Bitmap imageBitmap;
    private StorageReference mStorage;
    private String urlImagen;
    public ProgressDialog progressDialog;
    private static final String CERO = "0";
    private static final String BARRA = "/";
    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();
    //Variables para obtener la fecha
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);
    private String userId;
    private User2 datosUsuario;


    public MiCuenta() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        firebaseAuth = FirebaseAuth.getInstance();
        currentuser = FirebaseAuth.getInstance().getCurrentUser();
        authlistener();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        mDatabaseMensaje = FirebaseDatabase.getInstance().getReference("mensajeperfil");
        mStorage = FirebaseStorage.getInstance().getReference();
        //Toast.makeText(getActivity(), "onCreate3 " + currentuser.getUid().toString(), Toast.LENGTH_LONG).show();


        //Toast.makeText(getContext(), "getUid: " + currentuser.getUid(), Toast.LENGTH_LONG).show();
        cargarDatos();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.content_cuenta, container, false);
        //Toast.makeText(getActivity(), "Algo salió mal " + uid, Toast.LENGTH_SHORT).show();
        vistas(view);
        cargarMensaje();
        btn_GuardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardardatos();
            }
        });
        btn_CerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cerrarSesion();
            }
        });
        img_User.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cambiarImagen(view);
            }
        });
        nacimiento_User.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerFecha();
            }
        });
        return view;
    }
    private void authlistener() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuthh) {
                currentuser = firebaseAuth.getCurrentUser();
            }
        };

    }


    public void obtenerFecha() {
        DatePickerDialog recogerFecha = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                final int mesActual = month + 1;
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String diaFormateado = (dayOfMonth < 10) ? CERO + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10) ? CERO + String.valueOf(mesActual) : String.valueOf(mesActual);
                //Muestro la fecha con el formato deseado
                nacimiento_User.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);


            }
            //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
            /**
             *También puede cargar los valores que usted desee
             */
        }, anio, mes, dia);
        //Muestro el widget
        recogerFecha.show();

    }

    private void cambiarImagen(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");

        startActivityForResult(intent.createChooser(intent, "Selecciona la imagen para tu perfil"), SELECT_PICTURE);
    }

    //Depende la seleccion de Camara/Galeria (Case) hace una cosa o otra para mostrar las imagenes
    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            showProgressDialog();
            switch (requestCode) {
                case PHOTO_CODE:
                    MediaScannerConnection.scanFile(getContext(), new String[]{mPath}, null, new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String path, Uri uri) {
                            Toast.makeText(getContext(), "Scanned" + path + ":", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getContext(), "-> Uri = " + uri, Toast.LENGTH_SHORT).show();
                            //Log.i("ExternalStorage", "Scanned" + path + ":");
                            //Log.i("ExternalStorage", "-> Uri = " + uri);
                        }
                    });
                    imageBitmap = BitmapFactory.decodeFile(mPath);
                    break;
                case SELECT_PICTURE:
                    //FireBase Storage
                    final Uri path = data.getData();
                    final StorageReference filePath = mStorage.child("users").child(currentuser.getUid()).child("profile");
                    filePath.putFile(path).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    urlImagen = uri.toString();
                                    String userId = currentuser.getUid();
                                    mDatabase.child(userId).child("perfil").child("photoUser").setValue(urlImagen, new DatabaseReference.CompletionListener() {
                                        public void onComplete(DatabaseError error, DatabaseReference ref) {
                                            if (error != null) {
                                                hideProgressDialog();
                                                Toast.makeText(getContext(), "Error al Acttualizar: " + error.toString(), Toast.LENGTH_LONG).show();
                                            } else {
                                                hideProgressDialog();
                                                Toast.makeText(getContext(), "Imagen Cambiada con Éxito!!", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                }
                            });

                        }
                    });
                    break;
            }
        }
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
    }

    public void cargarMensaje() {

        mDatabaseMensaje.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String mensajeDeCuenta = dataSnapshot.getValue().toString();
                title_mensaje_User.setText(mensajeDeCuenta);
            }@Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Toast.makeText(getApplicationContext(), "Algo salió mal " + error.toException(), Toast.LENGTH_SHORT).show();
                //Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
    public void cargarDatos() {
        String user_id = currentuser.getUid();
      mDatabase.child(user_id).child("perfil").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario perfilCuenta = dataSnapshot.getValue(Usuario.class);
                //Obtenemos los valores que queres
                String nombreUsuario = perfilCuenta.getNameUser();
                String nombreUsuarioTitle = perfilCuenta.getNameUser();
                String documentoUsuario = perfilCuenta.getDiUser();
                String emailUsuario =       perfilCuenta.getEmailUser();
                String nacimientoUsuario = perfilCuenta.getNacimientoUser();
                String phoneUsuario = perfilCuenta.getPhoneUser();
                urlImagen = perfilCuenta.getPhotoUser();
                String direccionUsuario = perfilCuenta.getDireccionUser();
                String generoUsuario = perfilCuenta.getGeneroUser();
                /*if (generoUsuario.equals("masculino")) {
                    radio_M.setChecked(true);
                    radio_Femenino.setChecked(false);
                }
                if (generoUsuario.equals("femenino")) {
                    radio_M.setChecked(false);
                    radio_Femenino.setChecked(true);
                }*/
                nombre_User.setText(nombreUsuario);
                String [] primerNombre = new String[1];
                if(nombreUsuarioTitle != null){
                    primerNombre =nombreUsuarioTitle.split(" ");
                    title_name_User.setText(primerNombre[0]);
                }else{
                    nombreUsuarioTitle=" ";
                    title_name_User.setText("FESI");
                }
                di_User.setText(documentoUsuario);
                email_User.setText(emailUsuario);
                nacimiento_User.setText(nacimientoUsuario);
                telefono_User.setText(phoneUsuario);
                direccion_User.setText(direccionUsuario);
                if(urlImagen.isEmpty()){
                    Picasso.get()
                            .load(R.drawable.fesi_logo_bonton_1)
                            .placeholder(R.drawable.fesi_logo_bonton_1)
                            .centerCrop()
                            .error(R.drawable.fesi_logo_bonton_1)
                            .fit()
                            .priority(Picasso.Priority.HIGH)
                            .transform(new CircleTransform()).into(img_User);

                }else if(urlImagen.equals(null)){
                    Picasso.get()
                            .load(R.drawable.fesi_logo_bonton_1)
                            .placeholder(R.drawable.fesi_logo_bonton_1)
                            .centerCrop()
                            .error(R.drawable.fesi_logo_bonton_1)
                            .fit()
                            .priority(Picasso.Priority.HIGH)
                            .transform(new CircleTransform()).into(img_User);

                }else{
                    Picasso.get()
                            .load(urlImagen)
                            .placeholder(R.drawable.fesi_logo_bonton_1)
                            .centerCrop()
                            .error(R.drawable.fesi_logo_bonton_1)
                            .fit()
                            .priority(Picasso.Priority.HIGH)
                            .transform(new CircleTransform()).into(img_User);
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Toast.makeText(getApplicationContext(), "Algo salió mal " + error.toException(), Toast.LENGTH_SHORT).show();
                //Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void guardardatos() {
        showProgressDialog();
        String userId = currentuser.getUid();
        userGenero = null;
        /*if (radio_M.isChecked()) {
            userGenero = "masculino";
            radio_M.setChecked(true);
            radio_Femenino.setChecked(false);
        }else if (radio_Femenino.isChecked()) {
            userGenero = "femenino";
            radio_M.setChecked(false);
            radio_Femenino.setChecked(true);
        } else {
            userGenero = "";
        }*/
        String userDi = di_User.getText().toString().trim();
        String userNacimiento = nacimiento_User.getText().toString().trim();
        String userDireccion = direccion_User.getText().toString().trim();
        String emailFacebook = email_User.getText().toString().trim();
        String userNameFacebook = nombre_User.getText().toString().trim();
        String userPhotoFacebook = urlImagen;
        String userPhoneFacebook = telefono_User.getText().toString().trim();
        final User userModificado = new User(userId, userDi, userNacimiento, userGenero, emailFacebook, userNameFacebook, userPhotoFacebook, userPhoneFacebook, userDireccion);
        mDatabase.child(userId).child("perfil").setValue(userModificado, new DatabaseReference.CompletionListener() {
            public void onComplete(DatabaseError error, DatabaseReference ref) {
                if (error != null) {
                    hideProgressDialog();
                    Toast.makeText(getContext(), "Error al Acttualizar: " + error.toString(), Toast.LENGTH_LONG).show();
                } else {
                    hideProgressDialog();
                    Toast.makeText(getContext(), "Datos Actualizados con Éxito!!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void vistas(View view) {
        img_User = view.findViewById(R.id.ivImageUser);
        title_mensaje_User = view.findViewById(R.id.mensajeTitulo);
        nombre_User = view.findViewById(R.id.nombreUser);
        title_mensaje_User = view.findViewById(R.id.mensajeTitulo);
        title_name_User = view.findViewById(R.id.tvTitleCuenta);
        di_User = view.findViewById(R.id.diUser);
        email_User = view.findViewById(R.id.emailUser);
        nacimiento_User = view.findViewById(R.id.nacimientoUser);
        telefono_User = view.findViewById(R.id.telefonoUser);
        direccion_User = view.findViewById(R.id.direccionUser);
        radio_M = view.findViewById(R.id.radioM);
        radio_Femenino = view.findViewById(R.id.radioFemenino);
        btn_GuardarCambios = view.findViewById(R.id.btn_guardar);
        btn_CerrarSesion = view.findViewById(R.id.btnCerrarSesion);
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

    public void cerrarSesion() {

        startActivity(new Intent(getContext(), SlidesActivity.class));
        getActivity().finish();
        LoginManager.getInstance().logOut();
        firebaseAuth.signOut();
        Toast.makeText(getActivity(), "Se Cerró Exitosamente. ", Toast.LENGTH_LONG).show();
        getActivity().overridePendingTransition(R.anim.zoom_forward_in, R.anim.zoom_forward_out);
    }

}