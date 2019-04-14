package lexdikoy.garm;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import lexdikoy.garm.Model.Room;
import lexdikoy.garm.Model.RoomAdapter;
import lexdikoy.garm.Model.User;
import lexdikoy.garm.Model.UserAdapter;
import lexdikoy.garm.UI.LoginActivity;
import lexdikoy.garm.UI.RegisterActivity;
import lexdikoy.garm.UI.UserProfile;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    public ArrayList<User> users = new ArrayList<User>();

    public ArrayList<Room> rooms = new ArrayList<Room>();
    RecyclerView mRecyclerUsersList;
    RecyclerView mRecyclerRoomsList;
    NavigationView navigationView;


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

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //navigationView.getMenu().setGroupVisible();
        userProfile = new UserProfile(navigationView);

        mRecyclerUsersList = (RecyclerView) findViewById(R.id.recycler_users_list);
        mRecyclerUsersList.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerRoomsList = (RecyclerView) findViewById(R.id.recycler_rooms_list);
        mRecyclerRoomsList.setLayoutManager(new LinearLayoutManager(this));

        buildUsersList();
        buildRoomsList();
        renderNavigator();

    }

    private void renderNavigator() {
        initFirebase();
        if(currentUser != null) {
            navigationView.getMenu().setGroupVisible(R.id.identified_user_drawer, true);
            navigationView.getMenu().setGroupVisible(R.id.unidentified_user_drawer, false);
        } else {
            navigationView.getMenu().setGroupVisible(R.id.identified_user_drawer, false);
            navigationView.getMenu().setGroupVisible(R.id.unidentified_user_drawer, true);
        }

    }

    private void buildRoomsList() {
        initFirebase();

        if(currentUser!=null) {
            garmDataBaseReference
                    .child("rooms")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            rooms.clear();
                            GenericTypeIndicator<String> indicator = new GenericTypeIndicator<String>(){};
                            for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                                rooms.add(new Room(
                                        childDataSnapshot.getKey(),
                                        childDataSnapshot.child("room_name").getValue(indicator),
                                        childDataSnapshot.child("room_members").getValue(indicator),
                                        childDataSnapshot.child("room_master").getValue(indicator)
                                ));
                            }
                            RoomAdapter roomAdapter = new RoomAdapter(rooms, MainActivity.this);
                            mRecyclerRoomsList.setAdapter(roomAdapter);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }
    }


    private void buildUsersList() {
        initFirebase();
        if (currentUser != null) {
            garmDataBaseReference
                    .child("users")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            users.clear();
                            GenericTypeIndicator<String> indicator = new GenericTypeIndicator<String>(){};
                            for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                                if(!currentUser.getUid().equals(childDataSnapshot.getKey())) {

                                    users.add(new User(childDataSnapshot.getKey(),
                                            childDataSnapshot.child("alias").getValue(indicator),
                                            childDataSnapshot.child("email").getValue(indicator),
                                            childDataSnapshot.child("first_name").getValue(indicator),
                                            childDataSnapshot.child("last_name").getValue(indicator),
                                            childDataSnapshot.child("phone_number").getValue(indicator),
                                            childDataSnapshot.child("image_base64").getValue(indicator)));
                                }
                            }

                            UserAdapter userAdapter = new UserAdapter(users, MainActivity.this);
                            mRecyclerUsersList.setAdapter(userAdapter);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }
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

                break;
            case R.id.menu_reg_button:

                break;
            case R.id.menu_my_account_button:
                break;
            case R.id.menu_settings_button:
                break;
            case R.id.menu_logout_button:

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_my_profile:
                break;
            case R.id.nav_my_settings:
                break;
            case R.id.nav_planing:
                break;
            case R.id.nav_sig_in:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.nav_sig_out:
                showProgressDialog();
                mAuth.signOut();
                initFirebase();
                userProfile.initUpdateUserProfile();
                hideProgressDialog();
                renderNavigator();
                toastMessage("Вы вышли.");
                break;
            case R.id.nav_registration:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
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
