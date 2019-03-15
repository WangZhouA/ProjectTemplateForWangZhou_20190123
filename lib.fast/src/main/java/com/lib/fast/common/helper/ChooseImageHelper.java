package com.lib.fast.common.helper;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.elvishew.xlog.XLog;
import com.lib.fast.common.luban.Luban;
import com.lib.fast.common.luban.OnCompressListener;
import com.lib.fast.common.utils.FileProvider7;
import com.lib.fast.common.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by siwei on 2018/3/25.
 * 图片选择helper
 */

public class ChooseImageHelper {

    private Context mContext;
    private static final String IMAGE_SET_FRAGMENT_TAG = "ChooseImageHelper#image_choose_helper_fragment";

    private static final int DEFAULT_IGNORE_SIZE = 150;//忽略压缩的大小

    /**
     * 拍照并切图
     */
    protected static final int DO_TAKE_PHONE_AND_CROP = 0X100;
    /**
     * 拍照
     */
    protected static final int DO_TAKE_PHOTO = 0X101;
    /**
     * 选图并切图
     */
    protected static final int DO_SELECT_PHOTO_AND_CROP = 0X102;
    /**
     * 选图
     */
    protected static final int DO_SELECT_PHOTO = 0X103;

    private int doType;

    //======================请求code===========================

    /**
     * 拍照请求code
     */
    private static final int REQUEST_CODE_TAKE_PHOTO = 0x110;
    /**
     * 拍照请求权限code
     */
    private static final int REQ_PERMISSION_CODE_TAKE_PHOTO = 0X112;
    /**
     * 缺图请求code
     */
    private static final int REQ_CROP = 0X113;
    /**
     * 选择图片并切图code
     */
    private static final int REQ_SELECT_PHOTO_AND_CROP = 0x114;
    /**
     * 选择图片code
     */
    private static final int REQ_SELECT_PHOTO = 0x115;


    private String TAKE_PHOTO_DIR;//拍照文件的目录
    private File imageCompressBeforFile;//图片压缩前的文件
    private String mCurrentTakePhotoPath;
    private String cropPhotoPath;
    private ChooseImageFragment mChooseImageFragment;
    private ChooseOrCropListener mListener;
    private int ignoreImageCompressionSize = DEFAULT_IGNORE_SIZE;//忽略压缩的大小


    public ChooseImageHelper(FragmentActivity activity, String cropPhotoPath) {
        this.mContext = activity;
        this.cropPhotoPath = cropPhotoPath;
        TAKE_PHOTO_DIR = FileUtils.getAppFileDir(activity, "takephoto").getPath();
        mChooseImageFragment = getImageSetFragmentByActivity(activity);
    }

    public ChooseImageHelper(FragmentActivity activity) {
        this.mContext = activity;
        this.cropPhotoPath = FileUtils.getAppFileDir(activity).getPath() + "/crop_img.jpg";
        TAKE_PHOTO_DIR = FileUtils.getAppFileDir(activity, "takephoto").getPath();
        mChooseImageFragment = getImageSetFragmentByActivity(activity);
    }

    public ChooseImageHelper(Fragment fragment, String cropPhotoPath) {
        this.mContext = fragment.getContext();
        this.cropPhotoPath = cropPhotoPath;
        TAKE_PHOTO_DIR = FileUtils.getAppFileDir(mContext, "takephoto").getPath();
        mChooseImageFragment = getImageSetFragmentByV4Fragment(fragment);
    }

    public ChooseImageHelper(Fragment fragment) {
        this.mContext = fragment.getContext();
        this.cropPhotoPath = FileUtils.getAppFileDir(mContext).getPath() + "/crop_img.jpg";
        TAKE_PHOTO_DIR = FileUtils.getAppFileDir(mContext, "takephoto").getPath();
        mChooseImageFragment = getImageSetFragmentByV4Fragment(fragment);
    }

    public void setListener(ChooseOrCropListener listener) {
        this.mListener = listener;
    }

    /**
     * 拍照
     */
    public void takePhoto() {
        doType = DO_TAKE_PHOTO;
        doTakePhoto();
    }

    /**
     * 拍照并切图
     */
    public void takePhotoAndCrop() {
        doType = DO_TAKE_PHONE_AND_CROP;
        doTakePhoto();
    }

    /**
     * 选择图片并切图
     */
    public void selectPhotoAndCrop() {
        doType = DO_SELECT_PHOTO_AND_CROP;
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mChooseImageFragment.startActivityForResult(intent, REQ_SELECT_PHOTO_AND_CROP);
    }

