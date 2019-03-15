package com.lib.fast.common.helper;

import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.annotation.FloatRange;

import com.lib.fast.common.activity.BaseApplication;

import java.io.IOException;

/**
 * 铃声播放工具类
 */
public class RingPlayHelper {

    private MediaPlayer mediaPlayer;
    private static RingPlayHelper sRingPlayHelper;
    private int mRepeatCount;//重复次数
    private int mRingRawRes;
    private float mVolume;

    private boolean isPlaying = false;

    public static RingPlayHelper instance() {
        if (sRingPlayHelper == null) {
            synchronized (RingPlayHelper.class) {
                sRingPlayHelper = new RingPlayHelper();
            }
        }
        return sRingPlayHelper;
    }

    private RingPlayHelper() {
    }

    /**
     * 播放铃声
     */
    public void playRing(int ringRawRes) {
        playRing(ringRawRes, 1, 1);
    }

    /**
     * 播放铃声
     */
    public void playRing(int ringRawRes, @FloatRange(from = 0.0, to = 1.0) float volume) {
        playRing(ringRawRes, volume, 1);
    }

    /**
     * 播放铃声
     */
    public void playRing(int ringRawRes, int repeatCount) {
        playRing(ringRawRes, 1, repeatCount);
    }

    /**
     * 播放铃声
     */
    public void playRing(int ringRawRes, @FloatRange(from = 0.0, to = 1.0) float volume, int repeatCount) {
        mRingRawRes = ringRawRes;
        mVolume = volume;
        mRepeatCount = repeatCount;
        playRing(ringRawRes, volume, mOnCompletionListener);
    }

    private MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            mRepeatCount = Math.max(0, mRepeatCount - 1);
            if(mRepeatCount > 0){
                playRing(mRingRawRes, mVolume, mOnCompletionListener);
            }else{
                isPlaying = false;
            }
        }
    };

    private void playRing(int ringRawRes, @FloatRange(from = 0.0, to = 1.0) float volume, MediaPlayer.OnCompletionListener onCompletionListener){
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            mediaPlayer.stop();
            isPlaying = false;
            mRepeatCount = 0;
        }
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnCompletionListener(onCompletionListener);
        volume = Math.max(0, Math.min(volume, 1));
        AssetFileDescriptor fileDescriptor = BaseApplication.getContext().getResources().openRawResourceFd(ringRawRes);
        if (fileDescriptor != null) {
            try {
                mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(), fileDescriptor.getLength());
                fileDescriptor.close();
                mediaPlayer.setVolume(volume, volume);
                mediaPlayer.prepare();
                mediaPlayer.start();
                isPlaying = true;
            } catch (IOException e) {
                isPlaying = false;
                e.printStackTrace();
                mediaPlayer.stop();
                mediaPlayer = null;
            }
        }
    }


    public boolean isPlaying() {
        return isPlaying;
    }

    /**
     * 释放数据
     */
    public void release() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                mediaPlayer.stop();
            }
            mediaPlayer = null;
        }
    }

}
