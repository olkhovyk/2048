package com.ilya.game;

/**
 * Created by Илья on 28.02.2017.
 */
public class Constants {

    /* Размер одной плитки */
    public static final int CELL_SIZE = 64;

    /* Количество ячеек на экране по горизонтали и вертикали */
    public static final int COUNT_CELLS_X = 4;
    public static final int COUNT_CELLS_Y = 4;

    /* Параметры окна */
    public static final int SCREEN_WIDTH = COUNT_CELLS_X *CELL_SIZE;
    public static final int SCREEN_HEIGHT = COUNT_CELLS_Y *CELL_SIZE;
    public static final String SCREEN_NAME = "2048";

    /* В оригинальной игре есть небольшой шанс, что появится плитка со значением не 2, а 4
       Этот шанс (в процентах) определяется здесь */
    public static final int CHANCE_OF_LUCKY_SPAWN = 17; //%

    /* Состояния новосозданых клеток (при условии срабатывания CHANCE_OF_LUCKY_SPAWN и без него)*/
    public static final int LUCKY_INITIAL_CELL_STATE = 4;
    public static final int INITIAL_CELL_STATE = 2;

    /* Количество определённых к первому ходу пользователя ячеек */
    public static final int COUNT_INITITAL_CELLS = 2;
}
