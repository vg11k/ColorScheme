package vg11k.com.colorscheme;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Julien on 17/01/2020.
 */

public class ColorCircle extends View {

    private int m_circleRadius = 25;
    private String m_RGBColor = "#ffffff";
    private int m_RGBBackgroundColor = 1;
    private boolean m_visible = true;

    Paint paint = null;
    public ColorCircle(Context context, String RGBColor)
    {
        super(context);
        paint = new Paint();
        m_RGBColor = RGBColor;
    }

    public ColorCircle(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
    }

    public int getCircleRadius() {return m_circleRadius;}

    public String getRGBColor() {return m_RGBColor;}

    public void setRGBColor(String color) {
        m_RGBColor = color;
    }

    public void setRGBBackgroundColor(int color) {m_RGBBackgroundColor = color;}

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        int radius = m_circleRadius;
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(m_RGBBackgroundColor);
        canvas.drawPaint(paint);
        // Use Color.parseColor to define HTML colors

        paint.setColor(Color.parseColor(m_RGBColor));
        canvas.drawCircle(radius, radius, radius, paint);

        //canvas.translate(getWidth()/2f,getHeight()/2f);
        //canvas.drawCircle(0,0, radius, paint);
    }

    @Override
    protected void onMeasure(int a, int b) {
        if(m_visible) {
            setMeasuredDimension(m_circleRadius * 2, m_circleRadius * 2);
        }
        else {
            setMeasuredDimension(m_circleRadius * 0, m_circleRadius * 0);
        }
    }

    public void setVisible(boolean visible) {
        m_visible = visible;
    }
}
