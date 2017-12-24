package hello.android.com.recorder;

import hello.android.com.recorder.ImuDataPresenter;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


import android.graphics.PixelFormat;
/**
 * Created by chw on 17-12-15.
 */

public class MediaRecord implements android.hardware.Camera.PreviewCallback{
    private MediaRecorder mediarecorder;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;
    private long mRecordTime;
    private String mFilePath;
    private Activity mActivity;
    private static final String DFT_PATH_FILE = "/Pictures/";
    private static  File dataDir;
    private FileOutputStream yuvFos;
    ImuDataPresenter mImuDataPresenter;

    public void onPreviewFrame(byte[] data, Camera camera) {
//            Camera.Parameters parameters = camera.getParameters();
//            int temp = parameters.getPreviewFormat();
//            int wid = parameters.getPreviewSize().width;
//            int height = parameters.getPictureSize().height;
//            int length = data.length;
//            int k = ImageFormat.getBitsPerPixel(parameters.getPreviewFormat());
//            Log.d("wt", "w = " + wid + ", h = " + height + ", data_length =" + length);


            //处理data
//        File sdDir = null;
//        sdDir = Environment.getExternalStorageDirectory();//获取跟目录
//        long currentTime = System.currentTimeMillis() ;
//        mFilePath = sdDir.toString() + DFT_PATH_FILE;
//        System.out.println("mFilePath:" + mFilePath);
            //File yuvFile = getOutputMediaFile();
            try {
                yuvFos.write(data);
                yuvFos.flush();
                mImuDataPresenter.saveSensorData();
            } catch(Exception e) {

            }
//        int imageFormat = parameters.getPreviewFormat();
//        int width = camera.getParameters().getPreviewSize().width;
//        int height = camera.getParameters().getPreviewSize().height;
//        int length = width * height * 3 / 2;
//        byte[] dataYUV420P = new byte[width * height * 3 / 2];
//
//
//// 每一帧的大小
//        int framesize = width * height;
//        int i = 0, j = 0;
//
//// 这块没问题--Y
//        for (i = 0; i < framesize; i++) {
//            dataYUV420P[i] = data[i];
//        }
//// U
//        i = 0;
//        for (j = 0; j < framesize/2; j+=2) {
//            dataYUV420P[i + framesize*5/4] = data[j+framesize];
//            i++;
//        }
//        i = 0;
//        for (j = 1; j < framesize/2;j+=2) {
//            dataYUV420P[i+framesize] = data[j+framesize];
//            i++;
//        }

    }

    private static void createDataDir() {

        String dataFolder = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        dataDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), dataFolder);

        if (!dataDir.exists()) {
            if (!dataDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create image directory");
            }
        }
    }




    private File getOutputMediaFile() {
        // Create a media file name
        //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(new Date());
        String timeStamp = Long.toString(System.currentTimeMillis());
        File mediaFile;

        String folder = dataDir.getPath() + File.separator
                + "cam0";
        new File(folder).mkdirs();
        mediaFile = new File(folder + File.separator +
                timeStamp + ".yuv");
        return mediaFile;
    }

    public MediaRecord(Activity activity, SurfaceView surfaceView){
        mActivity = activity;
        mSurfaceView = surfaceView;
        initView();
    }



    private void initView(){
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mSurfaceHolder.setKeepScreenOn(true);
        mSurfaceHolder.addCallback(new Callback() {
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                releaseCamera();
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                initpreview();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                       int height) {

            }
        });
    }

    protected void releaseCamera() {
        if(mCamera!=null){
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    protected void initpreview() {
//        mCamera = Camera.open(CameraInfo.CAMERA_FACING_BACK);
//        Camera.Parameters parameters = mCamera.getParameters();
//        parameters.setPictureSize(640, 480);
//        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
////        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_FIXED);
////        mCamera.cancelAutoFocus();
//        mCamera.setParameters(parameters);
//        setCameraDisplayOrientation(mActivity, CameraInfo.CAMERA_FACING_BACK, mCamera);   //这里选择后置摄像头就行录制
//        try {
//            mCamera.setPreviewCallback(this);
//            mCamera.setPreviewDisplay(mSurfaceHolder);
//            mCamera.startPreview();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }
    //根据前后置摄像头设置旋转角度
    public static void setCameraDisplayOrientation(Activity activity, int cameraId, Camera camera) {
        CameraInfo info = new CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        System.out.println("rotation:" + rotation);
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }

        int result;
        //前置
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // 后置
            result = (info.orientation - degrees + 360) % 360;
        }
        System.out.println("result:" + result);
        camera.setDisplayOrientation(result);
    }

    public void startRecord(){
        createDataDir();
        try {
            yuvFos = new FileOutputStream(dataDir.getPath() + File.separator +"cam0.yuv", true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mImuDataPresenter = new ImuDataPresenter(mActivity, dataDir);
        mCamera = Camera.open(CameraInfo.CAMERA_FACING_BACK);
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPreviewSize(1280, 720);
        parameters.setPreviewFormat(ImageFormat.YV12);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        //parameters.setPreviewFpsRange();
       // parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_FIXED);
       // mCamera.cancelAutoFocus();
        mCamera.setParameters(parameters);
        setCameraDisplayOrientation(mActivity, CameraInfo.CAMERA_FACING_BACK, mCamera);   //这里选择后置摄像头就行录制
        try {
            mCamera.setPreviewCallback(this);
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        mCamera.unlock();
//        mediarecorder = new MediaRecorder();
//        mediarecorder.reset();
//        mediarecorder.setCamera(mCamera);
//        mediarecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
//        mediarecorder.setVideoEncodingBitRate(1*1024*1024);
//        mediarecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        mediarecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
//        mediarecorder.setOrientationHint(90);
//        mediarecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
//        mediarecorder.setVideoSize(480, 640);
//        mediarecorder.setVideoFrameRate(30);
//        mediarecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
//
//        // 设置视频文件输出的路径
//        if(!isFileExist())
//            return;
//        mFilePath = mFilePath + mRecordTime + ".mp4";
//        mediarecorder.setOutputFile(mFilePath);
//        mediarecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
//
//        try {
//            mediarecorder.prepare();
//            mediarecorder.start();
//        } catch (IllegalStateException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
    }

    public void stopRecord(){
        if (mediarecorder != null) {
            mediarecorder.stop();
            mediarecorder.reset();
            mediarecorder.release();
            mediarecorder = null;
        }
        mRecordTime = 0;
        mFilePath = "";
        mCamera.release();
        mImuDataPresenter.stopSaveData();
    }

    private boolean isFileExist() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist)
        {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
            mRecordTime = System.currentTimeMillis();
            mFilePath = sdDir.toString() + DFT_PATH_FILE;
            System.out.println("mFilePath:" + mFilePath);
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
}
