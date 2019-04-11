package lexdikoy.garm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import lexdikoy.garm.ImageViews.CircularImageView;
import lexdikoy.garm.Model.User;
import lexdikoy.garm.UI.LoginActivity;
import lexdikoy.garm.UI.RegisterActivity;
import lexdikoy.garm.UI.UserProfile;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    UserProfile userProfile;
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
        navigationView.setNavigationItemSelectedListener(this);

        userProfile = new UserProfile(navigationView);
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
        switch (item.getItemId()) {
            case R.id.menu_auth_button:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.menu_reg_button:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.menu_my_account_button:
                break;
            case R.id.menu_settings_button:
                break;
            case R.id.menu_logout_button:
                showProgressDialog();
                mAuth.signOut();
                initFirebase();
                userProfile.initUpdateUserProfile();
                hideProgressDialog();
                toastMessage("Вы вышли.");
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
showProgressDialog();

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        initFirebase();
        if (currentUser != null) {
            menu.setGroupVisible(R.id.unidentified_user, false);
            menu.setGroupVisible(R.id.identified_user, true);
        } else {
            menu.setGroupVisible(R.id.unidentified_user, true);
            menu.setGroupVisible(R.id.identified_user, false);
        }

        return super.onPrepareOptionsMenu(menu);
    }



}
