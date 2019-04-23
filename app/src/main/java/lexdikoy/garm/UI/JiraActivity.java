package lexdikoy.garm.UI;

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
import android.widget.CompoundButton;
import android.widget.Switch;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;

import lexdikoy.garm.BaseActivity;
import lexdikoy.garm.Model.JiraMessage;
import lexdikoy.garm.Model.JiraMessageAdapter;
import lexdikoy.garm.Model.Message;
import lexdikoy.garm.Model.MessageAdapter;
import lexdikoy.garm.R;

public class JiraActivity extends BaseActivity {

    private RecyclerView jiraIssueListView;
    private Switch only_my_issue;
    public ArrayList<JiraMessage> jMessage = new ArrayList<JiraMessage>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jira);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        jiraIssueListView = (RecyclerView) findViewById(R.id.recycler_jira_list);
        jiraIssueListView.setLayoutManager(new LinearLayoutManager(this));
        only_my_issue = (Switch) findViewById(R.id.jira_switch);
        only_my_issue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    writeJira();
                } else {
                    writeJira();
                }
            }
        });
        writeJira();
    }

    private void writeJira() {
        initFirebase();
        jMessage.clear();
        garmDataBaseReference
                .child("jira/")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        GenericTypeIndicator<String> indicator = new GenericTypeIndicator<String>(){};
                        GenericTypeIndicator<Long> indicatorLong = new GenericTypeIndicator<Long>(){};
                        if (only_my_issue.isChecked()) {
                            if (currentUser.getUid().equals(dataSnapshot.child("responsible").getValue(indicator)) && dataSnapshot.child("status").getValue(indicatorLong) != 0 ) {
                                jMessage.add(new JiraMessage(
                                        dataSnapshot.child("issue_link").getValue(indicator),
                                        dataSnapshot.child("responsible").getValue(indicator),
                                        dataSnapshot.child("issue_title").getValue(indicator),
                                        dataSnapshot.child("issue_time").getValue(indicatorLong)
                                ));
                            }
                        } else {
                            if (dataSnapshot.child("status").getValue(indicatorLong) != 0 ) {
                                jMessage.add(new JiraMessage(
                                        dataSnapshot.child("issue_link").getValue(indicator),
                                        dataSnapshot.child("responsible").getValue(indicator),
                                        dataSnapshot.child("issue_title").getValue(indicator),
                                        dataSnapshot.child("issue_time").getValue(indicatorLong)
                                ));
                            }
                        }
                        JiraMessageAdapter jMessageAdapter = new JiraMessageAdapter(jMessage, JiraActivity.this);
                        jiraIssueListView.setAdapter(jMessageAdapter);
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
}
