package by.bsuir.popravko.paint.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class PaintView2 extends View {
    //drawing path
    private Path drawPath;
    //drawing and canvas paint
    private Paint drawPaint, canvasPaint;
    //initial color
    private int paintColor = 0xFF660000;
    //canvas
    private Canvas drawCanvas;
    //canvas bitmaap
    private Bitmap canvasBitmap;

    private float x0;
    private float y0;
    private int status;


    void setupDrawing() {
        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(20);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        canvasPaint = new Paint(Paint.DITHER_FLAG | Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        switch (status) {
            case 1: drawCircle(event.getX(), event.getY(), event);
            break;
            case 2: drawRectangle(event.getX(), event.getY(), event);
            break;
            case 3: drawTriangle(event.getX(), event.getY(), event);
            case 4: drawline(event.getX(), event.getY(), event);
            break;
        }


        invalidate();
        return true;
    }

    public PaintView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setupDrawing();
    }

    public void drawline(float touchX, float touchY, MotionEvent event){
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                drawCanvas.drawPath(drawPath, drawPaint);
                drawPath.reset();
                break;
            default:
        }
    }

    public void drawRectangle(float touchX, float touchY, MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                x0 = touchX;
                y0 = touchY;
                break;

            case MotionEvent.ACTION_MOVE:
                drawPath.reset();


                drawPath.addRect(new RectF(x0, y0, touchX, touchY), Path.Direction.CW);
                drawPath.addRect(new RectF(x0, touchY, touchX, y0), Path.Direction.CW);

                drawPath.addRect(new RectF(touchX, y0, x0, touchY), Path.Direction.CW);
                drawPath.addRect(new RectF(touchX, touchY, x0, y0), Path.Direction.CW);

                break;

            case MotionEvent.ACTION_UP:
                drawCanvas.drawPath(drawPath, drawPaint);
                drawPath.reset();

                break;
        }
    }

    public void drawCircle(float touchX, float touchY, MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                x0 = touchX;
                y0 = touchY;
                break;

            case MotionEvent.ACTION_MOVE:

                drawPath.reset();


                if (Math.abs(touchY - y0) > Math.abs(touchX - x0)) {
                    if (y0 < touchY) {
                        drawPath.addCircle(x0, y0, touchY - y0, Path.Direction.CW);
                    } else {
                        drawPath.addCircle(x0, y0, y0 - touchY, Path.Direction.CW);
                    }
                } else {
                    if (x0 < touchX) {
                        drawPath.addCircle(x0, y0, touchX - x0, Path.Direction.CW);
                    } else {
                        drawPath.addCircle(x0, y0, x0 - touchX, Path.Direction.CW);
                    }
                }


                break;

            case MotionEvent.ACTION_UP:
                drawCanvas.drawPath(drawPath, drawPaint);

                drawPath.reset();

                break;
        }
    }

    public void drawTriangle(float touchX, float touchY, MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                x0 = touchX;
                y0 = touchY;
                break;

            case MotionEvent.ACTION_MOVE:

                drawPath.reset();


                if (x0 < touchX) {
                    drawPath.moveTo(x0, y0);
                    drawPath.lineTo(touchX, touchY);

                    drawPath.moveTo(x0, y0);
                    drawPath.lineTo((x0 - Math.abs(x0 - touchX)), touchY);

                    drawPath.lineTo(touchX, touchY);
                } else {

                    drawPath.moveTo(x0, y0);
                    drawPath.lineTo(touchX, touchY);

                    drawPath.moveTo(x0, y0);
                    drawPath.lineTo((x0 + Math.abs(x0 - touchX)), touchY);

                    drawPath.lineTo(touchX, touchY);

                }


                break;

            case MotionEvent.ACTION_UP:

                drawCanvas.drawPath(drawPath, drawPaint);

                drawPath.reset();

                break;
        }
    }

    public void clear() {

        canvasBitmap.eraseColor(Color.WHITE);
        invalidate();
    }

    public int getDrawingColor() {
        return drawPaint.getColor();
    }

    public void setDrawingColor(int argb) {
        drawPaint.setColor(argb);
    }

    public void setLineWidth(int progress) {
        drawPaint.setStrokeWidth(progress);
    }

    public void setStatus(int value) {
        status = value;
    }
}
