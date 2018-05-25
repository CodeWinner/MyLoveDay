package ngokhacbac.mydaylove.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;



import java.util.ArrayList;
import java.util.HashMap;

import ngokhacbac.mydaylove.R;

/**
 * Created by pc1 on 11/1/2017.
 */

public class Adapter_ListMusic extends BaseAdapter {
    ArrayList<HashMap<String, String>> listMusic;
    Context context;
    Dialog dialog;
    TextView textView;
    GetLinkFromDialog getLinkImage;

    public Adapter_ListMusic(ArrayList<HashMap<String, String>> listMusic, Context context, Dialog dialog,TextView textView) {
        this.listMusic = listMusic;
        this.context = context;
        this.dialog = dialog;
        this.textView = textView;
    }
   public void getLinkListenner(GetLinkFromDialog getLinkImage)
   {
       this.getLinkImage = getLinkImage;
   }
    @Override
    public int getCount() {
        return listMusic.size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list_music, parent, false);
            viewHolder.txtTenBaiHat = (TextView) convertView.findViewById(R.id.txtTenBaiHat);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.txtTenBaiHat.setText(listMusic.get(position).get("file_name"));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(listMusic.get(position).get("file_name"));

                getLinkImage.linkMusic(listMusic.get(position).get("file_path"),listMusic.get(position).get("file_name"));
                dialog.dismiss();
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView txtTenBaiHat;
    }
}
