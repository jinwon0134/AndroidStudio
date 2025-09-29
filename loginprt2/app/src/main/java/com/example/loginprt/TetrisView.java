package com.example.loginprt;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.Random;

public class TetrisView extends View {

    private static final int ROWS = 20;
    private static final int COLS = 10;

    private int[][] board = new int[ROWS][COLS];
    private int cellSize;

    private Block currentBlock;
    private int blockRow = 0, blockCol = 4;

    private Paint paint = new Paint();
    private Handler handler = new Handler();
    private int score = 0;

    // 버튼 영역 제한선 높이
    private int controlLineY = 0;
    private int controlsHeight = 200; // px, 필요시 dp->px 변환

    private Runnable tick = new Runnable() {
        @Override
        public void run() {
            moveDown();
            invalidate();
            handler.postDelayed(this, 500);
        }
    };

    private OnScoreChangeListener scoreChangeListener;
    private OnGameOverListener gameOverListener;

    public TetrisView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setFocusable(true);
        spawnBlock();
        handler.postDelayed(tick, 500);
    }

    private void spawnBlock() {
        currentBlock = new Block();
        blockRow = 0;
        blockCol = COLS / 2 - 1;
        if (!canMove(blockRow, blockCol, currentBlock.shape)) {
            if (gameOverListener != null) gameOverListener.onGameOver();
            handler.removeCallbacks(tick);
        }
    }

    private boolean canMove(int row, int col, int[][] shape) {
        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[r].length; c++) {
                if (shape[r][c] != 0) {
                    int newRow = row + r;
                    int newCol = col + c;
                    if (newRow < 0 || newRow >= ROWS || newCol < 0 || newCol >= COLS) return false;
                    if (board[newRow][newCol] != 0) return false;
                }
            }
        }
        return true;
    }

    private void mergeBlock() {
        int[][] shape = currentBlock.shape;
        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[r].length; c++) {
                if (shape[r][c] != 0) {
                    board[blockRow + r][blockCol + c] = currentBlock.color;
                }
            }
        }
        clearLines();
        spawnBlock();
    }

    private void clearLines() {
        for (int r = ROWS - 1; r >= 0; r--) {
            boolean full = true;
            for (int c = 0; c < COLS; c++) {
                if (board[r][c] == 0) {
                    full = false;
                    break;
                }
            }
            if (full) {
                score += 10;
                if (scoreChangeListener != null) scoreChangeListener.onScoreChange(score);
                for (int i = r; i > 0; i--) {
                    System.arraycopy(board[i - 1], 0, board[i], 0, COLS);
                }
                for (int c = 0; c < COLS; c++) board[0][c] = 0;
                r++;
            }
        }
    }

    private void moveDown() {
        int nextBottom = (blockRow + currentBlock.shape.length) * cellSize;
        if (nextBottom < controlLineY && canMove(blockRow + 1, blockCol, currentBlock.shape)) {
            blockRow++;
        } else {
            mergeBlock();
        }
    }

    public void restartGame() {
        board = new int[ROWS][COLS];
        score = 0;
        if (scoreChangeListener != null) scoreChangeListener.onScoreChange(score);
        spawnBlock();
        handler.postDelayed(tick, 500);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        cellSize = getWidth() / COLS;
        controlLineY = getHeight() - controlsHeight;

        // 제한선 표시 (디버그용)
        paint.setColor(Color.RED);
        paint.setStrokeWidth(4);
        canvas.drawLine(0, controlLineY, getWidth(), controlLineY, paint);

        // 보드 그리기
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (board[r][c] != 0) {
                    paint.setColor(board[r][c]);
                    canvas.drawRect(c * cellSize, r * cellSize,
                            (c + 1) * cellSize, (r + 1) * cellSize, paint);
                }
            }
        }

        // 현재 블록 그리기
        int[][] shape = currentBlock.shape;
        paint.setColor(currentBlock.color);
        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[r].length; c++) {
                if (shape[r][c] != 0) {
                    canvas.drawRect((blockCol + c) * cellSize,
                            (blockRow + r) * cellSize,
                            (blockCol + c + 1) * cellSize,
                            (blockRow + r + 1) * cellSize, paint);
                }
            }
        }
    }

    // 버튼 이동/회전
    public void moveLeft() {
        if (canMove(blockRow, blockCol - 1, currentBlock.shape)) {
            blockCol--;
            invalidate();
        }
    }

    public void moveRight() {
        if (canMove(blockRow, blockCol + 1, currentBlock.shape)) {
            blockCol++;
            invalidate();
        }
    }

    public void moveDownFast() {
        moveDown();
        invalidate();
    }

    public void rotateBlock() {
        int[][] rotated = currentBlock.rotate();
        if (canMove(blockRow, blockCol, rotated)) {
            currentBlock.shape = rotated;
            invalidate();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_LEFT:
                moveLeft();
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                moveRight();
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                moveDownFast();
                break;
            case KeyEvent.KEYCODE_SPACE:
            case KeyEvent.KEYCODE_DPAD_UP:
                rotateBlock();
                break;
        }
        return true;
    }

    public void setOnScoreChangeListener(OnScoreChangeListener listener) {
        this.scoreChangeListener = listener;
    }

    public void setOnGameOverListener(OnGameOverListener listener) {
        this.gameOverListener = listener;
    }

    public interface OnScoreChangeListener {
        void onScoreChange(int score);
    }

    public interface OnGameOverListener {
        void onGameOver();
    }

    // Block class
    private static class Block {
        int[][] shape;
        int color;

        private static final int[][][] SHAPES = {
                {{1,1,1,1}}, {{1,1},{1,1}}, {{0,1,0},{1,1,1}},
                {{1,1,0},{0,1,1}}, {{0,1,1},{1,1,0}}, {{1,0,0},{1,1,1}},
                {{0,0,1},{1,1,1}}
        };

        private static final int[] COLORS = {
                Color.CYAN, Color.YELLOW, Color.MAGENTA,
                Color.GREEN, Color.RED, Color.BLUE, Color.rgb(255,165,0)
        };

        Block() {
            Random rand = new Random();
            int index = rand.nextInt(SHAPES.length);
            shape = SHAPES[index];
            color = COLORS[index];
        }

        int[][] rotate() {
            int m = shape.length;
            int n = shape[0].length;
            int[][] rotated = new int[n][m];
            for (int i=0;i<m;i++)
                for (int j=0;j<n;j++)
                    rotated[j][m-1-i] = shape[i][j];
            return rotated;
        }
    }
}
