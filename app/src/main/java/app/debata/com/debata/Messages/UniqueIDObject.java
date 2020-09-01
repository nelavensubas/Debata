package app.debata.com.debata.Messages;

/**
 * UniqueIDObject will contain the opposite user's id.
 *
 * @author Nelaven Subaskaran
 * @since 1.0
 */
public class UniqueIDObject {
    private String uniqueID;

    public UniqueIDObject(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }
}
