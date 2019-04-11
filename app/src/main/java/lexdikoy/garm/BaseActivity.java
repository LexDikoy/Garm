package lexdikoy.garm;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Pattern;

import lexdikoy.garm.data.StaticConfig;

abstract public class BaseActivity extends AppCompatActivity {

    public final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public ProgressDialog mProgressDialog;

    public static final int AVATAR_WIDTH = 128;
    public static final int AVATAR_HEIGHT = 128;

    //Firebase
    protected FirebaseAuth mAuth;
    protected FirebaseUser currentUser;

    protected FirebaseFirestore garmFirestore;
    protected StorageReference garmStorage;

    protected FirebaseDatabase garmFirebaseRealTimeDataBase;
    protected DatabaseReference garmDataBaseReference;

    public void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            StaticConfig.UID = currentUser.getUid();
        }
        garmFirestore = FirebaseFirestore.getInstance();
        garmStorage = FirebaseStorage.getInstance().getReference();

        garmFirebaseRealTimeDataBase = FirebaseDatabase.getInstance();
        garmDataBaseReference = garmFirebaseRealTimeDataBase.getReference();
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this, R.style.AppCompatAlertDialogStyle);
            mProgressDialog.setMessage("Загрузка");
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void hideKeyboard(View view) {
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void toastMessage (String message) {
        switch (message) {
            case "The email address is already in use by another account.":
                message = "Адрес электронной почты уже используется другой учетной записью.";
                break;
            case "There is no user record corresponding to this identifier. The user may have been deleted.":
                message = "Такого пользователя не существует или был удален.";
                break;
            case "The password is invalid or the user does not have a password.":
                message = "Пароль недействителен или у пользователя нет пароля.";
                break;
            case "The email address is badly formatted.":
                message = "Адрес электронной почты указан не верно.";
                break;
        }
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public String getRandomAnimal () {
        String[] animals = {
                "Ленивец",
                "Карликовый бегемот",
                "Лори",
                "Хамелеон",
                "Сурикат",
                "Коала",
                "Пингвин",
                "Красная панда",
                "Белуха",
                "Рыба-клоун",
                "Шиншилла",
                "Косуля",
                "Дельфин-афалина",
                "Альпака",
                "Колибри",
                "Калан",
                "Гренландский тюлень",
                "Гигантская панда",
                "Филиппинский долгопят",
                "Фенек"
        };
        Random rand = new Random();
        return animals[rand.nextInt(20)];
    }

    public void saveUserInfo() {

    }

    protected void updateUserDB(final Map<String, Object> user) {
        initFirebase();
        showProgressDialog();
        garmDataBaseReference.child("users/" + currentUser.getUid())
                .setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            hideProgressDialog();
                            toastMessage("Данные пользователя сохранены успешно");
                        } else {
                            hideProgressDialog();
                            toastMessage(Objects.requireNonNull(task.getException()).getMessage());
                        }
                    }
                });
    }

    protected void updateUserAvatar(String uid, Uri file) {
        initFirebase();
        showProgressDialog();
        garmStorage
                .child("users_avatar/" + uid + ".jpg")
                .putFile(file)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            hideProgressDialog();
                            toastMessage("Изображение сохранено");
                        } else {
                            hideProgressDialog();
                            toastMessage(task.getException().getMessage());
                        }
                    }
                });
    }

    public static Bitmap makeImageLite(InputStream is, int width, int height, int reqWidth, int reqHeight) {
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(is, null, options);
    }

    public static String encodeBase64(Bitmap imgBitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imgBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }






}
