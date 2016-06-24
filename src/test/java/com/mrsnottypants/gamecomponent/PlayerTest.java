package com.mrsnottypants.gamecomponent;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Eric on 6/24/2016.
 */
public class PlayerTest {

    private final static String COMPUTER_PLAYER_MONKEY_NAME = "Monkey";
    private final static int PICK_STRATEGY_LOW_WEIGHT = 60;
    private final static int PICK_STRATEGY_UNPLAYED_WEIGHT = 40;
    private final static int PLAY_STRATEGY_HIGH_WEIGHT = 75;
    private final static int PLAY_STRATEGY_UNPLAYED_WEIGHT = 25;

    private final static String PLAYER_NAME = "Alice";

    // define two strategy types
    //
    private enum TestStrategyType implements PlayStrategyType {
        PICK_CARD(1),
        PLAY_CARD(2)
        ;

        private final int key;
        TestStrategyType(int key) { this.key = key; }

        @Override
        public int getKey() { return key; }
    }

    // define a pair of pick-card strategies
    //
    private class PickLowCardStrategy implements PlayStrategy {

        @Override
        public int weighChoice(GameState gameState) {
            // TODO
            return 1;
        }
    }
    private class PickUnplayedStrategy implements PlayStrategy {

        @Override
        public int weighChoice(GameState gameState) {
            // TODO
            return 2;
        }
    }

    // define a pair of play-card strategies
    //
    private class PlayHighCardStrategy implements PlayStrategy {

        @Override
        public int weighChoice(GameState gameState) {
            // TODO
            return 3;
        }
    }
    private class PlayUnplayedStrategy implements PlayStrategy {

        @Override
        public int weighChoice(GameState gameState) {
            // TODO
            return 4;
        }
    }

    @Test
    public void testComputerPlayer() {

        // use builder to create monkey player
        Player player = new Player.Builder(COMPUTER_PLAYER_MONKEY_NAME, true)
                .addPlayStrategy(TestStrategyType.PICK_CARD, PICK_STRATEGY_LOW_WEIGHT, new PickLowCardStrategy())
                .addPlayStrategy(TestStrategyType.PICK_CARD, PICK_STRATEGY_UNPLAYED_WEIGHT, new PickUnplayedStrategy())
                .addPlayStrategy(TestStrategyType.PLAY_CARD, PLAY_STRATEGY_HIGH_WEIGHT, new PlayHighCardStrategy())
                .addPlayStrategy(TestStrategyType.PLAY_CARD, PLAY_STRATEGY_UNPLAYED_WEIGHT, new PlayUnplayedStrategy())
                .build();

        // confirm basics
        Assert.assertEquals(COMPUTER_PLAYER_MONKEY_NAME, player.getName());
        Assert.assertTrue(player.isComputerControlled());
        Assert.assertEquals(0, player.getScore());

        // weigh a pick choice
        // TODO - game state, card choice
        int weight = player.weighChoice(TestStrategyType.PICK_CARD, null);
        Assert.assertEquals(PICK_STRATEGY_LOW_WEIGHT * 1 + PICK_STRATEGY_UNPLAYED_WEIGHT * 2, weight);

        // weigh a play choice
        weight = player.weighChoice(TestStrategyType.PLAY_CARD, null);
        Assert.assertEquals(PLAY_STRATEGY_HIGH_WEIGHT * 3 + PLAY_STRATEGY_UNPLAYED_WEIGHT * 4, weight);
    }

    @Test
    public void testHumanPlayer() {

        // use builder to create human player
        Player player = new Player.Builder(PLAYER_NAME, false)
                .build();

        // confirm basics
        Assert.assertEquals(PLAYER_NAME, player.getName());
        Assert.assertFalse(player.isComputerControlled());
        Assert.assertEquals(0, player.getScore());

        // weigh a pick choice - no strategies means no weight
        Assert.assertEquals(0, player.weighChoice(TestStrategyType.PICK_CARD, null));
    }

    @Test
    public void testScore() {

        // user builder to create human player, and start score at 100
        Player player = new Player.Builder(PLAYER_NAME, false)
                .setScore(100)
                .build();

        // confirm basics
        Assert.assertEquals(PLAYER_NAME, player.getName());
        Assert.assertFalse(player.isComputerControlled());
        Assert.assertEquals(100, player.getScore());

        // set score to 10
        player.setScore(10);
        Assert.assertEquals(10, player.getScore());

        // increment score by 20
        player.incrementScore(20);
        Assert.assertEquals(30, player.getScore());
    }

    @Test
    public void testToString() {

        // user builder to create human player, and start score at 100
        Player player = new Player.Builder(PLAYER_NAME, false)
                .setScore(100)
                .build();
        System.out.println(player.toString());
        Assert.assertNotNull(player.toString());
    }
}