package lexdikoy.garm.Model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.format.DateFormat;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import lexdikoy.garm.ImageViews.CircularImageView;
import lexdikoy.garm.R;

public class JiraMessageAdapter extends RecyclerView.Adapter<JiraMessageAdapter.JiraMessageViewHolder>{

    private ArrayList<JiraMessage> jiraMessages;
    private Context context;
    protected FirebaseAuth mAuth;
    protected FirebaseUser currentUser;
    protected FirebaseDatabase garmFirebaseRealTimeDataBase;
    protected DatabaseReference garmDataBaseReference;

    public JiraMessageAdapter(ArrayList<JiraMessage> jiraMessages, Context context) {
        this.jiraMessages = jiraMessages;
        this.context = context;

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        garmFirebaseRealTimeDataBase = FirebaseDatabase.getInstance();
        garmDataBaseReference = garmFirebaseRealTimeDataBase.getReference();
    }

    @NonNull
    @Override
    public JiraMessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate( R.layout.item_jira_message, viewGroup, false);
        return new JiraMessageAdapter.JiraMessageViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final JiraMessageViewHolder jiraMessageViewHolder, final int i) {



        garmDataBaseReference
                .child("users/" + jiraMessages.get(i).getResponsible())
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        GenericTypeIndicator<String> indicator = new GenericTypeIndicator<String>(){};



                        jiraMessageViewHolder.jira_issue_link.setText(jiraMessages.get(i).getIssue_link().substring(34));
                        jiraMessageViewHolder.jira_issue_link.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent openLink = new Intent(Intent.ACTION_VIEW, Uri.parse(jiraMessages.get(i).getIssue_link()));
                                context.startActivity(openLink);
                            }
                        });


                        jiraMessageViewHolder.jira_responsible.setText("Ответственный: " + dataSnapshot.child("first_name").getValue(indicator) + " " + dataSnapshot.child("last_name").getValue(indicator));
                        jiraMessageViewHolder.jira_issue_title.setText(jiraMessages.get(i).getIssue_title());

                        jiraMessageViewHolder.jira_message_time.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", jiraMessages.get(i).getTimeMessage()));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return jiraMessages.size();
    }

    class JiraMessageViewHolder extends RecyclerView.ViewHolder {
        public TextView jira_issue_link;
        public TextView jira_responsible;
        public TextView jira_issue_title;
        public TextView jira_message_time;

        public JiraMessageViewHolder(@NonNull final View itemView) {
            super(itemView);
            jira_issue_link =(TextView) itemView.findViewById(R.id.jira_issue_link);
            jira_responsible =(TextView) itemView.findViewById(R.id.jira_responsible);
            jira_issue_title =(TextView) itemView.findViewById(R.id.jira_issue_title);
            jira_message_time = (TextView) itemView.findViewById(R.id.jira_message_time);
        }
    }
}
