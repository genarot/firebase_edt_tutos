package dev.atomic.applogin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.internal.api.FirebaseNoSignedInUserException;

/**
 * Created by gjtinoco on 03/10/2018.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button              mBtnLogin;
    private Button              mBtnRegister;
    private TextInputEditText   mEtEmail, mEtPassword;
    private FirebaseAuth        mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mFirebaseAuthListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );

        initializeUI();
    }

    private void initializeUI(){
        mBtnLogin       = findViewById( R.id.activityLoginLogin );
        mBtnRegister    = findViewById( R.id.activityLoginRegister );

        mEtEmail        = findViewById( R.id.activityLoginEmail );
        mEtPassword     = findViewById( R.id.activityLoginPassword );
        mBtnLogin.setOnClickListener( this );
        mBtnRegister.setOnClickListener( this );

        mFirebaseAuth   = FirebaseAuth.getInstance();
        mFirebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user  = firebaseAuth.getCurrentUser();
                if ( user  != null ) {
                    Toast.makeText( LoginActivity.this, "Usuario creado con exito", Toast.LENGTH_SHORT ).show();
                    Intent intent = new Intent( LoginActivity.this, MainActivity.class );
                    intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                    startActivity( intent );
                }
            }
        };
    }

    private void startRegisterActivity() {
        Intent intent = new Intent( LoginActivity.this, RegisterActivity.class );
        intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY );
        startActivity( intent );
    }

    private void loginUser(){
        String email = mEtEmail.getText().toString().trim();
        String password = mEtPassword.getText().toString().trim();

        if ( TextUtils.isEmpty( email )) {
            mEtEmail.setError( "Ingresa un Email" );
            Toast.makeText( this, "Ingresa un Email!", Toast.LENGTH_SHORT ).show();
            return;
        }
        if ( TextUtils.isEmpty( password ) ) {
            mEtPassword.setError( "Ingresa un Password" );
            Toast.makeText(this, "Ingresa un Password.", Toast.LENGTH_SHORT).show();
            return;
        }

        mFirebaseAuth.signInWithEmailAndPassword( email, password )
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if ( !task.isSuccessful() ) {
                            Log.d( "MYTAG", "Error logeando" );
                            Log.d( "MYTAG", task.getException().getMessage() );
                            task.getException().printStackTrace();
                            try {
                                throw task.getException();
                            } catch ( FirebaseAuthInvalidUserException ee_) {
                                Log.d( "MYTAG", "El email proporcionado no se encuentra registrado!" );
                                Toast.makeText( getApplicationContext(), "El email proporcionado no se encuentra registrado!", Toast.LENGTH_SHORT ).show();
                            } catch ( FirebaseAuthInvalidCredentialsException  e ){

                                Toast.makeText( getApplicationContext(),"Contrasena incorrecta!" , Toast.LENGTH_SHORT ).show();
                                Log.d( "MYTAG", "Contrasena incorrecta!" );
                            } catch (Exception ex) {
                                Toast.makeText( getApplicationContext(), "Ocurrio un error en el login", Toast.LENGTH_SHORT ).show();
                                Log.d( "MYTAG", "Oter Exception" );
                            }
                        }
                    }
                } );

    }
    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener( mFirebaseAuthListener );
    }

    @Override
    protected void onPause() {
        super.onPause();
        if ( mFirebaseAuthListener != null ) {
            mFirebaseAuth.removeAuthStateListener( mFirebaseAuthListener );
        }
    }

    @Override
    public void onClick(View v) {
        switch ( v.getId() ) {
            case R.id.activityLoginLogin:
                loginUser();
                break;
            case R.id.activityLoginRegister:
                startRegisterActivity();
                break;
        }
    }
}
