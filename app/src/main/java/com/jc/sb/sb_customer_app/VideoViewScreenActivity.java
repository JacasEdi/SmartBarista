package com.jc.sb.sb_customer_app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import com.jc.sb.helper.AppConfig;

public class VideoViewScreenActivity extends Activity {

    private VideoView myVideoView;
    private int position = 0;
    private ProgressDialog pDialog;
    private MediaController mediaControls;
    private static String videoURL = AppConfig.VIDEO_URL_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view_screen);

        // Set the media contoller buttons
        if(mediaControls == null){
            mediaControls = new MediaController(VideoViewScreenActivity.this);

        }

        // Initialise the VideoView
        myVideoView = (VideoView) findViewById(R.id.surface_view);

        // Create a progress bar while the video file is loading
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Set a message for the progress bar
        pDialog.setMessage("Loading...");

        // Show the progress bar
        pDialog.show();

        // Play video from the resources folder
        /*
        try {
            // Set the media controller in the VideoView
            myVideoView.setMediaController(mediaControls);

            // Set the uri of the video to be played
            myVideoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.videoviewdemo));

        }catch (Exception e){
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        */

        // Play video from URL
        try {
            // Parse the video URL
            Uri video = Uri.parse(videoURL);

            // Set the media controller in the VideoView
            myVideoView.setMediaController(mediaControls);

            // Set the uri of the video to be played
            myVideoView.setVideoURI(video);

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        myVideoView.requestFocus();

        // We also set an setOnPreparedListener in order to know when the video file is ready to playback
        myVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {

                // Close the progress bar and play the video
                pDialog.dismiss();

                myVideoView.start();
                mediaPlayer.setLooping(true);

            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        // We use onSaveInstanceState in order to store the video playback position for orientation change
        savedInstanceState.putInt("Position", myVideoView.getCurrentPosition());
        myVideoView.pause();

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // We use onSaveInstanceState in order to store the video playback position for orientation change
        position = savedInstanceState.getInt("Position");
        myVideoView.seekTo(position);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(VideoViewScreenActivity.this, CategoryMenuScreenActivity.class);
        startActivity(intent);
        finish();
    }
}
