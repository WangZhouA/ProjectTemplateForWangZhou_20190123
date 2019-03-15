package me.iwf.photopicker;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

/**
 * 创建日期：2018/11/7 on 11:58
 * 描述: glide使用工具类
 */
public class GlideUtil {

    private  final  static String TAG ="GlideUtil";

    public static void DisplayImagesLoad(final Context context, final String url, final ImageView imageView, final Handler handler){

        final RequestOptions options = new RequestOptions();
        options.skipMemoryCache(false);
        options.diskCacheStrategy(DiskCacheStrategy.ALL);
        options.priority(Priority.HIGH);
        options.centerCrop();
        options.placeholder(R.drawable.__picker_ic_photo_black_48dp);
        options.error(R.drawable.__picker_ic_broken_image_black_48dp);
        Glide.with(context).load(url)
                .apply(options)
                .thumbnail(0.1f)

                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        String message = e.getMessage();
                        Log.e(TAG,message);
                        //有些图片加载失败时加载第二次
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Glide.with(context)
                                        .load(url)
                                        .apply(options)
                                        .into(imageView);
                            }
                        });


                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(imageView);
    }


    public static void DisplayImagesLoad(final Context context, final String url, final ImageView imageView, final Handler handler,int errorId,int placeholderId){

        final RequestOptions options = new RequestOptions();
        options.skipMemoryCache(false);
        options.diskCacheStrategy(DiskCacheStrategy.ALL);
        options.priority(Priority.HIGH);
        options.error(errorId);
        options.centerCrop();
        options.placeholder(placeholderId);
        Glide.with(context).load(url)
                .apply(options)
                .thumbnail(0.1f)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        String message = e.getMessage();
                        Log.e(TAG,message);
                        //有些图片加载失败时加载第二次
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Glide.with(context)
                                        .load(url)
                                        .apply(options)
                                        .into(imageView);
                            }
                        });
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(imageView);
    }


    public static void DisplayImagesLoad(final Context context, final String url, final ImageView imageView){

        final RequestOptions options = new RequestOptions();
        options.skipMemoryCache(false);
        options.diskCacheStrategy(DiskCacheStrategy.ALL);
        options.priority(Priority.HIGH);
        options.centerCrop();
        options.placeholder(R.drawable.__picker_ic_photo_black_48dp);
        options.error(R.drawable.__picker_ic_broken_image_black_48dp);
        Glide.with(context).load(url)
                .apply(options)
                .into(imageView);
    }

    }
