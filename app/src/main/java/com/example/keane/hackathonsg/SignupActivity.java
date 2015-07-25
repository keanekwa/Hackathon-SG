package com.example.keane.hackathonsg;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SignupActivity extends Activity {

    private TextView mUsernameBlank, mPasswordBlank, mCPasswordBlank, mEmailAddBlank;
    private Button mAddAccount;
    String usernameInput, passwordInput, cPasswordInput, emailInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mUsernameBlank = (EditText) findViewById(R.id.UsernameBlank);
        mPasswordBlank = (EditText) findViewById(R.id.PasswordBlank);
        mCPasswordBlank = (EditText) findViewById(R.id.PasswordBlank2);
        mEmailAddBlank = (EditText) findViewById(R.id.EmailBlank);
        mAddAccount = (Button) findViewById(R.id.CreateButton);

        final ProgressDialog mSignUpLoader = new ProgressDialog(SignupActivity.this);
        mSignUpLoader.setMessage("Loading...");
        mSignUpLoader.setIndeterminate(true);
        mAddAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameInput = mUsernameBlank.getText().toString();
                passwordInput = mPasswordBlank.getText().toString();
                cPasswordInput = mCPasswordBlank.getText().toString();
                emailInput = mEmailAddBlank.getText().toString();

                if (usernameInput.equals("") | passwordInput.equals("") | cPasswordInput.equals("") | emailInput.equals("")){
                    alertMessage("Please fill in the empty fields.");
                    //checks for empty fields
                }
                else if (!passwordInput.equals(cPasswordInput)){
                    alertMessage("Your password does not much.");
                    mPasswordBlank.setText("");
                    mCPasswordBlank.setText("");
                }
                else {
                    final ParseUser userObject = new ParseUser();
                    ArrayList <String> arrayList = new ArrayList<>();
                    userObject.setUsername(usernameInput);
                    userObject.setPassword(passwordInput);
                    userObject.setEmail(emailInput);
                    userObject.put("noOfJios", 0);
                    userObject.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e == null) {
                                ParseObject userData = new ParseObject("UserData");
                                userData.put("username", userObject.getUsername());
                                userData.put("userId", userObject.getObjectId());
                                userData.put("friendsIdArray", new ArrayList<>());
                                userData.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        ParseUser.logInInBackground(usernameInput, passwordInput, new LogInCallback() {
                                            public void done(ParseUser user, ParseException e) {
                                                if (user != null && e == null) {
                                                    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                                    SignupActivity.this.startActivity(intent);
                                                } else {
                                                    alertMessage(e.toString());
                                                }
                                            }
                                        });
                                    }
                                });
                            }
                            else {
                                alertMessage(e.toString());
                            }
                        }
                    });
                }
            }});



    }

    public void alertMessage(String message){
        if (message.startsWith("com.parse.ParseRequest $ParseRequestException: ")){
            message.replace("com.parse.ParseRequest $ParseRequestException: ", "");
        }
        Toast.makeText(SignupActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
