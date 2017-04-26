/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class MainActivity extends AppCompatActivity implements View.OnKeyListener, View.OnClickListener {

    Button signupLogin, orButton;
    EditText usernameEditText, passwordEditText;
    String text;

    public void showUserList(){

        Intent intent = new Intent(getApplicationContext(), UserListActivity.class);
        startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signupLogin = (Button) findViewById(R.id.signupLogin);
        orButton = (Button) findViewById(R.id.orButton);
        text = (String) signupLogin.getText();
        usernameEditText = (EditText) findViewById(R.id.editTextUsername);
        passwordEditText = (EditText) findViewById(R.id.editTextPassword);

        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.Blue)));
        ab.setTitle(R.string.appTitle);

        passwordEditText.setOnKeyListener(this); // to handle keypress on keyboard after typing the password

        RelativeLayout backgroundRelativeLayout = (RelativeLayout) findViewById(R.id.backgroundRelativeLayout);
        ImageView logoImageView = (ImageView) findViewById(R.id.logoImageView);

        backgroundRelativeLayout.setOnClickListener(this);
        logoImageView.setOnClickListener(this);

        if(ParseUser.getCurrentUser()!= null){
            showUserList();
        }

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }

    public void orButton(View view) {

        text = (String) signupLogin.getText();
        if (text.equals(getString(R.string.SignUp))) {
            signupLogin.setText(R.string.Login);
            orButton.setText(R.string.OrSignup);
        } else if (text.equals(getString(R.string.Login))) {
            signupLogin.setText(getString(R.string.SignUp));
            orButton.setText(R.string.OrLogin);
        }

    }

    public void signupLogin(View view) {

        if (usernameEditText.getText().toString().matches("") || passwordEditText.getText().toString().matches("")) {

            Toast.makeText(this, R.string.emptyLoginField, Toast.LENGTH_SHORT).show();

        } else {

            text = (String) signupLogin.getText();
            if (text.equals(getString(R.string.SignUp))) {

                ParseUser user = new ParseUser();
                user.setUsername(usernameEditText.getText().toString());
                user.setPassword(passwordEditText.getText().toString());
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {

                        if (e == null) {

                            //Log.i("signup", "success");
                            Toast.makeText(MainActivity.this, R.string.signupSuccess, Toast.LENGTH_SHORT).show();
                            showUserList();

                        } else {

                            //Log.i("signup", "failed  " + e.getMessage());
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });


            } else if (text.equals(getString(R.string.Login))) {

                ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {

                        if (user != null) {
                            Toast.makeText(MainActivity.this, R.string.loginSuccess, Toast.LENGTH_SHORT).show();
                            showUserList();
                        } else {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }

    @Override    // this is to let enter button on keyboard directly press the login button
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){

            signupLogin(v);

        }

        return false;
    }

    @Override
    public void onClick(View v) {

        if(v.getId()== R.id.backgroundRelativeLayout || v.getId() == R.id.logoImageView) {

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        }
    }
}