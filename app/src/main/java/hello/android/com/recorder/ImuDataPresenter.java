package hello.android.com.recorder;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Created by chw on 17-12-16.
 */

public class ImuDataPresenter implements SensorEventListener{
    private Activity mActivity;
    private String mFilePath;
    private FileOutputStream mSensorFos;
    private SensorManager sensorManager;
    private boolean mImuState = false;
    private float[] mArrayAcc = new float[3];
    private float[] mArrayGyro = new float[3];
    private float[] mGeoMagne = new float[3];
    private float[] mRotationVector = new float[5];

    private static final String DFT_PATH_FILE = "/Pictures/";
    private File dirPath;
    public static final int sensorCaptureFPS = 5;      // 每多少毫秒采集一次传感器数据

    public ImuDataPresenter(Activity activity, File dir){
        dirPath = dir;
        mActivity = activity;
        initView();
    }

    private void initView(){
        mFilePath = dirPath.getPath() + File.separator + "imu0.csv";
        File file = new File(mFilePath);
        try {
            mSensorFos = new FileOutputStream(file, true);
            String temp = "#gyroscope(x,y,z), accelerometer(x,y,z)";
            mSensorFos.write((temp + "\n").getBytes());
            mSensorFos.flush();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }
        sensorManager = (SensorManager) mActivity.getSystemService(Context.SENSOR_SERVICE);
        // 加速器
//        Sensor sensora = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//        sensorManager.registerListener(this, sensora, SensorManager.SENSOR_DELAY_GAME);
//        // 陀螺仪
//        Sensor sensorg = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
//        sensorManager.registerListener(this, sensorg, SensorManager.SENSOR_DELAY_GAME);
        // 旋转四元数
        Sensor sensorr = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        sensorManager.registerListener(this, sensorr, SensorManager.SENSOR_DELAY_GAME);
        // 磁力计
//        Sensor sensorMag = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
//        sensorManager.registerListener(this, sensorMag, SensorManager.SENSOR_DELAY_GAME);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
//        if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER) {
//            mArrayAcc = event.values.clone();
//        }
//
//        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
//            mArrayGyro = event.values.clone();
//        }
        // 旋转四元数
        if(event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            int k = event.values.length;
            mRotationVector = event.values.clone();
        }

//        if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
//            mGeoMagne = event.values.clone();
//        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private boolean isFileExist() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist)
        {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
            mFilePath = sdDir.toString() + DFT_PATH_FILE;
            createPath(mFilePath);
        }
        else {
            Toast.makeText(mActivity, "SD卡不存在！", Toast.LENGTH_SHORT).show();
        }
        return sdCardExist;
    }

    private void createPath(String path) {
        File pathDir = new File(path);
        if (!pathDir.exists()) {
            pathDir.mkdirs();
        }
        return;
    }

    public void saveSensorData() {
        try {
            String timeStamp = Long.toString(System.currentTimeMillis());
            //String tmp =  timeStamp + "," + mArrayGyro[0] + "," + mArrayGyro[1] + "," + mArrayGyro[2] + ","
            //        + mArrayAcc[0] + "," + mArrayAcc[1] + "," + mArrayAcc[2];
            //float rotateMatrix[] = new float[9];
            float temp[] = new float[9];

            //boolean isRight = sensorManager.getRotationMatrix(rotateMatrix,null,mArrayAcc, mGeoMagne);
            if(mRotationVector[4] == 0) {
//                float m0 = mRotationVector[3], m1= mRotationVector[0],
//                        m2 = mRotationVector[1], m3 = mRotationVector[2];
//                rotateMatrix[0] = 1 - 2*m2*m2 - 2*m3*m3;
//                rotateMatrix[1] = 2*m1*m2 + 2*m0*m3;
//                rotateMatrix[2] = 2*m1*m3 - 2*m0*m2;
//                rotateMatrix[3] = 2*m1*m2 - 2*m0*m3;
//                rotateMatrix[4] = 1 - 2*m1*m1 - 2*m3*m3;
//                rotateMatrix[5] = 2*m2*m3 + 2*m0*m1;
//                rotateMatrix[6] = 2*m1*m3 + 2*m0*m2;
//                rotateMatrix[7] = 2*m2*m3 - 2*m0*m1;
//                rotateMatrix[8] = 1-2*m1*m1 - 2*m2*m2;
//                float ff[] = {m0,m1,m2,m3};
                //mRotationVector[0];
//                mRotationVector[0] = mRotationVector[3];
//                mRotationVector[3] = ff;
                SensorManager.getRotationMatrixFromVector(temp, mRotationVector);
            }

//            final float[] rotationMatrix = new float[9];
//            mSensorManager.getRotationMatrix(rotationMatrix, null,
//                    accelerometerReading, magnetometerReading);
//
//// Express the updated rotation matrix as three orientation angles.
//            final float[] orientationAngles = new float[3];
//            mSensorManager.getOrientation(rotationMatrix, orientationAngles);
            //mSensorFos.write((tmp + "\n").getBytes());
//            String matrixStr ="{";
//            for(int i = 0; i < 3; i++)
//                for(int j =0; j < 3; j++) {
//                    matrixStr += temp[j * 3 + i] +",";
//                }
//            matrixStr += "}";
           // Log.d("wt", isRight + "\n" + temp[1] + temp[2] + ",\n"  + Arrays.toString(mRotationVector) + "\n" + Arrays.toString(rotateMatrix) + "\n" + Arrays.toString(temp));
            mSensorFos.write(( Arrays.toString(temp) + "\n").getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startSaveData() {
        mImuState = true;

        if(!isFileExist())
            return;
        mFilePath = mFilePath + "imu0.csv";
        File file = new File(mFilePath);
        try {
            mSensorFos = new FileOutputStream(file, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                    while (mImuState) {
                        Thread.sleep(sensorCaptureFPS);
                        saveSensorData();
                    }
                    mSensorFos.flush();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void stopSaveData() {
        mImuState = false;
        try {
            Thread.sleep(sensorCaptureFPS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try{
            if(mSensorFos != null){
                mSensorFos.close();
                mSensorFos = null;
            }
        } catch (IOException e){
            e.printStackTrace();
        }

        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
        mFilePath = "";
    }
}
