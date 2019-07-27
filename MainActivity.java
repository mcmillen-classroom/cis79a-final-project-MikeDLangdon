package com.mikelangdon.finalproject;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private static final int RECORD_AUDIO_REQUEST_CODE = 123;

    //private Toolbar toolbar;
    private Chronometer chronometer;
    // top left button
    private static Button sStopButtonTL;
    private static Button sPlayButtonTL;
    private static Button sRecordButtonTL;

    // top right button
    private static Button sStopButtonTR;
    private static Button sPlayButtonTR;
    private static Button sRecordButtonTR;

    // bottom left button
    private static Button sStopButtonBL;
    private static Button sPlayButtonBL;
    private static Button sRecordButtonBL;

    // bottom right button
    private static Button sStopButtonBR;
    private static Button sPlayButtonBR;
    private static Button sRecordButtonBR;

    private SeekBar seekBar;
    //private LinearLayout linearLayoutRecorder, linearLayoutPlay;

    private MediaRecorder mRecorder;
    private MediaPlayer mPlayer;
    private String fileName = null;
    private int lastProgress = 0;
    private Handler mHandler = new Handler();
    private boolean isPlaying = false;

    private boolean isRecording = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getPermissionToRecordAudio();
        }

        initViews();

    }

    private void initViews() {

//        /** setting up the toolbar  **/
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitle("Voice Recorder");
//        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));
//        setSupportActionBar(toolbar);

        //linearLayoutRecorder = (LinearLayout) findViewById(R.id.linearLayoutRecorder);
        chronometer = (Chronometer) findViewById(R.id.chronometerTimer);
        chronometer.setBase(SystemClock.elapsedRealtime());
        // top left button
        sStopButtonTL = (Button) findViewById(R.id.stop_button_top_left);
        sPlayButtonTL = (Button) findViewById(R.id.play_button_top_left);
        sRecordButtonTL = (Button) findViewById(R.id.record_button_top_left);

        // top right button
        sStopButtonTR = (Button) findViewById(R.id.stop_button_top_right);
        sPlayButtonTR = (Button) findViewById(R.id.play_button_top_right);
        sRecordButtonTR = (Button) findViewById(R.id.record_button_top_right);

        // bottom left button
        sStopButtonBL = (Button) findViewById(R.id.stop_button_bottom_left);
        sPlayButtonBL = (Button) findViewById(R.id.play_button_bottom_left);
        sRecordButtonBL = (Button) findViewById(R.id.record_button_bottom_left);

        // bottom right button
        sStopButtonBR = (Button) findViewById(R.id.stop_button_bottom_right);
        sPlayButtonBR = (Button) findViewById(R.id.play_button_bottom_right);
        sRecordButtonBR = (Button) findViewById(R.id.record_button_bottom_right);

        //linearLayoutPlay = (LinearLayout) findViewById(R.id.linearLayoutPlay);
        seekBar = (SeekBar) findViewById(R.id.seekBar);

        // top left button
        sStopButtonTL.setOnClickListener(this);
        sPlayButtonTL.setOnClickListener(this);
        sRecordButtonTL.setOnClickListener(this);

        // top right button
        sStopButtonTR.setOnClickListener(this);
        sPlayButtonTR.setOnClickListener(this);
        sRecordButtonTR.setOnClickListener(this);

        // bottom left button
        sStopButtonBL.setOnClickListener(this);
        sPlayButtonBL.setOnClickListener(this);
        sRecordButtonBL.setOnClickListener(this);

        // bottom right button
        sStopButtonBR.setOnClickListener(this);
        sPlayButtonBR.setOnClickListener(this);
        sRecordButtonBR.setOnClickListener(this);

        // If there is no mic, then no buttons are enabled
        if (!hasMicrophone()) {
            sStopButtonTL.setEnabled(false);
            sStopButtonTL.setVisibility(View.GONE);
            sStopButtonTR.setEnabled(false);
            sStopButtonTR.setVisibility(View.GONE);
            sStopButtonBL.setEnabled(false);
            sStopButtonBL.setVisibility(View.GONE);
            sStopButtonBR.setEnabled(false);
            sStopButtonBR.setVisibility(View.GONE);


            sPlayButtonTL.setEnabled(false);
            sPlayButtonTL.setVisibility(View.GONE);
            sPlayButtonTR.setEnabled(false);
            sPlayButtonTR.setVisibility(View.GONE);
            sPlayButtonBL.setEnabled(false);
            sPlayButtonBL.setVisibility(View.GONE);
            sPlayButtonBR.setEnabled(false);
            sPlayButtonBR.setVisibility(View.GONE);

            // making it visible just to show something on the screen
            sRecordButtonTL.setEnabled(false);
            sRecordButtonTL.setVisibility(View.VISIBLE);
            sRecordButtonTR.setEnabled(false);
            sRecordButtonTR.setVisibility(View.VISIBLE);
            sRecordButtonBL.setEnabled(false);
            sRecordButtonBL.setVisibility(View.VISIBLE);
            sRecordButtonBR.setEnabled(false);
            sRecordButtonBR.setVisibility(View.VISIBLE);
        }
        // If there is a mic, the record button is available
        else {
            sRecordButtonTL.setEnabled(true);
            sRecordButtonTL.setVisibility(View.VISIBLE);
            sRecordButtonTR.setEnabled(true);
            sRecordButtonTR.setVisibility(View.VISIBLE);
            sRecordButtonBL.setEnabled(true);
            sRecordButtonBL.setVisibility(View.VISIBLE);
            sRecordButtonBR.setEnabled(true);
            sRecordButtonBR.setVisibility(View.VISIBLE);

            sPlayButtonTL.setEnabled(false);
            sPlayButtonTL.setVisibility(View.GONE);
            sPlayButtonTR.setEnabled(false);
            sPlayButtonTR.setVisibility(View.GONE);
            sPlayButtonBL.setEnabled(false);
            sPlayButtonBL.setVisibility(View.GONE);
            sPlayButtonBR.setEnabled(false);
            sPlayButtonBR.setVisibility(View.GONE);

            sStopButtonTL.setEnabled(false);
            sStopButtonTL.setVisibility(View.GONE);
            sStopButtonTR.setEnabled(false);
            sStopButtonTR.setVisibility(View.GONE);
            sStopButtonBL.setEnabled(false);
            sStopButtonBL.setVisibility(View.GONE);
            sStopButtonBR.setEnabled(false);
            sStopButtonBR.setVisibility(View.GONE);
        }

    }

