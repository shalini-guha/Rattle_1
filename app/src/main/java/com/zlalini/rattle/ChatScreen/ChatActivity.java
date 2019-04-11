package com.zlalini.rattle.ChatScreen;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.zlalini.rattle.Home_Screen.ChatListItemPojo;
import com.zlalini.rattle.Login.UserContentPojo;
import com.zlalini.rattle.R;
import com.zlalini.rattle.RASS_Utility.GenerateKeys;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;


public class ChatActivity extends AppCompatActivity {
    private String mListId;
    private TextView mUsername, mUserText;
    private RecyclerView mRecyclerView;
    private FirebaseRecyclerAdapter<ChatPojo, ChatHolder> mAdapter;
    private LinearLayoutManager mLayoutManager;
    private ImageView  mSend;
    private FirebaseDatabase mDatabase;
    private FirebaseUser mUse ;
    private DatabaseReference mRef,mRef1,mRef2,mRef3,mChat2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
        mUsername = (TextView) findViewById(R.id.chat_name);
        mUserText = (EditText) findViewById(R.id.chat_talk);
        mSend = (ImageView) findViewById(R.id.send_button);

        Intent intent = this.getIntent();
        mListId = intent.getStringExtra("KEY_ACTIVE_LIST");//Receives the userId through the Intent of the clicked username
        mDatabase = FirebaseDatabase.getInstance();
        mUse = FirebaseAuth.getInstance().getCurrentUser();
        mRef = mDatabase.getReference("DISPLAY_NAMES").child(mUse.getUid()).child(mListId);//Creates a reference to the CHAT_LIST node present in the database
        // Creates a reference to the CHAT_DETAILS node present in the database
        mRef1 = mDatabase.getReference("CHAT_DETAILS");
        mRef2 = mRef1.child(mUse.getUid().toString()).child(mListId);
        mRef3 = mRef1.child(mListId).child(mUse.getUid().toString());
        // Call method to handle click listeners
        handleClickListeners();
    }

    private void handleClickListeners() {
        //The ValueEvent Listener here is attached to get the username in the chatpage
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ChatListItemPojo userPojo = dataSnapshot.getValue(ChatListItemPojo.class);
                mUsername.setText(userPojo.getName());
                prepareEntryData();
                mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_chat);
                mLayoutManager = new LinearLayoutManager(getApplicationContext());
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(mAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if (mSend != null) {
            //The on clickListener is attached to implement the functionality of the mSend button
            mSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ((mUserText.getText().toString()).trim().length() == 0) {
                    } else {
                        final String t=mUserText.getText().toString().trim();
                        mChat2=mDatabase.getReference("CONTACT_LIST").child(mListId);
                        mChat2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                UserContentPojo userpojo= dataSnapshot.getValue(UserContentPojo.class);
                                StringBuilder sb = new StringBuilder();
                                try {
                                    FileInputStream fis = getApplicationContext().openFileInput("key1.txt");
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
                                GenerateKeys ky = new GenerateKeys();
                                int sender_pub_key= userpojo.getKey();
                                int priv_key = ky.getKey(""+sender_pub_key,priv_num);
                                String my_priv_key = Integer.toString(priv_key);
                                Log.e("TAGG",my_priv_key);
                                Toast.makeText(getApplicationContext(),"ii   "+my_priv_key, Toast.LENGTH_LONG).show();
                                try {
                                    StorageReference storageRef= FirebaseStorage.getInstance().getReference();
                                   // InputStream stream = new FileInputStream(new File("path/to/images/rivers.jpg"));
                                    StorageReference islandRef = storageRef.child("lena.png");

                                    final long ONE_MEGABYTE = 1024 * 1024;
                                    islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                        @Override
                                        public void onSuccess(byte[] bytes) {
                                            // Data for "images/island.jpg" is returns, use this as needed
//                                            Glide.with(getApplicationContext())
//                                            InputStream in = new ByteArrayInputStream(imageInByte);
//                                            BufferedImage bImageFromConvert = ImageIO.read(in);
                                          Bitmap bmp = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                                          int [] pixels = new int[bmp.getHeight()*bmp.getWidth()];
//                                          bmp.getPixels(pixels,0,bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());
//                                            for(int i=0;i<50;i++){
//                                                Log.e("TYY",""+pixels[i]);
////
//                                            }
                                       int k=0;
//                                          for(int i=0;i<220;i++){
//                                             for(int j=0;j<220;j++){
//                                                  pixels[k++]=bmp.getPixel(i,j);
//                                                  if(k>)
//                                                  Log.e("TYY",""+pixels[k]);
//                                              }
//                                          }
                                            for(int i=0;i<220;i++){
                                                for(int j=0;j<220;j++){
                                                    int pix = bmp.getPixel(i,j);
                                                    int R = Color.red(pix);
                                                    int G = Color.green(pix);
                                                    int B = Color.blue(pix);
                                                    int gray = (R+B+G)/3;
                                                    pixels[k++]=gray;

                                                }
                                            }
                                            int arr[]=encoding(pixels,220,220,t);

                                       }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            // Handle any errors
                                        }
                                    });
                                }
                                catch(Exception e){

                                }
                                //The data is pushed twice-once in the sender's list and once in the receiver's list
                                ChatPojo user_details = new ChatPojo(mUserText.getText().toString().trim(), mUse.getUid());
                                mRef2.push().setValue(user_details);
                                mRef3.push().setValue(user_details);

                                mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_chat);
                                mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                mRecyclerView.setLayoutManager(mLayoutManager);
                                mAdapter.notifyDataSetChanged();
                                mUserText.setText("");

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                }
            });
        }


    }



    /**
     * Passes all the list items(chatmessage and owner as userId) to the ChatListItemHolder class(Holder class)
     */
    public int[] encoding(int ar[],int height,int width,String message){
        int ar2[]=new int[height*width];
        String bin="";
        String k="";
        int c=message.length()*8;
        int count=0;
        String ar1[]=new String[height*width];
        //Used for converting the decimal pixel values into a 8 bit binary number
        for(int i=0;i<height*width;i++){
            bin="00000000"+(Integer.toBinaryString(ar[i]));
            ar1[i]=bin.substring(bin.length()-8,bin.length());
        }
        // Used for obtaining the converting the message into a 8 bit binary number
        for(int i=0;i<message.length();i++){
            char ch=message.charAt(i);
            bin="00000000"+(Integer.toBinaryString((int)(ch)));
            k+=bin.substring(bin.length()-8,bin.length());
        }
        //Used for storing the length in the first pixel
        bin="00000000"+(Integer.toBinaryString(message.length()));
        ar1[0]=bin.substring(bin.length()-8,bin.length());
        // Replacing the last two LSB bits with the binary digits obtained from the inputted name
        for(int i=1;i<height*width;i++){
            if(count<c){
                ar1[i]=""+ar1[i].substring(0,6)+Integer.parseInt(""+k.charAt(count))+Integer.parseInt(""+k.charAt(count+1));
                count+=2;
            }
        }
        //Obtaining back the pixels
        for(int i=0;i<height*width;i++){
            ar2[i]=Integer.parseInt(ar1[i],2);
        }
        return ar2;
    }
    void prepareEntryData() {
        mRef2.keepSynced(true);
        mAdapter = new FirebaseRecyclerAdapter<ChatPojo, ChatHolder>(ChatPojo.class, R.layout.chat_message, ChatHolder.class, mRef2) {
            @Override
            public void populateViewHolder(ChatHolder chatMessageViewHolder, ChatPojo chatMessage, int position) {
                chatMessageViewHolder.setMessage(chatMessage.getMessage());
                chatMessageViewHolder.setOwner(chatMessage.getName());
            }
        };
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
