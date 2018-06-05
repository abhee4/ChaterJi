package com.abhishek.pal.chaterji;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Toolbar mToolBar;

    private ProgressDialog mRegProgress;

    private TextInputLayout mDisplayName, mEmail, mPassword;
    private Button mCreateBtn;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_register );

        mToolBar = (Toolbar) findViewById( R.id.register_toolbar );
        setSupportActionBar( mToolBar );
        getSupportActionBar().setTitle( "Create Account" );
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );

        mRegProgress = new ProgressDialog( this );
        mAuth = FirebaseAuth.getInstance();

        mDisplayName = findViewById( R.id.display_name );
        mEmail = findViewById( R.id.reg_email);
        mPassword = findViewById( R.id.reg_password );
        mCreateBtn = findViewById( R.id.reg_create_btn );


        mCreateBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String display_name = mDisplayName.getEditText().getText().toString();
                String email = mEmail.getEditText().getText().toString();
                String password = mPassword.getEditText().getText().toString();

                if (!TextUtils.isEmpty(display_name)&& !TextUtils.isEmpty( email )&& !TextUtils.isEmpty( password )){

                    mRegProgress.setTitle( "Registering you" );
                    mRegProgress.setMessage( "Please wait while we register you" );
                    mRegProgress.setCanceledOnTouchOutside( false );
                    mRegProgress.show();

                    register_user(display_name, email, password);
                }

            }
        } );

    }

    private void register_user(final String display_name, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            //preparing for Firebase DB creation
                            FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                            String uid =current_user.getUid();

                            //Creating DB, . after getReference is node then table name then column name then subtable
                            mDatabase = FirebaseDatabase.getInstance().getReference().child( "Users" ).child( uid );

                            //HashMap creates complex DB columns
                            HashMap<String, String> userMap = new HashMap<>(  );
                            userMap.put( "name",display_name );
                            userMap.put( "status", "Hi Folks, I'm using ChatErrJi!" );
                            userMap.put( "image", "default" );
                            userMap.put( "thumb_image", "default" );
                            mDatabase.setValue( userMap ).addOnCompleteListener( new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        mRegProgress.dismiss();

                                        Intent mainIntent = new Intent( RegisterActivity.this, MainActivity.class );
                                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity( mainIntent );
                                        finish();
                                    }
                                }
                            } );
                        } else if(task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(RegisterActivity.this, "This email already registered. Try again with another email !", Toast.LENGTH_LONG).show();
                            mRegProgress.hide();
                        }else{
                            Toast.makeText(RegisterActivity.this, "Cannot Sign In, please try again later !", Toast.LENGTH_LONG).show();
                            mRegProgress.hide();
                        }

                    }
                });
    }
}
