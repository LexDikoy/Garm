package lexdikoy.garm.Model;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import lexdikoy.garm.ImageViews.CircularImageView;
import lexdikoy.garm.R;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    public ArrayList<User> users;

    public UserAdapter(ArrayList<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_user, viewGroup, false);
        return new UserViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final UserViewHolder userViewAdapter, int i) {
        userViewAdapter.alias.setText(users.get(i).getAlias());
        userViewAdapter.firstLastName.setText(users.get(i).getFirstName() + " " + users.get(i).getLastName());

        byte[] decodedString = Base64.decode(users.get(i).avatar64, Base64.DEFAULT);
        Bitmap src = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        userViewAdapter.avatar.setImageBitmap(src);

        userViewAdapter.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userViewAdapter.alias.setText("asd");
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

        public UserViewHolder(@NonNull final View itemView) {
            super(itemView);
            avatar =(CircularImageView) itemView.findViewById(R.id.card_avatar);
            alias =(TextView) itemView.findViewById(R.id.card_alias);
            firstLastName =(TextView) itemView.findViewById(R.id.card_first_last_name);
            card = (CardView) itemView.findViewById(R.id.user_card_view);
        }
    }
}
