package app.debata.com.debata;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import app.debata.com.debata.Messages.Messages;

/**
 * Display the user's profile with their name, profile picture, and username.
 *
 * @author Nelaven Subaskaran
 * @since 1.0
 */
public class Profile extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    // Create the navigation buttons
    private ImageView GameButton, MessagesButton, SettingsButton;

    private ImageView Signout;
    private TextView Name, Username;
    private ImageView Prof_Pic;
    private GoogleApiClient googleApiClient;
    private GoogleSignInClient mGoogleSignInClient;

    AdView mAdview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_profile);

        // Initialize all of my buttons
        GameButton = findViewById(R.id.Game_Button);
        MessagesButton = findViewById(R.id.Messages_Button);
        SettingsButton = findViewById(R.id.Settings_Button);

        GameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGameButtonClick();
            }
        });

        MessagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMessagesButtonClick();
            }
        });

        SettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSettingsButtonClick();
            }
        });

        Signout = findViewById(R.id.Logout_Button);
        Name = findViewById(R.id.fullName);
        Username = findViewById(R.id.username);
        Prof_Pic = findViewById(R.id.profileIcon);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Go through the valid providers to see which one was used to sign in
        // Find out the user's name, username, and profile image
        for (UserInfo userAccount : FirebaseAuth.getInstance().getCurrentUser().getProviderData()) {
            if (userAccount.getProviderId().equals("facebook.com")) {
                if (FirebaseAuth.getInstance() != null) {
                    String currentUId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    FirebaseDatabase.getInstance().getReference().child("users").child(currentUId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                if (postSnapshot.getKey().equals("name")) {
                                    String discussion = postSnapshot.getValue(String.class);
                                    Name.setText(discussion);
                                }
                                if (postSnapshot.getKey().equals("username")) {
                                    String discussion = postSnapshot.getValue(String.class);
                                    Username.setText(discussion);
                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    Glide.with(this).load(userAccount.getPhotoUrl().toString() + "?type=large").into(Prof_Pic);
                    break;
                }
                break;
            }
            if (userAccount.getProviderId().equals("google.com")) {
                if (FirebaseAuth.getInstance() != null) {
                    String currentUId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    FirebaseDatabase.getInstance().getReference().child("users").child(currentUId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                if (postSnapshot.getKey().equals("name")) {
                                    String discussion = postSnapshot.getValue(String.class);
                                    Name.setText(discussion);
                                }
                                if (postSnapshot.getKey().equals("username")) {
                                    String discussion = postSnapshot.getValue(String.class);
                                    Username.setText(discussion);
                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    Uri photoUrl = userAccount.getPhotoUrl();
                    String originalPieceOfUrl = "s96-c/photo.jpg";
                    String newPieceOfUrlToAdd = "s400-c/photo.jpg";
                    String photoPath = photoUrl.toString();
                    String newString = photoPath.replace(originalPieceOfUrl, newPieceOfUrlToAdd);
                    Glide.with(this).load(newString).into(Prof_Pic);
                    break;
                }
                break;
            }
        }

        Signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                onSignOutButtonClick(v);
            }
        });
    }

    public void onGameButtonClick() {
        Intent intent = new Intent(this, Game.class);
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

    public void onSettingsButtonClick() {
        Intent intent = new Intent(this, Settings.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
        overridePendingTransition(0, 0);
    }

    private void onSignOutButtonClick(View v) {
        if (v.getId() == R.id.Logout_Button) {
            mGoogleSignInClient.signOut().addOnCompleteListener(this,
                    new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                                @Override
                                public void onResult(@NonNull Status status) {
                                    goToMainMenu();
                                }
                            });
                        }
                    });
        }
    }

    public void goToMainMenu() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
