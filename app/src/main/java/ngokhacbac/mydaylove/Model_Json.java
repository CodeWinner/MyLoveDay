package ngokhacbac.mydaylove;

import android.text.Editable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by pc1 on 10/31/2017.
 */
/*
* {
  "day_start":"",
  "music_link":"",
  "image_link_user":"",
  "name_user":"",
  "image_link_lover":"",
  "name_lover":"",
  "on_off_music":0,
  "mess":""
}
* */
public class Model_Json implements Serializable {
    private String day_start;
    private String music_link;
    private String music_name;
    private String image_link_user;
    private String name_user;
    private String image_link_lover;
    private String name_lover;
    private int on_off_music; // 0 là tắt , 1 là bật
    private String mess;
    private String messMacDinh;
    // danh cho lưu ky niem


    public Model_Json(String day_start, String music_link, String music_name, String image_link_user, String name_user, String image_link_lover, String name_lover, int on_off_music, String mess, String messMacDinh) {
        this.day_start = day_start;
        this.music_link = music_link;
        this.music_name = music_name;
        this.image_link_user = image_link_user;
        this.name_user = name_user;
        this.image_link_lover = image_link_lover;
        this.name_lover = name_lover;
        this.on_off_music = on_off_music;
        this.mess = mess;
        this.messMacDinh = messMacDinh;
    }
    public String getMessMacDinh() {
        return messMacDinh;
    }

    public void setMessMacDinh(String messMacDinh) {
        this.messMacDinh = messMacDinh;
    }




    public String getMusic_name() {
        return music_name;
    }

    public void setMusic_name(String music_name) {
        this.music_name = music_name;
    }

    public String getDay_start() {
        return day_start;
    }

    public void setDay_start(String day_start) {
        this.day_start = day_start;
    }

    public String getMusic_link() {
        return music_link;
    }

    public void setMusic_link(String music_link) {
        this.music_link = music_link;
    }

    public String getImage_link_user() {
        return image_link_user;
    }

    public void setImage_link_user(String image_link_user) {
        this.image_link_user = image_link_user;
    }

    public String getName_user() {
        return name_user;
    }

    public void setName_user(String name_user) {
        this.name_user = name_user;
    }

    public String getImage_link_lover() {
        return image_link_lover;
    }

    public void setImage_link_lover(String image_link_lover) {
        this.image_link_lover = image_link_lover;
    }

    public String getName_lover() {
        return name_lover;
    }

    public void setName_lover(String name_lover) {
        this.name_lover = name_lover;
    }

    public int getOn_off_music() {
        return on_off_music;
    }

    public void setOn_off_music(int on_off_music) {
        this.on_off_music = on_off_music;
    }

    public String getMess() {
        return mess;
    }

    public void setMess(String mess) {
        this.mess = mess;
    }


}
