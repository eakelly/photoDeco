package elizabethkelly.simplephotodeco;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by Elizabeth on 1/12/16.
 */
public class DrawView extends ImageView {

    private Paint drawBrush, canvasBrush;
    private static final float STROKE_SIZE_SMALL = 5f;
    private static final float STROKE_SIZE_MED = 10f;
    private static final float STROKE_SIZE_BIG = 20f;
    private static final float STROKE_SIZE_BIGGER = 35f;

    private Canvas canvasBoard;
    private Bitmap canvasBmp;

    private Path path;

    public DrawView (Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setBackgroundColor(Color.WHITE);
        path = new Path();
        drawBrush = new Paint();
        drawBrush.setStyle(Paint.Style.STROKE);
        drawBrush.setStrokeCap(Paint.Cap.ROUND);
        drawBrush.setStrokeJoin(Paint.Join.ROUND);
        drawBrush.setAntiAlias(true);
        canvasBrush = new Paint(Paint.DITHER_FLAG);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        canvasBoard = new Canvas(canvasBmp);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(canvasBmp, 0, 0, canvasBrush);
        canvas.drawPath(path, drawBrush);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        final int action = event.getAction();
        float x = event.getX();
        float y = event.getY();

        switch (action & MotionEvent.ACTION_MASK) {
            case (MotionEvent.ACTION_DOWN):
                Log.d("DEBUG_TAG", "Action was DOWN");
                path.moveTo(x, y);
                break;

            case (MotionEvent.ACTION_MOVE):
                Log.d("DEBUG_TAG", "Action was MOVE");
                path.lineTo(x, y);
                break;

            case (MotionEvent.ACTION_UP):
                Log.d("DEBUG_TAG", "Action was UP");
                path.lineTo(x, y);
                canvasBoard.drawPath(path, drawBrush); //draw the path created by the finger drag
                path.reset();
                break;

            default:
                return super.onTouchEvent(event);
        }
        invalidate();
        return true;
    }
    /*
    * clearDrawing() method
    * Clears the drawing on the canvas, but leaves the underlying photo intact
    */
    public void clearDrawing() {
        canvasBoard.drawColor(0, PorterDuff.Mode.CLEAR);
    }

    /*
    * chooseColor() method
    * Lets the user pick a brush color
    */
    public void chooseColor(String color) {
        if(color.equals("Black")) {
            drawBrush.setColor(Color.BLACK);
        }
        else if (color.equals("Red")) {
            drawBrush.setColor(Color.RED);
        }
        else if(color.equals("Blue")) {
            drawBrush.setColor(Color.BLUE);
        }
        else if(color.equals("Green")) {
            drawBrush.setColor(Color.GREEN);
        }
        else if(color.equals("Yellow")) {
            drawBrush.setColor(Color.YELLOW);
        }
        else if(color.equals("White")) {
            drawBrush.setColor(Color.WHITE);
        }
        else if(color.equals("Cyan")) {
            drawBrush.setColor(Color.CYAN);
        }
        else if(color.equals("Grey")) {
            drawBrush.setColor(Color.GRAY);
        }
        else if(color.equals("Magenta")) {
            drawBrush.setColor(Color.MAGENTA);
        }
    }

    /*
    * brushSize() method
    * Lets the user pick a brush size
    */
    public void brushSize(String size) {
        if(size.equals("Small")) {
            drawBrush.setStrokeWidth(STROKE_SIZE_SMALL);
        }
        else if(size.equals("Medium")) {
            drawBrush.setStrokeWidth(STROKE_SIZE_MED); //can use STROKE_SIZE
        }
        else if(size.equals("Big")) {
            drawBrush.setStrokeWidth(STROKE_SIZE_BIG);
        }
        else if(size.equals("Bigger")) {
            drawBrush.setStrokeWidth(STROKE_SIZE_BIGGER);
        }
    }

    /*
    * bkgdColor() method
    * Lets the user pick a background color
    */
    public void bkgdColor(String color) {

        if(color.equals("White")) {
            this.setBackgroundColor(Color.WHITE);
        }
        else if(color.equals("Black")) {
            this.setBackgroundColor(Color.BLACK);
        }
        else if(color.equals("Blue")) {
            this.setBackgroundColor(Color.BLUE);
        }
    }

}
