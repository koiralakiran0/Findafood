package com.example.kiran.recipe4155;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class ActivitySignup extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText fName;
    private EditText lName;
    private EditText email;
    private EditText password;
    private EditText passRepeat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        fName = (EditText) findViewById(R.id.editText_firstname);
        lName = (EditText) findViewById(R.id.editText_lastName);
        email = (EditText) findViewById(R.id.editText_email);
        password = (EditText) findViewById(R.id.editText_password);
        passRepeat = (EditText) findViewById(R.id.editText_repeatPass);

        findViewById(R.id.button_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        findViewById(R.id.button_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
                //Intent intent = new Intent(ActivitySignup.this, RecipeActivity.class);
                //startActivity(intent);
            }
        });

    }

    private void signUp() {
        final String fn = fName.getText().toString();
        final String ln = lName.getText().toString();
        String em = email.getText().toString();
        String pass = password.getText().toString();
        String passRe = passRepeat.getText().toString();

        if (validateEmail(em) && validatePassword(pass, passRe)){
            //CREATE USER IF THE EMAIL AND PASSWORD ARE VALID
            mAuth.createUserWithEmailAndPassword(em, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(ActivitySignup.this, task.getException().toString(), Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(ActivitySignup.this, "User Successfully Signed Up",
                                        Toast.LENGTH_SHORT).show();
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if (user != null) {
                                    // Name, email address, and profile photo Url
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(fn + " " + ln)
                                            .build();
                                    user.updateProfile(profileUpdates);

                                    Log.d("demo", user.getDisplayName() + " " + user.getEmail() + " " + user.getUid());
                                }
                                finish();
                            }

                        }
                    });
        }
    }

    private boolean validatePassword(String pass, String passRe) {
        if (pass.equals(passRe)){
            return true;
        }
        return false;
    }

    private boolean validateEmail(String em) {
        if (em == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(em).matches();
        }

    }
}
