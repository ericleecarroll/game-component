package com.mrsnottypants.gamecomponent;

import java.util.Optional;

/**
 * A single round within a game
 *
 * Created by Eric on 6/20/2016.
 */
public interface GameRound {

    /**
     * Perform this round
     * @param gameState state of the game
     * @return if a game round is returned it will be performed once and next
     */
    Optional<GameRound> perform(GameState gameState);
}
