package ngokhacbac.mydaylove;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ScrollView;
import android.widget.TextView;

import ngokhacbac.mydaylove.Adapter.Adapter_ListImage;
import ngokhacbac.mydaylove.Adapter.Adapter_ListMusic;
import ngokhacbac.mydaylove.Adapter.GetLinkFromDialog;
import com.squareup.picasso.Picasso;


import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.jar.*;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Main2Activity extends AppCompatActivity implements GetLinkFromDialog, TextWatcher {
    ImageButton imbBack, imbOnOffMusic;
    Button btnMess, btnSave;
    Model_Json model_json;
    TextView txtTenBaiHat, txtYourFace, txtShowMess, txtShowMess2;
    Dialog dialogMusic, dialogImage, dialogMess;
    ImageView imgShowUser, imgShowLover;
    TextInputLayout tipStartDay, tipNameUser, tipNameLover, tipMessMacDinh;

    String pathImage_User = "";

    String pathImage_Lover = "";
    String pathMusic = "";
    String music_name = "";
    //   CardView ="cvChonNhac,cvChonImageUser,cvChonImageLover;

    int countCharecter = 0;
    TextView txtCountCharecter;
    TextView txtCanhBao;
    MultiAutoCompleteTextView edtMess;
    EditText edtStartDay, edtNameUser, edtNameLove, edtMessMacDinh;
    int onOffMusic = 0;
    String mess = "";
    String messMacDinh = "";
    boolean isInput = true; // trên 33 ký tu = false ko đc nhập

    ScrollView showMess;

    FileManagerShare fileManagerShare;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }

        setContentView(R.layout.activity_main2);
        imbBack = (ImageButton) findViewById(R.id.imbBack);
        imbOnOffMusic = (ImageButton) findViewById(R.id.imbOnOffMusic);
        txtTenBaiHat = (TextView) findViewById(R.id.txtTenBaiHat);
        showMess = (ScrollView) findViewById(R.id.showMess);
        tipMessMacDinh = (TextInputLayout) findViewById(R.id.tipMessMacDinh);
        edtMessMacDinh = tipMessMacDinh.getEditText();
        tipStartDay = (TextInputLayout) findViewById(R.id.tipStartDay);
        edtStartDay = tipStartDay.getEditText();
        tipNameUser = (TextInputLayout) findViewById(R.id.tipNameUser);
        edtNameUser = tipNameUser.getEditText();
        tipNameLover = (TextInputLayout) findViewById(R.id.tipNameLover);
        edtNameLove = tipNameLover.getEditText();

        txtYourFace = (TextView) findViewById(R.id.txtYourFace);
        txtShowMess = (TextView) findViewById(R.id.txtShowMess);
        //     cvChonNhac = (CardView) findViewById(R.id.cvChonBaiHat);
        //     cvChonImageUser = (CardView) findViewById(R.id.cvChonBaiImageUser);
        //     cvChonImageLover = (CardView) findViewById(R.id.cvChonBaiImageLover);
        imgShowUser = (ImageView) findViewById(R.id.showYourImage);
        imgShowLover = (ImageView) findViewById(R.id.showYourLover);
        btnMess = (Button) findViewById(R.id.btnMess);
        btnSave = (Button) findViewById(R.id.btnSave);

          MainActivity.mInterstitialAd.show();

        fileManagerShare = new FileManagerShare(this);
        /*
        * gán dữ liệu đang có
        * */
        model_json = MainActivity.model_json_static;
        txtTenBaiHat.setText(model_json.getMusic_name());
        if (model_json.getOn_off_music() == 0) {

            imbOnOffMusic.setImageResource(R.drawable.tat);
        } else {
            imbOnOffMusic.setImageResource(R.drawable.bat);
        }

        pathMusic = model_json.getMusic_link();

        // ảnh user
        Picasso.with(this).load(new File(model_json.getImage_link_user()))

                .fit().centerCrop()
                .into(imgShowUser);
        pathImage_User = model_json.getImage_link_user();
        // ảnh lover
        Picasso.with(this).load(new File(model_json.getImage_link_lover()))

                .fit().centerCrop()
                .into(imgShowLover);
        pathImage_Lover = model_json.getImage_link_lover();

        edtNameUser.setText(model_json.getName_user());
        edtNameLove.setText(model_json.getName_lover());
        edtStartDay.setText(model_json.getDay_start());


        mess = model_json.getMess().toString();


        if (model_json.getMess().equals("")) {
            showMess.setVisibility(View.GONE);
        } else {
            txtShowMess.setText(toStringMess(customMess(mess)));
            showMess.setVisibility(View.VISIBLE);
        }
        messMacDinh = model_json.getMessMacDinh().toString();
        edtMessMacDinh.setText(messMacDinh);
    }

    /*
    * cover từ array string sang chuổi
    * */
    public String toStringMess(ArrayList<String> list) {
        String mess = "";
        for (int i = 0; i < list.size(); i++) {
            mess = mess + list.get(i) + ",\n";
        }
        return mess;
    }

    // check ung time ko
    public int isChuanNgay(String startDate) {
        // true là đúng , false là sai
        try {

            Date date1;
            Date date2;

            SimpleDateFormat dates = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat dates_day = new SimpleDateFormat("dd");
            SimpleDateFormat dates_mouth = new SimpleDateFormat("MM");
            SimpleDateFormat dates_year = new SimpleDateFormat("yyyy");
            Date date = new Date();
            // hiện tại
            String date1_temp = dates.format(Calendar.getInstance().getTime());
            date1 = dates.parse(date1_temp);

            int dateNow = Integer.parseInt(dates_day.format(date));
            int mouthNow = Integer.parseInt(dates_mouth.format(date));
            int yearNow = Integer.parseInt(dates_year.format(date));
            Log.d("TIME111", "day1 : " + dateNow + " - " + mouthNow + "-" + yearNow);
//bat đầu

            date2 = dates.parse(startDate);

            int dateStart = Integer.parseInt(dates_day.format(date2));
            int mouthStart = Integer.parseInt(dates_mouth.format(date2));
            int yearStart = Integer.parseInt(dates_year.format(date2));
            Log.d("TIME111", "day2 : " + date1.toString() + "-" + date2.toString() + "-" + yearStart + "  " + date1.compareTo(date2));
            if (date1.compareTo(date2) == -1) {
                return 0;
            }

        } catch (ParseException e) {
            e.printStackTrace();
            new SweetAlertDialog(Main2Activity.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText(this.getString(R.string.dialog_canhbao_chuan_code)).show();

            return -1;
        }
        return 1;
    }

    public void Click(View v) {
        switch (v.getId()) {
            case R.id.imbBack:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.imbOnOffMusic:
                if (model_json.getOn_off_music() == 0) {

                    onOffMusic = 1;
                    imbOnOffMusic.setImageResource(R.drawable.bat);
                    break;
                } else {
                    onOffMusic = 0;
                    imbOnOffMusic.setImageResource(R.drawable.tat);

                }
                break;
            case R.id.txtTenBaiHat:
                // String path = Environment.getExternalStorageDirectory().getAbsolutePath();
             //   String path = Environment.getExternalStorageDirectory()+"";
             //   Log.i("PATH", path);
                //     Toast.makeText(this, "Click" + getPlayList(path).get(0).get("file_name"), Toast.LENGTH_SHORT).show();
                music_name = txtTenBaiHat.getText().toString();
                showAlertDialog(getMusicList(), txtTenBaiHat);
                break;
            case R.id.txtYourFace:
                //        String path_image = Environment.getExternalStorageDirectory() + "/DCIM/Camera/";
                //     Log.i("PATH", String.valueOf(getImageList().size()));
                showAlertDialog_Image(getImageList(), imgShowUser, 0);
                break;
            case R.id.txtLoverFace:
                //        String path_image = Environment.getExternalStorageDirectory() + "/DCIM/Camera/";
                //   Log.i("PATH", String.valueOf(getImageList().size()));
                showAlertDialog_Image(getImageList(), imgShowLover, 1);
                break;
            case R.id.btnMess:
                showAlertMess(txtShowMess);
                break;
            case R.id.btnSave:
                //String day_start, String music_link, String image_link_user, String name_user, String image_link_lover, String name_lover, int on_off_music, ArrayList<String> mess
                Log.i("TEXT1111", customMess(mess).toString());
                int isChuuanNgay = isChuanNgay(edtStartDay.getText().toString());
                if (isChuuanNgay == 0) {
                    new SweetAlertDialog(Main2Activity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText(this.getString(R.string.dialog_canhbao_saingay_code)).show();
                } else {
                    if (isChuuanNgay == 1) {
                        Model_Json model_json = new Model_Json(
                                edtStartDay.getText().toString(),
                                pathMusic,
                                music_name,
                                pathImage_User,
                                edtNameUser.getText().toString(),
                                pathImage_Lover,
                                edtNameLove.getText().toString(),
                                onOffMusic,
                                mess,
                                edtMessMacDinh.getText().toString()
                        );
                        if (fileManagerShare.saveData(model_json) == true) {
                            // show khi dang ky thanh cong
                            new SweetAlertDialog(Main2Activity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText(this.getString(R.string.dialog_thanhcong_code))
                                    .setContentText("You clicked the button!")
                                    .show();
                        }
                    }

                }
                 /*
                *  this.day_start = day_start;
                 this.music_link = music_link;
              this.image_link_user = image_link_user;
              this.name_user = name_user;
             this.image_link_lover = image_link_lover;
              this.name_lover = name_lover;
                 this.on_off_music = on_off_music;
         this.mess = mess;
            * */


                break;
        }
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

    // dialog mess
    public String showAlertMess(final TextView textView) {
        // custom dialog
        dialogMess = new Dialog(this);
        dialogMess.setContentView(R.layout.dialog_mess);
        // làm mất backgrou mac định của dialog
        dialogMess.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // set the custom dialog components - text, image and
        txtCanhBao = (TextView) dialogMess.findViewById(R.id.txtWarring);
        txtCountCharecter = (TextView) dialogMess.findViewById(R.id.txtCountCharecter);

        edtMess = (MultiAutoCompleteTextView) dialogMess.findViewById(R.id.edtMess);
        edtMess.addTextChangedListener(this);

        Button btnOKMess = (Button) dialogMess.findViewById(R.id.btnOKMess);
        Button btnHuyMess = (Button) dialogMess.findViewById(R.id.btnHuyMess);

        btnHuyMess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtMess.setText("");
            }
        });

        btnOKMess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                countCharecter = 0;
                mess = edtMess.getText().toString();
                Log.i("SIZE_TEXT", customMess(mess).size() + "");
                if (customMess(mess).size() > 0) {
                    Log.i("SIZE_TEXT", customMess(mess).size() + "  rrr");
                    showMess.setVisibility(View.VISIBLE);
                    txtShowMess.setText(toStringMess(customMess(mess)));
                } else {
                    Log.i("SIZE_TEXT", customMess(mess).size() + "  rrr1");
                    showMess.setVisibility(View.GONE);
                }

                dialogMess.dismiss();
            }
        });
        dialogMess.show();
        return "";
    }
   // check permisstion

    // dialog music
    public String showAlertDialog(ArrayList<HashMap<String, String>> arrayList, TextView textView) {
        // custom dialog
        dialogMusic = new Dialog(this);
        dialogMusic.setContentView(R.layout.dialog_list_music);
        // làm mất backgrou mac định của dialog
        dialogMusic.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // set the custom dialog components - text, image and
        ListView listView = (ListView) dialogMusic.findViewById(R.id.listMusic);
        Log.i("SHOW", arrayList.size() + " dd");
        if (arrayList.size() == 0) {
            new SweetAlertDialog(Main2Activity.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText(this.getString(R.string.dialog_canhbao_mp3_code)).show();
        } else {
            Adapter_ListMusic adapter_listMusic = new Adapter_ListMusic(arrayList, this, dialogMusic, textView);
            adapter_listMusic.getLinkListenner(this);
            listView.setAdapter(adapter_listMusic);
            dialogMusic.show();
        }

        return "";
    }

    // dialog image
    public String showAlertDialog_Image(ArrayList<String> arrayList, ImageView imageView, int userOrLover) {
        // custom dialog
        dialogImage = new Dialog(this);
        dialogImage.setContentView(R.layout.dialog_image);
        // làm mất backgrou mac định của dialog
        dialogImage.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        // set the custom dialog components - text, image and
        GridView gridView = (GridView) dialogImage.findViewById(R.id.gridImage);
        Adapter_ListImage adapter_listImage = new Adapter_ListImage(arrayList, this, dialogImage, imageView, userOrLover);
        adapter_listImage.getLinkImageListenner(this);
        gridView.setAdapter(adapter_listImage);
        dialogImage.show();
        return "";
    }
/*
    // get list mp3 file
    ArrayList<HashMap<String, String>> getPlayList(String rootPath) {
        ArrayList<HashMap<String, String>> fileList = new ArrayList<>();


        try {
            File rootFolder = new File(rootPath);
            File[] files = rootFolder.listFiles(); //here you will get NPE if directory doesn't contains  any file,handle it like this.
            for (File file : files) {
                if (file.isDirectory()) {
                    if (getPlayList(file.getAbsolutePath()) != null) {
                        fileList.addAll(getPlayList(file.getAbsolutePath()));
                    } else {
                        break;
                    }
                } else if (file.getName().endsWith(".mp3")) {
                    HashMap<String, String> song = new HashMap<>();
                    song.put("file_path", file.getAbsolutePath());
                    song.put("file_name", file.getName());
                    fileList.add(song);
                }
            }
            return fileList;
        } catch (Exception e) {
            return null;
        }
    }
*/
    // lấy list ảnh

    // lấy list ảnh
    public ArrayList<String> getImageList() {


        final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
        final String orderBy = MediaStore.Images.Media._ID;
        //Stores all the images from the gallery in Cursor
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy);
        //Total number of images
        int count = cursor.getCount();

        //Create an array to store path to all the images
        ArrayList<String> arrPath = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            cursor.moveToPosition(i);
            int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            //Store the path of the image
            arrPath.add(cursor.getString(dataColumnIndex));

            Log.i("PATH", arrPath.get(i));
        }
        return arrPath;
    }

    // lấy list ảnh
    public  ArrayList<HashMap<String, String>> getMusicList() {
        ArrayList<HashMap<String, String>> fileList = new ArrayList<>();
        final String[] columns = {MediaStore.Audio.Media.DATA, MediaStore.Audio.Media._ID};
        final String orderBy = MediaStore.Audio.Media._ID;

        //Stores all the images from the gallery in Cursor
        Cursor cursor = getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy);


        //Total number of images
        int count = cursor.getCount();

        //Create an array to store path to all the images
        ArrayList<String> arrPath = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            cursor.moveToPosition(i);
            int dataColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            //Store the path of the image
         //   arrPath.add(cursor.getString(dataColumnIndex));
            HashMap<String, String> song = new HashMap<>();
            song.put("file_path", cursor.getString(dataColumnIndex));
            String name = cursor.getString(dataColumnIndex);
            song.put("file_name",name.substring(positon_kyTu(name)+1,name.length()-1));
            fileList.add(song);
//            Log.i("PATH", arrPath.get(i));
        }
        return fileList;
    }
    // lấy vị trí '/' cuối cùng
    public int positon_kyTu(String s)
    {
        int position = 0;
        for(int i=s.length()-1;i>=0;i--)
        {
            if(s.charAt(i)=='/')
            {
                position = i;
                break;
            }
        }
        return position;
    }
    @Override
    public void linkImage(String link, int uerOrLover) {
        if (uerOrLover == 0) // user
        {
            pathImage_User = link;
        } else {
            pathImage_Lover = link;
        }
    }

    @Override
    public void linkMusic(String linkMusic, String name) {
        pathMusic = linkMusic;
        music_name = name;
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    /*
    * // Các đoạn text ngăn cách nhau bởi dấu phẩy.
          textProgrammingLanguage.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
    * */

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        Log.i("COUT", countCharecter + "" + " s222  : " + s);
        countCharecter = s.length();
        txtCountCharecter.setText(countCharecter + "");


    }

   // int permissionCheck = ContextCompat.checkSelfPermission(thisActivity,
  //          android.Manifest.permission.WRITE_CALENDAR);
    @Override
    public void afterTextChanged(Editable s) {

    }
}
