package app.debata.com.debata;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * User can change their full name or username through the settings screen.
 *
 * @author Nelaven Subaskaran
 * @since 1.0
 */
public class Settings extends AppCompatActivity {

    private EditText mNameField, mUsernameField;
    private TextView mEmailField;
    private Button mBack, mConfirm;
    private ImageView mProfileImage;
    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;
    private String userId, name, username, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mNameField = findViewById(R.id.settingsName);
        mUsernameField = findViewById(R.id.settingsUsername);
        mEmailField = findViewById(R.id.settingsEmail);
        mProfileImage = findViewById(R.id.settingsProfilePicture);
        mBack = findViewById(R.id.settingsBack);
        mConfirm = findViewById(R.id.settingsConfirm);

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

        getUserInfo();

        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInformation();
            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, Profile.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);
            }
        });
    }

    private void getUserInfo() {
        for (UserInfo userAccount : FirebaseAuth.getInstance().getCurrentUser().getProviderData()) {
            if (userAccount.getProviderId().equals("facebook.com")) {
                if (FirebaseAuth.getInstance() != null) {
                    Glide.with(this).load(userAccount.getPhotoUrl().toString() + "?type=large").into(mProfileImage);
                    break;
                }
                break;
            }
            if (userAccount.getProviderId().equals("google.com")) {
                if (FirebaseAuth.getInstance() != null) {
                    Uri photoUrl = userAccount.getPhotoUrl();
                    String originalPieceOfUrl = "s96-c/photo.jpg";
                    String newPieceOfUrlToAdd = "s400-c/photo.jpg";
                    String photoPath = photoUrl.toString();
                    String newString = photoPath.replace(originalPieceOfUrl, newPieceOfUrlToAdd);
                    Glide.with(this).load(newString).into(mProfileImage);
                    break;
                }
                break;
            }
        }

        // Go through user's children to get the right information
        mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("name") != null) {
                        name = map.get("name").toString();
                        mNameField.setText(name);
                    }
                    if (map.get("username") != null) {
                        username = map.get("username").toString();
                        mUsernameField.setText(username);
                    }
                    if (map.get("email") != null) {
                        email = map.get("email").toString();
                        mEmailField.setText(email);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // Update the user's profile with their new information
    private void saveUserInformation() {
        name = mNameField.getText().toString();
        username = mUsernameField.getText().toString();

        Map userInfo = new HashMap();
        userInfo.put("name", name);
        userInfo.put("username", username);
        mUserDatabase.updateChildren(userInfo);

        Intent intent = new Intent(Settings.this, Profile.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
        overridePendingTransition(0, 0);
    }
}
