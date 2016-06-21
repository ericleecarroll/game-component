package com.mrsnottypants.gamecomponent;

import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;
import java.util.Random;

/**
 * Created by Eric on 6/20/2016.
 */
public class GameTest {

    private static class PickNumberState implements GameState {

        public static final int GAME_OVER_COUNT = 3;

        // answer, and bounds
        private int answer;
        private int low;
        private int high;

        // game statistics
        private int pickCount = 0;
        private int guessCount = 0;
        private int correctCount = 0;

        // game is over when a certain number of correct guesses are made
        //
        @Override
        public boolean isGameOver() {
            return correctCount >= GAME_OVER_COUNT;
        }

        // correct answer
        //
        public void setAnswer(int answer) { this.answer = answer; }
        public int getAnswer() { return answer; }

        // lower bound
        //
        public void setLow(int low) { this.low = low; }
        public int getLow() { return low; }

        // higher bound
        //
        public void setHigh(int high) { this.high = high; }
        public int getHigh() { return high; }

        // count of picks
        //
        public void incrementPickCount() { ++pickCount; }
        public int getPickCount() { return pickCount; }

        // count of guesses
        public void incrementGuessCount() { ++guessCount; }
        public int getGuessCount() { return guessCount; }

        // count of correct guesses
        public void incrementCorrectCount() { ++correctCount; }
        public int getCorrectCount() { return correctCount; }
    }

    private static class PickNumberRound implements GameRound {

        private static final int LOW  = 1;
        private static final int HIGH = 100;

        @Override
        public Optional<GameRound> perform(GameState gameState) {

            // expect a pick-number state
            PickNumberState pickNumberState = PickNumberState.class.cast(gameState);
            pickNumberState.incrementPickCount();

            // set high and low bounds
            pickNumberState.setLow(LOW);
            pickNumberState.setHigh(HIGH);

            // randomly pick a number in this range
            int answer = new Random().ints(pickNumberState.getLow(), pickNumberState.getHigh()+1)
                    .findFirst().getAsInt();
            pickNumberState.setAnswer(answer);

            // move to the next round
            return Optional.empty();
        }
    }

    private static class GuessNumberRound implements GameRound {

        @Override
        public Optional<GameRound> perform(GameState gameState) {

            // expect a pick-number state
            PickNumberState pickNumberState = PickNumberState.class.cast(gameState);
            pickNumberState.incrementGuessCount();

            // make a guess and, if correct, move to the next round
            int guess = new Random().ints(pickNumberState.getLow(), pickNumberState.getHigh()+1)
                    .findFirst().getAsInt();
            if (guess == pickNumberState.getAnswer()) {
                pickNumberState.incrementCorrectCount();
                return Optional.empty();
            }

            // if too high, our guess becomes the new high bound
            // if too low, the new low bound
            if (guess > pickNumberState.getAnswer()) {
                pickNumberState.setHigh(guess-1);
            } else {
                pickNumberState.setLow(guess+1);
            }

            // repeat the guess-number round
            return Optional.of(this);
        }
    }

    @Test
    public void testGame() {

        // pick-a-number game consists of one pick-number round, and 1+ guess-number rounds
        Game pickNumberGame = new Game.Builder()
                .addGameRound(new PickNumberRound())
                .addGameRound(new GuessNumberRound())
                .build();

        // play a game
        PickNumberState pickNumberState = new PickNumberState();
        pickNumberGame.play(pickNumberState);

        // confirm as many picks as correct guesses needed
        Assert.assertEquals(PickNumberState.GAME_OVER_COUNT, pickNumberState.getPickCount());

        // confirm at least as many guesses as correct guesses needed
        Assert.assertTrue(pickNumberState.getGuessCount() >= PickNumberState.GAME_OVER_COUNT);
    }
}