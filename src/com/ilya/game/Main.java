package com.ilya.game;

import com.ilya.game.graphics.GraphicsModule;
import com.ilya.game.graphics.LwjglGraphicsModule;
import com.ilya.game.keyboard.KeyboardHandleModule;
import com.ilya.game.keyboard.LwjglKeyboardHandleModule;

import java.util.Random;

import static com.ilya.game.Constants.*;
public class Main {
    private static class ShiftRowResult{
        boolean didAnythingMove;
        int[] shiftedRow;
    }

    private static int score; //Сумма всех чисел на поле
    private static boolean endOfGame; //Флаг для завершения основного цикла программы
    private static boolean isThere2048; //Хранит информацию о том, удалось ли игроку создать плитку 2048 (флаг победы)
    private static Direction direction; //Направление, в котором требуется сдвиг клеток поля.
    private static GraphicsModule graphicsModule;
    private static KeyboardHandleModule keyboardModule;
    private static GameField gameField;

    public static void main(String[] args){
	    initFields();
        
        createInitialCells();

        while(!endOfGame){
            input();

            logic();

            graphicsModule.draw(gameField);
        }

        graphicsModule.destroy();

    }

    private static void initFields() {
        score = 0;
        endOfGame = false;
        isThere2048 = false;
        direction = Direction.AWAITING;
        graphicsModule = new LwjglGraphicsModule();
        keyboardModule = new LwjglKeyboardHandleModule();
        gameField = new GameField();
    }

    private static void input() {
        keyboardModule.update();

        direction = keyboardModule.lastDirectionKeyPressed();

        endOfGame = endOfGame || graphicsModule.isCloseRequested() || keyboardModule.wasEscPressed();
    }

    private static void createInitialCells() {
        for (int i = 0; i < COUNT_INITITAL_CELLS; i++){
            generateNewCell();
        }
    }

    private static void generateNewCell() {
        int state = (new Random().nextInt(100) <= CHANCE_OF_LUCKY_SPAWN)
                ? LUCKY_INITIAL_CELL_STATE : INITIAL_CELL_STATE;

        int randomX, randomY;

        randomX = new Random().nextInt(COUNT_CELLS_X);
        int currentX = randomX;

        randomY = new Random().nextInt(COUNT_CELLS_Y);
        int currentY = randomY;

        boolean placed = false;
        while (!placed){
            if(gameField.getState(currentX, currentY) == 0){
                gameField.setState(currentX, currentY, state);
                placed = true;
            }
            else {
                if(currentX +1 < COUNT_CELLS_X){
                    currentX++;
                }
                else{
                    currentX = 0;
                    if(currentY + 1 < COUNT_CELLS_Y){
                        currentY++;
                    }
                    else {
                        currentY = 0;
                    }
                }
                if((currentX == randomX) && (currentY==randomY)){
                    ErrorCatcher.cellCreationFailure();
                }

            }
        }
        score += state;
    }

    private static void logic(){
        if(direction != Direction.AWAITING){
            if(shift(direction)) generateNewCell();

            direction=Direction.AWAITING;
        }
    }

    private static boolean shift(Direction direction) {
        boolean ret = false;

        switch (direction){
            case UP:
            case DOWN:

                for(int i = 0; i < COUNT_CELLS_X; i++){
                    int [] arg = gameField.getColumn(i);

                    if(direction==Direction.UP){
                        int [] tmp = new int[arg.length];
                        for(int e = 0; e < tmp.length; e++){
                            tmp[e] = arg[tmp.length-e-1];
                        }
                        arg = tmp;
                    }
                    ShiftRowResult result = shiftRow (arg);

                    if (direction==Direction.UP){
                        int[] tmp = new int[result.shiftedRow.length];
                        for(int e = 0; e < tmp.length; e++){
                            tmp[e] = result.shiftedRow[tmp.length-e-1];
                        }
                        result.shiftedRow = tmp;
                    }

                    gameField.setColumn(i, result.shiftedRow);

                    ret = ret || result.didAnythingMove;
                }
                break;
            case LEFT:
            case RIGHT:

                for(int i = 0; i< Constants.COUNT_CELLS_Y; i++){

                    int[] arg = gameField.getLine(i);


                    if(direction==Direction.RIGHT){
                        int[] tmp = new int[arg.length];
                        for(int e = 0; e < tmp.length; e++){
                            tmp[e] = arg[tmp.length-e-1];
                        }
                        arg = tmp;
                    }

                    ShiftRowResult result = shiftRow (arg);

                    if(direction==Direction.RIGHT){
                        int[] tmp = new int[result.shiftedRow.length];
                        for(int e = 0; e < tmp.length; e++){
                            tmp[e] = result.shiftedRow[tmp.length-e-1];
                        }
                        result.shiftedRow = tmp;
                    }

                    gameField.setLine(i, result.shiftedRow);

                    ret = ret || result.didAnythingMove;
                }

                break;
            default:
                ErrorCatcher.shiftFailureWrongParam();
                break;
        }
        return ret;
    }

    private static ShiftRowResult shiftRow (int[] oldRow) {
        ShiftRowResult ret = new ShiftRowResult();

        int[] oldRowWithoutZeroes = new int[oldRow.length];
        {
            int q = 0;

            for (int i = 0; i < oldRow.length; i++) {
                if(oldRow[i] != 0){
                    if(q != i){
                        /*
                         * Это значит, что мы передвинули ячейку
                         * на место какого-то нуля (пустой плитки)
                         */
                        ret.didAnythingMove = true;
                    }

                    oldRowWithoutZeroes[q] = oldRow[i];
                    q++;
                }
            }

            /* Чтобы избежать null'ов в конце массива */
            for(int i = q; i < oldRowWithoutZeroes.length; i++) {
                oldRowWithoutZeroes[i] = 0;
            }
        }

        ret.shiftedRow = new int[oldRowWithoutZeroes.length];

        {
            int q = 0;

            {
                int i = 0;


                while (i < oldRowWithoutZeroes.length) {
                    if((i+1 < oldRowWithoutZeroes.length) && (oldRowWithoutZeroes[i] == oldRowWithoutZeroes[i + 1])
                            && oldRowWithoutZeroes[i]!=0) {
                        ret.didAnythingMove = true;
                        ret.shiftedRow[q] = oldRowWithoutZeroes[i] * 2;
                        if(ret.shiftedRow[q] == 2048) merged2048();
                        i++;
                    } else {
                        ret.shiftedRow[q] = oldRowWithoutZeroes[i];
                    }

                    q++;
                    i++;
                }

            }

            for(int j = q; j < ret.shiftedRow.length; j++) {
                ret.shiftedRow[j] = 0;
            }
        }

        return ret;
    }

    private static void printGameResult() {
        System.out.println("You " + (isThere2048 ? "won :)" : "lost :(") );
        System.out.println("Your score is " + score);
    }

    private static void merged2048() {
        endOfGame = true;
        isThere2048 = true;
    }
}
