package com.example.tedhsieh.praticeyoutube.Views;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.SurfaceTexture;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.tedhsieh.praticeyoutube.R;

import java.io.IOException;

public class Activity_VideoPlay implements TextureView.SurfaceTextureListener{

    MediaPlayer video_player;
    private Point screen_size;
    private String VideoTitle;
    private TextureView video_surface;
    private TextView video_currenttime;
    private SeekBar video_seekbar;
    private TextView video_totaltime;
    private ImageView video_playstatus;
    private boolean isShowViewOnVideo;

    // Handler to update UI timer, progress bar etc,.
    private Handler seekbarupdate_handler = new Handler();

//    Activity_VideoPlay(Context context){
//        super(context);
//    }
//
//    Activity_VideoPlay(Context context, AttributeSet attrs){
//        super(context,attrs);
//    }

    public String getVideoTitle(){
        return VideoTitle;
    }

    public void setVideoPause(){
        video_playstatus.setImageResource(R.drawable.play);
        video_player.pause();
        seekbarupdate_handler.removeCallbacks(mUpdateTimeTask);
    }

    public void setVideoStart(){
        seekbarupdate_handler.postDelayed(mUpdateTimeTask, 300);
    }

    public void release(){
        if(video_player != null) {
            video_player.release();
            video_player = null;
            seekbarupdate_handler.removeCallbacks(mUpdateTimeTask);
        }
    }

    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height){
        Surface s = new Surface(surface);
        video_player.setSurface(s);
        video_player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                // set Progress bar values
                video_seekbar.setProgress(0);
                video_seekbar.setMax(100);

                // Updating progress bar
                updateProgressBar();

            }
        });

        video_player.prepareAsync();
    }

    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height){

    }

    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface){
        return true;
    }

    public void onSurfaceTextureUpdated(SurfaceTexture surface){

    }

    public void setVideoView(Activity ParentAct){

        screen_size = new Point();
        ParentAct.getWindowManager().getDefaultDisplay().getSize(screen_size);

        int margin_in_dp = 5;
        int resourceId = ParentAct.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            screen_size.y -= (ParentAct.getResources().getDimensionPixelSize(resourceId) +  (margin_in_dp * ParentAct.getResources().getDisplayMetrics().density));
        }

        try {
            Intent intent = ParentAct.getIntent();
            VideoTitle =  intent.getStringExtra(Fragment_FirstPage_SlidingTabs.Song_Message);
            int SongId = ParentAct.getResources().getIdentifier(VideoTitle.toLowerCase(), "raw", ParentAct.getPackageName());
            Uri videouri = Uri.parse("android.resource://" + ParentAct.getPackageName() + "/" + SongId);
            video_player = new MediaPlayer();
            video_player.setDataSource(ParentAct, videouri);

        } catch (IOException e) {
            e.printStackTrace();
        }

        video_surface = (TextureView) ParentAct.findViewById(R.id.videoview);
        video_surface.setSurfaceTextureListener(this);

        isShowViewOnVideo = false;

        video_surface.setOnTouchListener(new View.OnTouchListener() {
            float sy = 0.0f;
            boolean shouldShowControlState = true;
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_MOVE){
                    float y = event.getRawY();
                    float dy = y - sy;
                    if(dy > 0.f) {
                        float normalized_dy = (dy / (float) screen_size.y) * 0.5f;
                        float dx = normalized_dy * video_surface.getWidth() / 2 * 0.95f;

                        float translated_topy = video_surface.getTop() + dy;
                        float translated_bottomy = (video_surface.getBottom() * ( 1 - 0.5f*normalized_dy ) ) + dy ;
                        boolean shouldTranslate = (translated_topy > 0) && (translated_bottomy < screen_size.y);
                        if(shouldTranslate) {

                            video_surface.setScaleX(1 - normalized_dy);
                            video_surface.setScaleY(1 - normalized_dy);

                            video_surface.setTranslationX(dx);
                            video_surface.setTranslationY(dy);

                            video_currenttime.setVisibility(View.INVISIBLE);
                            video_seekbar.setVisibility(View.INVISIBLE);
                            video_totaltime.setVisibility(View.INVISIBLE);
                            video_playstatus.setVisibility(View.INVISIBLE);


                            shouldShowControlState = false;
                        }
                    }

                }
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    sy = event.getRawY();
                    shouldShowControlState = true;
                }
                if ((event.getAction() == MotionEvent.ACTION_UP) && shouldShowControlState) {
                    isShowViewOnVideo = !isShowViewOnVideo;
                    if (isShowViewOnVideo) {
                        video_currenttime.setVisibility(View.VISIBLE);
                        video_seekbar.setVisibility(View.VISIBLE);
                        video_totaltime.setVisibility(View.VISIBLE);
                        video_playstatus.setVisibility(View.VISIBLE);
                    } else {
                        video_currenttime.setVisibility(View.INVISIBLE);
                        video_seekbar.setVisibility(View.INVISIBLE);
                        video_totaltime.setVisibility(View.INVISIBLE);
                        video_playstatus.setVisibility(View.INVISIBLE);
                    }
                    return true;
                }

                return true;
            }
        });

        video_currenttime = (TextView) ParentAct.findViewById(R.id.seekbar_currenttime);
        video_seekbar = (SeekBar) ParentAct.findViewById(R.id.seekbar);
        video_totaltime = (TextView) ParentAct.findViewById(R.id.seekbar_totaltime);
        video_playstatus = (ImageView) ParentAct.findViewById(R.id.video_playstatus);

        video_currenttime.setVisibility(View.INVISIBLE);
        video_seekbar.setVisibility(View.INVISIBLE);
        video_totaltime.setVisibility(View.INVISIBLE);
        video_playstatus.setVisibility(View.INVISIBLE);

        video_playstatus.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                boolean isInsideVideoPlayStatus = (x > 0) && (x < video_playstatus.getWidth()) &&
                        (y > 0) && ( y < video_playstatus.getHeight());
                if ((event.getAction() == MotionEvent.ACTION_UP) && isInsideVideoPlayStatus) {
                    if (video_player.isPlaying()) {
                        video_playstatus.setImageResource(R.drawable.play);
                        video_player.pause();
                    } else {
                        video_playstatus.setImageResource(R.drawable.pause);
                        video_player.start();
                    }
                    return false;
                }
                return true;
            }
        });
    }

    /**
     * Update timer on seekbar
     * */
    public void updateProgressBar() {
        seekbarupdate_handler.postDelayed(mUpdateTimeTask, 300);
        video_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // remove message Handler from updating progress bar
                seekbarupdate_handler.removeCallbacks(mUpdateTimeTask);

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int totalDuration = video_player.getDuration();
                int currentPosition = progressToTimer(seekBar.getProgress(), totalDuration);

                // forward or backward to certain seconds
                video_player.seekTo(currentPosition);

                // update timer progress again
                updateProgressBar();
            }
        });
    }

    /**
     * Background Runnable thread
     * */
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = video_player.getDuration();
            long currentDuration = video_player.getCurrentPosition();

            // Displaying Total Duration time
            video_totaltime.setText("" + milliSecondsToTimer(totalDuration));
            // Displaying time completed playing
            video_currenttime.setText(""+milliSecondsToTimer(currentDuration));

            // Updating progress bar
            int progress = (int)(getProgressPercentage(currentDuration, totalDuration));
            //Log.d("Progress", ""+progress);
            video_seekbar.setProgress(progress);

            // Running this thread after 100 milliseconds
            seekbarupdate_handler.postDelayed(this, 100);
        }
    };

    public String milliSecondsToTimer(long milliseconds){
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int)( milliseconds / (1000*60*60));
        int minutes = (int)(milliseconds % (1000*60*60)) / (1000*60);
        int seconds = (int) ((milliseconds % (1000*60*60)) % (1000*60) / 1000);
        // Add hours if there
        if(hours > 0){
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if(seconds < 10){
            secondsString = "0" + seconds;
        }else{
            secondsString = "" + seconds;}

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }

    public int getProgressPercentage(long currentDuration, long totalDuration){
        Double percentage = (double) 0;

        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);

        // calculating percentage
        percentage =(((double)currentSeconds)/totalSeconds)*100;

        // return percentage
        return percentage.intValue();
    }

    public int progressToTimer(int progress, int totalDuration) {
        int currentDuration = 0;
        totalDuration = (int) (totalDuration / 1000);
        currentDuration = (int) ((((double) progress) / 100) * totalDuration);

        // return current duration in milliseconds
        return currentDuration * 1000;
    }

}
