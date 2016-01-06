package sample.guava.collections;

import com.google.common.collect.ImmutableRangeSet;
import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;
import com.google.common.collect.TreeRangeSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.util.Set;

public class RangeSetTest {
    Logger log = LogManager.getLogger(this.getClass().getName());

    @Test
    public void create() {
        ImmutableRangeSet<Integer> immutableRangeSet = new ImmutableRangeSet.Builder<Integer>()
                .add(Range.closedOpen(1, 10)).add(Range.closedOpen(10, 15)).add(Range.closedOpen(15, 20)).build();
        this.log.debug("immutableRangeSet = " + immutableRangeSet);
        RangeSet<Integer> rangeSet = TreeRangeSet.create();
        rangeSet.add(Range.closedOpen(-10, 0));
        rangeSet.add(Range.closedOpen(0, 10));
        this.log.debug("rangeSet = " + rangeSet);
    }

    @Test
    public void asRanges() {
        RangeSet<Integer> rangeSet = TreeRangeSet.create();
        rangeSet.add(Range.closedOpen(-10, 0));
        rangeSet.add(Range.closedOpen(0, 10));
        rangeSet.remove(Range.closed(-1, 1));
        Set<Range<Integer>> ranges = rangeSet.asRanges();
        this.log.debug("Set<Range<Integer>> ranges = " + ranges);
    }

    @Test
    public void complement() {
        RangeSet<Integer> rangeSet = TreeRangeSet.create();
        rangeSet.add(Range.closedOpen(-10, 0));
        rangeSet.add(Range.closedOpen(0, 10));
        this.log.debug("rangeSet = " + rangeSet);
        // returns the complement: (-∞‥-10), [10‥+∞)
        RangeSet<Integer> complement = rangeSet.complement();
        this.log.debug("RangeSet<Integer> complement = " + complement);
    }

    @Test
    public void contains() {
        RangeSet<Integer> rangeSet = TreeRangeSet.create();
        rangeSet.add(Range.closedOpen(-10, 0));
        rangeSet.add(Range.closedOpen(0, 10));
        this.log.debug("rangeSet.contains() = " + rangeSet.contains(10));
    }

    @Test
    public void rangeContaining() {
        RangeSet<Integer> rangeSet = TreeRangeSet.create();
        rangeSet.add(Range.closedOpen(-10, 0));
        rangeSet.add(Range.closedOpen(0, 10));
        this.log.debug("Range<Integer> rangeSet.rangeContaining() = " + rangeSet.rangeContaining(-5));
    }

    @Test
    public void span() {
        RangeSet<Integer> rangeSet = TreeRangeSet.create();
        rangeSet.add(Range.closedOpen(-10, -3));
        rangeSet.add(Range.closedOpen(5, 10));
        this.log.debug("Range<Integer> rangeSet.rangeContaining() = " + rangeSet.span());
    }

    @Test
    public void encloses() {
        RangeSet<Integer> rangeSet = TreeRangeSet.create();
        rangeSet.add(Range.closedOpen(-10, 0));
        rangeSet.add(Range.closedOpen(0, 10));
        this.log.debug("rangeSet.encloses() = " + rangeSet.encloses(Range.closed(-5, 5)));
    }


}
