package com.example.tedhsieh.praticeyoutube.Views;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tedhsieh.praticeyoutube.R;

import java.util.Collections;
import java.util.List;

public class Dialog_ShareProvider extends DialogFragment{

    private Intent shareIntent;
    private String VideoTitle;

    public void setVideoTitle(String VideoTitle){
        this.VideoTitle = VideoTitle;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_shareview_dialog, container,
                false);
        getDialog().setTitle("DialogFragment Tutorial");

        // Get the currently selected item, and retrieve it's share intent
        final Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Share Video");
        shareIntent.putExtra(Intent.EXTRA_TEXT, VideoTitle);

        WindowManager.LayoutParams WMLP = getDialog().getWindow().getAttributes();
        WMLP.gravity = Gravity.CENTER;
        getDialog().getWindow().setAttributes(WMLP);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        getDialog().setCanceledOnTouchOutside(true);
        getDialog().setCancelable(true);
        GridView lv=(GridView)rootView.findViewById(R.id.shareview);
        PackageManager pm=getActivity().getPackageManager();
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
        // Do something else
        return rootView;
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
            return(getActivity().getLayoutInflater().inflate(R.layout.shareview_dialog_item, parent, false));
        }

        private void bindView(int position, View row) {
            TextView label=(TextView)row.findViewById(R.id.share_text);

            label.setText(getItem(position).loadLabel(pm));

            ImageView icon=(ImageView)row.findViewById(R.id.share_image);

            icon.setImageDrawable(getItem(position).loadIcon(pm));
        }
    }



}
