package com.zlalini.rattle.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zlalini.rattle.Home_Screen.ChatListItemPojo;
import com.zlalini.rattle.Home_Screen.MainActivity;
import com.zlalini.rattle.R;
import com.zlalini.rattle.RASS_Utility.GenerateKeys;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;

import static com.zlalini.rattle.RASS_Utility.GenerateKeys.gen;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    //defining view objects
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextName;
    private Button buttonSignup;
    private TextView textViewSignin;

    private ProgressDialog progressDialog;

    private FirebaseUser mUse;
    private DatabaseReference mChat1,mChat,mChat3;

    //defining firebaseauth object
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);


        //Implementation of crytographic algorithm
        
        //initializing firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();
/*
        //if getCurrentUser does not returns null
        if(firebaseAuth.getCurrentUser() != null){
            //that means user is already logged in
            //so close this activity
            finish();

            //and open profile activity
            startActivity(new Intent(getApplicationContext(), SignInActivity.class));*/
        //}

        //initializing views
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextName = (EditText)findViewById(R.id.editTextName);
        textViewSignin = (TextView) findViewById(R.id.textViewSignin);


        buttonSignup = (Button) findViewById(R.id.buttonSignup);

        progressDialog = new ProgressDialog(this);

        //attaching listener to button
        buttonSignup.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);
    }

    private void registerUser(){

        //getting email and password from edit texts
        final String email = editTextEmail.getText().toString().trim();
        final String password  = editTextPassword.getText().toString().trim();
        final String username = editTextName.getText().toString().trim();

        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(username)){
            Toast.makeText(this,"Please enter username",Toast.LENGTH_LONG).show();
            return;
        }

        //if the email and password are not empty
        //displaying a progress dialog

        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();

        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                       Log.e("TAG","Error1");
                        if(task.isSuccessful()){
                            Log.e("TAG1","Error2");
                            GenerateKeys ky = new GenerateKeys();
                            String x = ky.gen();
                            Log.e("TAG",x+"key");
                            int pub_key = ky.getKey(x);
                            Log.e("TAG",pub_key+"pub key");
                            try {
                                FileOutputStream fileOut=openFileOutput("key1.txt", MODE_PRIVATE);
                                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                                out.writeChars(x);
                                out.close();
                                fileOut.close();
                                //display file saved message
                                Toast.makeText(getBaseContext(), "File saved successfully!",
                                        Toast.LENGTH_SHORT).show();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(getApplicationContext(),"Hi!Again",Toast.LENGTH_LONG).show();

                            final FirebaseDatabase database=FirebaseDatabase.getInstance();
                            mUse= FirebaseAuth.getInstance().getCurrentUser();
                            mChat=database.getReference("CONTACT_LIST").child(mUse.getUid());
                            UserContentPojo user_details=new UserContentPojo(username,mUse.getUid().toString(),email,pub_key);
                            mChat.setValue(user_details);
                            //finish();
                            Toast.makeText(getApplicationContext(),"You are registered! Now proceed to login",Toast.LENGTH_LONG).show();

                            //startActivity(new Intent(getApplicationContext(), MainActivity.class));


                        }else{
                            //display some message here
                            Toast.makeText(getApplicationContext(),"Registration Error",Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });

    }

    @Override
    public void onClick(View view) {

            registerUser();

        if(view == textViewSignin){
            //open login activity when user taps on the already registered textview
            finish();
            startActivity(new Intent(this, SignInActivity.class));
        }

    }
}