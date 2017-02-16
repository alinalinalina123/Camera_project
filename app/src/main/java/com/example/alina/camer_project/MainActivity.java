package com.example.alina.camer_project;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.CountDownLatch;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("NewApi")
public class MainActivity extends Activity {
    SurfaceView surfaceView;
    public static long time;
    public static int counter_2=1;
    public static CounterClass timer;
    Camera camera;
    MediaRecorder mediaRecorder;
    File videoFile;

@Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
    SurfaceHolder holder = surfaceView.getHolder();

    holder.addCallback(new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder)
            {
                 try
                {
                    camera.setPreviewDisplay(holder);
                    camera.startPreview();
                }
            catch (Exception e)
                {
                     e.printStackTrace();
                }
            }
       @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

                @Override
         public void surfaceDestroyed(SurfaceHolder holder) {
            }
       });

    }

        @Override
     protected void onResume()
        {
            super.onResume();
            camera = Camera.open();
        }

    @Override
    protected void onPause()
      {
         super.onPause();
         releaseMediaRecorder();
         if (camera != null)
         camera.release();
         camera = null;
      }


     public void onClickStartRecord(View view) {
   if (prepareVideoRecorder())
        {
            mediaRecorder.start();
        }
   else {
            releaseMediaRecorder();
        }
         timer= new CounterClass(60000, 1000);
         timer.start();
   }

    public void onClickStopRecord(View view)
    {
        if (mediaRecorder != null)
        {
            mediaRecorder.stop();
            releaseMediaRecorder();
        }
        timer.onFinish();
    }

     private boolean prepareVideoRecorder()
     {

         camera.unlock();
         mediaRecorder = new MediaRecorder();
         File pictures = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
         counter_2++;
             videoFile = new File(pictures,String.valueOf(counter_2).toString() +"myvideo.3gp");
                 mediaRecorder.setCamera(camera);
                 mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
                 mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                 mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
                 mediaRecorder.setOutputFile(videoFile.getAbsolutePath());
                 mediaRecorder.setPreviewDisplay(surfaceView.getHolder().getSurface());
         try {
             mediaRecorder.prepare();
             }
         catch (Exception e)
            {
             e.printStackTrace();
             releaseMediaRecorder();
             return false;
             }
         return true;
     }

      private void releaseMediaRecorder() {
              if (mediaRecorder != null)
              {
                          mediaRecorder.reset();
                          mediaRecorder.release();
                          mediaRecorder = null;
                          camera.lock();
              }
       }
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @SuppressLint("NewApi")
    public class CounterClass extends CountDownTimer
    {
        public CounterClass(long millisinFuture,long CountDownInterval)
        {
            super(millisinFuture,CountDownInterval);
        }
        @TargetApi(Build.VERSION_CODES.GINGERBREAD)
        @SuppressLint("NewApi")
        @Override
        public void onTick(long millisUntilFinished) {
           long millis=millisUntilFinished;
            time=millis;
        }

        @Override
        public void onFinish() {
            if (mediaRecorder != null)
            {
                mediaRecorder.stop();
                releaseMediaRecorder();
            }
           if(time<59000) Toast.makeText(MainActivity.this, "Снято!", Toast.LENGTH_SHORT).show();
               else Toast.makeText(MainActivity.this, "Снято! Лимит съемки 1 минута!", Toast.LENGTH_SHORT).show();
        }
    }
}