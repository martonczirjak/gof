package com.example.apptive.gom.ui.customview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.example.apptive.gom.model.Field;
import com.example.apptive.gom.model.Player;

import java.util.List;

public class GameView extends View {
    private static final int COLOR_BG = Color.WHITE;
    private static final int COLOR_LINES = Color.LTGRAY;
    private static final int COLOR_WIN_LINE = Color.CYAN;
    private static final int COLOR_LAST_MOVE = Color.RED;
    private static final int COLOR_PLAYER_X = Color.DKGRAY;
    private static final int COLOR_PLAYER_O = Color.rgb(139, 69, 19);

    private static float MIN_ZOOM = 0.6f;
    private static float MAX_ZOOM = 3.5f;

    private int fieldWidth, fieldHeight;

    private float panX = 0;
    private float panY = 0;

    private Field table[][];

    private double oldDist = 0;
    private float oldTouchCenterX = 0, oldTouchCenterY = 0;
    private PointF mDown = new PointF();

    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float lineWidth, lineHeight, scale;
    private RectF winLine = null;
    private Typeface font, font_bold;

    public interface OnPlayerMoveListener {
        public void onPlayerMove(Field field);
    }

    private OnPlayerMoveListener onPlayerMoveListener;

    public enum TouchMode {
        TOUCH_NORMAL, TOUCH_DRAG, TOUCH_PINCH
    }

    private TouchMode mTouchMode = TouchMode.TOUCH_NORMAL;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mPaint.setTextAlign(Paint.Align.CENTER);

        //font = UtilFontProvider.getPlainFont(context);
        //font_bold = UtilFontProvider.getBoldFont(context);

