package app.debata.com.debata.Messages;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import app.debata.com.debata.R;

/**
 * Get the new matched users.
 *
 * @author Nelaven Subaskaran
 * @since 1.0
 */
public class MessagesAdapter extends RecyclerView.Adapter<MessagesViewHolders> {
    private List<UsernameObject> usersList;
    private List<DebataObject> debataLists;
    private List<ImgObject> imgLists;
    private List<UniqueIDObject> uniqueLists;
    private Context context;

    public MessagesAdapter(List<UsernameObject> usersList, List<DebataObject> debataLists, List<ImgObject> imgLists, List<UniqueIDObject> uniqueLists, Context context) {
        this.usersList = usersList;
        this.imgLists = imgLists;
        this.debataLists = debataLists;
        this.uniqueLists = uniqueLists;
        this.context = context;
    }

    @Override
    public MessagesViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_matches, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        MessagesViewHolders rcv = new MessagesViewHolders((layoutView));
        return rcv;
    }

    @Override
    public void onBindViewHolder(MessagesViewHolders holder, int position) {
        holder.mMatchId.setText(usersList.get(position).getUsername());
        Glide.with(context).load(imgLists.get(position).getImg()).into(holder.mMatchImage);
        holder.mMatchDebate.setText(debataLists.get(position).getDebate());
        holder.mMatchUnique.setText(uniqueLists.get(position).getUniqueID());
    }

    @Override
    public int getItemCount() {
        return this.usersList.size();
    }
}
