package ngokhacbac.mydaylove;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import ngokhacbac.mydaylove.Adapter.AdapterListKyNiem;
import ngokhacbac.mydaylove.Adapter.Adapter_ListImage;
import ngokhacbac.mydaylove.Adapter.GetLinkFromDialog;
import ngokhacbac.mydaylove.Adapter.GetPosition;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListKyNiem extends AppCompatActivity implements GetLinkFromDialog, GetPosition {
    Model_KyNiem model_kyNiem;
    ListView listKyNiem_view;
    Button btnAddKyNiem;
    Dialog dialogMess, dialogImage;

    TextInputLayout tipNgayKyNiem, tipGhiChu;
    EditText edtNgayKyNiem, edtGhiChu;
    CircleImageView imageKyNiem;
    ImageButton imbBackKyNiem;
    String pathImage = "";
    //MediaPlayer mPlayer;
    private AdView mAdView;
    ;
    FileManager_KyNiem fileManager_kyNiem;
    MediaPlayer mPlayer;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_ky_niem);

        int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }


        mAdView = (AdView) findViewById(R.id.adView);
        //show qc
         MainActivity.mInterstitialAd.show();

        mAdView.loadAd(MainActivity.adRequest);

        listKyNiem_view = (ListView) findViewById(R.id.listKyNiem);
        btnAddKyNiem = (Button) findViewById(R.id.btnAddKyNiem);
        imbBackKyNiem = (ImageButton) findViewById(R.id.imbBackKyNiem);

        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(MainActivity.model_json_static.getMusic_link());
            mPlayer.prepare();

            if (MainActivity.model_json_static.getOn_off_music() == 0) {

                mPlayer.stop();
            } else {

                mPlayer.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fileManager_kyNiem = new FileManager_KyNiem(this);
            // fileManager_kyNiem.XoaStore();
            model_kyNiem = fileManager_kyNiem.readData();
            Log.i("KY_NIEM", "size : " + model_kyNiem.getListMess().size());

            SetAdapter();
        } catch (Exception e) {
            e.printStackTrace();
        }


        btnAddKyNiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertMess();
            }
        });
        imbBackKyNiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileManager_kyNiem.saveData(model_kyNiem);
                Intent intent = new Intent(ListKyNiem.this, MainActivity.class);
                mPlayer.stop();
                startActivity(intent);
                finish();
            }
        });
    }

    public void SetAdapter() {

        AdapterListKyNiem listKyNiem = new AdapterListKyNiem(model_kyNiem.getListPathAnh(),
                model_kyNiem.getListMess(),
                model_kyNiem.getListDay(), this);
        listKyNiem.getPositionListenner(this);
        listKyNiem_view.setAdapter(listKyNiem);
    }

    // dialog mess
    public String showAlertMess() {
        // custom dialog
        dialogMess = new Dialog(this);
        dialogMess.setContentView(R.layout.dialog_kyniem);
        // làm mất backgrou mac định của dialog
        dialogMess.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // set the custom dialog components - text, image and
        tipNgayKyNiem = (TextInputLayout) dialogMess.findViewById(R.id.tipNgayKyNiem);
        edtNgayKyNiem = tipNgayKyNiem.getEditText();
        tipGhiChu = (TextInputLayout) dialogMess.findViewById(R.id.tipGhiChu);
        edtGhiChu = tipGhiChu.getEditText();
        imageKyNiem = (CircleImageView) dialogMess.findViewById(R.id.imageKyNiem);
        Button btnOkKyNiem = (Button) dialogMess.findViewById(R.id.btnOK_KyNiem);
        btnOkKyNiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pathImage.equals("") == true) {
                    Toast.makeText(ListKyNiem.this, getString(R.string.dialog_chua_co_anh), Toast.LENGTH_SHORT).show();
                } else {
                    if (edtNgayKyNiem.getText().equals("")) {
                        Toast.makeText(ListKyNiem.this, getString(R.string.dialog_chua_co_ngay_ky_niem), Toast.LENGTH_SHORT).show();
                    } else {
                        String NgayKyNiem = edtNgayKyNiem.getText().toString();
                        String GhiChu = edtGhiChu.getText().toString();
                        if (GhiChu.contains(",") == false && NgayKyNiem.contains(",") == false) {
                            model_kyNiem.getListDay().add(NgayKyNiem);
                            model_kyNiem.getListMess().add(GhiChu);

                            model_kyNiem.getListPathAnh().add(pathImage);
                            SetAdapter();
                            dialogMess.dismiss();
                        } else {
                            Toast.makeText(ListKyNiem.this, getString(R.string.khong_chua), Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            }
        });

        imageKyNiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog_Image(getImageList(), imageKyNiem, 0);
            }
        });
        dialogMess.show();
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

    @Override
    public void linkImage(String link, int uerOrLover) {
        pathImage = link;
    }

    @Override
    public void linkMusic(String linkMusic, String name) {

    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        fileManager_kyNiem.saveData(model_kyNiem);
        Intent intent = new Intent(ListKyNiem.this, MainActivity.class);
        //mPlayer.stop();
        startActivity(intent);
        finish();


    }

    @Override
    public void getPositionListKyNiem(int position) {
        model_kyNiem.getListDay().remove(position);
        model_kyNiem.getListMess().remove(position);
        model_kyNiem.getListPathAnh().remove(position);
        SetAdapter();

    }
}
