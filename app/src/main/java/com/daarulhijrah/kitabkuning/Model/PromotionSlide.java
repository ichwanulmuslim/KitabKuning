package com.daarulhijrah.kitabkuning.Model;

/**
 * Created by Mac on 1/10/17.
 */

public class PromotionSlide {

    String pathPhoto, textPhoto;

    public PromotionSlide(String pathPhoto, String textPhoto) {
        this.pathPhoto = pathPhoto;
        this.textPhoto = textPhoto;
    }

    public PromotionSlide() {
    }

    public String getPathPhoto() {
        return pathPhoto;
    }

    public void setPathPhoto(String pathPhoto) {
        this.pathPhoto = pathPhoto;
    }

    public String getTextPhoto() {
        return textPhoto;
    }

    public void setTextPhoto(String textPhoto) {
        this.textPhoto = textPhoto;
    }
}