//    private void prepareforRecording() {
//        TransitionManager.beginDelayedTransition(linearLayoutRecorder);
//        imageViewRecord.setVisibility(View.GONE);
//        imageViewStop.setVisibility(View.VISIBLE);
//        linearLayoutPlay.setVisibility(View.GONE);
//    }


    private void startRecording(View v) {
        //we use the MediaRecorder class to record
        isRecording = true;

        if (v.getId() == R.id.record_button_top_left)
        {
            sStopButtonTL.setEnabled(true);
            sStopButtonTL.setVisibility(View.VISIBLE);

            sPlayButtonTL.setEnabled(false);
            sPlayButtonTL.setVisibility(View.GONE);

            sRecordButtonTL.setEnabled(false);
            sRecordButtonTL.setVisibility(View.GONE);
        }
        else if (v.getId() == R.id.record_button_bottom_left)
        {
            sStopButtonBL.setEnabled(true);
            sStopButtonBL.setVisibility(View.VISIBLE);
            sPlayButtonBL.setEnabled(false);
            sPlayButtonBL.setVisibility(View.GONE);
            sRecordButtonBL.setEnabled(false);
            sRecordButtonBL.setVisibility(View.GONE);
        }
        else if (v.getId() == R.id.play_button_top_right) {
            sStopButtonTR.setEnabled(true);
            sStopButtonTR.setVisibility(View.VISIBLE);
            sRecordButtonTR.setEnabled(false);
            sRecordButtonTR.setVisibility(View.GONE);
            sPlayButtonTR.setEnabled(false);
            sPlayButtonTR.setVisibility(View.GONE);

        }
        else if (v.getId() == R.id.record_button_bottom_right){
            sStopButtonBR.setEnabled(true);
            sStopButtonBR.setVisibility(View.VISIBLE);
            sPlayButtonBR.setEnabled(false);
            sPlayButtonBR.setVisibility(View.GONE);
            sRecordButtonBR.setEnabled(false);
            sRecordButtonBR.setVisibility(View.GONE);
        }

        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        /**In the lines below, we create a directory VoiceRecorderSimplifiedCoding/Audios in the phone storage
         * and the audios are being stored in the Audios folder **/
        File root = android.os.Environment.getExternalStorageDirectory();
        File file = new File(root.getAbsolutePath() + "/MikeLangdon/Audio");
        if (!file.exists()) {
            file.mkdirs();
        }

        fileName =  root.getAbsolutePath() + "/MikeLangdon/Audio/" +
                String.valueOf(System.currentTimeMillis() + ".mp3");
        Log.d("filename",fileName);
        mRecorder.setOutputFile(fileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
            mRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        lastProgress = 0;
        seekBar.setProgress(0);
        stopPlaying();
        //starting the chronometer
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
    }


    private void stopPlaying() {
        try{
            mPlayer.release();
        }catch (Exception e){
            e.printStackTrace();
        }
        mPlayer = null;
//        //showing the play button
//        imageViewPlay.setImageResource(R.drawable.play_button_24);
        chronometer.stop();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getPermissionToRecordAudio() {
        // 1) Use the support library version ContextCompat.checkSelfPermission(...) to avoid
        // checking the build version since Context.checkSelfPermission(...) is only available
        // in Marshmallow
        // 2) Always check for permission (even if permission has already been granted)
        // since the user can revoke permissions at any time through Settings
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {

            // The permission is NOT already granted.
            // Check if the user has been asked about this permission already and denied
            // it. If so, we want to give more explanation about why the permission is needed.
            // Fire off an async request to actually get the permission
            // This will show the standard permission request dialog UI
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    RECORD_AUDIO_REQUEST_CODE);

        }
    }

    // Callback with the request from calling requestPermissions(...)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        // Make sure it's our original READ_CONTACTS request
        if (requestCode == RECORD_AUDIO_REQUEST_CODE) {
            if (grantResults.length == 3 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED){

                //Toast.makeText(this, "Record Audio permission granted", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "You must give permissions to use this app. App is exiting.", Toast.LENGTH_SHORT).show();
                finishAffinity();
            }
        }

    }

