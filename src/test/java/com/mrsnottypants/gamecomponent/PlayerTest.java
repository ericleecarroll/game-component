package com.mrsnottypants.gamecomponent;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 * Created by Eric on 6/24/2016.
 */
public class PlayerTest {

    private final int HIGH_RANK = 13;

    // game state
    //
    private class TestGameState implements GameState {

        private final Set<Integer> played = new HashSet<>();

        void addToPlayed(int rank) {
            played.add(rank);
        }
        Set<Integer> getPlayed() {
            return played;
        }

        @Override
        public boolean isGameOver() {
            return false;
        }
    }
    
    // player state
    //
    private class TestPlayerState implements PlayerState {
        
        private final String name;
        private final Set<Integer> hand = new HashSet<>();
        
        TestPlayerState(String name) {
            this.name = name;
        }
        
        void addToHand(int rank) {
            hand.add(rank);
        }
        Set<Integer> getHand() {
            return hand;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public int getScore() {
            return 0;
        }
    }
    
    // play choice
    private class TestPlayChoice implements PlayChoice {
        private final Map<Integer, Integer> weights = new HashMap<>();
        
        void addWeight(int rank, int weight) {
            weights.put(rank, getWeight(rank) + weight);
        }

        int getWeight(int rank) {
            return weights.containsKey(rank) ? weights.get(rank) : 0;
        }
    }

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

    // strategy to pick low card
    //
    private class LowCardStrategy implements PlayStrategy {

        @Override
        public void consider(PlayerState playerState, GameState gameState, PlayChoice playChoice) {
            
            // expect a test state and choice
            TestPlayerState testPlayerState = TestPlayerState.class.cast(playerState);
            TestPlayChoice testPlayChoice = TestPlayChoice.class.cast(playChoice);

            // update weights with rank
            testPlayerState.getHand().stream().forEach(rank -> testPlayChoice.addWeight(rank, HIGH_RANK - rank));
        }
    }

    // define a pair of play-card strategies
    //
    private class HighCardStrategy implements PlayStrategy {

        @Override
        public void consider(PlayerState playerState, GameState gameState, PlayChoice playChoice) {

            // expect a test state and choice
            TestPlayerState testPlayerState = TestPlayerState.class.cast(playerState);
            TestPlayChoice testPlayChoice = TestPlayChoice.class.cast(playChoice);

            // update weights with rank
            testPlayerState.getHand().stream().forEach(rank -> testPlayChoice.addWeight(rank, rank));
        }
    }

    // strategy to pick unplayed card
    //
    private class UnplayedStrategy implements PlayStrategy {

        @Override
        public void consider(PlayerState playerState, GameState gameState, PlayChoice playChoice) {

            // expect a test state and choice
            TestPlayerState testPlayerState = TestPlayerState.class.cast(playerState);
            TestGameState testGameState = TestGameState.class.cast(gameState);
            TestPlayChoice testPlayChoice = TestPlayChoice.class.cast(playChoice);

            // assign weights based on if copy already played
            testPlayerState.getHand().stream()
                    .forEach(rank -> testPlayChoice.addWeight(rank,
                            testGameState.getPlayed().contains(rank) ? 0 : HIGH_RANK));
        }
    }

    @Test
    public void testComputerPlayer() {

        // use builder to create monkey player
        Player player = new Player.Builder(true)
                .addPlayStrategy(TestStrategyType.PICK_CARD, new LowCardStrategy())
                .addPlayStrategy(TestStrategyType.PICK_CARD, new UnplayedStrategy())
                .addPlayStrategy(TestStrategyType.PLAY_CARD, new HighCardStrategy())
                .addPlayStrategy(TestStrategyType.PLAY_CARD, new UnplayedStrategy())
                .build();
        Assert.assertTrue(player.isComputerControlled());

        // player state
        TestPlayerState playerState = new TestPlayerState("Alice");
        playerState.addToHand(5);
        playerState.addToHand(8);
        playerState.addToHand(10);

        // game state
        TestGameState gameState = new TestGameState();
        gameState.addToPlayed(5);
        gameState.addToPlayed(8);
        gameState.addToPlayed(2);

        // have player weigh pick choices
        TestPlayChoice playChoice = new TestPlayChoice();
        player.consider(TestStrategyType.PICK_CARD, playerState, gameState, playChoice);
        Assert.assertEquals(HIGH_RANK - 5, playChoice.getWeight(5));
        Assert.assertEquals(HIGH_RANK - 8, playChoice.getWeight(8));
        Assert.assertEquals(HIGH_RANK - 10 + HIGH_RANK, playChoice.getWeight(10));

        // have player weight play choices
        playChoice = new TestPlayChoice();
        player.consider(TestStrategyType.PLAY_CARD, playerState, gameState, playChoice);
        Assert.assertEquals(5, playChoice.getWeight(5));
        Assert.assertEquals(8, playChoice.getWeight(8));
        Assert.assertEquals(10 + HIGH_RANK, playChoice.getWeight(10));
    }

    @Test
    public void testToString() {

        // user builder to create human player, and start score at 100
        Player player = new Player.Builder(false).build();
        System.out.println(player.toString());
        Assert.assertNotNull(player.toString());
    }
}