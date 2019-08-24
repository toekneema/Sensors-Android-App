package com.example.secondproject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

public class CustomView2 extends View {


    private Paint paint;

    private float[] xvalues = new float[7]; //values
    private float[] yvalues = new float[7];

    private float[] xmeans = new float[7]; //these need to be for the means
    private float[] ymeans = new float[7];

    private float[] xstds = new float[7]; //stds
    private float[] ystds = new float[7];

    private float maxx = 6.0f, maxy = 600.0f, minx = 0.0f, miny = 0.0f,
            locxAxis = 0.0f, locyAxis = 0.0f;
    private int vectorLength;
    private int axes = 1;

    ArrayList<Float> values = new ArrayList<Float>(7);
    ArrayList<Float> means = new ArrayList<Float>(7);
    ArrayList<Float> stds = new ArrayList<Float>(7);

    public void plotData() { //original parameter was: float[] xvalues, float[] yvalues, int axes
        for(int i = 0; i < 7; i++) {
            this.xvalues[i] = i;
            this.yvalues[i] = values.get(i);

            this.xmeans[i] = i;
            this.ymeans[i] = means.get(i);

            this.xstds[i] = i;
            this.ystds[i] = stds.get(i);
        }
        //this.axes = axes;
        vectorLength = xvalues.length;
        // paint = new Paint();

        //getAxes(xvalues, yvalues);
        invalidate();
    }

    public CustomView2(Context context) {
        super(context);
    }

