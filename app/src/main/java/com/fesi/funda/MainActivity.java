package com.fesi.funda;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;



public class MainActivity extends AppCompatActivity {

    private Button txtOlvide;
    private AppCompatButton btnIngresar;
    private CheckBox recordar;
    private TextInputLayout inputEmail, inputPassword;
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Toolbar toolbar;
    private static final int RC_SIGN_IN = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_ingresar);
        agregarVistas();
        botonatras();
        verificarSesion();
        txtOlvide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                olvidepsw();
            }
        });
        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingresar();
            }
        });
        recordar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recordarme();
            }
        });

        loginButton.setReadPermissions("email", "public_profile");//user_status, publish_actions..
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "Inicio sesión cancelado", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(MainActivity.this, "Algo malo sucedió, Vuelve a intentar", Toast.LENGTH_SHORT).show();
            }
        });



        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (AccessToken.getCurrentAccessToken() != null)
                    Toast.makeText(MainActivity.this, "Bienvenido: " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
                if (user != null) {
                    String email = user.getEmail();
                    String userName = user.getDisplayName();
                    //Picasso.get().load(user.getPhotoUrl()).into(imgProfile);
                  //  loginButton.setVisibility(View.GONE);
                } else {
                  //  loginButton.setVisibility(View.GONE);
                }
            }
        };

    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Falló la Autenticación.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            loginButton.setVisibility(View.GONE);
                            Intent intent = new Intent(MainActivity.this, MainUsuario.class);
                            intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            //finish();
                            overridePendingTransition(R.anim.left_in, R.anim.left_out);


                        }
                    }
                });
    }

    private void agregarVistas() {
        auth = FirebaseAuth.getInstance();
        txtOlvide = (Button) findViewById(R.id.txt_olvide);
        /*btnIngresar = (AppCompatButton) findViewById(R.id.btn_ingresar);
        recordar = (CheckBox) findViewById(R.id.recuerdame);
        inputEmail = (TextInputLayout) findViewById(R.id.inputtxtemail);
        inputPassword = (TextInputLayout) findViewById(R.id.inputtxtpsw);*/
        loginButton = (LoginButton) findViewById(R.id.btnFacebookIn);
        callbackManager = CallbackManager.Factory.create();
        toolbar = (Toolbar) findViewById(R.id.toolbaringresar);
    }


    private void verificarSesion() {
        // Primero, verificamos la existencia de una sesión.
        if (auth.getCurrentUser() != null) {
            firebaseUser = auth.getCurrentUser();
            Toast.makeText(
                    this,
                    "Usuario logeuado:" + firebaseUser.getDisplayName(),
                    Toast.LENGTH_SHORT)
                    .show();
            Intent intent = new Intent(MainActivity.this, MainUsuario.class);
            startActivity(intent);
            finish();
        }

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

    private void olvidepsw() {

        Toast.makeText(this, "Ingresar Olvidar Contraseña",
                Toast.LENGTH_LONG).show();

    }

    private void ingresar() {

            btnIngresar.setEnabled(true);
            loginButton.setVisibility(View.VISIBLE);

            String email = inputEmail.getEditText().getText().toString();
            final String password = inputPassword.getEditText().getText().toString();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getApplicationContext(), "Ingrese Email!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(getApplicationContext(), "Ingrese Contraseña!", Toast.LENGTH_SHORT).show();
                return;
            }

            //authenticate user
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                // there was an error
                                if (password.length() < 6) {
                                    inputPassword.setError(getString(R.string.minimum_password));
                                } else {
                                    Toast.makeText(MainActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                }
                            } else {

                                Intent intent = new Intent(MainActivity.this, MainUsuario.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });


    }

    private void creacionCuenta() {
        Intent intent = new Intent(MainActivity.this, ActivityRegistro.class);
        startActivity(intent);

    }

    private void recordarme() {
        Toast.makeText(this, "Recordarme",
                Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,
                resultCode, data);
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            auth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onDestroy() {
        auth.signOut();
        FirebaseAuth.getInstance().signOut();
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        super.onDestroy();
    }
}
