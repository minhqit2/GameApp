package com.example.gameapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class MyView extends View implements Runnable {
    @Override
    public void run(){

    }
    private Bitmap ball;
    private Bitmap ballResize;

    private Bitmap thanhngang;
    private Bitmap thanhngangsize;

    private Bitmap gameover;

//    int point = 0;


    private SoundManager soundManager;

    ArrayList<Brick> lists;



    private int x1=100, y1=100, dx1=50, dy1=50;
    private int x2=150, y2=150, dx2=20, dy2=20;

    private int x3 = 400 - getWidth(), y3 = 1480 - getHeight();

    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        ball = BitmapFactory.decodeResource(getResources(),R.drawable.ball);
        ballResize = Bitmap.createScaledBitmap(ball,50,50,false);

        thanhngang =BitmapFactory.decodeResource(getResources(),R.drawable.thanhngang);
        thanhngangsize =Bitmap.createScaledBitmap(thanhngang, 240, 150, false);

        gameover = BitmapFactory.decodeResource( getResources(), R.drawable.gameover );

        lists = new ArrayList<Brick>();

        for (int i = 0; i < 9; i++) {
            Brick brick = new Brick( 110 * i + 50, 10, 105, 70 );
            Brick brick2 = new Brick( 110 * i + 50, 85, 105, 70 );
            Brick brick3 = new Brick( 110 * i + 50, 160, 105, 70 );
            Brick brick4 = new Brick( 110 * i + 50, 235, 105, 70 );
            Brick brick5 = new Brick( 110 * i + 50, 310, 105, 70 );
            /*Brick brick6 = new Brick(110 * i + 50, 385, 105, 70);*/

            lists.add( brick );
            lists.add( brick2 );
            lists.add( brick3 );
            lists.add( brick4 );
            lists.add( brick5 );
            /* lists.add( brick6 );*/
        }
        soundManager = SoundManager.getInstance();
        soundManager.init(context);

    }


    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        int x = getWidth();
        int y = getHeight();
        int radius ;

        radius = 100;

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        canvas.drawPaint(paint);
        paint.setColor(Color.parseColor(("#CD5C5C")));
       /* canvas.drawCircle(x1, y1, 20, paint);*/
        canvas.drawBitmap(ballResize, x2, y2, null);
        canvas.drawBitmap(thanhngangsize, x3, y3, null);


        Paint paint2 = new Paint();
        paint2.setColor( Color.GREEN );
        paint2.setTextSize( 120 );
//        canvas.drawText( String.valueOf( point ), 480, 590, paint2 );

        /*cho ni la cho no vang len khi cham thanh ok*/

        if (y2 > y3 + 30) {
            if (x2 > x3 - 20 && x2 < x3 + 250) {
                soundManager.playSound( R.raw.sound2 );
                dy2 = -dy2;
            } else {
                canvas.drawBitmap(gameover, 220, 200, null);
                return;


            }
        }
        for (Brick element : lists) {
            element.drawBrick( canvas, paint );
            if (element.getVisibility()) {
                //kiểm tra ball va chạm với gạch
                // viên nào bể thì set visible = false

                if (y2 < element.getY()) {
                    if (x2 > element.getX() && x2 < (element.getX() + element.getWidth() /*+ element.getHeight()*/)) {
                        element.setInVisible();
                        soundManager.playSound( R.raw.sound1 );
//                        point += 10;
                        dy2 = -dy2;

                    }
                }

            }
        }

        update();
        invalidate();
    }

    private void update() {
        x1 += dx1;
        y1 += dy1;

        if (x1 > this.getWidth() || x1 < 0)
            dx1 = -dx1;
        if (y1 > this.getHeight() || y1 < 0)
            dy1 = -dy1;

        // bong 2
        x2 += dx2;
        y2 += dy2;

        if (x2 > this.getWidth() || x2 < 0) dx2 = -dx2;
        if (y2 > this.getHeight() || y2 < 0) dy2 = -dy2;
    }
    @Override
    public  boolean onTouchEvent(MotionEvent event){

        boolean handled = false;

        int xTouch;
        int yTouch;
        int actionIndex = event.getActionIndex();

        switch (event.getActionMasked()){
            case  MotionEvent.ACTION_DOWN:

                xTouch = (int ) event.getX(0);
                yTouch = (int) event.getY(0);


                handled = true;
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                xTouch = (int) event.getX(actionIndex);
                yTouch = (int) event.getY(actionIndex);

                handled = true;
                break;

            case  MotionEvent.ACTION_MOVE:
                final int pointterCount = event.getPointerCount();
                for (actionIndex = 0; actionIndex < pointterCount;actionIndex++){

                    xTouch = (int) event.getX(actionIndex);
                    yTouch = (int) event.getY(actionIndex);

                    x3 = xTouch;
                    /*cho ni la cho thanh di chuyen ok */

                }

                invalidate();
                handled = true;
                break;

            case MotionEvent.ACTION_UP:
                invalidate();
                handled = true;
                break;

            case MotionEvent.ACTION_CANCEL:

                handled = true;
                break;


             default:
                 // k lam gi
                 break;



        }
        return super.onTouchEvent(event) || handled;
    }
}