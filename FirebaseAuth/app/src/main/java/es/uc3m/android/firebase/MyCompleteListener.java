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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MyCompleteListener implements OnCompleteListener<AuthResult> {

    Context context;
    FirebaseAuth mAuth;

    public MyCompleteListener(Context context, FirebaseAuth mAuth) {
        this.context = context;
        this.mAuth = mAuth;
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()) {
            // Sign in success
            FirebaseUser user = mAuth.getCurrentUser();
            Log.d(this.getClass().getName(),
                    "createUserWithEmail:success " + user);
            context.startActivity(new Intent(context, MainActivity.class));
        } else {
            // Sign in failure
            Log.w(this.getClass().getName(), "createUserWithEmail:failure",
                    task.getException());

            String message = context.getResources().getString(R.string.auth_failed) + " " +
                    task.getException().getMessage();
            Snackbar snackbar = Snackbar.make(((Activity) context).getCurrentFocus(),
                    message, Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }
}
