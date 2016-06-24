package com.mrsnottypants.gamecomponent;

import java.util.*;

/**
 * Represents one player, either computer controlled or human.  Maintains name and score.
 *
 * Also contains sets of play strategies by type.  This is how computer controlled players weigh choices, but these can
 * also be used to provide hints to human players.
 *
 * Created by Eric on 6/24/2016.
 */
public class Player {

    private final boolean computerControlled;
    private final String name;
    private final Map<PlayStrategyType, Set<WeightedPlayStrategy>> playStrategies;

    private int score;

    /**
     * Construct a player
     * @param builder Player builder
     */
    private Player(Builder builder) {
        this.name = builder.name;
        this.computerControlled = builder.computerControlled;
        this.playStrategies = builder.playStrategies;
        this.score = builder.score;
    }

    /**
     * Return true if this is a computer controlled player
     * @return true if computer controlled
     */
    public boolean isComputerControlled() {
        return computerControlled;
    }

    /**
     * Return player's name
     * @return player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Return player's score
     * @return player's score
     */
    public int getScore() {
        return score;
    }

    /**
     * Set player's score
     * @param score player's new score
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Increment player's score
     * @param delta added to player's current score
     */
    public void incrementScore(int delta) {
        this.score += delta;
    }

    /**
     * Return friendly string description
     * @return description
     */
    @Override
    public String toString() {
        return String.format("%s: Score=%d", name, score);
    }

    // TODO - card(s)
    //
    public int weighChoice(PlayStrategyType type, GameState gameState) {

        // default to empty set, if unknown
        Set<WeightedPlayStrategy> weightedPlayStrategies = playStrategies.containsKey(type) ?
                playStrategies.get(type) : Collections.emptySet();

        // sum weights of relevant strategies
        return weightedPlayStrategies.stream()
                .mapToInt(wps -> wps.getWeight() * wps.getPlayStrategy().weighChoice(gameState))
                .sum();
    }

    // A play strategy, along with the weight this player assigns to this strategy
    //
    private final static class WeightedPlayStrategy {
        private final int weight;
        private final PlayStrategy playStrategy;

        WeightedPlayStrategy(int weight, PlayStrategy playStrategy) {
            this.weight = weight;
            this.playStrategy = playStrategy;
        }

        int getWeight() { return weight; }
        PlayStrategy getPlayStrategy() { return playStrategy; }
    }

    // Used to build a player
    //
    public final static class Builder {

        private final String name;
        private final boolean computerControlled;
        private final Map<PlayStrategyType, Set<WeightedPlayStrategy>> playStrategies = new HashMap<>();

        private int score;

        /**
         * Construct a player builder
         * @param name player's name
         * @param computerControlled true if this is a computer controlled payer
         */
        public Builder(String name, boolean computerControlled) {
            this.name = name;
            this.computerControlled = computerControlled;
            this.score = 0;
        }

        /**
         * Add a play strategy to this player
         * @param type type of strategy, to which decisions does this strategy apply
         * @param weight how much weight this player assigns to this strategy
         * @param playStrategy the strategy
         * @return builder, for easy chaining
         */
        public Builder addPlayStrategy(PlayStrategyType type, int weight, PlayStrategy playStrategy) {

            // add set for this type, if this is the first time we've seen this type
            if (!playStrategies.containsKey(type)) {
                playStrategies.put(type, new HashSet<>());
            }

            // save strategy by type
            playStrategies.get(type).add(new WeightedPlayStrategy(weight, playStrategy));
            return this;
        }

        /**
         * Set an initial score.  Defaults to zero if not set.
         * @param score player's score
         * @return builder, for easy chaining
         */
        public Builder setScore(int score) {
            this.score = score;
            return this;
        }

        /**
         * Construct and return a player
         * @return player
         */
        public Player build() {
            return new Player(this);
        }
    }
}
