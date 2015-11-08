package com.example.tedhsieh.praticeyoutube.Views;

import android.graphics.Point;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.tedhsieh.praticeyoutube.R;

import java.io.IOException;

public class Fragment_VideoView_SurfaceView extends Fragment implements SurfaceHolder.Callback {

    MediaPlayer video_player;
    private String VideoTitle;
    private SurfaceView video_surfaceview;
    private SurfaceHolder video_surfaceholder;
    private TextView video_currenttime;
    private SeekBar video_seekbar;
    private TextView video_totaltime;
    private ImageView video_tosamllview;
    private ImageView video_playstatus;

    private Point screen_size;
    final private float smallviewPercentage = 0.6f;
    private float MaxDy;
    private boolean isStopPlaying;
    private boolean isShowViewOnVideo;
    private boolean isShowVideoView;
    private boolean isInSamllView;
    private int stopTime;

    // Handler to update UI timer, progress bar etc,.
    private Handler seekbarupdate_handler = new Handler();

    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_videoview, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        isInSamllView = false;
        isStopPlaying = false;

        video_surfaceview = (SurfaceView) view.findViewById(R.id.videoview);
        video_surfaceholder = video_surfaceview.getHolder();
        video_surfaceholder.addCallback(this);

        video_currenttime = (TextView) view.findViewById(R.id.seekbar_currenttime);
        video_seekbar = (SeekBar) view.findViewById(R.id.seekbar);
        video_totaltime = (TextView) view.findViewById(R.id.seekbar_totaltime);
        video_playstatus = (ImageView) view.findViewById(R.id.video_playstatus);
        video_tosamllview = (ImageView) view.findViewById(R.id.video_tosmallview);

        screen_size = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(screen_size);

        int margin_in_dp = 5;
        int barheightId = getActivity().getResources().getIdentifier("status_bar_height", "dimen", "android");
        int barheight = barheightId>0 ? (getActivity().getResources().getDimensionPixelSize(barheightId)) : 0 ;
//        int navigationheightId = getActivity().getResources().getIdentifier("navigation_bar_height", "dimen", "android");
//        int navigationheight = navigationheightId>0 ? (getActivity().getResources().getDimensionPixelSize(navigationheightId)) : 0;

