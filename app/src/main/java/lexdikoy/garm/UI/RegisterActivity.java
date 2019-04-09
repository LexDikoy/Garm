package lexdikoy.garm.UI;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;

import lexdikoy.garm.BaseActivity;
import lexdikoy.garm.ImageViews.CircularImageView;
import lexdikoy.garm.MainActivity;
import lexdikoy.garm.R;

public class RegisterActivity extends BaseActivity {

    private EditText rUserAlias, rUserEmail, rUserFirstName, rUserLastName, rUserPhoneNumber, rUserPassword, rUserRepeatPassword;
    private final int Pick_image = 1;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        rUserAlias = (EditText) findViewById(R.id.reg_alias);
        rUserEmail = (EditText) findViewById(R.id.reg_email);
        rUserFirstName = (EditText) findViewById(R.id.reg_first_name);
        rUserLastName = (EditText) findViewById(R.id.reg_last_name);
        rUserPhoneNumber = (EditText) findViewById(R.id.reg_phone_number);
        rUserPassword = (EditText) findViewById(R.id.reg_password);
        rUserRepeatPassword = (EditText) findViewById(R.id.reg_repeat_password);

        Button rSelectImageButton = (Button) findViewById(R.id.reg_choice_image_button);
        rSelectImageButton.setOnClickListener(clickListener);
        Button rRegisterButton = (Button) findViewById(R.id.reg_button);
        rRegisterButton.setOnClickListener(clickListener);
        initFirebase();
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.reg_choice_image_button:
                    Intent choiceImage = new Intent(Intent.ACTION_GET_CONTENT);
                    choiceImage.setType("image/*");
                    choiceImage.addCategory(Intent.CATEGORY_OPENABLE);
                    startActivityForResult(choiceImage, Pick_image);
                    break;
                case R.id.reg_button:
                    if (validate()) {
                        registration(v);
                    }
                    break;
            }
        }
    };

    private boolean validate () {
        String alias = rUserAlias.getText().toString();
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(rUserEmail.getText().toString());
        String firstName = rUserFirstName.getText().toString();
        String lastName = rUserLastName.getText().toString();
        String phoneNumber = rUserPhoneNumber.getText().toString();
        String password = rUserPassword.getText().toString();
        String repeatPassword = rUserRepeatPassword.getText().toString();
        boolean result = true;

        if (alias.isEmpty()) {
            rUserAlias.setError("Вы не ввели псевдоним.");
            result = false;
        }
        if (!matcher.find()) {
            rUserEmail.setError("E-mail введен не корректно.");
            result = false;
        }
        if (firstName.isEmpty()) {
            rUserFirstName.setError("Вы не ввели имя.");
            result = false;
        }
        if (lastName.isEmpty()) {
            rUserLastName.setError("Вы не ввели фамилию.");
            result = false;
        }
        if (phoneNumber.isEmpty()) {
            rUserPhoneNumber.setError("Вы не ввели телефон.");
            result = false;
        }
        if (password.length() < 6) {
            rUserPassword.setError("Пароль должен содержать минимум 6 символов.");
            result = false;
        }
        if (repeatPassword.length() < 6) {
            rUserRepeatPassword.setError("Пароль должен содержать минимум 6 символов.");
            result = false;
        }

        if (!password.equals(repeatPassword)) {
            rUserPassword.setError("Пароли не совпадают.");
            rUserRepeatPassword.setError("Пароли не совпадают.");
            result = false;
        }

        return result;

    }

    private void registration(final View v) {
        showProgressDialog();

        String email = rUserEmail.getText().toString();
        String password = rUserPassword.getText().toString();

        final Map<String, Object> user = new HashMap<>();
        user.put("alias", rUserAlias.getText().toString());
        user.put("first_name", rUserFirstName.getText().toString());
        user.put("last_name", rUserLastName.getText().toString());
        user.put("phone_number", rUserPhoneNumber.getText().toString());

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            updateUserDB(currentUser.getUid(), user);
                            saveUserInfo();
                            hideProgressDialog();
                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                            RegisterActivity.this.finish();
                        } else {
                            hideKeyboard(v);
                            hideProgressDialog();
                            toastMessage(Objects.requireNonNull(task.getException()).getMessage());
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CircularImageView rAvatar = (CircularImageView) findViewById(R.id.reg_avatar);
        switch(requestCode) {
            case Pick_image:
                if(resultCode == RESULT_OK){
                    try {
                        imageUri = data.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        rAvatar.setImageBitmap(selectedImage);
                    } catch (FileNotFoundException e) {
                        toastMessage(e.getMessage());
                    }
                }
        }
    }
}
