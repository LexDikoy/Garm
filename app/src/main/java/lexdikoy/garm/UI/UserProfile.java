package lexdikoy.garm.UI;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.util.Base64;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import lexdikoy.garm.BaseActivity;
import lexdikoy.garm.ImageViews.CircularImageView;
import lexdikoy.garm.Model.User;
import lexdikoy.garm.R;

@SuppressLint("Registered")
public class UserProfile extends BaseActivity {
    private CircularImageView userAvatarDraw;
    private TextView userAliasView, userFirstLastNameView, userPhoneNumberView;
    private User user = new User("", "", "","", "", "", "");

    public UserProfile(NavigationView headerNavigatorView) {
        userAvatarDraw = (CircularImageView) headerNavigatorView.getHeaderView(0).findViewById(R.id.header_user_avatar);
        userPhoneNumberView = (TextView) headerNavigatorView.getHeaderView(0).findViewById(R.id.header_user_phone_number);
        userAliasView = (TextView) headerNavigatorView.getHeaderView(0).findViewById(R.id.header_user_alias);
        userFirstLastNameView = (TextView) headerNavigatorView.getHeaderView(0).findViewById(R.id.header_user_first_last_name);
        initUpdateUserProfile();
    }

    public void initUpdateUserProfile() {
        initFirebase();
        if (currentUser != null) {
            garmDataBaseReference
                    .child("users/" + currentUser.getUid())
                    .addValueEventListener(new ValueEventListener() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            initFirebase();
                            if (currentUser != null) {
                                GenericTypeIndicator<String> stringIndicator = new GenericTypeIndicator<String>(){};
                                String newAlias = dataSnapshot.child("alias").getValue(stringIndicator);
                                String newFirstName = dataSnapshot.child("first_name").getValue(stringIndicator);
                                String newLastName = dataSnapshot.child("last_name").getValue(stringIndicator);
                                String newPhoneNumber = dataSnapshot.child("phone_number").getValue(stringIndicator);
                                String newAvatarBase64 = dataSnapshot.child("image_base64").getValue(stringIndicator);
                                if (!user.alias.equals(newAlias)) {
                                    user.alias = newAlias;
                                }
                                if (!user.firstName.equals(newFirstName)) {
                                    user.firstName = newFirstName;
                                }
                                if (!user.lastName.equals(newLastName)) {
                                    user.lastName = newLastName;
                                }
                                if (!user.phoneNumber.equals(newPhoneNumber)) {
                                    user.phoneNumber = newPhoneNumber;
                                }
                                if (!user.avatar64.equals(newAvatarBase64)) {
                                    user.avatar64 = newAvatarBase64;
                                    byte[] decodedString = Base64.decode(user.avatar64, Base64.DEFAULT);
                                    Bitmap src = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                    userAvatarDraw.setImageBitmap(src);
                                }
                                userAliasView.setText(user.alias);
                                userPhoneNumberView.setText(user.phoneNumber);
                                userFirstLastNameView.setText(user.firstName + " " + user.lastName);
                            } else {
                                userAvatarDraw.setImageResource(R.drawable.ico);
                                userAliasView.setText(getRandomAnimal());
                                userPhoneNumberView.setText("");
                                userFirstLastNameView.setText("Пользователь не авторизован.");
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
        } else {
            userAvatarDraw.setImageResource(R.drawable.ico);
            userAliasView.setText(getRandomAnimal());
            userPhoneNumberView.setText("");
            userFirstLastNameView.setText("Пользователь не авторизован.");
        }
    }
}