package com.mrsnottypants.gamecomponent;

import java.util.Iterator;

/**
 * This class decorates an iterable such that it loops back to the start once it reaches the end
 *
 * It is common for games to make endless loops through the same iterable, until outside state terminates it
 * ex: A list of game rounds, or a list of players
 *
 * Created by Eric on 6/21/2016.
 */
public class LoopingIterable<E> implements Iterable<E> {

    // each loop we ask the source iterable for another iterator
    private final Iterable<E> iterable;

    /**
     * Return a looping-iterable based on the passed iterable
     * @param iterable source iterable
     * @param <E> type of elements the iterable serves
     * @return looping-iterable
     */
    public static <E> Iterable<E> of(Iterable iterable) {
        return new LoopingIterable<>(iterable);
    }

    /**
     * Construct and return a looping-iterable based on the passed iterable
     * @param iterable looping-iterable
     */
    private LoopingIterable(Iterable iterable) {
        this.iterable = iterable;
    }

    /**
     * Return an iterator
     * @return iterator
     */
    @Override
    public Iterator<E> iterator() {
        return new LoopingIterator();
    }

    /**
     * Return friendly string description
     * @return description
     */
    @Override
    public String toString() {
        return String.format("Looping:: Iterable=%s", iterable.toString());
    }

    // Looping-iterator implementation
    //
    private final class LoopingIterator implements Iterator<E> {

        // true if iterable is empty
        private final boolean empty;

        // current iterator - used until exhausted
        private Iterator<E> iterator;

        /**
         * Construct a looping iterator
         */
        private LoopingIterator() {
            iterator = iterable.iterator();
            empty = !iterator.hasNext();
        }

        /**
         * There is always a next element, unless the iterable is itself empty
         *
         * @return true unless iterable is empty
         */
        @Override
        public boolean hasNext() {
            return !empty;
        }

        /**
         * Returns the next element in the iteration
         * If the final element has been reached, loops back to the first
         *
         * @return the next element in the iteration
         */
        @Override
        public E next() {

            // if current iterator is done, get another
            if (!iterator.hasNext()) {
                iterator = iterable.iterator();
            }

            // continue to use current iterator as long as it has elements
            // if we have an empty iterable this will throw the expected no-such-element exception
            return iterator.next();
        }
    }
}
