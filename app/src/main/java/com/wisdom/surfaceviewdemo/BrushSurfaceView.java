package com.wisdom.surfaceviewdemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * @author xumao
 */
public class BrushSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private SurfaceHolder mHolder;
    private Canvas mCanvas;
    private boolean mIsDrawing;
    private Paint mPaint;
    private Path mPath;
    private int x = 0, y;

    public BrushSurfaceView(Context context) {
        super(context);
        initView();
    }

    public BrushSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public BrushSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mHolder = getHolder();
        mHolder.addCallback(this);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setKeepScreenOn(true);
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(20);
        mPaint.setAntiAlias(true);
        mPath = new Path();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mIsDrawing = true;
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mIsDrawing = false;
    }

    @Override
    public void run() {
        long start = System.currentTimeMillis();
        while (mIsDrawing) {
            draw();
        }
        long end = System.currentTimeMillis();
        if (end - start < 100) {
            try {
                Thread.sleep(100 - (end - start));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPath.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                mPath.lineTo(x, y);
                break;
            default:
                break;
        }
        return true;
    }

    private void draw() {
        try {
            mCanvas = mHolder.lockCanvas();
            mCanvas.drawColor(Color.YELLOW);
            mCanvas.drawPath(mPath, mPaint);
        } catch (Exception ignored) {

        } finally {
            if (mCanvas != null) {
                mHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }

    public void clearAll() {
        mHolder.addCallback(this);
        mPath = new Path();
    }
}
