package lexdikoy.garm;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Pattern;

import lexdikoy.garm.data.StaticConfig;

abstract public class BaseActivity extends AppCompatActivity {

    public final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public ProgressDialog mProgressDialog;

    //Firebase
    protected FirebaseAuth mAuth;
    protected FirebaseUser currentUser;
    protected FirebaseFirestore garmDataBase;

    public void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            StaticConfig.UID = currentUser.getUid();
        }
        garmDataBase = FirebaseFirestore.getInstance();
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
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

    protected void updateUserDB(String uid, final Map<String, Object> user) {
        initFirebase();
        showProgressDialog();
        garmDataBase.collection("users")
                .document(uid)
                .set(user)
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
}
