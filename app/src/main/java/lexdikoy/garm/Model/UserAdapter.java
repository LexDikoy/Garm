package lexdikoy.garm.Model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import lexdikoy.garm.BaseActivity;
import lexdikoy.garm.ImageViews.CircularImageView;
import lexdikoy.garm.MainActivity;
import lexdikoy.garm.R;
import lexdikoy.garm.UI.LoginActivity;
import lexdikoy.garm.UI.UserProfileActivity;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    public ArrayList<User> users;
    public Context context;

    public UserAdapter(ArrayList<User> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_user, viewGroup, false);
        return new UserViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final UserViewHolder userViewAdapter, final int i) {
        userViewAdapter.alias.setText(users.get(i).getAlias());
        userViewAdapter.firstLastName.setText(users.get(i).getFirstName() + " " + users.get(i).getLastName());

        byte[] decodedString = Base64.decode(users.get(i).avatar64, Base64.DEFAULT);
        Bitmap src = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        userViewAdapter.avatar.setImageBitmap(src);

        userViewAdapter.checkProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserProfileActivity.class);
                intent.putExtra(User.class.getSimpleName(), users.get(i));
                context.startActivity(intent);
            }
        });

        userViewAdapter.sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView alias;
        public TextView firstLastName;
        public CircularImageView avatar;
        public CardView card;
        public ImageButton sendMessage;
        public ImageButton checkProfile;

        public UserViewHolder(@NonNull final View itemView) {
            super(itemView);
            avatar =(CircularImageView) itemView.findViewById(R.id.card_avatar);
            alias =(TextView) itemView.findViewById(R.id.card_alias);
            firstLastName =(TextView) itemView.findViewById(R.id.card_first_last_name);
            card = (CardView) itemView.findViewById(R.id.user_card_view);
            sendMessage = (ImageButton) itemView.findViewById(R.id.card_btn_send_message);
            checkProfile = (ImageButton) itemView.findViewById(R.id.card_btn_profile);
        }
    }
}
