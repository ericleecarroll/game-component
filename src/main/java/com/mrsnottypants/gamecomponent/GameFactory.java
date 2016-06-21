package com.mrsnottypants.gamecomponent;

/**
 * To create new instances of game components
 *
 * Created by Eric on 6/20/2016.
 */
public interface GameFactory {

    /**
     * Return a new game
     * Note: Game objects are stateless, and can be used for multiple games
     * @return New game
     */
    Game newGame();

    /**
     * Return a new game state
     * @return New game state
     */
    GameState newGameState();
}
