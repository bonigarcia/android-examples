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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.login_button).setOnClickListener(this::login);
        findViewById(R.id.sign_up).setOnClickListener(this::signUp);
    }

    private void login(View view) {
        EditText userEmail = findViewById(R.id.user_email);
        EditText userPassword = findViewById(R.id.user_password);

        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();

        if (email.isEmpty()) {
            displayDialog(this, R.string.login_error_title, R.string.login_error_empty_email);
        } else if (password.isEmpty()) {
            displayDialog(this, R.string.login_error_title, R.string.login_error_empty_passwd);
        } else {
            // Initialize Firebase Auth
            FirebaseAuth mAuth = FirebaseAuth.getInstance();

            // Login user
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new MyCompleteListener(this, mAuth));
        }
    }

    private void signUp(View view) {
        Intent intent = new Intent(getBaseContext(), SignUpActivity.class);
        startActivity(intent);
    }

    public static void displayDialog(Context context, int title_id, int error_message_id) {
        AlertDialog.Builder ad = new AlertDialog.Builder(context);
        ad.setTitle(title_id);
        ad.setMessage(error_message_id);
        ad.setPositiveButton(R.string.sing_up_error_button,
                (dialog, position) -> dialog.cancel());
        ad.show();
    }

}