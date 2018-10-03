package dev.atomic.applogin;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by gjtinoco on 03/10/2018.
 */

public class RegisterActivity  extends AppCompatActivity{
    private Button              mBtnLogin;
    private Button              mBtnRegister;
    private TextInputEditText   mEtEmail, mEtPassword;
    private FirebaseAuth        mFirebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );

        initializeUI();
    }

    private void initializeUI(){
        mBtnLogin       = findViewById( R.id.activityLoginLogin );
        mBtnLogin.setVisibility( View.GONE );
        mBtnRegister    = findViewById( R.id.activityLoginRegister );

        mFirebaseAuth   = FirebaseAuth.getInstance();
        mBtnRegister.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEtEmail.getText().toString().trim();
                String password = mEtPassword.getText().toString().trim();

                if ( TextUtils.isEmpty( email )) {
                    Toast.makeText( RegisterActivity.this, "Ingresa un Email!", Toast.LENGTH_SHORT ).show();
                    return;
                }
                if ( TextUtils.isEmpty( password ) ) {
                    Toast.makeText( RegisterActivity.this, "Ingresa un Password.", Toast.LENGTH_SHORT).show();
                    return;
                }
                mFirebaseAuth.createUserWithEmailAndPassword( email, password )
                        .addOnFailureListener( RegisterActivity.this, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText( getApplicationContext(), "Error creando el usuario. " + e.getMessage(), Toast.LENGTH_SHORT ).show();
                                Log.d("MYTAG", e.getMessage());
                                e.printStackTrace();
                            }
                        } );
            }
        } );

        mEtEmail        = findViewById( R.id.activityLoginEmail );
        mEtPassword     = findViewById( R.id.activityLoginPassword );
        getActionBar().setTitle( "Actividad de Registro" );
    }
}
