package com.thea.widget;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private HeartProgressBar mHeartProgressBar;
    private FaceProgressBar mFaceProgressBar;

    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mHeartProgressBar.setProgress(mHeartProgressBar.getProgress() + 1);
            mHandler.postDelayed(mRunnable, 60);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHeartProgressBar = (HeartProgressBar) findViewById(R.id.hpb);
        mHandler.postDelayed(mRunnable, 3000);

        mFaceProgressBar = (FaceProgressBar) findViewById(R.id.fpb);
//        assert mFaceProgressBar != null;
//        mFaceProgressBar.startAnim();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mFaceProgressBar.finish(true);
            }
        }, 10000);

        BallLoadingView ballLoadingView = (BallLoadingView) findViewById(R.id.blv);
//        assert ballLoadingView != null;
//        ballLoadingView.startAnim();
    }
}
