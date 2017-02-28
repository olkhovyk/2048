package com.ilya.game.keyboard;

/**
 * Created by Илья on 28.02.2017.
 */
public interface KeyboardHandleModule {
    void update();

    com.ilya.game.Direction lastDirectionKeyPressed();

    boolean wasEscPressed();
}
