package com.daarulhijrah.kitabkuning.Utilities;

public class Config {

    public static String API_URL = "https://get.daarulhijrah.com/api.php/";

    //  Nama Kitab Kuning
    public static String TABEL_NAMA_KITAB = "jawahirul_kalamiyah";

    public static String TABEL_TOKO = "toko_mitra";
//    public static String TABEL_NAMA_KITAB = "test";
    public static String TABEL_PROMOSI = "PromotionSlide";
    public static String TABEL_CHANNEL_YOUTUBE = "ChannelYoutube";

    public static String JSON_FORMAT = "?transform=1";

    public static String ORDER_KITAB_ASC = "&order=id,asc";
    public static String ORDER_TOKO_DESC = "&order=id_toko,desc";
    public static String ORDER_SLIDE_DESC = "&order=id_img,desc";
    public static String FILTER_CHANNELYOUTUBE = "&filter=nama_db,sw,";
    public static String STATUS = "&filter=status,sw,1";


//    public static String TEST_DEVICE = "71E90D594D288DBD92FC81E2BDD60A68";
    public static String TEST_DEVICE = "CACB2292D0B26334CE22941B0B081FA0"; // XIAOMI NOTE 8

}
