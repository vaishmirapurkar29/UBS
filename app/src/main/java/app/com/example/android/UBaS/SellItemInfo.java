package app.com.example.android.UBaS;

/**
 * Created by MandipSilwal on 11/26/16.
 */

public class SellItemInfo {

    public String description;
    public String category;
    public String price;
    public String image;
    public String contactEmail;


    public SellItemInfo() {

    }

    public SellItemInfo(String description, String category, String price, String image, String contactEmail) {
        this.description = description;
        this.category = category;
        this.price = price;
        this.image = image;
        this.contactEmail = contactEmail;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

}
