/*
 * (C) Copyright 2024 Boni Garcia (https://bonigarcia.github.io/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package es.uc3m.android.firebase;

import static es.uc3m.android.firebase.LoginActivity.displayDialog;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        // Button listeners
        findViewById(R.id.sing_up_button).setOnClickListener(this::signUp);
        findViewById(R.id.sign_in).setOnClickListener(this::signIn);
    }

    private void signUp(View view) {
        EditText userEmail = findViewById(R.id.user_email);
        EditText userPassword = findViewById(R.id.user_password);
        EditText userPasswordConfirm = findViewById(R.id.user_password_confirm);

        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();
        String passwordConfirm = userPasswordConfirm.getText().toString();

        if (!isValidEmailAddress(email)) {
            displayDialog(this, R.string.sing_up_error_title, R.string.sing_up_error_invalid_email);
        } else if (!password.equals(passwordConfirm)) {
            displayDialog(this, R.string.sing_up_error_title, R.string.sing_up_error_passwd_diff);
        } else if (password.length() < 6) {
            displayDialog(this, R.string.sing_up_error_title, R.string.sing_up_error_passwd_diff);
        } else {
            // Initialize Firebase Auth
            FirebaseAuth mAuth = FirebaseAuth.getInstance();

            // Create user
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new MyCompleteListener(this, mAuth));
        }

    }

    private void signIn(View view) {
        finish();
    }

    // Source: https://stackoverflow.com/questions/624581/what-is-the-best-java-email-address-validation-method
    public boolean isValidEmailAddress(String email) {
        String ePattern =
                "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

}