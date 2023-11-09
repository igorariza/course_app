package com.fesi.funda;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.fesi.funda.src.Usuario;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

public class ActivityRegistro extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private CallbackManager callbackManager;
    private FirebaseAuth firebaseAuth;
    public ProgressDialog progressDialog;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TextInputLayout txt_inputemail, txt_inputpassword;
    private LoginButton loginButton;
    private Toolbar toolbar;
    private TextView btnPolitica, txt_accionUser;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private AppCompatButton btn_ingresar;
    private String accionUser, txtAccionUser, btnAccionUser;
    private static final int SIGN_IN_GOOGLE_CODE = 1;
    private GoogleApiClient googleApiClient;
    private SignInButton btnIngresarGoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        FacebookSdk.sdkInitialize(getApplicationContext());
        //AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_registro);
        /*Intent intent = this.getIntent();
        Bundle extra = intent.getExtras();
        accionUser = extra.getString("accionUser");
        txtAccionUser = extra.getString("txtaccionUser");
        btnAccionUser = extra.getString("btnaccionUser");*/
        accionUser = "Inicia Sesión";
        txtAccionUser = "Ingresa con tu email";
        btnAccionUser = "Ingresa";
        LoginManager.getInstance().logOut();
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();
        toolbar = (Toolbar) findViewById(R.id.toolbarunete);
        botonatras();

        vistas();
        initialize();
        setButtonListeners();
        botonatras();

        /*loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email"));//user_status, publish_actions..
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(ActivityRegistro.this, "Inicio sesión cancelado", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(ActivityRegistro.this, "Algo malo sucedió, Vuelve a intentar", Toast.LENGTH_SHORT).show();
            }
        });*/

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (AccessToken.getCurrentAccessToken() != null)
//                    Toast.makeText(ActivityRegistro.this, AccessToken.getCurrentAccessToken().getExpires().toString() + user.getEmail(), Toast.LENGTH_SHORT).show();
                    if (user != null) {
                        //String email = user.getEmail();
                        //String userName = user.getDisplayName();
                        //Picasso.get().load(user.getPhotoUrl()).into(imgProfile);
                        //loginButton.setVisibility(View.GONE);
                    } else {
                        //loginButton.setVisibility(View.VISIBLE);
                    }
            }
        };
    }

    private void signInGoogleFirebase(GoogleSignInResult googleSignInResult) {
        if (googleSignInResult.isSuccess()) {
            AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInResult.getSignInAccount().getIdToken(), null);
            firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ActivityRegistro.this, "Ingresocorrecto Google", Toast.LENGTH_LONG);
                        Intent intent = new Intent(ActivityRegistro.this, MainUsuario.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        hideProgressDialog();
                        startActivity(intent);
                        //finish();
                        overridePendingTransition(R.anim.left_in, R.anim.left_out);

                    } else {
                        Toast.makeText(ActivityRegistro.this, "Algo sucedió Google!! ", Toast.LENGTH_LONG);
                    }

                }
            });

        } else {

        }

    }

    private void setButtonListeners() {
        //login button
        btn_ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleRegistrationLogin();
            }
        });

        btnIngresarGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog();
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, SIGN_IN_GOOGLE_CODE);

            }
        });

