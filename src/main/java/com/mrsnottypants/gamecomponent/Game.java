package com.mrsnottypants.gamecomponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The structure of a game - its rounds
 *
 * Created by Eric on 6/20/2016.
 */
public class Game {

    // games are structured as an ordered series of rounds
    private final List<GameRound> gameRounds;

    /**
     * Construct a game from a game builder
     * @param builder game builder
     */
    private Game(Builder builder) {

        // sanity check - games must consist of one or more rounds
        if (builder.gameRounds.size() == 0) {
            throw new IllegalStateException("Games must consist of one or more rounds");
        }

        // save the ordered series of rounds
        this.gameRounds = builder.gameRounds;
    }

    /**
     * Play game by playing full sets of rounds until the game is over
     * @param gameState passed to rounds, and tells us when game is over
     */
    public void play(GameState gameState) {

        // keep playing through the list of rounds until the game is over
        while (!gameState.isGameOver()) {
            playRounds(gameState);
        }
    }

    /**
     * Play a full set of rounds.  Check if game is over after each round
     * @param gameState passed to rounds, and tells us when game is over
     */
    private void playRounds(GameState gameState) {

        // play through one full list of rounds
        for (GameRound gameRound : gameRounds) {

            // a round can return a new round if it wants to insert/repeat a round
            // ex: to play cards until a player is out: return the play-card round until one player is out of cards
            Optional<GameRound> nextRound = Optional.of(gameRound);
            while (nextRound.isPresent() && !gameState.isGameOver()) {
                nextRound = gameRound.perform(gameState);
            }
        }
    }

    // Builder for constructing a game
    //
    public static class Builder {

        // games are structured as an ordered series of rounds
        private final List<GameRound> gameRounds = new ArrayList<>();

        /**
         * Construct a game builder
         */
        public Builder() {}

        /**
         * Add a round to the game
         * @param gameRound
         * @return this builder, for easy chaining
         */
        public Builder addGameRound(GameRound gameRound) {
            gameRounds.add(gameRound);
            return this;
        }

        /**
         * Return a game
         * @return game
         */
        public Game build() {
            return new Game(this);
        }
    }
}
