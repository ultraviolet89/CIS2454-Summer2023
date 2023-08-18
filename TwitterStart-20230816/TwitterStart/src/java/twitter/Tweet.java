package twitter;

import java.sql.Timestamp;
import java.util.Base64;

/**
 *
 * @author CONTROL
 */
public class Tweet {
    private int id;
    private String text;
    private Timestamp timestamp;
    private int user_id;
    private String user_name;
    private String filename;
    private byte[] image;
    private int likesCount;

    public Tweet(int id, String text, Timestamp timestamp, int user_id) {
        this.id = id;
        this.text = text;
        this.timestamp = timestamp;
        this.user_id = user_id;
    }
    
    /**
     * An Overloaded Constructor to Tweet Class, multiple constructors with different parameter counts.
     * Beneficial if there are multiple places where you create a Tweet and don't always have all the information immediately available.
     */
    public Tweet(int id, String text, Timestamp timestamp, int user_id, String user_name) {
        this(id, text, timestamp, user_id);
        this.user_name = user_name;
    }
    
    // Extended constructor to include filename and image
    public Tweet(int id, String text, Timestamp timestamp, int user_id, String user_name, String filename, byte[] image) {
        this(id, text, timestamp, user_id, user_name);
        this.filename = filename;
        this.image = image;
    }
    
    public int getId() { return id; }

    public String getText() { return text; }

    public Timestamp getTimestamp() { return timestamp; }

    public int getUser_id() { return user_id; }
    
    public String get_user_name(){ return user_name; }
    
    public String getFilename() { return filename; }

    // Method to get image data in base64 format for embedding in HTML
    public String getBase64Image() {
        if (image != null && image.length > 0) {
            String mimeType = "image/jpeg"; // default MIME type
            if (filename.toLowerCase().endsWith(".png")) {
                mimeType = "image/png";
            }
            return "data:" + mimeType + ";base64," + Base64.getEncoder().encodeToString(this.image);
        }
        return null;
    }
    
    // Getter for byte[] image directly
    public byte[] getImage() {
        return image;
    }
    
    public int getLikesCount() {
        return likesCount;
    }
    
    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }
}
