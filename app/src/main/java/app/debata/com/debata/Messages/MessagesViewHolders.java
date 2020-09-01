package app.debata.com.debata.Messages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import app.debata.com.debata.Chats.Chat;
import app.debata.com.debata.R;

/**
 * Display the matched user's profile picture, username, and the debate discussion.
 *
 * @author Nelaven Subaskaran
 * @since 1.0
 */
public class MessagesViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView mMatchId, mMatchDebate, mMatchUnique;
    public ImageView mMatchImage;

    public MessagesViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        mMatchId = itemView.findViewById(R.id.userID);
        mMatchDebate = itemView.findViewById(R.id.debateCard);
        mMatchUnique = itemView.findViewById(R.id.userUniqueID);
        mMatchImage = itemView.findViewById(R.id.MatchImage);
    }

    // Send the opposite user's information to the chat class
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(view.getContext(), Chat.class);
        Bundle b = new Bundle();
        b.putString("matchId", mMatchId.getText().toString());
        b.putString("debateDisc", mMatchDebate.getText().toString());
        b.putString("uniqueID", mMatchUnique.getText().toString());
        intent.putExtras(b);
        view.getContext().startActivity(intent);
    }
}
