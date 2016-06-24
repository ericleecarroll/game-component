package com.mrsnottypants.gamecomponent;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 * Created by Eric on 6/21/2016.
 */
public class LoopingIterableTest {

    @Test
    public void testList() {

        // create a list
        List<String> source = Arrays.asList("Apple", "Banana", "Carrot");

        // create the iterator
        Iterable<String> iterable = LoopingIterable.of(source);

        // confirm first pass is as expected
        Iterator<String> iterator = iterable.iterator();
        for (String element : source) {
            Assert.assertTrue(iterator.hasNext());
            Assert.assertEquals(element, iterator.next());
        }

        // confirm second pass repeats first
        for (String element : source) {
            Assert.assertTrue(iterator.hasNext());
            Assert.assertEquals(element, iterator.next());
        }
    }

    @Test
    public void testEmptyList() {

        // create the iterator from an empty list
        Iterable<Integer> iterable = LoopingIterable.of(Collections.emptyList());

        // should not indicate it has a next element
        Assert.assertFalse(iterable.iterator().hasNext());

        // an attempt to get an element throws a no-such-element exception
        boolean noSuchElement = false;
        try {
            iterable.iterator().next();
        } catch (NoSuchElementException ex) {
            noSuchElement = true;
        }
        Assert.assertTrue(noSuchElement);
    }

    @Test
    public void testToString() {

        // create the iterable
        List<String> source = Arrays.asList("Apple", "Banana", "Carrot");
        Iterable<String> iterable = LoopingIterable.of(source);
        Assert.assertNotNull(iterable.toString());
    }
}