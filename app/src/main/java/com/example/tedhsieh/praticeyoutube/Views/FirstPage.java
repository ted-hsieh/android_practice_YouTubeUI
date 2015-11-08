package com.example.tedhsieh.praticeyoutube.Views;

import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.example.tedhsieh.praticeyoutube.R;

public class FirstPage extends AppCompatActivity implements Fragment_FirstPage_SlidingTabs.PlayVideoWithTitleListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);

        Toolbar firstpageToolbar = (Toolbar) findViewById(R.id.firstpage_toolbar);
        firstpageToolbar.setTitleTextColor(0xFFFFFFFF);
        setSupportActionBar(firstpageToolbar);

        setTitle("首頁");

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        if (savedInstanceState == null) {
            FragmentTransaction content_transaction = getSupportFragmentManager().beginTransaction();
            Fragment_FirstPage_SlidingTabs fragment_slidingtabs = new Fragment_FirstPage_SlidingTabs();
            content_transaction.replace(R.id.firstpage_content, fragment_slidingtabs);
            content_transaction.commit();

            FragmentTransaction videoview_transaction = getSupportFragmentManager().beginTransaction();
            Fragment_VideoView fragment_videoview = new Fragment_VideoView();
            videoview_transaction.replace(R.id.fragment_videoview, fragment_videoview);
            videoview_transaction.commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_singlevideolist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void PlayVideoWithTitle(String Title){
        // Get Video View
        Fragment_VideoView frag_videoview = (Fragment_VideoView)
                getSupportFragmentManager().findFragmentById(R.id.fragment_videoview);
        frag_videoview.receiveNewVideoTitle(Title);
    }
}
