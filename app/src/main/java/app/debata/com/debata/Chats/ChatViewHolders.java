package app.debata.com.debata.Chats;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import app.debata.com.debata.R;

/**
 * ImgObject will contain the profile image url.
 *
 * @author Nelaven Subaskaran
 * @since 1.0
 */
public class ChatViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView mMessage;
    public LinearLayout mContainer;

    public ChatViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        // message is the actual text
        mMessage = itemView.findViewById(R.id.chat_message);
        // container is the background of the message
        mContainer = itemView.findViewById(R.id.container);
    }

    @Override
    public void onClick(View view) {

    }
}
