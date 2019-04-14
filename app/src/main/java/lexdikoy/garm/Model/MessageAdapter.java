package lexdikoy.garm.Model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.text.format.DateFormat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import lexdikoy.garm.ImageViews.CircularImageView;
import lexdikoy.garm.R;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{
    public ArrayList<Message> messages;
    public ArrayList<User> usersList;
    public Context context;

    protected FirebaseAuth mAuth;
    protected FirebaseUser currentUser;


    public MessageAdapter(ArrayList<Message> messages, ArrayList<User> usersList, Context context) {
        this.messages = messages;
        this.usersList = usersList;
        this.context = context;

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
    }


    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_my_message, viewGroup, false);
        return new MessageAdapter.MessageViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder messageViewHolder, int i) {


//        for (User user : usersList) {
//            if (messages.get(i).getAuthor().equals(user.getUserUID())) {
                messageViewHolder.messageAuthor.setText(messages.get(i).getAuthor());
                messageViewHolder.messageContent.setText(messages.get(i).getTextMessage());
                messageViewHolder.messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", messages.get(i).getTimeMessage()));
//                break;
////            }
////
////        }






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

        public MessageViewHolder(@NonNull final View itemView) {
            super(itemView);


            messageTime =(TextView) itemView.findViewById(R.id.my_message_time);
            messageAuthor =(TextView) itemView.findViewById(R.id.my_message_first_last_name);
            messageContent =(TextView) itemView.findViewById(R.id.my_message_text);

            messageAuthorAvatar =(CircularImageView) itemView.findViewById(R.id.my_message_avatar);

        }
    }






}
