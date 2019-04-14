package lexdikoy.garm.UI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import lexdikoy.garm.BaseActivity;
import lexdikoy.garm.ImageViews.CircularImageView;
import lexdikoy.garm.MainActivity;
import lexdikoy.garm.Model.User;
import lexdikoy.garm.Model.UserAdapter;
import lexdikoy.garm.R;

public class UserProfileActivity extends BaseActivity {

    EditText profileAlias;
    EditText profileEmail;
    EditText profileFirstName;
    EditText profileLastName;
    EditText profilePhoneNumber;
    CircularImageView profileAvatar;
    Button buttonSave;
    User user;
    FloatingActionButton fab;
    private final int Pick_image = 1;
    private Uri imageUri;
    private Bitmap selectImage, littleSelectImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);
        profileAlias = (EditText) findViewById(R.id.profile_alias);
        profileEmail = (EditText) findViewById(R.id.profile_email);
        profileFirstName = (EditText) findViewById(R.id.profile_first_name);
        profileLastName = (EditText) findViewById(R.id.profile_last_name);
        profilePhoneNumber = (EditText) findViewById(R.id.profile_phone_number);
        profileAvatar = (CircularImageView) findViewById(R.id.profile_avatar);
        buttonSave = (Button) findViewById(R.id.profile_save_button);
        initFirebase();
        Bundle arguments = getIntent().getExtras();

        user = (User) arguments.getSerializable(User.class.getSimpleName());

        if (!currentUser.getUid().equals(user.getUserUID()))
        {
            isNotCurrentUser();
        } else {
            isCurrentUser();
        }
    }

    @SuppressLint("RestrictedApi")
    private void isCurrentUser() {
        byte[] decodedString = Base64.decode(user.getAvatar64(), Base64.DEFAULT);
        Bitmap src = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        profileAvatar.setImageBitmap(src);

        profileAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent choiceImage = new Intent(Intent.ACTION_GET_CONTENT);
                choiceImage.setType("image/*");
                choiceImage.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(Intent.createChooser(choiceImage, "Выбрать фотографию"), Pick_image);
            }
        });

        profileAlias.setText(user.getAlias());
        profileEmail.setText(user.getEmail());
        profileFirstName.setText(user.getFirstName());
        profileLastName.setText(user.getLastName());
        profilePhoneNumber.setText(user.getPhoneNumber());

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initFirebase();
                showProgressDialog();
                currentUser.updateEmail(profileEmail.getText().toString());

                final Map<String, Object> userMap = new HashMap<>();
                userMap.put("alias", profileAlias.getText().toString());
                userMap.put("email", profileEmail.getText().toString());
                userMap.put("first_name", profileFirstName.getText().toString());
                userMap.put("last_name", profileLastName.getText().toString());
                userMap.put("phone_number", profilePhoneNumber.getText().toString());
                userMap.put("image_base64", encodeBase64(littleSelectImage));
                updateUserDB(userMap);
                startActivity(new Intent(UserProfileActivity.this, MainActivity.class));
                UserProfileActivity.this.finish();
                hideProgressDialog();
            }
        });

        fab.setVisibility(View.GONE);
    }

    private void isNotCurrentUser() {
        byte[] decodedString = Base64.decode(user.getAvatar64(), Base64.DEFAULT);
        Bitmap src = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        profileAvatar.setImageBitmap(src);

        profileAlias.setFocusable(false);
        profileAlias.setText(user.getAlias());

        profileEmail.setFocusable(false);
        profileEmail.setText(user.getEmail());

        profileFirstName.setFocusable(false);
        profileFirstName.setText(user.getFirstName());

        profileLastName.setFocusable(false);
        profileLastName.setText(user.getLastName());

        profilePhoneNumber.setFocusable(false);
        profilePhoneNumber.setText(user.getPhoneNumber());
        profilePhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + user.getPhoneNumber()));
                startActivity(callIntent);
            }
        });

        buttonSave.setVisibility(View.GONE);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toastMessage("hi!3");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case Pick_image:
                if(resultCode == RESULT_OK){
                    try {
                        imageUri = data.getData();
                        selectImage = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        littleSelectImage = makeImageLite(getContentResolver().openInputStream(imageUri), selectImage.getWidth(), selectImage.getHeight(), AVATAR_WIDTH, AVATAR_HEIGHT);
                        profileAvatar.setImageBitmap(littleSelectImage);
                    } catch (FileNotFoundException e) {
                        toastMessage(e.getMessage());
                    }
                }
        }
    }
}