        if (barheightId > 0) {
            screen_size.y -=  (barheight + (margin_in_dp * getActivity().getResources().getDisplayMetrics().density) );
        }

    }

    @Override
    public void onStart(){
        super.onStart();

        setSurfaceOnTouchListener();
        setToSmallViewOnTouchListener();
        setVideoStatusOnTouchListener();

        if(isStopPlaying)
            video_surfaceholder.addCallback(this);
    }

    @Override
    public void onPause(){
        super.onPause();
        if(video_player!=null) {
            video_player.pause();
            video_playstatus.setImageResource(R.drawable.play);
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        if(video_player!=null) {
            stopTime = video_player.getCurrentPosition();
            video_player.stop();
        }
        video_surfaceview.setVisibility(View.VISIBLE);
        removeVideoResouces();
        isStopPlaying = true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // TODO Auto-generated method stub
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        MaxDy = screen_size.y - (1- (1.0f - smallviewPercentage)*0.5f) * video_surfaceview.getHeight();

        try {
            video_player = new MediaPlayer();
            int SongId = getActivity().getResources().getIdentifier("song_1", "raw", getActivity().getPackageName());
            Uri videouri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + SongId);
            video_player.setDataSource(getContext(), videouri);
            video_player.setDisplay(video_surfaceview.getHolder());

        } catch (IOException e) {
            e.printStackTrace();
        }

        if(isStopPlaying){
            if(isShowVideoView){
                video_playstatus.setImageResource(R.drawable.play);
                playVideo();
            }
            else {
                video_playstatus.setImageResource(R.drawable.pause);
//                isStopPlaying = false;
//                isShowVideoView = false;
//                video_surfaceview.setVisibility(View.INVISIBLE);
            }
        }
        else {
//            isShowViewOnVideo = false;
//            isShowVideoView = false;
//            setShowAllView();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub

    }

    public void setVideoStatusOnTouchListener(){
        video_playstatus.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                boolean isInsideVideoPlayStatus = (x > 0) && (x < video_playstatus.getWidth()) &&
                        (y > 0) && (y < video_playstatus.getHeight());
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

    public void setShowVideoControl(){
        int shouldShowVideoControl = isShowViewOnVideo ? View.VISIBLE : View.INVISIBLE;
        video_currenttime.setVisibility(shouldShowVideoControl);
        video_seekbar.setVisibility(shouldShowVideoControl);
        video_totaltime.setVisibility(shouldShowVideoControl);
        video_playstatus.setVisibility(shouldShowVideoControl);
        video_tosamllview.setVisibility(shouldShowVideoControl);
    }

    public void setShowAllView(){
        int shouldShowVideoControl = isShowVideoView ? View.VISIBLE : View.INVISIBLE;
        video_surfaceview.setVisibility(shouldShowVideoControl);
        setShowVideoControl();
    }


    public void setToSmallViewOnTouchListener(){
        video_tosamllview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                boolean isInsideVideoPlayStatus = (x > 0) && (x < video_tosamllview.getWidth()) &&
                        (y > 0) && (y < video_tosamllview.getHeight());
                if ((event.getAction() == MotionEvent.ACTION_UP) && isInsideVideoPlayStatus) {
                    //TODO animation to video small view
                    removeVideoResouces();
                    isShowVideoView = false;
                    isShowViewOnVideo = false;
                    setShowAllView();
                    return false;
                }
                return true;
            }
        });
    }

    private void removeVideoResouces() {

        video_playstatus.setOnTouchListener(null);
        video_tosamllview.setOnTouchListener(null);

        if (video_player != null) {
            video_player.release();
            video_player = null;
            seekbarupdate_handler.removeCallbacks(mUpdateTimeTask);
        }

    }

    public void setSurfaceOnTouchListener(){
        video_surfaceview.setOnTouchListener(new View.OnTouchListener() {
            float sy = 0.0f;
            boolean shouldShowControlState = true;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (!isShowVideoView)
                    return false;

                if (event.getAction() == MotionEvent.ACTION_MOVE) {

                    float y = event.getRawY();
                    float dy = isInSamllView ? (sy - y) : (y - sy);
                    if (dy < 0)
                        dy = 0;
                    if (dy > MaxDy)
                        dy = MaxDy;

                    float drageventThresholdInDp = 3;
                    float drageventThresholdInPx = drageventThresholdInDp * getActivity().getResources().getDisplayMetrics().density;
                    float normalized_dy = isInSamllView ? (1 - (dy / MaxDy)) : (dy / MaxDy);
                    VideoViewDragEvent(normalized_dy);

                    if (dy > drageventThresholdInPx) {
                        shouldShowControlState = false;
                        isShowViewOnVideo = false;
                        setShowVideoControl();
                    }

                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    sy = event.getRawY();
                    shouldShowControlState = true;
                }
                if ((event.getAction() == MotionEvent.ACTION_UP) && shouldShowControlState && isShowVideoView && !isInSamllView) {
                    isShowViewOnVideo = !isShowViewOnVideo;
                    setShowVideoControl();
                    return true;
                } else if ((event.getAction() == MotionEvent.ACTION_UP) && shouldShowControlState && isShowVideoView && isInSamllView) {
                    long totalAnimationTime = 1500; //in milliseconds
                    VideoSurfaceViewSizeAnimation animation = new VideoSurfaceViewSizeAnimation(video_surfaceview, MaxDy, smallviewPercentage, 0.0f,
                            false, true, totalAnimationTime);
                    video_surfaceview.startAnimation(animation);
                    isInSamllView = !isInSamllView;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    float y = event.getRawY();
                    float dy = isInSamllView ? (sy - y) : (y - sy);
                    dy = (dy > 0) ? dy : 0;
                    if (dy > MaxDy)
                        dy = MaxDy;
                    float normalized_dy = (dy / MaxDy);
                    boolean overThreshold = dy > (MaxDy / 3);
                    long totalAnimationTime = 1500; //in milliseconds

                    VideoSurfaceViewSizeAnimation animation;

                    if (overThreshold) {

                        isInSamllView = !isInSamllView;

                        long remainingAnimationTime = (long) ((1.0f - normalized_dy) * totalAnimationTime);
                        animation = new VideoSurfaceViewSizeAnimation(video_surfaceview, MaxDy, smallviewPercentage, normalized_dy,
                                isInSamllView, overThreshold, remainingAnimationTime);
                    } else {

                        long remainingAnimationTime = (long) (normalized_dy * totalAnimationTime);
                        animation = new VideoSurfaceViewSizeAnimation(video_surfaceview, MaxDy, smallviewPercentage, normalized_dy,
                                isInSamllView, overThreshold, remainingAnimationTime);
                    }

                    video_surfaceview.startAnimation(animation);

                }

                return true;
            }
        });
    }

    /**
     * Update timer on seekbar
     * */
    public void updateProgressBar() {

        seekbarupdate_handler.postDelayed(mUpdateTimeTask, 100);
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

    private void VideoViewDragEvent(float normalized_dy){
        float scale_factor = (1 - (normalized_dy * (1.0f- smallviewPercentage)) );
        float tx = (1 - scale_factor) * video_surfaceview.getWidth() / 2 * 0.95f;
        float ty = normalized_dy * MaxDy;

        video_surfaceview.setScaleX(scale_factor);
        video_surfaceview.setScaleY(scale_factor);

        video_surfaceview.setTranslationX(tx);
        video_surfaceview.setTranslationY(ty);

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
            video_currenttime.setText("" + milliSecondsToTimer(currentDuration));

            // Updating progress bar
            int progress = (int) (getProgressPercentage(currentDuration, totalDuration));
            //Log.d("Progress", ""+progress);
            video_seekbar.setProgress(progress);

            // Running this thread after 100 milliseconds
            seekbarupdate_handler.postDelayed(this, 100);
        }
    };

    public String milliSecondsToTimer(long milliseconds){
        String finalTimerString = "";
        String minutesString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int)( milliseconds / (1000*60*60));
        int minutes = (int)(milliseconds % (1000*60*60)) / (1000*60);
        int seconds = (int) ((milliseconds % (1000*60*60)) % (1000*60) / 1000);
        // Add hours if there
        if(hours > 0){
            finalTimerString = hours + ":";
        }

        if(minutes < 10){
            minutesString = "0" + minutes;
        }else{
            minutesString = "" + minutes;
        }

        // Prepending 0 to seconds if it is one digit
        if(seconds < 10){
            secondsString = "0" + seconds;
        }else{
            secondsString = "" + seconds;}

        finalTimerString = finalTimerString + minutesString + ":" + secondsString;

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

    public void receiveNewVideoTitle(String Title){
        VideoTitle = Title;
//        while (video_surface != null);
        playVideo();
    }

    public void playVideo(){
        if(video_player == null) {

            video_player = new MediaPlayer();

            setToSmallViewOnTouchListener();
            setVideoStatusOnTouchListener();
        }
        else
//            video_player.reset();

//        try {
//            int SongId = getActivity().getResources().getIdentifier(VideoTitle.toLowerCase(), "raw", getActivity().getPackageName());
//            Uri videouri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + SongId);
//            video_player.setDataSource(getContext(), videouri);
//            video_player.setDisplay(video_surfaceholder);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        video_player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (isStopPlaying) {
                    mp.seekTo(stopTime);
                    isStopPlaying = false;
                } else
                    mp.start();
                // set Progress bar values
                video_seekbar.setProgress(0);
                video_seekbar.setMax(100);

                // Updating progress bar
                updateProgressBar();

            }
        });

        video_player.prepareAsync();
        isShowViewOnVideo = !isInSamllView;
        isShowVideoView = true;
        setShowAllView();
    }


}
