package com.abhishek.pal.chaterji;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private ProgressDialog mMainProgressDialog;
    private android.support.v7.widget.Toolbar mToolBar;

    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private TabLayout mTabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        mAuth = FirebaseAuth.getInstance();

        mToolBar = findViewById( R.id.main_page_toolbar );
        setSupportActionBar( mToolBar);
        getSupportActionBar().setTitle( "ChatErrJi" );

        //Tabs
        mViewPager = (ViewPager) findViewById( R.id.main_tabPager );
        mSectionsPagerAdapter = new SectionsPagerAdapter( getSupportFragmentManager() );

        mViewPager.setAdapter( mSectionsPagerAdapter );

        mTabLayout = findViewById( R.id.main_tabs );
        mTabLayout.setupWithViewPager( mViewPager );

        mMainProgressDialog = new ProgressDialog( this );


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser ==null){

            sendToStart();

        }
    }

    private void sendToStart() {

        Intent startIntent = new Intent( MainActivity.this, StartActivity.class );
        startActivity( startIntent );
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu( menu );
        getMenuInflater().inflate( R.menu.main_menu, menu );

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected( item );

        if (item.getItemId()== R.id.main_logout_btn){

            mMainProgressDialog.setTitle( "Logging out" );
            mMainProgressDialog.setMessage( "Please wait while we log you off!" );
            mMainProgressDialog.setCanceledOnTouchOutside( false );
            mMainProgressDialog.show();

            FirebaseAuth.getInstance().signOut();
            sendToStart();
        }
        return true;
    }
}
