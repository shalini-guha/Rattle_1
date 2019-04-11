package com.zlalini.rattle.RASS_Utility;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zlalini.rattle.ChatKeyPojo;

import java.util.Random;

public class GenerateKeys {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference mRef1;
    private static int g=9,p=23;
    public int  power(int  a,int  b,int P)
    {
        if (b == 1)
            return a;

        else
            return (((int)Math.pow(a, b)) % P);
    }

    public static String gen()
    {
        String key="";int len=8;
        Random rand = new Random();
        int max =15,min=1;
        int i = rand.nextInt(max - min + 1) + min;
//       /* for(int i=0;i<len;i++)
//        {
//            key+=(Math.random()>0.5)?1:0;   //0 or 1 stored
//        }*/
        return String.format("%8s",Integer.toBinaryString(i)).replace(" ", "0");  //Returns 8 bit Binary key String
    }

    //Driver program
    public int getKey(String key)
    {
        int x;
//        database=FirebaseDatabase.getInstance();
//        // Both the persons will be agreed upon the
//        // public keys G and P
//        mRef1=database.getReference("KEYS");
//        mRef1.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                ChatKeyPojo ch = dataSnapshot.getValue(ChatKeyPojo.class);
//                p=(Integer.parseInt(ch.getP().trim()));
//                g=(Integer.parseInt(ch.getg().trim()));
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        int key_val = Integer.parseInt(key,2);
        Log.e("U",""+key_val);
        x = power(g, key_val, p);
        Log.e("V",""+x);
        return x;
    }
    public int getKey(String key,int priv_num)
    {
        int x;
//        database=FirebaseDatabase.getInstance();
//        // Both the persons will be agreed upon the
//        // public keys G and P
//        mRef1=database.getReference("KEYS");
//        mRef1.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                ChatKeyPojo ch = dataSnapshot.getValue(ChatKeyPojo.class);
//                p=(Integer.parseInt(ch.getP().trim()));
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        int key_val = Integer.parseInt(key);
        x = power(key_val, priv_num, p);
        return x;
    }
}