        mPaint.setTypeface(font);
    }

    public void setOnPlayerMoveListener(OnPlayerMoveListener listener) {
        onPlayerMoveListener = listener;
    }

    public void init(int fieldWidth, int fieldHeight) {
        this.fieldWidth = fieldWidth;
        this.fieldHeight = fieldHeight;
        this.setEnabled(true);

        setScale(1f);

        panX = -(fieldWidth * lineWidth / 2);
        panY = -(fieldHeight * lineHeight / 2);

        winLine = null;

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // long start = System.currentTimeMillis();

        canvas.drawColor(COLOR_BG);

        if (table == null) return;

        mPaint.setStrokeWidth(0);

        int leftVisibleX = (int) (-panX / lineWidth);
        int leftVisibleY = (int) (-panY / lineHeight);

        if (leftVisibleX < 0) leftVisibleX = 0;
        if (leftVisibleY < 0) leftVisibleY = 0;

        int rightVisibleX = (int) (leftVisibleX + (float) getWidth() / lineWidth) + 1;
        int rightVisibleY = (int) (leftVisibleY + (float) getHeight() / lineHeight) + 1;

        if (rightVisibleX > fieldWidth - 1) rightVisibleX = fieldWidth - 1;
        if (rightVisibleY > fieldHeight - 1) rightVisibleY = fieldHeight - 1;

        for (int y = leftVisibleY; y <= rightVisibleY; y++)
            for (int x = leftVisibleX; x <= rightVisibleX; x++) {
                mPaint.setColor(COLOR_LINES);

                float lineX = x * lineWidth;
                float lineY = y * lineHeight;

                // Szélek
                if (x == 0)
                    canvas.drawLine(lineX + panX, lineY + panY, lineX + lineWidth + panX, lineY + panY, mPaint);
                if (x == fieldWidth - 1)
                    canvas.drawLine(lineX + lineWidth + panX, lineY + panY, lineX + lineWidth + panX, lineY + lineHeight + panY, mPaint);

                // Mert feleslegesen nem dolgozunk..
                if (lineX + panX > getWidth() || lineY + panY > getHeight()) break;

                canvas.drawLine(lineX + panX, lineY + panY, lineX + panX, lineY + lineHeight + panY, mPaint);
                canvas.drawLine(lineX + panX, lineY + lineHeight + panY, lineX + lineWidth + panX, lineY + lineHeight + panY, mPaint);


                mPaint.setColor(COLOR_PLAYER_X);

                if (table[x][y] != null) {
                    mPaint.setTypeface(font_bold);
                    mPaint.setColor(COLOR_LAST_MOVE);
                } else
                    mPaint.setTypeface(font);

                DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
                float textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 35, metrics) * scale;

                mPaint.setTextSize(textSize);
                mPaint.setTextAlign(Paint.Align.CENTER);
                // mPaint.setTextSkewX(-0.15f);
                canvas.drawText(table[x][y].getM(), (float) (lineX + lineWidth / 2 + panX), (float) (lineY + lineHeight * 0.75 + panY), mPaint);

            }

        if (winLine != null) {
            mPaint.setColor(COLOR_WIN_LINE);
            mPaint.setStrokeWidth(8f * scale);

            canvas.drawLine(winLine.left * lineWidth + lineWidth / 2 + panX, winLine.top * lineHeight + lineHeight / 2 + panY, winLine.right * lineWidth + lineWidth / 2 + panX, winLine.bottom * lineHeight + lineHeight / 2 + panY, mPaint);
        }

        // long end = System.currentTimeMillis();
        // Log.d("debug", "Run: " + (end - start));
    }

    public void setWinLine(RectF winLine) {
        this.winLine = winLine;

        invalidate();
    }

    public void setTable(Field table[][]) {
        this.table = table;

        invalidate();
    }


    public void setScale(float scale, float touchCenterX, float touchCenterY) {
        this.scale = scale;

        touchCenterX = -touchCenterX / 2f * scale * (scale / MAX_ZOOM);
        touchCenterY = -touchCenterY / 2f * scale * (scale / MAX_ZOOM);

        Log.d("debug", "PanX: " + panX + ", centerX: " + touchCenterX);

        panX = (float) (((panX - oldTouchCenterX) / (fieldWidth * lineWidth)) * (fieldWidth * 100 * scale)) + touchCenterX;
        panY = (float) (((panY - oldTouchCenterY) / (fieldHeight * lineHeight)) * (fieldHeight * 100 * scale)) + touchCenterY;

        oldTouchCenterX = touchCenterX;
        oldTouchCenterY = touchCenterY;

        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        lineWidth = lineHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, metrics) * scale;

        if (winLine != null) {
            float left = (float) (((winLine.left) / (fieldWidth * lineWidth)) * (fieldWidth * 100f * scale));
            float top = (float) (((winLine.top) / (fieldHeight * lineHeight)) * (fieldHeight * 100f * scale));
            float right = (float) (((winLine.right) / (fieldWidth * lineWidth)) * (fieldWidth * 100f * scale));
            float bottom = (float) (((winLine.bottom) / (fieldHeight * lineHeight)) * (fieldHeight * 100f * scale));

            winLine = new RectF(left, top, right, bottom);
        }

        invalidate();
    }

    public void setScale(float scale) {
        setScale(scale, 0, 0);
    }

    public float getScale() {
        return scale;
    }

    public float getPanX() {
        return panX;
    }

    public float getPanY() {
        return panY;
    }

    public void setPanX(float panX) {
        this.panX = panX;
    }

    public void setPanY(float panY) {
        this.panY = panY;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        float x2 = 0, y2 = 0;

        if (event.getPointerCount() > 1) x2 = event.getX(1);
        if (event.getPointerCount() > 1) y2 = event.getY(1);

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                // User elkezi húzni a cuccot
                mDown.set(x, y);
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                // User letette a második ujját --> pinch to zoom
                mTouchMode = TouchMode.TOUCH_PINCH;
                oldDist = Math.sqrt((x2 - x) * (x2 - x) + (y2 - y) * (y2 - y));
                return true;

            case MotionEvent.ACTION_POINTER_UP:
                // Vége a pinch to zoomnak
                mDown.set(0, 0);
                mTouchMode = TouchMode.TOUCH_DRAG;
                oldDist = -1;
                break;

            case MotionEvent.ACTION_UP:
                if (mTouchMode == TouchMode.TOUCH_DRAG || mTouchMode == TouchMode.TOUCH_PINCH) {
                    mDown.set(0, 0);
                    mTouchMode = TouchMode.TOUCH_NORMAL;
                    break;
                }

                if (!isEnabled()) break;

                // lépés
                for (int _y = 0; _y < fieldHeight; _y++)
                    for (int _x = 0; _x < fieldWidth; _x++) {
                        float lineX = table[_x][_y].getX() * lineWidth + panX;
                        float lineY = table[_x][_y].getY() * lineHeight + panY;

                        if (x >= lineX && x <= lineX + lineWidth && y >= lineY && y <= lineY + lineHeight) {
                            onPlayerMoveListener.onPlayerMove(table[_x][_y]);
                            break;
                        }
                    }

                break;

            case MotionEvent.ACTION_MOVE:
                if (mTouchMode == TouchMode.TOUCH_PINCH && event.getPointerCount() == 2) {
                    double newDist = Math.sqrt((x2 - x) * (x2 - x) + (y2 - y) * (y2 - y));
                    if (scale * (newDist / oldDist) < MIN_ZOOM || scale * (newDist / oldDist) > MAX_ZOOM || Math.abs(newDist - oldDist) < 1)
                        break;

                    float centerX = (x + x2) / 2f;
                    float centerY = (y + y2) / 2f;

                    setScale((float) (scale * (newDist / oldDist)), centerX, centerY);
                    oldDist = newDist;

                    break;
                }

                // A user húzza a cuccot.
                float scrollByX = (x - mDown.x);
                float scrollByY = (y - mDown.y);

                if (mDown.x == 0 && mDown.y == 0) {
                    mDown.set(x, y);
                    break;
                }

                // T�vols�g = sqrt( (x2-x1)^2 + (y2-y1)^2 )
                double dist = Math.sqrt((x - mDown.x) * (x - mDown.x) + (y - mDown.y) * (y - mDown.y));
                if ((dist > 50 || mTouchMode == TouchMode.TOUCH_DRAG || mTouchMode == TouchMode.TOUCH_PINCH)) {
                    if (mTouchMode != TouchMode.TOUCH_DRAG && mTouchMode != TouchMode.TOUCH_PINCH)
                        mTouchMode = TouchMode.TOUCH_DRAG;
                    mDown.set(x, y);

                    // Ne tudja tov�bb h�zni a sz�lekn�l
                    boolean exitX = false, exitY = false;
                    if (panX + scrollByX < -fieldWidth * lineWidth + getWidth()) {
                        panX = -fieldWidth * lineWidth + getWidth();
                        exitX = true;
                    }
                    if (panX + scrollByX > 0) {
                        panX = 0;
                        exitX = true;
                    }
                    if (panY + scrollByY < -fieldHeight * lineHeight + getHeight()) {
                        panY = -fieldHeight * lineHeight + getHeight();
                        exitY = true;
                    }
                    if (panY + scrollByY > 0) {
                        panY = 0;
                        exitY = true;
                    }

                    if (!exitX) panX += scrollByX;
                    if (!exitY) panY += scrollByY;
                }

                invalidate();
                break;
        }

        return true;
    }

    // public static void DragScreen(PointF To)
    // {
    // float ratio = (Math.abs(To.x - GameActivity.totalX) > Math.abs(To.y -
    // GameActivity.totalY) ? Math.abs(To.x - GameActivity.totalX) :
    // Math.abs(To.y - GameActivity.totalY));
    // float vecX = (To.x - GameActivity.totalX) / ratio;
    // float vecY = (To.y - GameActivity.totalY) / ratio;
    //
    // // long lastTime = System.currentTimeMillis();
    //
    // while ((int) GameActivity.totalX != (int) To.x || (int)
    // GameActivity.totalY != (int) To.y)
    // {
    // // if (System.currentTimeMillis()-lastTime>=2)
    // // {
    // // lastTime = System.currentTimeMillis();
    // try
    // {
    // Thread.sleep(1);
    // } catch (InterruptedException e)
    // {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // // if (Math.abs(TicTacToe.totalX-newPosX)>3) TicTacToe.totalX +=
    // // vecX;
    // // if (Math.abs(TicTacToe.totalY-newPosY)>3) TicTacToe.totalY +=
    // // vecY;
    //
    // if ((int) GameActivity.totalX != (int) To.x) GameActivity.totalX += vecX;
    // if ((int) GameActivity.totalY != (int) To.y) GameActivity.totalY += vecY;
    //
    // // }
    //
    // // TicTacToe.totalX =
    // // -ToX*GameView.lineWidth+TicTacToe.width/2-GameView.lineWidth/2;
    // // TicTacToe.totalY =
    // // -ToY*GameView.lineHeight+TicTacToe.height/2-GameView.lineHeight/2;
    // }
    // }

}
