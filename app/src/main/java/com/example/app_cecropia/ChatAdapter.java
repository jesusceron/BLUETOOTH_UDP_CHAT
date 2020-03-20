package com.example.app_cecropia;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import java.util.ArrayList;

public class ChatAdapter extends ArrayAdapter<MessageModel> implements View.OnClickListener {
    private ArrayList<MessageModel> messageSet;
    Context mContext;

    @Override
    public void onClick(View v) {}

    private static class ViewHolder {
        TextView txtMessage;
        CardView card;
    }

    public ChatAdapter(ArrayList<MessageModel> data, Context context) {
        super(context, R.layout.item_message, data);
        this.messageSet = data;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MessageModel dataModel = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_message, parent, false);

            viewHolder.txtMessage = (TextView) convertView.findViewById(R.id.message);
            viewHolder.card = (CardView) convertView.findViewById(R.id.card);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txtMessage.setText(dataModel.getMessage());
        viewHolder.card.setBackgroundColor(dataModel.getColor());
        // Return the completed view to render on screen
        return convertView;
    }
}
