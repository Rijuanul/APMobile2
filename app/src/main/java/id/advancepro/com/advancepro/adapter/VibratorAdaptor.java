package id.advancepro.com.advancepro.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.net.Uri;
import android.os.Vibrator;

import id.advancepro.com.advancepro.R;

/**
 * Created by TASIV on 4/25/2017.
 */

public class VibratorAdaptor {
    private Vibrator vibrator;
    private Ringtone r;
    private Context contx;
    private Uri notification;
    private MediaPlayer mpScanAndTouch=null;
    private MediaPlayer mpComplete=null;
    private MediaPlayer mpError=null;
    public VibratorAdaptor(Context ctx){
        contx=ctx;
        vibrator = (Vibrator) ctx.getSystemService(Context.VIBRATOR_SERVICE);

        mpScanAndTouch = MediaPlayer.create(ctx, R.raw.scantouchsound);
        mpComplete= MediaPlayer.create(ctx, R.raw.completesound);
        mpError= MediaPlayer.create(ctx, R.raw.errorsound);

    }
    //Vibrate
    public void makeVibration(){
        vibrator.vibrate(300);
    }

    //Scan and touchSound
    public void makeScanAndTouchSound(){
        mpScanAndTouch.start();

    }
    //Complete Sound
    public void completeSound(){
        mpComplete.start();
    }
    //Error Sound
    public void errorSound(){
        mpError.start();
    }


}
