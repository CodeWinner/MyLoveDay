package ngokhacbac.mydaylove.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import ngokhacbac.mydaylove.MainActivity;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import ngokhacbac.mydaylove.R;

/**
 * Created by pc1 on 11/2/2017.
 */

public class AdapterListKyNiem extends BaseAdapter {
    ArrayList<String> listPath;
    ArrayList<String> listMess;
    ArrayList<String> listDay;
    Context context;
    GetPosition getPosition;

    public AdapterListKyNiem(ArrayList<String> listPath, ArrayList<String> listMess, ArrayList<String> listDay, Context context) {
        this.listPath = listPath;
        this.listMess = listMess;
        this.listDay = listDay;
        this.context = context;


    }

    public void getPositionListenner(GetPosition getPosition) {
        this.getPosition = getPosition;
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
        ViewHolder_Left viewHolder_left = new ViewHolder_Left();

        if (convertView == null) {
            if (position % 2 != 0) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_list_kyniem_left, parent, false);
                viewHolder_left.txtListMess = (TextView) convertView.findViewById(R.id.txtMessList);
                viewHolder_left.txtListMess.setTypeface(MainActivity.custom_font);

                viewHolder_left.circleImageView = (CircleImageView) convertView.findViewById(R.id.image_list);




            } else {
                if (position % 2 == 0) {
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_list_kyniem_right, parent, false);
                    viewHolder_left.txtListMess = (TextView) convertView.findViewById(R.id.txtMessList_right);
                    viewHolder_left.txtListMess.setTypeface(MainActivity.custom_font);
                    viewHolder_left.circleImageView = (CircleImageView) convertView.findViewById(R.id.image_list_right);



                }

            }
            convertView.setTag(viewHolder_left);

        } else {


            viewHolder_left = (ViewHolder_Left) convertView.getTag();


        }
        viewHolder_left.txtListMess.setText(listDay.get(position) + "\n\"" + listMess.get(position) + "\"");
        Picasso.with(context).load(new File(listPath.get(position)))

                .fit().centerCrop()
                .into(viewHolder_left.circleImageView);


        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(context.getString(R.string.dialog_show_xoa))
                        .setContentText(context.getString(R.string.dialog_hoi_de_xoa))
                        .setCancelText(context.getString(R.string.dialog_khong))
                        .setConfirmText(context.getString(R.string.dialog_dung))
                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                Log.d("XOA1", "OCLICK");
                                // interface
                                getPosition.getPositionListKyNiem(position);
                                sweetAlertDialog.dismiss();

                            }
                        })
                        .show();
                return true;
            }
        });
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    class ViewHolder_Left {
        TextView txtListMess;
        CircleImageView circleImageView;
    }


}
