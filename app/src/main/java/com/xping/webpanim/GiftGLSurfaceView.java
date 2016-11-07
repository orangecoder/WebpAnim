package com.xping.webpanim;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;

import com.facebook.animated.webp.WebPFrame;
import com.facebook.animated.webp.WebPImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by xpmbp on 2016/10/30.
 */

public class GiftGLSurfaceView extends GLSurfaceView {

    private GiftRenderer giftRenderer;

    private Subscription subscription;
    private WebPImage webPImage;

    public GiftGLSurfaceView(Context context) {
        super(context);
        init(context);
    }

    public GiftGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        setZOrderOnTop(true);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);

        giftRenderer = new GiftRenderer(context);
        setRenderer(giftRenderer); // Use a custom renderer
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        cancleAnim();
    }

    public void updateImage(Bitmap bitmap){
        if(bitmap==null || bitmap.isRecycled() || giftRenderer==null){
            return;
        }

        giftRenderer.updateImage(bitmap);
        requestRender();
    }

    private void loadWebp(Integer res){
        Observable.just(res)
                .observeOn(Schedulers.io())
                .map(new Func1<Integer, WebPImage>() {
                    @Override
                    public WebPImage call(Integer integer) {
                        InputStream inputStream = getResources().openRawResource(integer);
                        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                        byte[] data = new byte[100];
                        int count = -1;
                        try {
                            while ((count = inputStream.read(data, 0, 100)) != -1) {
                                outStream.write(data, 0, count);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        byte[] out = outStream.toByteArray();
                        return WebPImage.create(out);
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<WebPImage>() {
                    @Override
                    public void call(WebPImage image) {
                        webPImage = image;
                        showWebp();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e("Main", Log.getStackTraceString(throwable));
                    }
                });
    }

    private void showWebp (){
        subscription = Observable.interval(50, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.computation())
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        return aLong % webPImage.getFrameCount();
                    }
                })
                .observeOn(Schedulers.computation())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        Log.d("Main","draw " + aLong + " frame");

                        WebPFrame frame = webPImage.getFrame(aLong.intValue());
                        Bitmap bitmap = Bitmap.createBitmap(frame.getWidth(), frame.getHeight(), Bitmap.Config.ARGB_8888);
                        frame.renderFrame(frame.getWidth(), frame.getHeight(), bitmap);
                        updateImage(bitmap);

                        if(aLong == webPImage.getFrameCount()-1){
                            cancleAnim();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e("Main", Log.getStackTraceString(throwable));
                    }
                });
    }

    public void cancleAnim(){
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        giftRenderer.clearScreen();
    }

    public void show_100(){
        cancleAnim();
        loadWebp(R.raw.output_100);
    }

    public void show_70(){
        cancleAnim();
        loadWebp(R.raw.output_70);
    }

    public void show_8sec(){
        cancleAnim();
        loadWebp(R.raw.output_8sen);
    }



}
