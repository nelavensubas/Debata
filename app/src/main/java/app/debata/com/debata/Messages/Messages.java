package app.debata.com.debata.Messages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import app.debata.com.debata.Game;
import app.debata.com.debata.Profile;
import app.debata.com.debata.R;

/**
 * Display a list of user messages.
 *
 * @author Nelaven Subaskaran
 * @since 1.0
 */
public class Messages extends AppCompatActivity {

    // Create the buttons to navigate the app
    private ImageView ProfileButton, GameButton;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mMessagesAdapter;
    private RecyclerView.LayoutManager mMessagesLayoutManager;

    private String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Initialize all of my buttons
        ProfileButton = findViewById(R.id.Profile_Button);
        GameButton = findViewById(R.id.Game_Button);

        // This block of code controls the messages that are displayed
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        mMessagesLayoutManager = new LinearLayoutManager(Messages.this);
        mRecyclerView.setLayoutManager(mMessagesLayoutManager);
        mMessagesAdapter = new MessagesAdapter(getDataUserMessages(), getDataDebataMessages(), getDataImgMessages(), getDataUniqueMessages(), Messages.this);
        mRecyclerView.setAdapter(mMessagesAdapter);

        getUserMatchId();

        ProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onProfileButtonClick();
            }
        });

        GameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGameButtonClick();
            }
        });
    }

    private void getUserMatchId() {
        DatabaseReference answered = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserID).child("debates");

        // Find the current user to chat with depending on the key
        answered.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot debateKey : dataSnapshot.getChildren()) {
                        String choice = debateKey.getValue(String.class);
                        FetchMatchInformation(debateKey.getKey(), choice);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void FetchMatchInformation(final String key, final String choice) {
        // Get a reference to the answers child with a certain debate key to find all the users that have answered
        DatabaseReference matchDb = FirebaseDatabase.getInstance().getReference().child("answers").child(key);

        // Go through the answers key to find a match
        matchDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        String opposite = postSnapshot.getValue(String.class);
                        if (opposite.equals("agree") && choice.equals("disagree")) {
                            DatabaseReference found = FirebaseDatabase.getInstance().getReference().child("users").child(postSnapshot.getKey());
                            String uniqueUserID = postSnapshot.getKey();
                            // Get the opposite user's id, this will not be displayed
                            // Only be used to communicate
                            UniqueIDObject objID = new UniqueIDObject(uniqueUserID);
                            uniqueMessages.add(objID);
                            mMessagesAdapter.notifyDataSetChanged();
                            found.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                        // Get their username, this will be displayed
                                        if (postSnapshot.getKey().equals("username")) {
                                            String username = postSnapshot.getValue(String.class);
                                            UsernameObject objUser = new UsernameObject(username);
                                            userMessages.add(objUser);
                                            mMessagesAdapter.notifyDataSetChanged();
                                        }
                                        // Get their profile image url to display their profile image
                                        if (postSnapshot.getKey().equals("profileImgURL")) {
                                            String photoUrl = postSnapshot.getValue(String.class);
                                            String profileImage = "";
                                            if (photoUrl.contains("facebook")) {
                                                profileImage = photoUrl + "?type=large";
                                            } else if (photoUrl.contains("google")) {
                                                String originalPieceOfUrl = "s96-c/photo.jpg";
                                                String newPieceOfUrlToAdd = "s400-c/photo.jpg";
                                                String newString = photoUrl.replace(originalPieceOfUrl, newPieceOfUrlToAdd);
                                                profileImage = newString;
                                            }
                                            ImgObject objImg = new ImgObject(profileImage);
                                            imgMessages.add(objImg);
                                            mMessagesAdapter.notifyDataSetChanged();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            FirebaseDatabase.getInstance().getReference().child("debates").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                        if (postSnapshot.getKey().equals(key)) {
                                            // Show a bit of the debate value to the user
                                            String discussion = postSnapshot.getValue(String.class);
                                            String[] words = discussion.split(" ");
                                            String display = "";
                                            for (int i = 0; i < words.length; i++) {
                                                display += words[i] + " ";
                                                if (i == 3) {
                                                    display += "...";
                                                    break;
                                                }
                                            }
                                            DebataObject objDeb = new DebataObject(display);
                                            debataMessages.add(objDeb);
                                            mMessagesAdapter.notifyDataSetChanged();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        } else if (opposite.equals("disagree") && choice.equals("agree")) {
                            DatabaseReference found = FirebaseDatabase.getInstance().getReference().child("users").child(postSnapshot.getKey());
                            String uniqueUserID = postSnapshot.getKey();
                            UniqueIDObject objID = new UniqueIDObject(uniqueUserID);
                            uniqueMessages.add(objID);
                            mMessagesAdapter.notifyDataSetChanged();
                            found.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                        if (postSnapshot.getKey().equals("username")) {
                                            String username = postSnapshot.getValue(String.class);
                                            UsernameObject objUser = new UsernameObject(username);
                                            userMessages.add(objUser);
                                            mMessagesAdapter.notifyDataSetChanged();
                                        }
                                        if (postSnapshot.getKey().equals("profileImgURL")) {
                                            String photoUrl = postSnapshot.getValue(String.class);
                                            String profileImage = "";
                                            if (photoUrl.contains("facebook")) {
                                                profileImage = photoUrl + "?type=large";
                                            } else if (photoUrl.contains("google")) {
                                                String originalPieceOfUrl = "s96-c/photo.jpg";
                                                String newPieceOfUrlToAdd = "s400-c/photo.jpg";
                                                String newString = photoUrl.replace(originalPieceOfUrl, newPieceOfUrlToAdd);
                                                profileImage = newString;
                                            }
                                            ImgObject objImg = new ImgObject(profileImage);
                                            imgMessages.add(objImg);
                                            mMessagesAdapter.notifyDataSetChanged();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            FirebaseDatabase.getInstance().getReference().child("debates").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                        if (postSnapshot.getKey().equals(key)) {
                                            String discussion = postSnapshot.getValue(String.class);
                                            String[] words = discussion.split(" ");
                                            String display = "";
                                            for (int i = 0; i < words.length; i++) {
                                                display += words[i] + " ";
                                                if (i == 3) {
                                                    display += "...";
                                                    break;
                                                }
                                            }
                                            DebataObject objDeb = new DebataObject(display);
                                            debataMessages.add(objDeb);
                                            mMessagesAdapter.notifyDataSetChanged();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private ArrayList<UsernameObject> userMessages = new ArrayList<UsernameObject>();

    private List<UsernameObject> getDataUserMessages() {
        return userMessages;
    }

    private ArrayList<DebataObject> debataMessages = new ArrayList<DebataObject>();

    private List<DebataObject> getDataDebataMessages() {
        return debataMessages;
    }

    private ArrayList<ImgObject> imgMessages = new ArrayList<ImgObject>();

    private List<ImgObject> getDataImgMessages() {
        return imgMessages;
    }

    private ArrayList<UniqueIDObject> uniqueMessages = new ArrayList<UniqueIDObject>();

    private List<UniqueIDObject> getDataUniqueMessages() {
        return uniqueMessages;
    }

    private void onProfileButtonClick() {
        Intent intent = new Intent(this, Profile.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
        overridePendingTransition(0, 0);
    }

    public void onGameButtonClick() {
        Intent intent = new Intent(getApplicationContext(), Game.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
        overridePendingTransition(0, 0);
    }
}