//    private void prepareforStop() {
//        TransitionManager.beginDelayedTransition(linearLayoutRecorder);
//        imageViewRecord.setVisibility(View.VISIBLE);
//        imageViewStop.setVisibility(View.GONE);
//        linearLayoutPlay.setVisibility(View.VISIBLE);
//    }

    private void stopRecording() {

        try{
            mRecorder.stop();
            mRecorder.release();
        }catch (Exception e){
            e.printStackTrace();
        }
        mRecorder = null;
        //starting the chronometer
        chronometer.stop();
        chronometer.setBase(SystemClock.elapsedRealtime());
        //showing the play button
        Toast.makeText(this, "Recording saved successfully.", Toast.LENGTH_SHORT).show();
    }




    // The stopAudio() method is responsible for enabling the Play button,
    // disabling the Stop button and then stopping
    // and resetting the MediaRecorder instance
    public void stopAudio (View v) {
        if (v.getId() == R.id.stop_button_top_left) {
            sStopButtonTL.setEnabled(false);
            sStopButtonTL.setVisibility(View.GONE);
            sPlayButtonTL.setEnabled(true);
            sPlayButtonTL.setVisibility(View.VISIBLE);
            sRecordButtonTL.setEnabled(false);
            sRecordButtonTL.setVisibility(View.GONE);
        }
        else if (v.getId() == R.id.stop_button_bottom_left) {
            sStopButtonBL.setEnabled(false);
            sStopButtonBL.setVisibility(View.GONE);
            sPlayButtonBL.setEnabled(true);
            sPlayButtonBL.setVisibility(View.VISIBLE);
            sRecordButtonBL.setEnabled(false);
            sRecordButtonBL.setVisibility(View.GONE);
        }
        else if (v.getId() == R.id.stop_button_top_right) {
            sStopButtonTR.setEnabled(false);
            sStopButtonTR.setVisibility(View.GONE);
            sPlayButtonTR.setEnabled(true);
            sPlayButtonTR.setVisibility(View.VISIBLE);
            sRecordButtonTR.setEnabled(false);
            sRecordButtonTR.setVisibility(View.GONE);
        }
        else if (v.getId() == R.id.stop_button_bottom_right) {
            sStopButtonBR.setEnabled(false);
            sStopButtonBR.setVisibility(View.GONE);
            sPlayButtonBR.setEnabled(true);
            sPlayButtonBR.setVisibility(View.VISIBLE);
            sRecordButtonBR.setEnabled(false);
            sRecordButtonBR.setVisibility(View.GONE);
        }

        if (isRecording) {

            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
            isRecording = false;

            mRecorder = null;
            //starting the chronometer
            chronometer.stop();
            chronometer.setBase(SystemClock.elapsedRealtime());
            //showing the play button
            Toast.makeText(this, "Recording saved successfully.", Toast.LENGTH_SHORT).show();
        }
        else {
            mPlayer.release();
            mPlayer = null;

        }
    }





    private void startPlaying(View v) {

        if (v.getId() == R.id.play_button_top_left) {
            sPlayButtonTL.setEnabled(false);
            sPlayButtonTL.setVisibility(View.GONE);
            sRecordButtonTL.setEnabled(false);
            sRecordButtonTL.setVisibility(View.GONE);
            sStopButtonTL.setEnabled(true);
            sStopButtonTL.setVisibility(View.VISIBLE);
        }
        else if (v.getId() == R.id.play_button_bottom_left) {
            sPlayButtonBL.setEnabled(false);
            sPlayButtonBL.setVisibility(View.GONE);
            sRecordButtonBL.setEnabled(false);
            sRecordButtonBL.setVisibility(View.GONE);
            sStopButtonBL.setEnabled(true);
            sStopButtonBL.setVisibility(View.VISIBLE);
        }
        else if (v.getId() == R.id.play_button_top_right) {
            sPlayButtonTR.setEnabled(false);
            sPlayButtonTR.setVisibility(View.GONE);
            sRecordButtonTR.setEnabled(false);
            sRecordButtonTR.setVisibility(View.GONE);
            sStopButtonTR.setEnabled(true);
            sStopButtonTR.setVisibility(View.VISIBLE);
        }
        else if (v.getId() == R.id.play_button_bottom_right) {
            sPlayButtonBR.setEnabled(false);
            sPlayButtonBR.setVisibility(View.GONE);
            sRecordButtonBR.setEnabled(false);
            sRecordButtonBR.setVisibility(View.GONE);
            sStopButtonBR.setEnabled(true);
            sStopButtonBR.setVisibility(View.VISIBLE);
        }

        mPlayer = new MediaPlayer();
        try {
//fileName is global string. it contains the Uri to the recently recorded audio.
            mPlayer.setDataSource(fileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e("LOG_TAG", "prepare() failed");
        }
        //making the imageview pause button
        //imageViewPlay.setImageResource(R.drawable.pause_24);

        seekBar.setProgress(lastProgress);
        mPlayer.seekTo(lastProgress);
        seekBar.setMax(mPlayer.getDuration());
        seekUpdation();
        chronometer.start();

        /** once the audio is complete, timer is stopped here**/
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //imageViewPlay.setImageResource(R.drawable.play_button_24);
                isPlaying = false;
                chronometer.stop();
            }
        });

        /** moving the track as per the seekBar's position**/
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if( mPlayer!=null && fromUser ){
                    //here the track's progress is being changed as per the progress bar
                    mPlayer.seekTo(progress);
                    //timer is being updated as per the progress of the seekbar
                    chronometer.setBase(SystemClock.elapsedRealtime() - mPlayer.getCurrentPosition());
                    lastProgress = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            seekUpdation();
        }
    };

    private void seekUpdation() {
        if(mPlayer != null){
            int mCurrentPosition = mPlayer.getCurrentPosition() ;
            seekBar.setProgress(mCurrentPosition);
            lastProgress = mCurrentPosition;
        }
        mHandler.postDelayed(runnable, 100);
    }

    protected boolean hasMicrophone() {
        PackageManager packageManager = this.getPackageManager();
        return packageManager.hasSystemFeature(PackageManager.FEATURE_MICROPHONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.record_button_top_left:
            case R.id.record_button_top_right:
            case R.id.record_button_bottom_left:
            case R.id.record_button_bottom_right:
                startRecording(v);
                break;
            case R.id.stop_button_top_left:
            case R.id.stop_button_top_right:
            case R.id.stop_button_bottom_left:
            case R.id.stop_button_bottom_right:
                stopRecording();
                break;
            case R.id.play_button_top_left:
            case R.id.play_button_top_right:
            case R.id.play_button_bottom_left:
            case R.id.play_button_bottom_right:
                if( !isPlaying && fileName != null ){
                    isPlaying = true;
                    startPlaying(v);
                }else{
                    isPlaying = false;
                    stopPlaying();
                }
                break;
        }
//        if( view == imageViewRecord ){
//            prepareforRecording();
//            startRecording();
//        }else if( view == imageViewStop ){
//            prepareforStop();
//            stopRecording();
//        }else if( view == imageViewPlay ){
//            if( !isPlaying && fileName != null ){
//                isPlaying = true;
//                startPlaying();
//            }else{
//                isPlaying = false;
//                stopPlaying();
//            }
//        }

    }

}