    /**
     * 选择图片并切图
     */
    public void selectPhoto() {
        doType = DO_SELECT_PHOTO;
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mChooseImageFragment.startActivityForResult(intent, REQ_SELECT_PHOTO);
    }

    //data中读取图片地址
    private String handleImage(Intent data) {
        Uri uri = data.getData();
        String imagePath = null;
        if (Build.VERSION.SDK_INT >= 19) {
            if (DocumentsContract.isDocumentUri(mContext, uri)) {
                String docId = DocumentsContract.getDocumentId(uri);
                if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                    String id = docId.split(":")[1];
                    String selection = MediaStore.Images.Media._ID + "=" + id;
                    imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
                } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse("" +
                            "content://downloads/public_downloads"), Long.valueOf(docId));
                    imagePath = getImagePath(contentUri, null);
                }
            } else if ("content".equals(uri.getScheme())) {
                imagePath = getImagePath(uri, null);
            }
        } else {
            imagePath = getImagePath(uri, null);
        }
        return imagePath;
    }

    //获取图片路径
    private String getImagePath(Uri uri, String seletion) {
        String path = null;
        Cursor cursor = mContext.getContentResolver().query(uri, null, seletion, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    //拍照
    private void doTakePhoto() {
        if (ContextCompat.checkSelfPermission(mChooseImageFragment.getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            mChooseImageFragment.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQ_PERMISSION_CODE_TAKE_PHOTO);

        } else if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            mChooseImageFragment.requestPermissions(new String[]{Manifest.permission.CAMERA},
                    REQ_PERMISSION_CODE_TAKE_PHOTO);
        } else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(mContext.getPackageManager()) != null) {
                String filename = new SimpleDateFormat("yyyyMMdd-HHmmss")
                        .format(new Date()) + ".png";
                File file = new File(TAKE_PHOTO_DIR, filename);
                mCurrentTakePhotoPath = file.getPath();

                Uri fileUri = FileProvider7.getUriForFile(mContext, file);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                mChooseImageFragment.startActivityForResult(takePictureIntent, REQUEST_CODE_TAKE_PHOTO);
            }
        }
    }

    //切图
    private void crop(String imagePath) {
        File cropImageFile = new File(cropPhotoPath);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(getImageContentUri(new File(imagePath)), "image/*");
        intent.putExtra("crop", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 500);
        intent.putExtra("outputY", 500);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cropImageFile));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        mChooseImageFragment.startActivityForResult(intent, REQ_CROP);
    }

    //把fileUri转换成ContentUri
    private Uri getImageContentUri(File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = mContext.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return mContext.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    //权限申请返回
    private void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQ_PERMISSION_CODE_TAKE_PHOTO) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhotoAndCrop();
            } else {
                dispatchOnSetFaild();
            }
        }
    }

    //Activity返回
    private void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_TAKE_PHOTO) {
                if (doType == DO_TAKE_PHONE_AND_CROP) {//拍照并切图
                    crop(mCurrentTakePhotoPath);
                } else if (doType == DO_TAKE_PHOTO) {//拍照
                    imageCompression(new File(mCurrentTakePhotoPath));
                }
            } else if (requestCode == REQ_SELECT_PHOTO_AND_CROP && data != null) {
                String filePath = handleImage(data);
                if (TextUtils.isEmpty(filePath)) {
                    dispatchOnSetFaild();
                } else {
                    crop(filePath);
                }
            } else if (requestCode == REQ_CROP) {
                //imageCompression(new File(cropPhotoPath));
                //切图的图片已被系统压缩过,不需要重复的去压缩
                dispatchOnCropSuccess(new File(cropPhotoPath));
            } else if (requestCode == REQ_SELECT_PHOTO) {
                //选择的图片,避免图片过大,进行压缩
                imageCompression(new File(handleImage(data)));
                //dispatchOnCropSuccess(new File(handleImage(data)));
            } else {
                dispatchOnSetFaild();
            }
        }
    }

    //图片压缩
    private void imageCompression(File imageFile) {
        if (imageFile == null) {
            dispatchOnSetFaild();
        } else {
            if (doType == DO_TAKE_PHONE_AND_CROP || doType == DO_SELECT_PHOTO_AND_CROP) {
                imageCompressBeforFile = imageFile;
            }
            String compressFilePath = imageFile.getParent() + "/" + "compress_" + imageFile.getName();
            XLog.tag("ChooseImg").e("compressFilePath:%s", compressFilePath);
            lubanImageCompression(imageFile, compressFilePath);
        }
    }

    //鲁班图片压缩
    private void lubanImageCompression(File imageFile, String compressFilePath) {
        File file = new File(compressFilePath);
        if (!file.exists()) {
            try {
                file.mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Luban.with(mContext)
                .load(imageFile)
                .ignoreBy(ignoreImageCompressionSize)
                .setTargetDir(compressFilePath)
                .setCompressListener(mOnCompressListener)
                .launch();
    }

    private OnCompressListener mOnCompressListener = new OnCompressListener() {
        @Override
        public void onStart() {
        }

        @Override
        public void onSuccess(File file) {
            XLog.tag("ChooseImg").e("mOnCompressListener onSuccess file:" + file);
            dispatchOnCropSuccess(file);
        }

        @Override
        public void onError(Throwable e) {
            XLog.tag("ChooseImg").e("mOnCompressListener e:" + e);
            dispatchOnSetFaild();
        }
    };

    //删除拍照留下的中间图片
    private void deleteFile(File file) {
        if (file.isDirectory()) {
            File[] fs = file.listFiles();
            for (File file1 : fs) {
                deleteFile(file1);
            }
        } else {
            file.delete();
        }
    }

    private void dispatchOnCropSuccess(File file) {
        if (mListener != null) {
            mListener.onChooseSuccess(file);
        }
        //删除拍照或者切图的时候产生的中间文件
        deleteFile(new File(TAKE_PHOTO_DIR));
        if (imageCompressBeforFile != null) {
            deleteFile(imageCompressBeforFile);
        }
    }

    private void dispatchOnSetFaild() {
        if (mListener != null) {
            mListener.onChooseFaild();
        }
        //删除拍照或者切图的时候产生的中间文件
        deleteFile(new File(TAKE_PHOTO_DIR));
        if (imageCompressBeforFile != null) {
            deleteFile(imageCompressBeforFile);
        }
    }

    public interface ChooseOrCropListener {

        /**
         * 图片选择成功
         */
        void onChooseSuccess(File file);

        /**
         * 图片选择失败
         */
        void onChooseFaild();
    }

    private ChooseImageFragment getImageSetFragmentByActivity(FragmentActivity activity) {
        ChooseImageFragment imageSetFragment = findImageSetFragmentByActivity(activity);
        if (imageSetFragment == null) {
            imageSetFragment = new ChooseImageFragment();
            imageSetFragment.setImageSetHelper(this);
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            fragmentManager.beginTransaction().add(imageSetFragment, IMAGE_SET_FRAGMENT_TAG).commit();
            fragmentManager.executePendingTransactions();
        }
        return imageSetFragment;
    }

    private ChooseImageFragment findImageSetFragmentByActivity(FragmentActivity activity) {
        return (ChooseImageFragment) activity.getSupportFragmentManager().findFragmentByTag(IMAGE_SET_FRAGMENT_TAG);
    }

    private ChooseImageFragment getImageSetFragmentByV4Fragment(Fragment fragment) {
        ChooseImageFragment imageSetFragment = findImageSetFragmentByV4Fragment(fragment);
        if (imageSetFragment == null) {
            imageSetFragment = new ChooseImageFragment();
            imageSetFragment.setImageSetHelper(this);
            FragmentManager fragmentManager = fragment.getChildFragmentManager();
            fragmentManager.beginTransaction().add(imageSetFragment, IMAGE_SET_FRAGMENT_TAG).commit();
            fragmentManager.executePendingTransactions();
        }
        return imageSetFragment;
    }

    private ChooseImageFragment findImageSetFragmentByV4Fragment(Fragment fragment) {
        return (ChooseImageFragment) fragment.getChildFragmentManager().findFragmentByTag(IMAGE_SET_FRAGMENT_TAG);
    }


    @SuppressLint("ValidFragment")
    public static class ChooseImageFragment extends Fragment {

        private ChooseImageHelper mImgSetHelper;

        private void setImageSetHelper(ChooseImageHelper helper) {
            mImgSetHelper = helper;
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            mImgSetHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            mImgSetHelper.onActivityResult(requestCode, resultCode, data);
        }

        @Override
        public void onDetach() {
            super.onDetach();
            if (mImgSetHelper != null) {
                mImgSetHelper.release();
                mImgSetHelper = null;
            }
        }
    }

    private void release() {
        mContext = null;
    }
}
