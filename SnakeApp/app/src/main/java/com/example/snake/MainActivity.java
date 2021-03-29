package com.example.snake;

import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.snake.engine.GameEngine;
import com.example.snake.enums.Direction;
import com.example.snake.enums.GameState;
import com.example.snake.views.SnakeView;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    private GameEngine gameEngine;
    private SnakeView snakeView;
    private final Handler handler = new Handler();
    private long updateDelay = 120;

    private float prevX, prevY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameEngine = new GameEngine();
        gameEngine.initGame();

        snakeView = (SnakeView) findViewById(R.id.snakeView);
        snakeView.setOnTouchListener(this);

        startUpdateHandler();

    }

    private String getScore() {
        return Integer.toString(gameEngine.getScore());
    }

    private void startUpdateHandler() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                TextView textView = (TextView) findViewById(R.id.score);
                textView.setText("Wynik: " + getScore());
                gameEngine.update();

                if (gameEngine.getCurrentGameState() == GameState.Won) {
                    updateDelay = updateDelay - updateDelay / 10;
                    if (gameEngine.getLevel() < gameEngine.getMaxLevel()) {

                        gameEngine.nextLevel();
                        if (gameEngine.getLevel() == 3) {
                            OnDeactivatingWalls();
                        } else {
                            OnGameWon();
                        }
                    } else {
                        OnEndOfTheGame();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (gameEngine.getCurrentGameState() == GameState.Running) {
                    handler.postDelayed(this, updateDelay);
                }

                if (gameEngine.getCurrentGameState() == GameState.Lost) {
                    OnGameLost();
                }

                snakeView.setSnakeViewMap(gameEngine.getMap());
                snakeView.invalidate();

            }
        }, updateDelay);
    }

    private void OnDeactivatingWalls() {
        Toast.makeText(this, "!!!Ściany --> portale!!!", Toast.LENGTH_SHORT).show();
    }

    private void OnEndOfTheGame() {
        String text = "Wygrałeś z wynikiem: " + getScore();
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private void OnGameWon() {
        Toast.makeText(this, "Wygrałeś!", Toast.LENGTH_SHORT).show();
    }

    private void OnGameLost() {
        Toast.makeText(this, "Koniec!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                prevX = event.getX();
                prevY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                float newX = event.getX();
                float newY = event.getY();

                if (Math.abs(newX - prevX) > Math.abs(newY - prevY)) {
                    if (newX > prevX) {
                        gameEngine.UpdateDirection(Direction.East);
                    } else {
                        gameEngine.UpdateDirection(Direction.West);
                    }
                } else {
                    if (newY > prevY) {
                        gameEngine.UpdateDirection(Direction.South);
                    } else {
                        gameEngine.UpdateDirection(Direction.North);
                    }
                }
                break;
        }
        return true;
    }
}
