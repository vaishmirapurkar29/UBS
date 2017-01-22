package app.com.example.android.UBaS;

/**
 * Created by MandipSilwal on 11/29/16.
 */

public class ClubPostInfo {

    public String fullname;
    public String post;

    public ClubPostInfo() {

    }

    public ClubPostInfo(String fullname, String post) {
        this.fullname = fullname;
        this.post = post;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }
}