    public CustomView2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomView2(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // xy-plot code
        //

        float canvasHeight = getHeight();
        float canvasWidth = getWidth();

        int[] xvaluesInPixels = toPixel(canvasWidth, minx, maxx, xvalues);
        int[] yvaluesInPixels = toPixel(canvasHeight, miny, maxy, yvalues);

        int[] xmeansInPixels = toPixel(canvasWidth, minx, maxx, xmeans);
        int[] ymeansInPixels = toPixel(canvasHeight, miny, maxy, ymeans);

        int[] xstdsInPixels = toPixel(canvasWidth, minx, maxx, xstds);
        int[] ystdsInPixels = toPixel(canvasHeight, miny, maxy, ystds);

        int locxAxisInPixels = toPixelInt(canvasHeight, miny, maxy, locxAxis);
        int locyAxisInPixels = toPixelInt(canvasWidth, minx, maxx, locyAxis);
        paint = new Paint();
        paint.setStrokeWidth(5);
        int grey = 225;
        canvas.drawARGB(255, grey, grey, grey);
        for (int i = 0; i < vectorLength - 1; i++) {
            paint.setStrokeWidth(7);
            paint.setColor(Color.RED); //creates the values plot
            canvas.drawLine(xvaluesInPixels[i], canvasHeight
                    - yvaluesInPixels[i], xvaluesInPixels[i + 1], canvasHeight
                    - yvaluesInPixels[i + 1], paint);
            canvas.drawCircle(xvaluesInPixels[i], canvasHeight - yvaluesInPixels[i], 10, paint);
            canvas.drawCircle(xvaluesInPixels[i+1], canvasHeight - yvaluesInPixels[i+1], 10, paint);

            paint.setColor(Color.BLUE);
            canvas.drawLine(xmeansInPixels[i], canvasHeight
                    - ymeansInPixels[i], xmeansInPixels[i + 1], canvasHeight
                    - ymeansInPixels[i + 1], paint);
            canvas.drawCircle(xmeansInPixels[i], canvasHeight - ymeansInPixels[i], 10, paint);
            canvas.drawCircle(xmeansInPixels[i+1], canvasHeight - ymeansInPixels[i+1], 10, paint);

            paint.setColor(Color.YELLOW);
            canvas.drawLine(xstdsInPixels[i], canvasHeight
                    - ystdsInPixels[i], xstdsInPixels[i + 1], canvasHeight
                    - ystdsInPixels[i + 1], paint);
            canvas.drawCircle(xstdsInPixels[i], canvasHeight - ystdsInPixels[i], 10, paint);
            canvas.drawCircle(xstdsInPixels[i+1], canvasHeight - ystdsInPixels[i+1], 10, paint);
        }

        //creating the X and Y axis labels
        Paint p1 = new Paint();
        p1.setStrokeWidth(10);
        p1.setTextSize(40);
        p1.setColor(Color.BLACK);
        canvas.drawText("Time (100ms)", (float).40 * getWidth(), (float).96 * getHeight(), p1);
        canvas.drawText("Data", (float) .001 * getWidth(), (float).48 *getHeight(), p1);

        //creating the chart Key/LEGEND
        Paint p2 = new Paint();
        p2.setStrokeWidth(8);
        p2.setTextSize(27);
        p2.setColor(Color.BLACK);

        Paint p3 = new Paint();
        p3.setColor(Color.RED);
        canvas.drawText("Values", (float).3*getWidth(),(float).03*getHeight(), p2);
        canvas.drawCircle((float).34*getWidth(),(float).055*getHeight(), 10, p3);
        p3.setColor(Color.BLUE);
        canvas.drawText("Means", (float).5*getWidth(),(float).03*getHeight(), p2);
        canvas.drawCircle((float).535*getWidth(),(float).055*getHeight(), 10, p3);
        p3.setColor(Color.YELLOW);
        canvas.drawText("STDevs", (float).7*getWidth(),(float).03*getHeight(), p2);
        canvas.drawCircle((float).743*getWidth(),(float).055*getHeight(), 10, p3);


        paint.setColor(Color.BLACK);
        canvas.drawLine(0, canvasHeight - locxAxisInPixels, canvasWidth,
                canvasHeight - locxAxisInPixels, paint);
        canvas.drawLine(locyAxisInPixels, 0, locyAxisInPixels, canvasHeight,
                paint);

        // Automatic axes markings, modify n to control the number of axes
        // labels
        if (axes != 0) {
            float temp = 0.0f;
            int n = 6;
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize(35.0f);
            for (int i = 1; i <= n; i++) {
                temp = Math.round(10 * (minx + (i - 1) * (maxx - minx) / n)) / 10;
                canvas.drawText("" + temp,
                        (float) toPixelInt(canvasWidth, minx, maxx, temp),
                        canvasHeight - locxAxisInPixels + 20, paint);
                temp = Math.round(10 * (miny + (i - 1) * (maxy - miny) / n)) / 10;
                canvas.drawText("" + temp, locyAxisInPixels + 20, canvasHeight
                                - (float) toPixelInt(canvasHeight, miny, maxy, temp),
                        paint);
            }
            canvas.drawText("" + maxx,
                    (float) toPixelInt(canvasWidth, minx, maxx, maxx),
                    canvasHeight - locxAxisInPixels + 20, paint);
            canvas.drawText("" + maxy, locyAxisInPixels + 20, canvasHeight
                    - (float) toPixelInt(canvasHeight, miny, maxy, maxy), paint);
//             canvas.drawText(xAxis,
//             canvasWidth/2,canvasHeight-locxAxisInPixels+45, paint);
//             canvas.drawText(yAxis, locyAxisInPixels-40,canvasHeight/2,
//             paint);
        }
        //end of xy-plot code
        //
    }

    public void sendAndReceiveData(ArrayList<Float> one, ArrayList<Float> two, ArrayList<Float> three) {
        values = one;
        means = two;
        stds = three;
    }


    //everything below this is helper methods

    private int[] toPixel(float pixels, float min, float max, float[] value) {
        double[] p = new double[value.length];
        int[] pint = new int[value.length];

        for (int i = 0; i < value.length; i++) {
            p[i] = .1 * pixels + ((value[i] - min) / (max - min)) * .8 * pixels;
            pint[i] = (int) p[i];
        }

        return (pint);
    }

    private int toPixelInt(float pixels, float min, float max, float value) {

        double p;
        int pint;
        p = .1 * pixels + ((value - min) / (max - min)) * .8 * pixels;
        pint = (int) p;
        return (pint);
    }
}
