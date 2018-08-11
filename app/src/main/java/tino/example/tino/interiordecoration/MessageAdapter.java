package tino.example.tino.interiordecoration;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<ChatMessage> {
    public MessageAdapter(Context context, int resource, List<ChatMessage> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.message_item, parent, false);
        }

        ImageView photoImageView =  convertView.findViewById(R.id.message_imageview);
        TextView messageTextView =  convertView.findViewById(R.id.message_content_textview);
        TextView authorTextView = convertView.findViewById(R.id.message_name_textview);
        TextView timeTextView = convertView.findViewById(R.id.time_textview);

        final ChatMessage message = getItem(position);

        boolean isPhoto = message.getPhotoUrl() != null;
        if (isPhoto) {
            messageTextView.setVisibility(View.GONE);
            photoImageView.setVisibility(View.VISIBLE);
            Glide.with(photoImageView.getContext())
                    .load(message.getPhotoUrl())
                    .into(photoImageView);
            photoImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), EnlargedImageActivity.class);
                    intent.putExtra("5", message.getPhotoUrl());
                    view.getContext().startActivity(intent);
                }
            });
        } else {
            messageTextView.setVisibility(View.VISIBLE);
            photoImageView.setVisibility(View.GONE);
            messageTextView.setText(message.getMessageContent());
        }
        authorTextView.setText(message.getName());
        timeTextView.setText(message.getTimeOfPost());

        return convertView;
    }
}
