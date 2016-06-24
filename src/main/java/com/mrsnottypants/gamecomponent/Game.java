package com.mrsnottypants.gamecomponent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * Games are structured as an ordered series of rounds
 * During normal play, a round may ask to repeat itself, or spawn a new round type
 * After the final round, the game loops back to the first round again, until game-over
 *
 * Created by Eric on 6/20/2016.
 */
public class Game {

    // the ordered series of rounds
    private final Iterable<GameRound> gameRounds;

    /**
     * Construct a game from a game builder
     * @param builder game builder
     */
    private Game(Builder builder) {

        // save the ordered series of rounds, decorated with looping-iterable
        // looping-iterable loops back to the start after the final round
        this.gameRounds = LoopingIterable.of(builder.gameRounds);
    }

    /**
     * Play game by looping full sets of rounds until the game is over
     * @param gameState passed to rounds, and tells us when game is over
     */
    public void play(GameState gameState) {

        // keep looping through the list of rounds until the game is over
        Iterator<GameRound> rounds = gameRounds.iterator();
        while (rounds.hasNext() && !gameState.isGameOver()) {

            // a round can spawn a new round if it wants to insert/repeat a round
            // ex: to play cards until there are no more cards: the play-card round returns itself until out of cards
            Optional<GameRound> nextRound = Optional.of(rounds.next());
            while (nextRound.isPresent() && !gameState.isGameOver()) {
                nextRound = nextRound.get().perform(gameState);
            }
        }
    }

    /**
     * Return friendly string description
     * @return description
     */
    @Override
    public String toString() {
        return String.format("Game: Rounds=%s", gameRounds.toString());
    }

    // Builder for constructing a game
    //
    public final static class Builder {

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
