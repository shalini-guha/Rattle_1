package com.zlalini.rattle.Home_Screen;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zlalini.rattle.ChatScreen.ChatActivity;
import com.zlalini.rattle.Login.SignInActivity;
import com.zlalini.rattle.Login.SignUpActivity;
import com.zlalini.rattle.Login.UserContentPojo;
import com.zlalini.rattle.R;
import com.zlalini.rattle.RASS_Utility.GenerateKeys;
import com.zlalini.rattle.RecyclerItemClickListener;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;

import static com.zlalini.rattle.RASS_Utility.GenerateKeys.gen;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private FirebaseRecyclerAdapter<ChatListItemPojo,ChatListItemHolder> mAdapter;
    private FirebaseUser mUse;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference mChat2,mChat3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        database=FirebaseDatabase.getInstance();
        mUse= FirebaseAuth.getInstance().getCurrentUser();
        //Setting up the recyclerview and accordingly passing the data to the recyclerview by means of an adapter
        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view_home);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        prepareEntryData();
        mRecyclerView.setAdapter(mAdapter);


        mRecyclerView.addOnItemTouchListener(   //triggered when a recyclerview item(consisting of usernames) is clicked
                new RecyclerItemClickListener(getApplicationContext(),new RecyclerItemClickListener.OnItemClickListener(){
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click
                        ChatListItemPojo selectedList = mAdapter.getItem(position);

                        if (selectedList != null) {
                            String listId = mAdapter.getRef(position).getKey();//Gets the user's unique userId whose user name is clicked

                                Intent intent = new Intent(MainActivity.this, ChatActivity.class);

                            intent.putExtra("KEY_ACTIVE_LIST", listId); //Sends the UserId to the ChatActivity through an Intent
                            startActivity(intent);
                        }
                    }
                })
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_listing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        firebaseAuth = FirebaseAuth.getInstance();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            firebaseAuth.signOut();
            //closing activity
            finish();
            //starting login activity
            startActivity(new Intent(this, SignUpActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     *  Passes all the list items(user names) to the ChatListItemHolder class(Holder class)
     */
    void prepareEntryData() {
        final DatabaseReference chat=database.getReference("DISPLAY_NAMES").child(mUse.getUid()); // Creates a reference to the DSIPLAY_NAMES node present in the database
        chat.keepSynced(true);

        mAdapter = new FirebaseRecyclerAdapter<ChatListItemPojo, ChatListItemHolder>(ChatListItemPojo.class, R.layout.items_home, ChatListItemHolder.class, chat) {
            @Override
            public void populateViewHolder(ChatListItemHolder chatMessageViewHolder, ChatListItemPojo chatMessage, int position) {
                chatMessageViewHolder.setName(chatMessage.getName());

            }
        };
    }
}