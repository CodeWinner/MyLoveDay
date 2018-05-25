package ngokhacbac.mydaylove;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.hanks.htextview.base.HTextView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends BaseActivity {
    private HTextView hTextView;
    TextView txtShowDay, txtNameAnh, txtNameEm, txtStartDay, txtMinliTime, txtNgayTheoThang;
    MediaPlayer mPlayer;
    ImageView imgTraiTim;
    private SeekBar seekBar;
    public String testCalendar = "8/8/2017";

    public static long militime = 0;
    public static long giay = 0;
    public int numDay = 0;
    //4380976965
    CircleImageView profile_image, profile_image_ny;
    public static Model_Json model_json_static;
    public static Typeface custom_font;
    // quảng cáo
   public static InterstitialAd mInterstitialAd;

    Dialog dialog_back;
    public static AdRequest adRequest;
    //Analyssys firbase
    private FirebaseAnalytics mFirebaseAnalytics;
    private static GoogleAnalytics analytics;
    private static Tracker tracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        seekBar = (SeekBar) findViewById(R.id.seekbar);
        hTextView = (HTextView) findViewById(R.id.txtEvaporateText);
        txtNameAnh = (TextView) findViewById(R.id.textView2);
        txtNameEm = (TextView) findViewById(R.id.textView);
        txtNgayTheoThang = (TextView) findViewById(R.id.txtNgayTheoThang);
        txtShowDay = (TextView) findViewById(R.id.txtNgay);
        txtStartDay = (TextView) findViewById(R.id.textView3);
        txtMinliTime = (TextView) findViewById(R.id.textView4);
        imgTraiTim = (ImageView) findViewById(R.id.imgTraiTim);
        profile_image = (CircleImageView) findViewById(R.id.profile_image);
        profile_image_ny = (CircleImageView) findViewById(R.id.profile_image_ny);
        analytics = GoogleAnalytics.getInstance(this);

        // TODO: Replace the tracker-id with your app one from https://www.google.com/analytics/web/
        tracker = analytics.newTracker(getString(R.string.id_analys));

        // Provide unhandled exceptions reports. Do that first after creating the tracker
        tracker.enableExceptionReporting(true);

        // Enable Remarketing, Demographics & Interests reports
        // https://developers.google.com/analytics/devguides/collection/android/display-features
        tracker.enableAdvertisingIdCollection(true);

        // Enable automatic activity tracking for your app
        tracker.enableAutoActivityTracking(true);
        //Load ads

        adRequest = new AdRequest.Builder().build();
        //Nếu quảng cáo đã tắt tiến hành load quảng cáo

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.quang_cao_full));
        mInterstitialAd.setAdListener(new AdListener() {

            @Override
            public void onAdClosed() {
                loadInterstitialAd();
            }
        });
        //Load sẵn quảng cáo khi ứng dụng mở
        loadInterstitialAd();

        // gán data
        try {
            model_json_static = setData();
            Log.i("SHOW", "Show" + model_json_static.getMess() + "  " + model_json_static.getName_user());
        } catch (Exception e) {

        }
        //setAnh
        if (model_json_static.getImage_link_user().equals("") == false) {

            Picasso.with(this).load(new File(model_json_static.getImage_link_user()))

                    .fit().centerCrop()
                    .into(profile_image);

        }
        if (model_json_static.getImage_link_lover().equals("") == false) {

            Picasso.with(this).load(new File(model_json_static.getImage_link_lover()))

                    .fit().centerCrop()
                    .into(profile_image_ny);
        }
        custom_font = Typeface.createFromAsset(getAssets(), "fonts/scripti.ttf");
        hTextView.setTypeface(custom_font);
        txtNameAnh.setTypeface(custom_font);
        txtNameEm.setTypeface(custom_font);
        txtShowDay.setTypeface(custom_font);
        txtStartDay.setTypeface(custom_font);
        txtMinliTime.setTypeface(custom_font);

        txtNameAnh.setText(model_json_static.getName_user());
        txtNameEm.setText(model_json_static.getName_lover());
        hTextView.setText("Click Me !");


        hTextView.setOnClickListener(new ClickListener());
        hTextView.setAnimation(new SimpleAnimationListener(this));


        mPlayer = new MediaPlayer();
        Log.i("SHOW", "ON/OFF " + model_json_static.getOn_off_music() + " path " + model_json_static.getMusic_link());
        try {
            mPlayer.setDataSource(model_json_static.getMusic_link());
            mPlayer.prepare();

            if (model_json_static.getOn_off_music() == 0) {

                mPlayer.stop();
            } else {

                mPlayer.start();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        txtShowDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tracker.send(new HitBuilders.EventBuilder("Button Ky Niem", "ListKyNiem")
                        .setLabel("ListKyNiem")
                        .build());

                Intent intent = new Intent(MainActivity.this, ListKyNiem.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mPlayer.stop();
                startActivity(intent);
                finish();

            }
        });
        numDay();
        //4362432860
        countGiay();

    }

    //Load InterstitialAd
    private void loadInterstitialAd() {
        if (mInterstitialAd != null) {
            AdRequest adRequest = new AdRequest.Builder().build();

            mInterstitialAd.loadAd(adRequest);
        }

    }

    public void Click(View v) {
        switch (v.getId()) {
            case R.id.imgTraiTim: {
                tracker.send(new HitBuilders.EventBuilder("Button Setting", "Button Setting")
                        .setLabel("Button Setting")
                        .build());
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mPlayer.stop();
                startActivity(intent);
                finish();

            }
        }
    }

    // dialog image
    public String showAlertDialog_Back() {
        // custom dialog
        dialog_back = new Dialog(this);
        dialog_back.setContentView(R.layout.dialog_hoi_back);
        // làm mất backgrou mac định của dialog
        dialog_back.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
         AdView adView = (AdView) dialog_back.findViewById(R.id.adView_dialog);

        //   adView.loadAd(adRequest);
        Button btn_co = (Button) dialog_back.findViewById(R.id.btnCoDialog);
        Button btn_khong = (Button) dialog_back.findViewById(R.id.btnKhongDialog);

        btn_co.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent i = new Intent();
                i.putExtra("finish", true);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// To clean up all activities
                dialog_back.dismiss();
                finish();
            }
        });

        btn_khong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_back.dismiss();
            }
        });
        // set the custom dialog components - text, image and

        dialog_back.show();
        return "";
    }

    /**/
    int i = 0;

    public void countGiay() {
        CountDownTimer timer = new CountDownTimer(86400000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                giay++;
                if (giay % 86400 == 0) {
                    numDay++;
                    txtShowDay.setText(numDay + " day");
                }
                //     i++;
                txtMinliTime.setText(giay + " s");
                // Log.d("COUNT",i+"");
            }

            @Override
            public void onFinish() {
                countGiay();
            }
        }.start();
    }

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
    public Model_Json setData() {
        Model_Json model_json = null;
        FileManagerShare fileManagerShare = new FileManagerShare(this);
        model_json = fileManagerShare.readData();
        return model_json;
    }

    /*
    * Đọc file json
    * */
    /*
    public String readJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("Infor.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
*/
    public void numDay() {
        try {
            //Dates to compare

            String startDate = model_json_static.getDay_start();
            txtStartDay.setText(this.getString(R.string.ngay_bat_dau_code) + " " + model_json_static.getDay_start());
            Date date1;
            Date date2;

            SimpleDateFormat dates = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat dates_day = new SimpleDateFormat("dd");
            SimpleDateFormat dates_mouth = new SimpleDateFormat("MM");
            SimpleDateFormat dates_year = new SimpleDateFormat("yyyy");
            Date date = new Date();
            // hiện tại
            date1 = Calendar.getInstance().getTime();
            int dateNow = Integer.parseInt(dates_day.format(date));
            int mouthNow = Integer.parseInt(dates_mouth.format(date));
            int yearNow = Integer.parseInt(dates_year.format(date));
            Log.d("TIME111", "day1 : " + dateNow + " - " + mouthNow + "-" + yearNow);
//bat đầu
            date2 = dates.parse(startDate);
            int dateStart = Integer.parseInt(dates_day.format(date2));
            int mouthStart = Integer.parseInt(dates_mouth.format(date2));
            int yearStart = Integer.parseInt(dates_year.format(date2));
            Log.d("TIME111", "day2 : " + dateStart + "-" + mouthStart + "-" + yearStart);
            // hiển thị ngày theo tháng

            //Comparing dates
            long difference = Math.abs(date1.getTime() - date2.getTime());
            Log.i("TIME111", "TIME : " + difference);
            militime = difference;
            giay = difference / 1000;
            numDay = (int) (difference / (86400 * 1000));
            txtShowDay.setText(numDay + " day");

//4381452221 61452221
        } catch (Exception exception) {
            Log.e("DIDN'T WORK", "exception " + exception);
        }
    }

    @Override
    public void onBackPressed() {

        //  super.onBackPressed();
        showAlertDialog_Back();


    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("VD", "onResume");

        numDay();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("VD", "onStart");

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("VD", "onRestart");


    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("VD", "onPause");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayer.stop();
        Log.d("VD", "onDestroy");

    }


}
