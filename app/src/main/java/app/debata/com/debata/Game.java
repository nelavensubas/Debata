package app.debata.com.debata;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import app.debata.com.debata.Debates.Cards;
import app.debata.com.debata.Debates.arrayAdapter;
import app.debata.com.debata.Messages.Messages;

/**
 * User can agree or disagree with the cards by swiping them.
 *
 * @author Nelaven Subaskaran
 * @since 1.0
 */
public class Game extends AppCompatActivity {

    // Create the components that I need to use
    private ImageView ProfileButton, MessagesButton, noDebates;

    private Cards cards_data[];
    private app.debata.com.debata.Debates.arrayAdapter arrayAdapter;
    List<Cards> rowItems; // Contains the debates

    private DatabaseReference debatesChildren = FirebaseDatabase.getInstance().getReference().child("debates");
    private DatabaseReference usersDb = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("debates");
    private DatabaseReference mDatabase;

    private DatabaseReference userConnection = FirebaseDatabase.getInstance().getReference().child("users");

    private DatabaseReference answers = FirebaseDatabase.getInstance().getReference().child("answers");
    private DatabaseReference matches = FirebaseDatabase.getInstance().getReference().child("answers");

    private FirebaseAuth mAuth;

    private String currentUId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mAuth = FirebaseAuth.getInstance();
        currentUId = mAuth.getCurrentUser().getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        // Initialize all of my components
        ProfileButton = findViewById(R.id.Profile_Button);
        MessagesButton = findViewById(R.id.Messages_Button);
        noDebates = findViewById(R.id.no_debates);

        ProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onProfileButtonClick();
            }
        });

        MessagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMessagesButtonClick();
            }
        });

        for (final UserInfo userAccount : FirebaseAuth.getInstance().getCurrentUser().getProviderData()) {
            if (userAccount.getProviderId().equals("facebook.com")) {
                if (FirebaseAuth.getInstance() != null) {
                    String[] getFirst = userAccount.getDisplayName().split(" ");
                    final String firstName = getFirst[0].toLowerCase();
                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.hasChild(currentUId)) {
                                User user = new User(userAccount.getDisplayName(), userAccount.getEmail(), userAccount.getPhotoUrl().toString(), "facebook.com");
                                mDatabase.child(currentUId).setValue(user);
                                mDatabase.child(currentUId).child("username").setValue(firstName);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    break;
                }
                break;
            }
            if (userAccount.getProviderId().equals("google.com")) {
                if (FirebaseAuth.getInstance() != null) {
                    String[] getFirst = userAccount.getDisplayName().split(" ");
                    Random rand = new Random();
                    int num = rand.nextInt(9000000) + 1000000;
                    final String firstName = getFirst[0].toLowerCase() + Integer.toString(num);
                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.hasChild(currentUId)) {
                                User user = new User(userAccount.getDisplayName(), userAccount.getEmail(), userAccount.getPhotoUrl().toString(), "google.com");
                                mDatabase.child(currentUId).setValue(user);
                                mDatabase.child(currentUId).child("username").setValue(firstName);
                                //mDatabase.child(currentUId).child("notifications").setValue("true");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    break;
                }
                break;
            }
        }

        rowItems = new ArrayList<Cards>();
        arrayAdapter = new arrayAdapter(this, R.layout.questions, rowItems);
        addCards();

        SwipeFlingAdapterView flingContainer = findViewById(R.id.frame);

        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                Cards obj = (Cards) dataObject;
                final String userId = obj.getUserId();
                usersDb.child(userId).setValue("disagree");
                answers.child(userId).child(currentUId).setValue("disagree");
                matches.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            String opposite = postSnapshot.getValue(String.class);
                            if (opposite.equals("agree")) {
                                String key = FirebaseDatabase.getInstance().getReference().child("chat").push().getKey();
                                String uniqueID = UUID.randomUUID().toString();
                                FirebaseDatabase.getInstance().getReference().child("users").child(postSnapshot.getKey()).child("match").child(currentUId).child(userId).setValue(uniqueID);
                                FirebaseDatabase.getInstance().getReference().child("users").child(currentUId).child("match").child(postSnapshot.getKey()).child(userId).setValue(uniqueID);
                                Toast.makeText(Game.this, "New Match!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Cards obj = (Cards) dataObject;
                final String userId = obj.getUserId();
                usersDb.child(userId).setValue("agree");
                answers.child(userId).child(currentUId).setValue("agree");
                matches.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            String opposite = postSnapshot.getValue(String.class);
                            if (opposite.equals("disagree")) {
                                String key = FirebaseDatabase.getInstance().getReference().child("chat").push().getKey();
                                String uniqueID = UUID.randomUUID().toString();
                                FirebaseDatabase.getInstance().getReference().child("users").child(postSnapshot.getKey()).child("match").child(currentUId).child(userId).setValue(uniqueID);
                                FirebaseDatabase.getInstance().getReference().child("users").child(currentUId).child("match").child(postSnapshot.getKey()).child(userId).setValue(uniqueID);
                                Toast.makeText(Game.this, "New Match!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {

            }

            @Override
            public void onScroll(float scrollProgressPercent) {
            }
        });

        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {

            }
        });
    }

    public void check(final String key, final Cards newItem) {
        usersDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild(key)) {
                    rowItems.add(newItem);
                    arrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addCards() {
        debatesChildren.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String discussion = postSnapshot.getValue(String.class);
                    Cards item = new Cards(postSnapshot.getKey(), discussion);
                    check(postSnapshot.getKey(), item);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void onProfileButtonClick() {
        Intent intent = new Intent(this, Profile.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
        overridePendingTransition(0, 0);
    }

    public void onMessagesButtonClick() {
        Intent intent = new Intent(this, Messages.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
        overridePendingTransition(0, 0);
    }
}
