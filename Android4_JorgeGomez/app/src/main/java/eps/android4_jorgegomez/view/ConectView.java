package eps.android4_jorgegomez.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import eps.android4_jorgegomez.R;
import eps.android4_jorgegomez.model.ConectBoard;
import es.uam.eps.multij.Tablero;

public class ConectView extends View {

    private final String DEBUG = "ERView";
    private int numero;
    private OnPlayListener onPlayListener;
    private Paint backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float heightOfTile;
    private float widthOfTile;
    private float radio;
    private int size;
    private ConectBoard board;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredWidth = 500;
        String wMode, hMode;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;
        if (widthSize < heightSize)
            width = height = heightSize = widthSize;
        else
            width = height = widthSize = heightSize;

        setMeasuredDimension(width, height);
    }

    public ConectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        backgroundPaint.setColor(Color.BLACK);
        linePaint.setStrokeWidth(2);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        widthOfTile = w / size;
        heightOfTile = h / size;

        if (widthOfTile < heightOfTile)
            radio = widthOfTile * 0.3f;
        else
            radio = heightOfTile * 0.3f;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float boardWidth = getWidth();
        float boardHeight = getHeight();
        canvas.drawRect(0, 0, boardWidth, boardHeight, backgroundPaint);
        drawCircles(canvas, linePaint);
    }

    private void drawCircles(Canvas canvas, Paint paint) {
        float centerRaw, centerColumn;
        for (int i = 0; i < size; i++) {
            int pos = size - i - 1;
            centerRaw = heightOfTile * (1 + 2 * pos) / 2f;
            for (int j = 0; j < size; j++) {
                centerColumn = widthOfTile * (1 + 2 * j) / 2f;
                setPaintColor(paint, i, j);
                canvas.drawCircle(centerColumn, centerRaw, radio, paint);
            }
        }
    }

    private void setPaintColor(Paint paint, int i, int j) {
        if (board.getTablero(i, j) == ConectBoard.JUGADOR1)
            paint.setColor(Color.BLUE);
        else if (board.getTablero(i, j) == ConectBoard.VACIO)
            paint.setColor(Color.GRAY);
        else
            paint.setColor(Color.GREEN);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (board.getEstado() != Tablero.EN_CURSO) {
            Snackbar.make(findViewById(R.id.board_erview), R.string.round_already_finished,
                    Snackbar.LENGTH_SHORT).show();
            return super.onTouchEvent(event);
        }
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            onPlayListener.onPlay(fromEventToI(event), fromEventToJ(event));
        }
        return true;
    }

    private int fromEventToI(MotionEvent event) {
        int pos = (int) (event.getY() / heightOfTile);
        return size - pos - 1;
    }

    private int fromEventToJ(MotionEvent event) {
        return (int) (event.getX() / widthOfTile);
    }

    public interface OnPlayListener {
        void onPlay(int row, int column);
    }

    public void setOnPlayListener(OnPlayListener listener) {
        this.onPlayListener = listener;
    }

    public void setBoard(int size, ConectBoard board) {
        this.size = size;
        this.board = board;
    }
}
