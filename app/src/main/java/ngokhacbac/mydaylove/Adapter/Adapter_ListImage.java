package ngokhacbac.mydaylove.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import ngokhacbac.mydaylove.R;


/**
 * Created by pc1 on 11/1/2017.
 */

///storage/emulated/0/DCIM/Camera/IMG_20170522_151515.jpg
public class Adapter_ListImage extends BaseAdapter {
    ArrayList<String> listPath;
    Context context;
    Dialog dialog;
    ImageView imageView;
    GetLinkFromDialog getLinkImage;
    int uerOrLover; // 0 là user , 1 là lover

    public Adapter_ListImage(ArrayList<String> listPath, Context context, Dialog dialog, ImageView imageView, int userOrLover) {
        this.listPath = listPath;
        this.context = context;
        this.dialog = dialog;
        this.imageView = imageView;
        this.uerOrLover = userOrLover;
    }

    public void getLinkImageListenner(GetLinkFromDialog getLinkImage) {
        this.getLinkImage = getLinkImage;
    }

    @Override
    public int getCount() {
        return listPath.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list_image, parent, false);
            viewHolder.imgAnh = (ImageView) convertView.findViewById(R.id.imgImage);
            Log.d("PATH", listPath.get(position));


            convertView.setTag(viewHolder);


        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        Picasso.with(context).load(new File(listPath.get(position)))

                .fit().centerCrop()
                .into(viewHolder.imgAnh);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Picasso.with(context).load(new File(listPath.get(position)))

                        .fit().centerCrop()
                        .into(imageView);

                getLinkImage.linkImage(listPath.get(position),uerOrLover);

                dialog.dismiss();

            }
        });

        return convertView;
    }

    class ViewHolder {
        ImageView imgAnh;
    }
}
