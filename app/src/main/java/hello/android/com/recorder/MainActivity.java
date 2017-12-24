package hello.android.com.recorder;

import android.app.Activity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    private Button mStartRecord;
    private Button mStopRecord;
    private boolean mRecordState = false;
    private boolean mImuState = false;
    private MediaRecord mediaRecord;
    private ImuDataPresenter imuDataPresenter;
    private SurfaceView mSurfaceView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("onCreate:" );
        initView();
        setupView();
    }

    private void initView(){
        mStartRecord = (Button) findViewById(R.id.start) ;
        mStopRecord = (Button) findViewById(R.id.stop) ;
        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceview) ;
        mediaRecord = new MediaRecord(this, mSurfaceView);
       // imuDataPresenter = new ImuDataPresenter(this);
    }

    private void setupView(){
        mStartRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecordState = true;
                mImuState = true;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mediaRecord.startRecord();
                     //   imuDataPresenter.startSaveData();
                    }
                }).start();
            }
        });

        mStopRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mImuState = false;
                mRecordState = false;
              //  imuDataPresenter.stopSaveData();
                mediaRecord.stopRecord();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaRecord != null){
            mediaRecord.stopRecord();
        }
        if(imuDataPresenter != null){
            imuDataPresenter.stopSaveData();
        }
    }
}
