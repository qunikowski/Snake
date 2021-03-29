package com.example.snake.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.snake.enums.TileType;

public class SnakeView extends View {

    private Paint mPaint = new Paint();
    private TileType[][] snakeViewMap;

    public SnakeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setSnakeViewMap(TileType[][] map) {
        this.snakeViewMap = map;
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (snakeViewMap != null) {
            float tileSizeX = canvas.getWidth() / snakeViewMap.length;
            float tileSizeY = canvas.getHeight() / snakeViewMap[0].length;

            float cellRadius = Math.max(tileSizeX, tileSizeY) / 2;
            for (int x = 0; x < snakeViewMap.length; x++) {
                for (int y = 0; y < snakeViewMap[0].length; y++) {
                    switch (snakeViewMap[x][y]) {

                        case Nothing:
                            mPaint.setColor(Color.BLACK);
                            break;
                        case SnakeHead:
                            mPaint.setColor(Color.YELLOW);
                            break;
                        case Apple:
                            mPaint.setColor(Color.RED);
                            break;
                        case Wall:
                            mPaint.setColor(Color.BLUE);
                            break;
                        case Snake:
                            mPaint.setColor(Color.GREEN);
                            break;
                    }
                    /*if(snakeViewMap[x][y] == TileType.Apple){
                        canvas.drawRect(x * tileSizeX + tileSizeX / 2f - cellRadius, y * tileSizeY + tileSizeY / 2f + cellRadius, x * tileSizeX + tileSizeX / 2f + cellRadius,y * tileSizeY + tileSizeY / 2f - cellRadius, new Paint(Color.BLACK));
                        canvas.drawCircle(x*tileSizeX + tileSizeX/2f , y*tileSizeY + tileSizeY/2f , cellRadius/2, mPaint);
                    }else{*/
                    canvas.drawRect(x * tileSizeX + tileSizeX / 2f - cellRadius, y * tileSizeY + tileSizeY / 2f + cellRadius, x * tileSizeX + tileSizeX / 2f + cellRadius, y * tileSizeY + tileSizeY / 2f - cellRadius, mPaint);

                }
            }

        }
    }
}
