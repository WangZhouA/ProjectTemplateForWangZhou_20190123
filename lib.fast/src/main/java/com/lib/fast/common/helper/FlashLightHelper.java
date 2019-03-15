package com.lib.fast.common.helper;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.Collection;

/**
 * 控制闪光灯开关,当Camera是传入的时候,需要自己去负责Camera的释放
 */
public class FlashLightHelper {

    private Camera mCamera;
    private FlashLightSurfaceView mFlashLightSurfaceView;
    private static final String FLASH_LIGHT_VIEW_TAG = "flash_helper_flash_light_surface_view";
    private Activity mContext;

    public FlashLightHelper(Activity context) {
        this.mContext = context;
        mFlashLightSurfaceView = getFlashLightSurfaceView(context);
    }

    private FlashLightSurfaceView getFlashLightSurfaceView(Activity activity) {
        if (activity != null) {
            ViewGroup contenview = activity.findViewById(android.R.id.content);
            mFlashLightSurfaceView = contenview.findViewWithTag(FLASH_LIGHT_VIEW_TAG);
            if (mFlashLightSurfaceView == null) {
                mFlashLightSurfaceView = new FlashLightSurfaceView(activity);
                mFlashLightSurfaceView.setTag(FLASH_LIGHT_VIEW_TAG);
                contenview.addView(mFlashLightSurfaceView);
            }
        }
        return mFlashLightSurfaceView;
    }

    private void removeFlashlightSurfaceView(Activity activity) {
        if (activity != null) {
            ViewGroup contenview = activity.findViewById(android.R.id.content);
            mFlashLightSurfaceView = contenview.findViewWithTag(FLASH_LIGHT_VIEW_TAG);
            if (mFlashLightSurfaceView != null) {
                contenview.removeView(mFlashLightSurfaceView);
            }
        }
    }


    /*    *
     * 是否开启了闪光灯
     * @return
     */
    public boolean isFlashlightOn() {
        try {
            Camera.Parameters parameters = mCamera.getParameters();
            String flashMode = parameters.getFlashMode();
            if (Camera.Parameters.FLASH_MODE_OFF.equals(flashMode)) {
                return false;
            } else if (Camera.Parameters.FLASH_MODE_ON.equals(flashMode) || Camera.Parameters.FLASH_MODE_TORCH.equals(flashMode)) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 打开手电筒
     */
    public void openFlashLight() {
        try {
            if (mCamera != null && flashLightAvailable() && !isFlashlightOn()) {
                Camera.Parameters parameters = mCamera.getParameters();
                String flashModel = findSettableValue(parameters.getSupportedFlashModes(),
                        Camera.Parameters.FLASH_MODE_TORCH, Camera.Parameters.FLASH_MODE_ON, Camera.Parameters.FLASH_MODE_RED_EYE);
                parameters.setFlashMode(flashModel);
                mCamera.setParameters(parameters);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //开启相机
    public void startCamera() {
        if (mCamera != null) return;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int cameraId = 0; cameraId < Camera.getNumberOfCameras(); cameraId++) {
            Camera.getCameraInfo(cameraId, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                mCamera = Camera.open(cameraId);
                //Android 7.0以后,需要开启闪光灯,必须要使用SurfaceView的方式
                mFlashLightSurfaceView.setCamera(mCamera);
                break;
            }
        }
    }

    //停止相机
    public void stopCamera() {
        if (mCamera != null) {
            mFlashLightSurfaceView.stopCameraPreview();
            mFlashLightSurfaceView.setCamera(null);
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * 关闭手电筒
     */
    public void closeFlashLight() {
        try {
            if (mCamera != null && flashLightAvailable() && isFlashlightOn()) {
                Camera.Parameters parameters = mCamera.getParameters();
                String flashModel = findSettableValue(parameters.getSupportedFlashModes(), Camera.Parameters.FLASH_MODE_OFF);
                parameters.setFlashMode(flashModel);
                mCamera.setParameters(parameters);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //查找当前设置支持的模式
    private static String findSettableValue(Collection<String> supportedValues, String... desiredValues) {
        String result = null;
        if (supportedValues != null) {
            for (String desiredValue : desiredValues) {
                if (supportedValues.contains(desiredValue)) {
                    result = desiredValue;
                    break;
                }
            }
        }
        return result;
    }

    //闪光灯是否可用
    public boolean flashLightAvailable() {
        boolean flashAvailable = mFlashLightSurfaceView != null && mFlashLightSurfaceView.flashLightAvailable();
        return flashAvailable;
    }

    /**释放*/
    public void release() {
        stopCamera();
        removeFlashlightSurfaceView(mContext);
        mContext = null;
        mCamera = null;
        mFlashLightSurfaceView = null;
    }

    private class FlashLightSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

        private Camera mCamera;
        private boolean mPreviewing;
        private boolean mSurfaceCreated;

        public FlashLightSurfaceView(Context context) {
            super(context);
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            mSurfaceCreated = true;
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            if (holder.getSurface() == null) {
                return;
            }
            stopCameraPreview();

            post(new Runnable() {
                public void run() {
                    startCameraPreview();
                }
            });
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            mSurfaceCreated = false;
            stopCameraPreview();
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(MeasureSpec.makeMeasureSpec(1, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(1, MeasureSpec.EXACTLY));
        }

        public void setCamera(Camera mCamera) {
            this.mCamera = mCamera;
            if (mCamera != null) {
                getHolder().addCallback(this);
                if (!mPreviewing) {
                    startCameraPreview();
                } else {
                    requestLayout();
                }
            }
        }


        protected void startCameraPreview() {
            if (mCamera != null) {
                try {
                    mPreviewing = true;
                    mCamera.setPreviewDisplay(getHolder());
                    mCamera.startPreview();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        protected void stopCameraPreview() {
            if (mCamera != null) {
                mPreviewing = false;
                mCamera.stopPreview();
            }
        }

        private boolean flashLightAvailable() {
            return mCamera != null && mPreviewing && mSurfaceCreated && getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        }
    }
}
