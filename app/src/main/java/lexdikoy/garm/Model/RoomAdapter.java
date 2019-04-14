package lexdikoy.garm.Model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import lexdikoy.garm.R;
import lexdikoy.garm.UI.ChatActivity;

import java.io.Serializable;
import java.util.ArrayList;

// extends RecyclerView.Adapter<UserAdapter.UserViewHolder>
public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {
    public ArrayList<Room> rooms;
    public Context context;

    public RoomAdapter(ArrayList<Room> rooms, Context context) {
        this.rooms = rooms;
        this.context = context;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_room, viewGroup, false);
        return new RoomViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder roomViewHolder, final int i) {
        roomViewHolder.roomName.setText(rooms.get(i).getRoomTitle());

        roomViewHolder.roomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra(Room.class.getSimpleName(), rooms.get(i));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }



    class RoomViewHolder extends RecyclerView.ViewHolder {
        public TextView roomName;
        public ImageButton roomIn;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.roomName = (TextView) itemView.findViewById(R.id.card_room_name);
            this.roomIn = (ImageButton) itemView.findViewById(R.id.card_room_in);
        }
    }




}
