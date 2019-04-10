package lexdikoy.garm.UI;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import java.util.Objects;
import java.util.regex.Matcher;
import lexdikoy.garm.BaseActivity;
import lexdikoy.garm.MainActivity;
import lexdikoy.garm.R;

public class LoginActivity extends BaseActivity {

    private EditText aUserEmail, aUserPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        aUserEmail = (EditText) findViewById(R.id.auth_email);
        aUserPassword = (EditText) findViewById(R.id.auth_password);
        Button aLoginButton = (Button) findViewById(R.id.auth_login);
        aLoginButton.setOnClickListener(authButtonClickListener);
        Button aForgotPassword = (Button) findViewById(R.id.auth_forgot_password);
        aForgotPassword.setOnClickListener(authButtonClickListener);
        initFirebase();
    }

    private View.OnClickListener authButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String email = aUserEmail.getText().toString();
            String password = aUserPassword.getText().toString();
            switch (v.getId()) {
                case R.id.auth_login:
                    if (validate(email, password)) {
                        signIn(email, password, v);
                    }
                    break;
                case R.id.auth_forgot_password:
                    resetPassword(email, v);
                    break;
            }
        }
    };

    private boolean validate(String email, String password) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        boolean result = true;
        if (!matcher.find()) {
            aUserEmail.setError("E-mail введен не корректно.");
            result = false;
        }
        if (password.length() < 6) {
            result = false;
            aUserPassword.setError("Пароль должен содержать минимум 6 символов.");
        }
        return result;
    }

    private void signIn(String email, String password, final View v) {
        showProgressDialog();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            saveUserInfo();
                            initFirebase();
                            hideProgressDialog();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            LoginActivity.this.finish();
                        } else {
                            hideKeyboard(v);
                            hideProgressDialog();
                            toastMessage(Objects.requireNonNull(task.getException()).getMessage());
                        }
                    }
                });
    }

    private void resetPassword(final String email, final View v) {
        showProgressDialog();
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            hideKeyboard(v);
                            hideProgressDialog();
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setTitle("Восстановление пароля.")
                                    .setMessage("На почту " + email + " направлено письмо для востановления пароля.")
                                    .setCancelable(true)
                                    .setNegativeButton("Спасибо",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        } else {
                            hideKeyboard(v);
                            hideProgressDialog();
                            toastMessage(Objects.requireNonNull(task.getException()).getMessage());
                        }
                    }
                });
    }
}
