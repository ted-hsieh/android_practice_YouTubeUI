package com.example.tedhsieh.praticeyoutube.Views;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tedhsieh.praticeyoutube.R;

import java.util.Collections;
import java.util.List;

public class Activity_VideoView extends AppCompatActivity {

    private boolean isResumeFromPause;
    private Activity_VideoPlay videoview;

    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoview);

        isResumeFromPause = false;

        videoview = new Activity_VideoPlay();
        videoview.setVideoView(this);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        // Inflate the menu resource
//        getMenuInflater().inflate(R.menu.menu_videoplay, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case android.R.id.home:
//                // app icon in action bar clicked; goto parent activity.
//                this.finish();
//                return true;
//            case R.id.menu_share:
////                Dialog_ShareProvider customized_shareprovider = new Dialog_ShareProvider();
////                customized_shareprovider.setVideoTitle(videoview.getVideoTitle());
////                customized_shareprovider.setStyle( DialogFragment.STYLE_NORMAL, android.R.style.Theme);
////                customized_shareprovider.show(getSupportFragmentManager(), "Customized Provider");
//                showCustomizedIntentChooser(videoview.getVideoTitle());
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showCustomizedIntentChooser(String VideoTitle){

        // Get the currently selected item, and retrieve it's share intent
        final Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Share Video");
        shareIntent.putExtra(Intent.EXTRA_TEXT, VideoTitle);

        final Dialog dialog = new Dialog(this);
        WindowManager.LayoutParams WMLP = dialog.getWindow().getAttributes();
        WMLP.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(WMLP);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.layout_shareview_dialog);
        dialog.setTitle("Customized ShareView");
        dialog.setCancelable(true);
        GridView lv=(GridView)dialog.findViewById(R.id.shareview);
        PackageManager pm=getPackageManager();
        List<ResolveInfo> launchables=pm.queryIntentActivities(shareIntent, 0);

        Collections.sort(launchables, new ResolveInfo.DisplayNameComparator(pm));

        final ShareDialogAdapter adapter=new ShareDialogAdapter(pm, launchables);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                // TODO Auto-generated method stub
                ResolveInfo launchable=adapter.getItem(position);
                ActivityInfo activity=launchable.activityInfo;
                ComponentName name=new ComponentName(activity.applicationInfo.packageName,
                        activity.name);
                shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                shareIntent.setComponent(name);
                startActivity(shareIntent);
            }
        });
        dialog.show();
    }

    class ShareDialogAdapter extends BaseAdapter {
        private PackageManager pm=null;
        private List<ResolveInfo> apps;

        ShareDialogAdapter(PackageManager pm, List<ResolveInfo> apps) {
            this.pm = pm;
            this.apps =apps;
        }

        @Override
        public int getCount(){
            return apps.size();
        }

        @Override
        public ResolveInfo getItem(int position){
            return apps.get(position);
        }

        @Override
        public long getItemId(int position){
            return getItem(position).hashCode();
        }

        @Override
        public View getView(int position, View convertView,
                            ViewGroup parent) {
            if (convertView==null) {
                convertView=newView(parent);
            }

            bindView(position, convertView);

            return(convertView);
        }

        private View newView(ViewGroup parent) {
            return(getLayoutInflater().inflate(R.layout.shareview_dialog_item, parent, false));
        }

        private void bindView(int position, View row) {
            TextView label=(TextView)row.findViewById(R.id.share_text);

            label.setText(getItem(position).loadLabel(pm));

            ImageView icon=(ImageView)row.findViewById(R.id.share_image);

            icon.setImageDrawable(getItem(position).loadIcon(pm));
        }
    }

    private void setShareIntent(String VideoTitle) {

        // Get the currently selected item, and retrieve it's share intent
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Share Video");
        shareIntent.putExtra(Intent.EXTRA_TEXT, VideoTitle);

        startActivity(Intent.createChooser(shareIntent, "Share Event"));

    }

    @Override
    public void onPause(){
        super.onPause();
        videoview.setVideoPause();
        isResumeFromPause = true;
    }

    @Override
    public void onStart(){
        super.onStart();
        if(videoview == null)
            videoview.setVideoView(this);
    }

    @Override
    public void onResume(){
        super.onResume();
        if (isResumeFromPause){
            videoview.setVideoStart();
            isResumeFromPause = false;
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(videoview != null) {
            videoview.release();
            videoview = null;
        }
    }



}
