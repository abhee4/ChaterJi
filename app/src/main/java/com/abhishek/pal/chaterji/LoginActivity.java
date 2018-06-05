package com.abhishek.pal.chaterji;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class LoginActivity extends AppCompatActivity {

    private Toolbar mToolBar;
    private TextInputLayout mLoginEmail;
    private TextInputLayout mLoginPassword;

    private FirebaseAuth mAuth;

    private Button mLogin_btn, mLoginForgotPassword;

    private ProgressDialog mLoginProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );

        mToolBar = (Toolbar) findViewById( R.id.login_toolbar );
        setSupportActionBar( mToolBar );
        getSupportActionBar().setTitle( "Login" );
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );

        mAuth = FirebaseAuth.getInstance();

        mLoginForgotPassword = findViewById( R.id.login_forgot_password );

        mLoginProgress = new ProgressDialog( this );

        mLoginEmail = findViewById( R.id.display_name );
        mLoginPassword = findViewById( R.id.login_password );

        mLogin_btn = findViewById( R.id.login_btn );


        mLogin_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mLoginEmail.getEditText().getText().toString();
                String password = mLoginPassword.getEditText().getText().toString();

                if (!TextUtils.isEmpty( email ) && !TextUtils.isEmpty( password )){

                    mLoginProgress.setTitle( "Logging In..." );
                    mLoginProgress.setMessage( "Please wait while we check your credentials" );
                    mLoginProgress.setCanceledOnTouchOutside( false );
                    mLoginProgress.show();

                    loginUser(email, password);
                }else{

                }

            }
        } );

        mLoginForgotPassword.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mLoginEmail.getEditText().getText().toString();
                if (!TextUtils.isEmpty( email )){

                    mLoginProgress.setTitle( "Logging In..." );
                    mLoginProgress.setMessage( "Please wait while we check your credentials" );
                    mLoginProgress.setCanceledOnTouchOutside( false );
                    mLoginProgress.show();
                    resetPasswordEmail(email);
                }else{
                    Toast.makeText( LoginActivity.this, "Please fill in the email", Toast.LENGTH_LONG ).show();
                }

            }
        } );
    }

    public void resetPasswordEmail(String email) {
        mAuth.sendPasswordResetEmail( email )
                .addOnCompleteListener( new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText( LoginActivity.this, "Password reset email has been sent to your email id.", Toast.LENGTH_LONG ).show();
                        }else{ createAccount();
                        }
                    }
                } );
    }

    private void createAccount() {


        mLoginProgress.setTitle( "Not found" );
        mLoginProgress.setMessage( "Your email is not found, Please register first !" );
        mLoginProgress.setCanceledOnTouchOutside( false );

        Intent createAccountIntent =  new Intent( LoginActivity.this, RegisterActivity.class );
        startActivity( createAccountIntent );
        finish();
    }


    public void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword( email, password ).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    
                    mLoginProgress.dismiss();
                    Intent mainIntent = new Intent( LoginActivity.this, MainActivity.class );
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity( mainIntent );
                    finish();
                }else{
                    mLoginProgress.hide();
                    Toast.makeText( LoginActivity.this, "Cannot Sign in, please check the form and try again.", Toast.LENGTH_LONG ).show();
                }
            }
        } );

    }




}