/*
        //reset password - for unauthenticated user
        findViewById(R.id.rest_password_b).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendResetPasswordEmail();
            }
        });

        //logout button
        findViewById(R.id.logout_b).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
            }
        });

        //Verify email button
        findViewById(R.id.verify_b).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmailVerificationMsg();
            }
        });

        //update password - for signed in user
        findViewById(R.id.update_password_b).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePassword();
            }
        });

        //Order functionality to show how to secure firestore data
        //using firebase authentication and firestore security rules
        findViewById(R.id.order_b).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setClass(EmailPasswordAuthActivity.this, OrderActivity.class);
                startActivity(i);
            }
        });*/
    }

    public void vistas() {
        txt_inputemail = (TextInputLayout) findViewById(R.id.inputemail);
        txt_inputpassword = (TextInputLayout) findViewById(R.id.inputpassword);
        firebaseAuth = FirebaseAuth.getInstance();
        callbackManager = CallbackManager.Factory.create();
        //loginButton = (LoginButton) findViewById(R.id.btnFacebookIn);
        toolbar = (Toolbar) findViewById(R.id.toolbarunete);
        toolbar.setTitle(accionUser);
        btnPolitica = (TextView) findViewById(R.id.politica);
        txt_accionUser = (TextView) findViewById(R.id.txt_accionuser);
        txt_accionUser.setText(txtAccionUser);
        btn_ingresar = (AppCompatButton) findViewById(R.id.btn_registrar);
        btn_ingresar.setText(btnAccionUser);
        //btnIngresarGoogle = (SignInButton) findViewById(R.id.googleIngresar);
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

    private void initialize() {
        firebaseAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    Toast.makeText(ActivityRegistro.this,
                            "Bienvenido: ",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ActivityRegistro.this, MainUsuario.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    //finish();
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);

                } else {
                    Toast.makeText(ActivityRegistro.this, "Sesion Cerrada", Toast.LENGTH_LONG);

                }


            }
        };
        //Inicia Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN_GOOGLE_CODE) {
            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            signInGoogleFirebase(googleSignInResult);
        }
        callbackManager.onActivityResult(requestCode,
                resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        showProgressDialog();
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(ActivityRegistro.this, "Falló la Autenticación.",
                                    Toast.LENGTH_SHORT).show();
                        } else {

                            Intent intent = new Intent(ActivityRegistro.this, MainUsuario.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                            //hide progress dialog
                            hideProgressDialog();
                            overridePendingTransition(R.anim.left_in, R.anim.left_out);

                        }
                    }
                });
    }


    private void addDocumentToCollection() {
        String userId = firebaseAuth.getUid();
        String userDi = "";
        String userNacimiento = "";
        String userGenero = "";
        String userDireccion = "";
        String emailFacebook = firebaseAuth.getCurrentUser().getEmail();
        String userNameFacebook = firebaseAuth.getCurrentUser().getDisplayName();
        String userPhotoFacebook = "";
        String userPhoneFacebook = "";
        final Usuario user = new Usuario(userId, userDi, userNacimiento, userGenero, emailFacebook, userNameFacebook, userPhotoFacebook, userPhoneFacebook, userDireccion);

        //User sharedData = User.getInstance();
        //sharedData.setValue(userId);
        DatabaseReference usrID = myRef.child("users").child(userId);
        usrID.child("perfil").setValue(user);

    }

    private void handleRegistrationLogin() {
        final String email = txt_inputemail.getEditText().getText().toString();
        final String password = txt_inputpassword.getEditText().getText().toString();

        if (!validateEmailPass(email, password)) {
            return;
        }

        //show progress dialog
        showProgressDialog();

        //perform login and account creation depending on existence of email in firebase
        performLoginOrAccountCreation(email, password);
    }


    @Override
    public void onStart() {
        firebaseAuth.addAuthStateListener(mAuthListener);
        super.onStart();
        //showAppropriateOptions();

    }

    @Override
    public void onStop() {
        if (mAuthListener != null) {
            firebaseAuth.removeAuthStateListener(mAuthListener);
        }
        super.onStop();
        hideProgressDialog();

    }

    @Override
    protected void onDestroy() {
        if (mAuthListener != null) {
            firebaseAuth.removeAuthStateListener(mAuthListener);
        }
        super.onDestroy();

    }

    private void performLoginOrAccountCreation(final String email, final String password) {
        firebaseAuth.fetchProvidersForEmail(email).addOnCompleteListener(
                this, new OnCompleteListener<ProviderQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                        if (task.isSuccessful()) {
                            //Log.d(TAG, "checking to see if user exists in firebase or not");
                            ProviderQueryResult result = task.getResult();

                            if (result != null && result.getProviders() != null
                                    && result.getProviders().size() > 0) {
                                //Log.d(TAG, "User exists, trying to login using entered credentials");
                                performLogin(email, password);
                            } else {
                                //Log.d(TAG, "User doesn't exist, creating account");
                                registerAccount(email, password);
                            }
                        } else {
                            //Log.w(TAG, "User check failed", task.getException());
                            Toast.makeText(ActivityRegistro.this,
                                    "Hay un problema, por favor intente de nuevo más tarde.",
                                    Toast.LENGTH_SHORT).show();

                        }
                        //hide progress dialog
                        hideProgressDialog();
                        //enable and disable login, logout buttons depending on signin status
                        //showAppropriateOptions();
                    }
                });
    }

    private void performLogin(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Log.d(TAG, "login success");
                            Toast.makeText(ActivityRegistro.this,
                                    "Bienvenido: ",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ActivityRegistro.this, MainUsuario.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            //finish();
                            overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        } else {
                            //Log.e(TAG, "Login fail", task.getException());
                            Toast.makeText(ActivityRegistro.this,
                                    "La autenticación falló",
                                    Toast.LENGTH_SHORT).show();
                        }
                        //hide progress dialog
                        //hideProgressDialog();
                        //enable and disable login, logout buttons depending on signin status
                        // showAppropriateOptions();
                    }
                });
    }

    private void registerAccount(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Log.d(TAG, "account created");
                            Toast.makeText(ActivityRegistro.this,
                                    "Cuenta Registrada: " + firebaseAuth.getCurrentUser().getEmail(),
                                    Toast.LENGTH_SHORT).show();
                            addDocumentToCollection();
                            Intent intent = new Intent(ActivityRegistro.this, MainUsuario.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            //finish();
                            overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        } else {
                            //Log.d(TAG, "register account failed", task.getException());
                            Toast.makeText(ActivityRegistro.this,
                                    "El registro de la cuenta falló.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        //hide progress dialog
                        //hideProgressDialog();
                        //enable and disable login, logout buttons depending on signin status
                        //showAppropriateOptions();
                    }
                });
    }

    private boolean validateEmailPass(String email, String password) {
        boolean valid = true;

        if (TextUtils.isEmpty(email)) {
            txt_inputemail.setError("Necesario.");
            valid = false;
        } else if (!email.contains("@")) {
            txt_inputemail.setError("No es una Dirección de correo electrónico.");
            valid = false;
        } else {
            txt_inputemail.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            txt_inputpassword.setError("Necesario.");
            valid = false;
        } else if (password.length() < 6) {
            txt_inputpassword.setError("Min. 6 caracteres.");
            valid = false;
        } else {
            txt_inputpassword.setError(null);
        }

        return valid;
    }

    private boolean validateResetPassword(String password) {
        boolean valid = true;
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            valid = false;
        }
        return valid;
    }

    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    /*
    private void showAppropriateOptions(){
        hideProgressDialog();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            findViewById(R.id.login_items).setVisibility(View.GONE);
            findViewById(R.id.logout_items).setVisibility(View.VISIBLE);

            findViewById(R.id.verify_b).setEnabled(!user.isEmailVerified());
        } else {
            findViewById(R.id.login_items).setVisibility(View.VISIBLE);
            findViewById(R.id.logout_items).setVisibility(View.GONE);
        }
    }

    private void sendEmailVerificationMsg() {
        findViewById(R.id.verify_b).setEnabled(false);

        final FirebaseUser user = firebaseAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        findViewById(R.id.verify_b).setEnabled(true);
                        if (task.isSuccessful()) {
                            Toast.makeText(EmailPasswordAuthActivity.this,
                                    "Verification email has been sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "Error in sending verification email",
                                    task.getException());
                            Toast.makeText(EmailPasswordAuthActivity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    //non-singed in user reset password email
    private void sendResetPasswordEmail() {
        final String email = ((EditText) findViewById(R.id.reset_password_email))
                .getText().toString();
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(EmailPasswordAuthActivity.this,
                                    "Reset password code has been emailed to "
                                            + email,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "Error in sending reset password code",
                                    task.getException());
                            Toast.makeText(EmailPasswordAuthActivity.this,
                                    "There is a problem with reset password, try later.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updatePassword() {

        final FirebaseUser user = firebaseAuth.getCurrentUser();
        final String newPwd = ((EditText) findViewById(R.id.update_password_t)).getText().toString();
        if(!validateResetPassword(newPwd)){
            Toast.makeText(EmailPasswordAuthActivity.this,
                    "Invalid password, please enter valid password",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        user.updatePassword(newPwd)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(EmailPasswordAuthActivity.this,
                                    "Password has been updated",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "Error in updating passowrd",
                                    task.getException());
                            Toast.makeText(EmailPasswordAuthActivity.this,
                                    "Failed to update passwrod.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void logOut() {
        firebaseAuth.signOut();
        //showAppropriateOptions();
    }*/

}
