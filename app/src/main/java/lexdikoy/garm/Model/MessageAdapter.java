package lexdikoy.garm.Model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.text.format.DateFormat;
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

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{
    public ArrayList<Message> messages;
    public Context context;
    protected FirebaseAuth mAuth;
    protected FirebaseUser currentUser;
    protected FirebaseDatabase garmFirebaseRealTimeDataBase;
    protected DatabaseReference garmDataBaseReference;

    public MessageAdapter(ArrayList<Message> messages, Context context) {
        this.messages = messages;
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        garmFirebaseRealTimeDataBase = FirebaseDatabase.getInstance();
        garmDataBaseReference = garmFirebaseRealTimeDataBase.getReference();
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        currentUser = mAuth.getCurrentUser();
        View view = LayoutInflater.from(viewGroup.getContext()).inflate( R.layout.item_message, viewGroup, false);
        return new MessageAdapter.MessageViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder messageViewHolder, final int i) {
        garmDataBaseReference
                .child("users/"+messages.get(i).getAuthor())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        GenericTypeIndicator<String> indicator = new GenericTypeIndicator<String>(){};
                        if (!currentUser.getUid().equals(messages.get(i).getAuthor())) {
                            messageViewHolder.avatarLayout.setGravity(Gravity.LEFT);
                            messageViewHolder.messageContentLayout.setGravity(Gravity.LEFT);
                            messageViewHolder.messageContentLayout.setPadding(90, 0, 0, 0);
                            messageViewHolder.messageRelativaLayout.setPadding(0, 0, 90, 0);
                            messageViewHolder.messageHeaderLayout.setGravity(Gravity.LEFT);
                        }
                        messageViewHolder.messageAuthor.setText(dataSnapshot.child("first_name").getValue(indicator) + " " + dataSnapshot.child("last_name").getValue(indicator));
                        messageViewHolder.messageContent.setText(messages.get(i).getTextMessage());
                        messageViewHolder.messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", messages.get(i).getTimeMessage()));

                        String image64 = dataSnapshot.child("image_base64").getValue(indicator);
                        byte[] decodedString = Base64.decode(image64, Base64.DEFAULT);
                        Bitmap src = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        messageViewHolder.messageAuthorAvatar.setImageBitmap(src);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView messageTime;
        public TextView messageAuthor;
        public TextView messageContent;
        public CircularImageView messageAuthorAvatar;
        public LinearLayout avatarLayout;
        public LinearLayout messageContentLayout;
        public LinearLayout messageHeaderLayout;
        public RelativeLayout messageRelativaLayout;
        public MessageViewHolder(@NonNull final View itemView) {
            super(itemView);
            messageTime =(TextView) itemView.findViewById(R.id.my_message_time);
            messageAuthor =(TextView) itemView.findViewById(R.id.my_message_first_last_name);
            messageContent =(TextView) itemView.findViewById(R.id.my_message_text);

            avatarLayout = (LinearLayout) itemView.findViewById(R.id.avatar_layout);
            messageHeaderLayout = (LinearLayout) itemView.findViewById(R.id.message_header);
            messageContentLayout = (LinearLayout) itemView.findViewById(R.id.message_content_layout);
            messageRelativaLayout = (RelativeLayout) itemView.findViewById(R.id.relative_layout_message);
            messageAuthorAvatar =(CircularImageView) itemView.findViewById(R.id.my_message_avatar);
        }
    }
}
