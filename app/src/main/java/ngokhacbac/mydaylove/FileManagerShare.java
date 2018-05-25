package ngokhacbac.mydaylove;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by pc1 on 11/2/2017.
 */

public class FileManagerShare {
    /*
      this.day_start = day_start;
        this.music_link = music_link;
        this.music_name = music_name;
        this.image_link_user = image_link_user;
        this.name_user = name_user;
        this.image_link_lover = image_link_lover;
        this.name_lover = name_lover;
        this.on_off_music = on_off_music;
        this.mess = mess;
    */
    Context context;
    public static String Key_Day_Start = "Key_Day_Start";
    public static String Key_Music_Link = "Key_Music_Link";
    public static String Key_Music_Name = "Key_Music_Name";
    public static String Key_Link_Image_User = "Key_Link_Image_User";
    public static String Key_USER_NAME = "Key_USER_NAME";
    public static String Key_Link_Image_Lover = "Key_Link_Image_Lover";
    public static String Key_Lover_Name = "Key_Lover_Name";
    public static String Key_On_Off_Music = "Key_On_Off_Music";
    public static String Key_Mess_Mac_Dinh = "Key_Mess_Mac_Dinh";
    public static String Key_Mess = "Key_Mess";

    SharedPreferences store;


    public FileManagerShare(Context context) {
        this.context = context;
        this.store =  context.getSharedPreferences("MY_DAY_LOVE", context.MODE_PRIVATE);
    }

    public boolean saveData(Model_Json model_json) {
        SharedPreferences.Editor editor = store.edit();
        editor.putString(Key_Day_Start, model_json.getDay_start());
        editor.putString(Key_Music_Link, model_json.getMusic_link());
        editor.putString(Key_Music_Name, model_json.getMusic_name());
        editor.putString(Key_Link_Image_User, model_json.getImage_link_user());
        editor.putString(Key_USER_NAME, model_json.getName_user());
        editor.putString(Key_Link_Image_Lover, model_json.getImage_link_lover());
        editor.putString(Key_Lover_Name, model_json.getName_lover());
        editor.putInt(Key_On_Off_Music, model_json.getOn_off_music());
        editor.putString(Key_Mess, model_json.getMess());
        editor.putString(Key_Mess_Mac_Dinh,model_json.getMessMacDinh());
        editor.commit();
        return true;
    }

    public Model_Json readData() {
        Model_Json model_json = null;
        if(store!=null) {
            Log.i("SHOW","store not null");
            String day_start = store.getString(Key_Day_Start, "");
            String music_link = store.getString(Key_Music_Link, "");
            String music_name = store.getString(Key_Music_Name, "");
            String image_link_user = store.getString(Key_Link_Image_User, "");
            String name_user = store.getString(Key_USER_NAME, "");
            String image_link_lover = store.getString(Key_Link_Image_Lover, "");
            String name_lover = store.getString(Key_Lover_Name, "");
            int on_off_music = store.getInt(Key_On_Off_Music, 0);
            String messMacDinh = store.getString(Key_Mess_Mac_Dinh, "");
            String mess = store.getString(Key_Mess, "");
            model_json = new Model_Json(day_start, music_link, music_name, image_link_user, name_user, image_link_lover, name_lover, on_off_music, mess,messMacDinh);
        }
        else {
            Log.i("SHOW","store null");
            String day_start ="";
            String music_link = "";
            String music_name ="";
            String image_link_user = "";
            String name_user = "";
            String image_link_lover = "";
            String name_lover = "";
            int on_off_music = 0;
            String mess = "";
            String messMacDinh =  context.getString(R.string.mess_macding_code);
            Log.i("SHOW","ttt : "+ messMacDinh);
            model_json = new Model_Json(day_start, music_link, music_name, image_link_user, name_user, image_link_lover, name_lover, on_off_music, mess,messMacDinh);
            saveData(model_json);
        }
        return model_json;
    }
}
