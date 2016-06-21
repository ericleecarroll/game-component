package com.mrsnottypants.gamecomponent;

/**
 * State of a single game
 *
 * Created by Eric on 6/20/2016.
 */
public interface GameState {

    /**
     * Return true if game is over
     * @return true if game is over
     */
    public boolean isGameOver();
}
