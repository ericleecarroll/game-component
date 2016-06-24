package com.mrsnottypants.gamecomponent;

/**
 * A player may apply different sets of strategies to make different choice.
 * This interface is implemented by game-specific classes to provide a mechanism for identifying which set a
 * strategy belongs to.
 * Created by Eric on 6/24/2016.
 */
public interface PlayStrategyType {

    /**
     * Return a unique key for identifying which set a strategy belongs to.
     * @return Unique key
     */
    int getKey();
}
