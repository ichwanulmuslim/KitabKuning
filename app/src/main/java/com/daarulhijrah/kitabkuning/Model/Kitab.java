package com.daarulhijrah.kitabkuning.Model;

public class Kitab {

    int idTable;
    int id;
    String judulIndonesia;
    String judulArab;
    String isiIndonesia;
    String isiArab;
    String urlGambar;
    String urlAudio;
    int favorite;
    int recent;

    public Kitab(int idTable, int id, String judulArab, String judulIndonesia, String isiArab, String isiIndonesia, String urlGambar, String urlAudio, int favorite, int recent) {
        this.idTable = idTable;
        this.id = id;
        this.judulIndonesia = judulIndonesia;
        this.judulArab = judulArab;
        this.isiIndonesia = isiIndonesia;
        this.isiArab = isiArab;
        this.urlGambar = urlGambar;
        this.urlAudio = urlAudio;
        this.favorite = favorite;
        this.recent = recent;
    }

    public Kitab() {
    }

    public int getIdTable() {
        return idTable;
    }

    public void setIdTable(int idTable) {
        this.idTable = idTable;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJudulIndonesia() {
        return judulIndonesia;
    }

    public void setJudulIndonesia(String judulIndonesia) {
        this.judulIndonesia = judulIndonesia;
    }

    public String getJudulArab() {
        return judulArab;
    }

    public void setJudulArab(String judulArab) {
        this.judulArab = judulArab;
    }

    public String getIsiIndonesia() {
        return isiIndonesia;
    }

    public void setIsiIndonesia(String isiIndonesia) {
        this.isiIndonesia = isiIndonesia;
    }

    public String getIsiArab() {
        return isiArab;
    }

    public void setIsiArab(String isiArab) {
        this.isiArab = isiArab;
    }

    public String getUrlGambar() {
        return urlGambar;
    }

    public void setUrlGambar(String urlGambar) {
        this.urlGambar = urlGambar;
    }

    public String getUrlAudio() {
        return urlAudio;
    }

    public void setUrlAudio(String urlAudio) {
        this.urlAudio = urlAudio;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public int getRecent() {
        return recent;
    }

    public void setRecent(int recent) {
        this.recent = recent;
    }
}
