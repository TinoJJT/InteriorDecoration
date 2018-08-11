package tino.example.tino.interiordecoration;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RequestAdapter extends ArrayAdapter<DecorationRequest> {
    public RequestAdapter(Context context, int resource, List<DecorationRequest> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.browse_list_item, parent, false);
        }

        TextView nameTextView = convertView.findViewById(R.id.browse_name);
        TextView roomsTextView = convertView.findViewById(R.id.rooms_number);
        TextView budgetTextView = convertView.findViewById(R.id.budget_number);

        DecorationRequest request = getItem(position);

        nameTextView.setText(request.getName());
        roomsTextView.setText(request.getRooms());
        budgetTextView.setText(request.getBudget());

        return convertView;
    }
}
