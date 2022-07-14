package com.example.dailapp;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

public class RecordingService extends Service {
    private MediaRecorder rec;
    private boolean recoderstarted;
    private File file;
    String name,phonenumber;
    String path = "sdcard/recordcalls/";


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //return super.onStartCommand(intent, flags, startId);
        File sampleDir=new File(Environment.getExternalStorageDirectory(),"/DailAppCallRecords");
        if (!sampleDir.exists())
        {
         sampleDir.mkdirs();
        }
        String file_name="Name";

        try{
            file=File.createTempFile(file_name,".mp4",sampleDir);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        Date date = new Date();
        String stringDate = DateFormat.getDateTimeInstance().format(date);
        rec = new MediaRecorder();
        rec.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
        rec.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        rec.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        rec.setOutputFile(file.getAbsoluteFile());

        TelephonyManager manager = (TelephonyManager) getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        manager.listen(new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                super.onCallStateChanged(state, incomingNumber);
                if (TelephonyManager.CALL_STATE_IDLE == state ){
                    try {
                        if (rec == null) {
                            rec.stop();
                            rec.reset();
                            rec.release();
                            recoderstarted = false;
                            rec = null;
                            stopSelf();
                            System.out.println("Vinay CALL_STATE_IDLE");
                        } else {
                            rec.stop();
                            rec.reset();
                            rec.release();
                            recoderstarted = false;
                            rec = null;
                            stopSelf();
                            System.out.println("Vinay CALL_STATE_IDLE");
                        }
                    }
                    catch(Exception e) {
                        e.printStackTrace();
                    }
                }
                else if(TelephonyManager.CALL_STATE_OFFHOOK==state){
                    System.out.println("VINAY CALL_STATE_OFFHOOK");
                    try {
                        File sampleDir=new File(Environment.getExternalStorageDirectory(),"/DailAppCallRecords");
                        if (!sampleDir.exists())
                        {
                            sampleDir.mkdirs();
                        }

                        String file_name="Name";
                        try{
                            file=File.createTempFile(file_name,".mp4",sampleDir);
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                        Date date = new Date();
                        String stringDate = DateFormat.getDateTimeInstance().format(date);
                        rec = new MediaRecorder();
                        rec.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
                        rec.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                        rec.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                        rec.setOutputFile(file.getAbsoluteFile());
                        TelephonyManager manager = (TelephonyManager) getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
                    rec.prepare();
                    rec.start();
                    recoderstarted=true;
                    } catch (IOException e) {
                        System.out.println("vinay Exception ="+ e.getMessage());
                        e.printStackTrace();
                    }
                }


            }
        }, PhoneStateListener.LISTEN_CALL_STATE);

        return START_STICKY;
    }
}