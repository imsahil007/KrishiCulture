package com.humandroid.team.krishiculture;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        Home.OnFragmentInteractionListener, ApplyLoan.OnFragmentInteractionListener,
        FAQ.OnFragmentInteractionListener,HireTractor.OnFragmentInteractionListener,
        RentTractor.OnFragmentInteractionListener,Schemes.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.home);
        View view =  navigationView.getHeaderView(0);
        TextView name = (TextView)view.findViewById(R.id.nav_name);
        name.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        TextView username = (TextView)view.findViewById(R.id.nav_username);
        try{
        username.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());}
        catch (Exception e){
            username.setText("+91-"+FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
        }
        ImageView pic=(ImageView)view.findViewById(R.id.profile_logo_icon);
      /*            Add an image uploading code which we can reuse while adding tractors for renting purpose
        Glide.with(this)     //profile
                .load(Image uri will come here)
                .into(pic);
                */


        navigationView.setNavigationItemSelectedListener(this);




        /********************audio code here**************/
        //Leave this part!We will complete it after vacations

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "We will play audio here later", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        /**********************/
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.sign_out) {
            Toast.makeText(getApplicationContext(), "Signout success!",
                    Toast.LENGTH_LONG).show();
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment=null;

        if (id == R.id.home) {
            fragment= new Home();

        } else if (id == R.id.rent_tractor) {
            fragment= new RentTractor();

        } else if (id == R.id.hire_tractor) {
            fragment= new HireTractor();

        } else if (id == R.id.hire_equipment) {
            fragment= new Hire_Equipment();

        } else if (id == R.id.apply_loan) {
            fragment= new ApplyLoan();

        } else if (id == R.id.schemes) {
            fragment= new Schemes();

        }else if (id == R.id.faq) {
            fragment= new FAQ();

        }else if (id == R.id.contact) {
            fragment= new Contact_us();

        }else if (id == R.id.share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Playstore link will come here.Download now.");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);

        }

        if (fragment != null) {
            Bundle bundle=null;// use bundle to transfer any data within this class and fragments
            //fragment.setArguments(bundle);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(android.R.id.content, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri)  {
        getSupportActionBar().setTitle(uri.toString());
    }
}