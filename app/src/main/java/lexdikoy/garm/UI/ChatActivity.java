package lexdikoy.garm.UI;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Objects;

import lexdikoy.garm.BaseActivity;
import lexdikoy.garm.MainActivity;
import lexdikoy.garm.Model.Message;
import lexdikoy.garm.Model.MessageAdapter;
import lexdikoy.garm.Model.Room;
import lexdikoy.garm.Model.User;
import lexdikoy.garm.Model.UserAdapter;
import lexdikoy.garm.R;
import android.view.Menu;
import android.view.MenuItem;

public class ChatActivity extends BaseActivity {

    public Room room;
    private EditText textMessageUser;
    private RecyclerView chatView;

    public ArrayList<Message> messages = new ArrayList<Message>();
    public ArrayList<User> users = new ArrayList<User>();


    @SuppressLint({"WrongViewCast", "CutPasteId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Bundle arguments = getIntent().getExtras();
        room = (Room) arguments.getSerializable(Room.class.getSimpleName());
        textMessageUser = (EditText) findViewById(R.id.chat_message_view);
        chatView = (RecyclerView) findViewById(R.id.chat_recycler_view);
        chatView.setLayoutManager(new LinearLayoutManager(this));
        ImageButton buttonSendMessage = (ImageButton) findViewById(R.id.chat_btn_send);
        buttonSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushMessage();
            }
        });
        ImageButton buttonAttacjFileMessage = (ImageButton) findViewById(R.id.chat_btn_file);
        buttonAttacjFileMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        writeMessage();
    }

    private void pushMessage() {
        initFirebase();
        garmDataBaseReference
                .child("rooms/" + room.getRoomID())
                .child("messages")
                .push()
                .setValue(new Message(currentUser.getUid(),
                        textMessageUser.getText().toString()));
        textMessageUser.setText("");
    }

    private void writeMessage() {
        initFirebase();
        messages.clear();
        garmDataBaseReference
                .child("rooms/" + room.getRoomID())
                .child("messages")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        GenericTypeIndicator<String> indicator = new GenericTypeIndicator<String>(){};
                        GenericTypeIndicator<Long> indicatorLong = new GenericTypeIndicator<Long>(){};
                        messages.add(new Message(
                                dataSnapshot.child("author").getValue(indicator),
                                dataSnapshot.child("textMessage").getValue(indicator)
                                        ));
                        messages.get(messages.size()-1).setTimeMessage(dataSnapshot.child("timeMessage").getValue(indicatorLong));

                        MessageAdapter messageAdapter = new MessageAdapter(messages, ChatActivity.this);
                        chatView.setAdapter(messageAdapter);


                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        initFirebase();
        if (room.roomMaster.equals(currentUser.getUid())) {
            getMenuInflater().inflate(R.menu.chat_menu, menu);
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_rename_room_button:

                break;
            case R.id.menu_delete_room_button:
                deleteRoom();
                break;
        }

        return super.onOptionsItemSelected(item);
    }








    private void deleteRoom() {
        initFirebase();
        AlertDialog.Builder alertDialog;
        String buttonYes = "  Да  ";
        String buttonNo = "  Нет  ";
        alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Подтверждение действия");
        alertDialog.setMessage("Вы хотите удалить профиль?");
        alertDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                garmDataBaseReference
                        .child("rooms/" + room.getRoomID())
                        .removeValue()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    startActivity(new Intent(ChatActivity.this, MainActivity.class));

                                } else {
                                    toastMessage(task.getException().getMessage());
                                }
                            }
                        });

            }
        });
        alertDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
            }
        });
        alertDialog.setCancelable(true);
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
            }
        });
        alertDialog.show();
    }
}
