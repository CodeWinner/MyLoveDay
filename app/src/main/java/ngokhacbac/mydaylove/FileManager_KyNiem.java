package ngokhacbac.mydaylove;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by pc1 on 11/2/2017.
 */

public class FileManager_KyNiem {
    Context context;
    public static String Key_List_Mess = "Key_List_Mess";
    public static String Key_List_Day = "Key_List_Day";
    public static String Key_List_Path = "Key_List_Path";

    SharedPreferences store;
    SharedPreferences.Editor editor;

    public FileManager_KyNiem(Context context) {
        this.context = context;
        this.store = context.getSharedPreferences("MY_DAY_LOVE_KY_NIEM", context.MODE_PRIVATE);
    }

    public boolean saveData(Model_KyNiem model_kyNiem) {
        editor = store.edit();
        Log.i("SHOW", model_kyNiem.toStringListMess() + " - " + model_kyNiem.toStringListDay() + " - " + model_kyNiem.toStringListPath());
        editor.putString(Key_List_Mess, model_kyNiem.toStringListMess());
        editor.putString(Key_List_Day, model_kyNiem.toStringListDay());
        editor.putString(Key_List_Path, model_kyNiem.toStringListPath());

        editor.commit();
        return true;
    }

    public Model_KyNiem readData() {
        Model_KyNiem model_kyNiem = null;
        if (store != null) {
            Log.i("SHOW", "store not null");
            String Mess = store.getString(Key_List_Mess, "");
            String Day = store.getString(Key_List_Day, "");
            String Path = store.getString(Key_List_Path, "");
            ArrayList<String> listMess = customMess(Mess);
            ArrayList<String> listPath = customMess(Path);
            ArrayList<String> listDay = customMess(Day);
            model_kyNiem = new Model_KyNiem(listMess, listPath, listDay);
            Log.i("SHOW", "model_kyNiem : "+model_kyNiem.getListMess().toString()+" - "+model_kyNiem.getListDay().toString()+" - "+model_kyNiem.getListPathAnh().toString());
        } else {
            Log.i("SHOW", "store null");
            ArrayList<String> listMess = new ArrayList<>();
            ArrayList<String> listPath = new ArrayList<>();
            ArrayList<String> listDay = new ArrayList<>();
            model_kyNiem = new Model_KyNiem(listMess, listPath, listDay);
            saveData(model_kyNiem);
        }
        return model_kyNiem;
    }

    public void XoaStore() {
        editor.clear();
        editor.commit();

    }

    /*
  * Hàm cover từ chuyuooir thàng mãng
  * */
    public ArrayList<String> customMess(String mess) {
        Log.i("TEXT1111", mess);
        ArrayList<String> messses = new ArrayList<>();
        int start = 0;
        int end = 0;

        for (int i = 0; i < mess.length(); i++) {

            if (mess.charAt(i) == ',') {
                Log.i("TEXT1111", ",");
                end = i;
                String temp = mess.substring(start, end);
                Log.i("TEXT1111", "temp : " + temp);
                messses.add(temp);
                start = end + 1;
            }
            if (i == mess.length() - 1) {
                if (mess.charAt(mess.length() - 1) == ',') {
                    Log.i("TEXT1111", "leng " + (mess.length() - 1));
                    break;
                } else {
                    String temp = mess.substring(start, mess.length());
                    Log.i("TEXT1111", "temp : " + temp);
                    messses.add(temp);
                }
            }


        }
        return messses;
    }
}
