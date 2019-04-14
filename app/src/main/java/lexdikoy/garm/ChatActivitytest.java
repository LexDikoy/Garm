package lexdikoy.garm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import lexdikoy.garm.BaseActivity;
import lexdikoy.garm.MainActivity;

@SuppressLint("Registered")
public class ChatActivitytest extends BaseActivity {
    Context context;


    public ChatActivitytest(final Context cont) {
        context = cont;
    }

    public void checkAllChats () {
        initFirebase();
        garmDataBaseReference
                .child("chats/")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {

                            String[] separ = childDataSnapshot.child("members").getValue(new GenericTypeIndicator<String>(){}).split(";");

                            for (String user: separ) {
                                if (user.equals(currentUser.getUid()))
                                    Toast.makeText(context, childDataSnapshot.getKey() , Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void getAllChats() {

      //  Toast.makeText(context, chatList.get(0), Toast.LENGTH_SHORT).show();
//        for (String id : chatList) {
//            Toast.makeText(cont, id, Toast.LENGTH_LONG).show();
//        }


    }











}
