package com.mrsnottypants.gamecomponent;

/**
 * Created by Eric on 6/24/2016.
 */
public interface PlayStrategy {

    /**
     * Updates playChoice with values/weight related to choices available to this strategy.
     * For example: When a card needs to be played, the high-card strategy updates playChoices with weights based on
     * card rank.
     * @param playerState State of the player
     * @param gameState State of the game
     * @param playChoice Updated with values related to choices
     */
    public void consider(PlayerState playerState, GameState gameState, PlayChoice playChoice);
}
