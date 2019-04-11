package com.zlalini.rattle.ChatScreen;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zlalini.rattle.R;

import me.himanshusoni.chatmessageview.ChatMessageView;

/**
 * Holder class for the chats(chat list with two fields-message(to be displayed) and owner(for checking purpose))
 */

public class ChatHolder extends RecyclerView.ViewHolder {
    private View mView;
    private Context mContext;
    private ChatMessageView mChatMessageView,mChatMessageView_Image;
    private FirebaseUser mUser;

    public ChatHolder(View itemView) {
        super(itemView);
        mView = itemView;
    }

    /**
     * Used for displaying the messages and the images along with the text for  file_type text and image respectively
     * @param message
     */
    public void setMessage(String message) {
            TextView field = (TextView) mView.findViewById(R.id.message);
            field.setText(" "+message+" ");


    }

    public void setOwner(final String userId) {
        TextView field = (TextView) mView.findViewById(R.id.message);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mContext = mView.getContext();
        mChatMessageView = (ChatMessageView) mView.findViewById(R.id.chatmessage_view);
        mChatMessageView_Image = (ChatMessageView) mView.findViewById(R.id.chatmessage_viewimage);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("Previous_chatOwner", userId);//Used for storing the sender of the previous chat_text
        editor.apply();

      /*  ViewGroup.LayoutParams params = mChatMessageView.getLayoutParams();
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        mChatMessageView.setLayoutParams(params);
        if (mUser.getUid().equalsIgnoreCase(userId)) {
            mChatMessageView.setBackgroundColor(Color.MAGENTA);
            mChatMessageView_Image.setBackgroundColor(Color.MAGENTA);
            mChatMessageView.setGravity(Gravity.END);
        } else {
            mChatMessageView.setBackgroundColor(Color.RED);
            mChatMessageView_Image.setBackgroundColor(Color.RED);
            mChatMessageView.setGravity(Gravity.START);
        }*/
        if (mUser.getUid().equalsIgnoreCase(userId)) {
            mChatMessageView.setGravity(Gravity.END);
        }
        else
            mChatMessageView.setGravity(Gravity.START);
    }

}
