package app.debata.com.debata;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Information about the user that will be stored in the Firebase Realtime Database.
 *
 * @author Nelaven Subaskaran
 * @since 1.0
 */
@IgnoreExtraProperties
public class User {
    public String name;
    public String email;
    public String profileImgURL;
    public String providerId;

    public String key;
    public String value;

    public String username;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String key) {
        this.key = key;
    }

    public User(String user, String username) {
        this.username = username;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    // This constructor contains all the information we need to display the user in the messages screen
    public User(String name, String email, String profileImgURL, String providerId) {
        this.name = name;
        this.email = email;
        this.profileImgURL = profileImgURL;
        this.providerId = providerId;
    }
}
