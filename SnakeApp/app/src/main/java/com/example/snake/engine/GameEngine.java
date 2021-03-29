package com.example.snake.engine;

import com.example.snake.classes.Coordinate;
import com.example.snake.enums.Direction;
import com.example.snake.enums.GameState;
import com.example.snake.enums.TileType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.example.snake.enums.Direction.South;

public class GameEngine {
    private static final int GameWidth = 28;
    private static final int GameHeight = 42;
    private boolean increase = false;

    private List<Coordinate> snake = new ArrayList<>();
    private List<Coordinate> walls = new ArrayList<>();
    private List<Coordinate> apples = new ArrayList<>();

    private Direction currentDirection = South;
    private boolean portalWalls = false;
    private GameState currentGameState = GameState.Running;
    private int level = 1;
    private Random random = new Random();
    private int score = 0;
    private final int scoreLimit = 50;
    private final int maxLevel = 5;


    private Coordinate snakeHead() {
        return snake.get(0);
    }

    public GameEngine() {

    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public int getLevel() {
        return level;
    }

    public int getScore() {
        return score;
    }

    public void initGame() {
        AddWalls();
        AddSnake();
        removeApples();
        addApple();
    }

    public void nextLevel() {
        level++;
        if (level > 2) portalWalls = true;
        currentDirection = South;
        score = 0;
        initGame();
        currentGameState = GameState.Running;
    }

    public void update() {
        switch (currentDirection) {
            case North:
                updateSnake(0, -1);
                break;
            case South:
                updateSnake(0, 1);
                break;
            case West:
                updateSnake(-1, 0);
                break;
            case East:
                updateSnake(1, 0);
                break;
        }

        for (Coordinate w : walls) {
            if (snake.get(0).equals(w)) {
                if (portalWalls) {
                    if (snakeHead().getX() == 0) snakeHead().setX(GameWidth - 1);
                    else if (snakeHead().getX() == GameWidth - 1) snakeHead().setX(1);
                    else if (snakeHead().getY() == GameHeight - 1) snakeHead().setY(1);
                    else if (snakeHead().getY() == 0) snakeHead().setY(GameHeight - 1);
                    break;
                }
                currentGameState = GameState.Lost;
                break;
            }
        }

        for (int i = 1; i < snake.size(); i++) {
            if (snakeHead().equals(snake.get(i))) {
                currentGameState = GameState.Lost;
                return;
            }
        }

        Coordinate eatenApple = null;

        for (Coordinate a : apples) {
            if (snakeHead().equals(a)) {
                eatenApple = a;
                increase = true;
            }
            if (eatenApple != null) {
                if (level < 5) {
                    if (++score > scoreLimit * level) currentGameState = GameState.Won;
                } else {
                    if (++score == 1036) currentGameState = GameState.Won;
                }

                apples.remove(eatenApple);
                addApple();
            }
        }
    }

    public TileType[][] getMap() {
        TileType[][] map = new TileType[GameWidth][GameHeight];

        for (int x = 0; x < GameWidth; x++)
            for (int y = 0; y < GameHeight; y++)
                map[x][y] = TileType.Nothing;

        for (Coordinate s : snake) {
            map[s.getX()][s.getY()] = TileType.Snake;
        }

        map[snake.get(0).getX()][snake.get(0).getY()] = TileType.SnakeHead;
        for (Coordinate wall : walls) {
            map[wall.getX()][wall.getY()] = TileType.Wall;
        }

        for (Coordinate a : apples) {
            map[a.getX()][a.getY()] = TileType.Apple;
        }

        return map;
    }

    public void UpdateDirection(Direction newDirection) {
        if (Math.abs(newDirection.ordinal() - currentDirection.ordinal()) % 2 == 1)
            currentDirection = newDirection;
    }

    private void updateSnake(int x, int y) {

        int newX = snake.get(snake.size() - 1).getX();
        int newY = snake.get(snake.size() - 1).getY();

        if (increase) {
            snake.add(new Coordinate(newX, newY));
            increase = false;
        }

        for (int i = snake.size() - 1; i > 0; i--) {
            snake.get(i).setX(snake.get(i - 1).getX());
            snake.get(i).setY(snake.get(i - 1).getY());
        }
        snake.get(0).setX(snake.get(0).getX() + x);
        snake.get(0).setY(snake.get(0).getY() + y);
    }

    private void AddSnake() {
        snake.clear();
        snake.add(new Coordinate(2, 10));
        snake.add(new Coordinate(2, 9));
        snake.add(new Coordinate(2, 8));
        snake.add(new Coordinate(2, 7));
    }

    private void AddWalls() {
        walls.clear();
        for (int i = 0; i < GameWidth; i++) {
            walls.add(new Coordinate(i, 0));
            walls.add(new Coordinate(i, GameHeight - 1));
        }

        for (int i = 0; i < GameHeight; i++) {
            walls.add(new Coordinate(0, i));
            walls.add(new Coordinate(GameWidth - 1, i));
        }
        if (level % 2 == 0) {
            for (int i = GameHeight / 3; i < 0.7 * GameHeight; i++) {
                walls.add(new Coordinate(GameWidth / 2, i));
            }
            for (int i = GameWidth / 3; i < 0.7 * GameWidth; i++) {
                walls.add(new Coordinate(i, GameHeight / 2));
            }
        }
    }

    private void addApple() {
        Coordinate coordinate = null;

        boolean added = false;
        while (!added) {
            int x = 1 + random.nextInt(GameWidth - 2);
            int y = 1 + random.nextInt(GameHeight - 2);

            coordinate = new Coordinate(x, y);
            boolean collision = false;

            for (Coordinate s : snake) {
                if (s.equals(coordinate)) {
                    collision = true;
                    break;
                }
            }

            for (Coordinate w : walls) {
                if (w.equals(coordinate)) {
                    collision = true;
                    break;
                }
            }

            if (collision)
                continue;

            for (Coordinate a : apples) {
                if (a.equals(coordinate)) {
                    collision = true;
                    break;
                }
            }

            added = !collision;
        }

        apples.add(coordinate);
    }

    private void removeApples() {
        apples.clear();
    }

    public GameState getCurrentGameState() {
        return currentGameState;
    }
}
