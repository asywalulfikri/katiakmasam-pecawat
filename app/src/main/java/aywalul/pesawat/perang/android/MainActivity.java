package aywalul.pesawat.perang.android;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import asywalul.pesawat.perang.android.R;

public class MainActivity extends BaseActivity {

    private AdView mAdView;
    private MediaPlayer mMediaPlayer;
    private ImageView mIcon,mPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        sound();
        mAdView.loadAd(adRequest);
        mPlay = (ImageView) findViewById(R.id.play);
        mIcon = (ImageView)findViewById(R.id.icon);
        mPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaPlayer.stop();
                Intent i = new Intent(MainActivity.this, GameActivity.class);
                startActivity(i);
            }
        });
        startTootipAnimation();
    }

    public void sound(){

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer = MediaPlayer.create(this, R.raw.soundplane);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();
    }

    private void startTootipAnimation() {
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(mIcon, "scaleY", 0.8f);
        scaleY.setDuration(200);

        ObjectAnimator scaleYBack = ObjectAnimator.ofFloat(mIcon, "scaleY", 1f);
        scaleYBack.setDuration(500);


        scaleYBack.setInterpolator(new BounceInterpolator());
        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setStartDelay(1000);
        animatorSet.playSequentially(scaleY, scaleYBack);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                animatorSet.setStartDelay(2000);
                animatorSet.start();
            }
        });
        mIcon.setLayerType(View.LAYER_TYPE_HARDWARE,null);
        animatorSet.start();
    }

    @Override
    public void onBackPressed() {
            sound();
            mMediaPlayer.stop();
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.dialog_info, null);

            TextView messageTv = (TextView) view.findViewById(R.id.tv_message);

            messageTv.setText("YOU WANNA QUIT GAME ?");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton("YES",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent (MainActivity.this,InterestialLogout.class);
                                    mMediaPlayer.stop();
                                    startActivity(intent);

                                }
                            });

            builder.create().show();
        }
    }


