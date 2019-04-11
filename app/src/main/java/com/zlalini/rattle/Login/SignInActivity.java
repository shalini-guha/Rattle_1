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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static com.zlalini.rattle.RASS_Utility.GenerateKeys.gen;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {


    //defining views
    private Button buttonSignIn;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignup;

    private FirebaseUser mUse;
    private DatabaseReference mChat1,mChat2;
    //firebase auth object
    private FirebaseAuth firebaseAuth;

    //progress dialog
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        //getting firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        //if the objects getcurrentuser method is not null
        //means user is already logged in
        /*if(firebaseAuth.getCurrentUser() != null){
            //close this activity
            finish();
            //opening profile activity
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }*/

        //initializing views
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonSignIn = (Button) findViewById(R.id.buttonSignin);
        textViewSignup  = (TextView) findViewById(R.id.textViewSignUp);

        progressDialog = new ProgressDialog(this);

        //attaching click listener
        buttonSignIn.setOnClickListener(this);
        textViewSignup.setOnClickListener(this);
    }

    //method for user login
    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password  = editTextPassword.getText().toString().trim();


        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }

        //if the email and password are not empty
        //displaying a progress dialog

        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();

        //logging in the user
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        //if the task is successfull
                        if(task.isSuccessful()){
                            //start the profile activity
                            finish();
                            final FirebaseDatabase database=FirebaseDatabase.getInstance();
                            mUse= FirebaseAuth.getInstance().getCurrentUser();
                            mChat1=database.getReference("DISPLAY_NAMES").child(mUse.getUid());

                            database.getReference("CONTACT_LIST").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    mChat1.removeValue();
                                    for(DataSnapshot topsnap:dataSnapshot.getChildren()){
                                        final UserContentPojo userpojo=topsnap.getValue(UserContentPojo.class);
                                        if(!(userpojo.getId().toString().equals(""+mUse.getUid()))) {
                                           // GenerateKeys ky = new GenerateKeys();
                                           // StringBuilder sb = new StringBuilder();
                                           /* try {
                                                FileInputStream fis = getApplicationContext().openFileInput("key1.txt");
                                                InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
                                                BufferedReader bufferedReader = new BufferedReader(isr);
                                                String line;
                                                while (fis.available()>0) {
                                                    char ch = (char)fis.read();
                                                    if(ch=='1' || ch=='0')
                                                    sb.append(""+ch);
                                                }
                                            } catch (Exception e) {

                                            }
                                            String sur = String.format("%8s",sb).replace(" ", "0");
                                            int priv_num = Integer.parseInt(sur);

                                            int sender_pub_key= userpojo.getKey();
                                            int priv_key = ky.getKey(""+sender_pub_key,priv_num);
                                            String my_priv_key = Integer.toString(priv_key);
                                            key.priv_key = my_priv_key;
                                            Toast.makeText(getApplicationContext(),"Hi!All", Toast.LENGTH_LONG).show();
                                            */
                                            ChatListItemPojo user_details = new ChatListItemPojo(userpojo.getName().toString(),userpojo.getId().toString(),userpojo.getEmail().toString());
                                            mChat1.child(userpojo.getId().toString()).setValue(user_details);

                                        }
                                        }

                                        }


                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                    }
                });

    }

    @Override
    public void onClick(View view) {
        if(view == buttonSignIn){
            userLogin();
        }

        if(view == textViewSignup){
            finish();
            startActivity(new Intent(this, SignUpActivity.class));
        }
    }


}