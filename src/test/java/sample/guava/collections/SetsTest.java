package sample.guava.collections;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.util.Set;

public class SetsTest {
    Logger log = LogManager.getLogger(this.getClass().getName());

    Set<String> hashSet = Sets.newHashSet("1", "2", "3", "3");

    enum Color {
        BLUE, GREEN, YELLOW, RED;
    }

    @Test
    public void newSet() {
        Set<String> stringConcurrentHashSet = Sets.newConcurrentHashSet();
        Set<String> stringCopyOnWriteArraySet = Sets.newCopyOnWriteArraySet();
        Set<Color> enumSet = Sets.newEnumSet(Sets.newHashSet(Color.BLUE, Color.GREEN, Color.YELLOW, Color.RED),
                Color.class);
        this.log.debug("enumSet == " + enumSet);
    }

    @Test
    public void difference() {
        Set<String> s1 = Sets.newHashSet("1", "2", "3", "5", "6", "7");
        Set<String> s2 = Sets.newHashSet("3", "2", "4");
        // returns the view of all the elements exists in s1 but not in s2
        Sets.SetView<String> view = Sets.difference(s1, s2);
        this.log.debug("Sets.SetView<String> view == " + view);
        // get an immutable set
        ImmutableSet<String> copy = view.immutableCopy();
        // copy all the elements in view to mainSet
        Set<String> mainSet = Sets.newHashSet("8", "9", "0");
        view.copyInto(mainSet);
        this.log.debug("mainSet == " + mainSet);
    }

    @Test
    public void symmetricDifference() {
        Set<String> s1 = Sets.newHashSet("1", "2", "3", "5", "6", "7");
        Set<String> s2 = Sets.newHashSet("3", "2", "4");
        // returns the view of all the elements not existing in s1 or s2
        Sets.SetView<String> view = Sets.symmetricDifference(s1, s2);
        this.log.debug("Sets.SetView<String> view == " + view);
    }

    @Test
    public void intersection() {
        Set<String> s1 = Sets.newHashSet("1", "2", "3", "5", "6", "7");
        Set<String> s2 = Sets.newHashSet("3", "2", "4");
        // returns the view of all the elements existing in both s1 and s2
        Sets.SetView<String> view = Sets.intersection(s1, s2);
        this.log.debug("Sets.SetView<String> view == " + view);
    }

    @Test
    public void union() {
        Set<String> s1 = Sets.newHashSet("1", "2", "3", "5", "6", "7");
        Set<String> s2 = Sets.newHashSet("3", "2", "4");
        Sets.SetView<String> view = Sets.union(s1, s2);
        this.log.debug("Sets.SetView<String> view == " + view);
    }
}
