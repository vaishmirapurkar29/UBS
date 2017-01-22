package app.com.example.android.UBaS;

/**
 * Created by MandipSilwal on 11/27/16.
 */

public class LendItemInfo {

    public String description;
    public String category;
    public String terms;
    public String image;
    public String contactEmail;


    public LendItemInfo() {

    }
    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }
    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public void setTerms() {
        this.terms = terms;
    }

    public String getTerms() {
        return terms;
    }

    public String getImage() {
        return image;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
