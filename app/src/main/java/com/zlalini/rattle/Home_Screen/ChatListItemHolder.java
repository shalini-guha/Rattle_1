package com.zlalini.rattle.Home_Screen;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.zlalini.rattle.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Holder class for the chatlistitems (user list with only one field-names)
 */


public class ChatListItemHolder extends RecyclerView.ViewHolder {
    private View mView;
    public ChatListItemHolder(View itemView) {
        super(itemView);

        mView = itemView;
    }
    public void setName(String name) {
        TextView field = (TextView) mView.findViewById(R.id.name);
        field.setText(name);

    }


}