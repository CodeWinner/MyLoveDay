package ngokhacbac.mydaylove;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.hanks.htextview.base.HTextView;

import java.util.ArrayList;

/**
 * Created by pc1 on 9/27/2017.
 */

public class BaseActivity extends AppCompatActivity {
   // max 33 chareter cho 1 lần
    public ArrayList<String> sentences ;

    int index = 0;
    int click = 0;

    class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            if (v instanceof HTextView) {

                if (click % 2 == 0) {
                    index = 0;
                    RunText runText = new RunText(v);
                    runText.execute();
                } else {
                    index = sentences.size() + 1;
                    ((HTextView) v).animateText(MainActivity.model_json_static.getMessMacDinh());
                }
                click++;


            }
        }
    }
    public String[] toArrayMess( ArrayList<String> customMess)
    {
        String[] temp = new String[customMess.size()];
        for (int i =0;i<temp.length;i++)
        {
            temp[i] = customMess.get(i);
        }
        return temp;

    }
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
    class SimpleAnimationListener extends Animation implements Animation.AnimationListener {


        private Context context;

        public SimpleAnimationListener(Context context) {
            this.context = context;


        }


        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            Toast.makeText(context, "Animation finished", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    class RunText extends AsyncTask<Void, String, Void> {
        View v;

        RunText(View v) {
            this.v = v;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(MainActivity.model_json_static.getMess().equals("")==false)
            {
                sentences = customMess(MainActivity.model_json_static.getMess());
                Log.i("SHOW","toArrayMess : "+ toArrayMess(customMess(MainActivity.model_json_static.getMess())).length);
                Log.i("SHOW","sentences : "+ sentences.size());
            }
            else {
                sentences = new ArrayList<>();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            while (index < sentences.size()) {
                // index++;
                if (index == sentences.size() ) {
                    index = 0;
                }

                publishProgress(sentences.get(index++));
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            ((HTextView) v).animateText(values[0].toString());
        }
    }
}
