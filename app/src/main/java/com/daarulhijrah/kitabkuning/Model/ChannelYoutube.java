package com.daarulhijrah.kitabkuning.Model;

public class ChannelYoutube {

    String namaToko, imgToko, urlToko, asalToko;

    public ChannelYoutube(String namaToko, String imgToko, String urlToko, String asalToko) {
        this.namaToko = namaToko;
        this.imgToko = imgToko;
        this.urlToko = urlToko;
        this.asalToko = asalToko;
    }

    public String getNamaToko() {
        return namaToko;
    }

    public void setNamaToko(String namaToko) {
        this.namaToko = namaToko;
    }

    public String getImgToko() {
        return imgToko;
    }

    public void setImgToko(String imgToko) {
        this.imgToko = imgToko;
    }

    public String getUrlToko() {
        return urlToko;
    }

    public void setUrlToko(String urlToko) {
        this.urlToko = urlToko;
    }

    public String getAsalToko() {
        return asalToko;
    }

    public void setAsalToko(String asalToko) {
        this.asalToko = asalToko;
    }
}
