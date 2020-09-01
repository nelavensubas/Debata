package app.debata.com.debata.Debates;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import app.debata.com.debata.R;

/**
 * Store the debate cards in an array.
 *
 * @author Nelaven Subaskaran
 * @since 1.0
 */
public class arrayAdapter extends android.widget.ArrayAdapter<Cards> {
    public arrayAdapter(Context context, int resourceId, List<Cards> items) {
        super(context, resourceId, items);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Cards card_item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.questions, parent, false);
        }

        TextView name = convertView.findViewById(R.id.cards);
        name.setText(card_item.getName());

        return convertView;
    }
}
