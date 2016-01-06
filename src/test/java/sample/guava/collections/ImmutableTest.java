package sample.guava.collections;

import com.google.common.base.Optional;
import com.google.common.collect.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import sample.guava.bean.Person;

import java.util.Comparator;

/**
 * 为什么要使用不可变集合
 * <p>
 * 不可变对象有很多优点，包括：
 * <p>
 * 当对象被不可信的库调用时，不可变形式是安全的； 不可变对象被多个线程调用时，不存在竞态条件问题
 * 不可变集合不需要考虑变化，因此可以节省时间和空间。所有不可变的集合都比它们的可变形式有更好的内存利用率（分析和测试细节）；
 * 不可变对象因为有固定不变，可以作为常量来安全使用。
 * <p>
 * 创建对象的不可变拷贝是一项很好的防御性编程技巧。Guava为所有JDK标准集合类型和Guava新集合类型都提供了简单易用的不可变版本。
 * JDK也提供了Collections.unmodifiableXXX方法把集合包装为不可变形式，但我们认为不够好：
 * <p>
 * 笨重而且累赘：不能舒适地用在所有想做防御性拷贝的场景； 不安全：要保证没人通过原集合的引用进行修改，返回的集合才是事实上不可变的；
 * 低效：包装过的集合仍然保有可变集合的开销，比如并发修改的检查、散列表的额外空间，等等。
 * <p>
 * 如果你没有修改某个集合的需求，或者希望某个集合保持不变时，把它防御性地拷贝到不可变集合是个很好的实践。
 * <p>
 * 重要提示：所有Guava不可变集合的实现都不接受null值。我们对Google内部的代码库做过详细研究，发现只有5%的情况需要在集合中允许null元素，
 * 剩下的95%场景都是遇到null值就快速失败。如果你需要在不可变集合中使用null
 * ，请使用JDK中的Collections.unmodifiableXXX方法。更多细节建议请参考“使用和避免null”。
 *
 * @author Ivar
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ImmutableTest {
    Logger log = LogManager.getLogger(this.getClass().getName());

    enum Color {
        BLUE, GREEN, YELLOW, RED;
    }

    Comparator<Color> enumCompare = new Comparator<Color>() {
        @Override
        public int compare(Color left, Color right) {
            // Compareble is implemented by enum type by default
            return left.compareTo(right);
        }

    };

    Person studentJohn = new Person("John", 20, "Student");
    Person yongPeter = new Person("Peter", 20, "Student");
    Person olderPeter = new Person("Peter", 21, "Student");
    Person boxerJohn = new Person("John", 20, "Boxer");

    Comparator<Person> byProperties = new Comparator<Person>() {
        @Override
        public int compare(Person left, Person right) {
            if (!Optional.fromNullable(left).isPresent() && !Optional.fromNullable(left).isPresent()) {
                return 0;
            } else if (!Optional.fromNullable(left).isPresent() && Optional.fromNullable(left).isPresent()) {
                return -1;
            } else if (Optional.fromNullable(left).isPresent() && !Optional.fromNullable(left).isPresent()) {
                return 1;
            } else {
                return left.compareTo(right);
            }

        }
    };

    @Test
    public void test001ImmutableList() {
        this.log.debug("ImmutableList = " + ImmutableList.of(1, 2, 4, 3, 5));
    }

    @Test
    public void test002ImmutableMultiset() {
        this.log.debug("ImmutableMultiset = " + ImmutableMultiset.of(1, 1, 2, 2, 3, 3, 4, 4));
        this.log.debug("count(1) = " + ImmutableMultiset.of(1, 1, 2, 2, 3, 3, 4, 4).count(1));
        this.log.debug("elementSet() = " + ImmutableMultiset.of(1, 1, 2, 2, 3, 3, 4, 4).elementSet());
        this.log.debug("entrySet() = " + ImmutableMultiset.of(1, 1, 2, 2, 3, 3, 4, 4).entrySet());
    }

    @Test
    public void test003ImmutableSortedMultiset() {
        this.log.debug("ImmutableSortedMultiset = " + ImmutableSortedMultiset.of(4, 4, 3, 3, 2, 2, 1, 1));
        this.log.debug("ImmutableSortedMultiset = "
                + new ImmutableSortedMultiset.Builder<>(byProperties).addCopies(studentJohn, 2).addCopies(yongPeter, 3)
                .addCopies(olderPeter, 4).addCopies(boxerJohn, 3).build());
        this.log.debug("ImmutableSortedMultiset = "
                + new ImmutableSortedMultiset.Builder<>(enumCompare).addCopies(Color.BLUE, 2).addCopies(Color.GREEN, 3)
                .addCopies(Color.RED, 4).addCopies(Color.YELLOW, 3).build());
        this.log.debug("headMultiset(3, BoundType.OPEN) = "
                + ImmutableSortedMultiset.of(4, 4, 3, 3, 2, 2, 1, 1).headMultiset(3, BoundType.OPEN));
        this.log.debug("headMultiset(3, BoundType.CLOSED) = "
                + ImmutableSortedMultiset.of(4, 4, 3, 3, 2, 2, 1, 1).headMultiset(3, BoundType.CLOSED));
        this.log.debug("tailMultiset = "
                + ImmutableSortedMultiset.of(4, 4, 3, 3, 2, 2, 1, 1).tailMultiset(3, BoundType.CLOSED));
        this.log.debug("subMultiset = "
                + ImmutableSortedMultiset.of(4, 4, 3, 3, 2, 2, 1, 1).subMultiset(2, BoundType.CLOSED, 3,
                BoundType.CLOSED));

    }

    @Test
    public void test004ImmutableRangeSet() {
        ImmutableRangeSet<Integer> immutableRangeSet = new ImmutableRangeSet.Builder<Integer>()
                .add(Range.closedOpen(1, 10)).add(Range.closedOpen(10, 15)).add(Range.closedOpen(15, 20)).build();
        this.log.debug("immutableRangeSet = " + immutableRangeSet);
        this.log.debug("immutableRangeSet.contains(10) = " + immutableRangeSet.contains(10));
        this.log.debug("immutableRangeSet.contains(20) = " + immutableRangeSet.contains(20));
    }

    @Test
    public void test005ImmutableMap() {
        this.log.debug("ImmutableMap = " + ImmutableMap.of("k1", "v1", "k3", "v3", "k2", "v2"));
        this.log.debug("ImmutableMap = "
                + ImmutableMap.builder().put("k1", "v1").put("k2", "v2").put("k3", "v3").build());
    }

    @Test
    public void test005ImmutableRangeMap() {
        ImmutableRangeMap<Integer, String> thermometer = new ImmutableRangeMap.Builder<Integer, String>()
                .put(Range.closedOpen(-30, 0), "freezing").put(Range.closedOpen(0, 5), "chilly")
                .put(Range.closedOpen(5, 10), "cold").put(Range.closedOpen(10, 20), "cool")
                .put(Range.closedOpen(20, 40), "hot").build();
        this.log.debug("thermometerRangeMap = " + thermometer);
        this.log.debug("ImmutableMap<Range<Integer>, String> asMapOfRanges() = "
                + thermometer.asMapOfRanges());
        this.log.debug("thermometerRangeMap.get(-3) = " + thermometer.get(-3));
        this.log.debug("thermometerRangeMap.get(0) = " + thermometer.get(0));
        this.log.debug("thermometerRangeMap.get(3) = " + thermometer.get(3));
        this.log.debug("thermometerRangeMap.get(5) = " + thermometer.get(5));
        this.log.debug("thermometerRangeMap.get(7) = " + thermometer.get(7));
        this.log.debug("thermometerRangeMap.get(10) = " + thermometer.get(10));
        this.log.debug("thermometerRangeMap.get(15) = " + thermometer.get(15));
        this.log.debug("thermometerRangeMap.get(20) = " + thermometer.get(20));
        this.log.debug("thermometerRangeMap.get(20) = " + thermometer.get(30));
        this.log.debug("thermometerRangeMap.get(40) = " + thermometer.get(40));
        this.log.debug("Entry<Range<Integer>, String>: thermometerRangeMap.getEntry(15) = "
                + thermometer.getEntry(15));
        this.log.debug("Range<Integer>: thermometerRangeMap.span() = " + thermometer.span());
        this.log.debug("ImmutableRangeMap<Integer, String>: thermometerRangeMap.subRangeMap(Range.closed(0, 30)) = "
                + thermometer.subRangeMap(Range.closed(0, 30)));
    }

    @Test
    public void test006ImmutableSortedMap() {
        this.log.debug("ImmutableSortedMap = " + ImmutableSortedMap.of("k1", "v1", "k3", "v3", "k2", "v2"));
        this.log.debug("ImmutableSortedMap = "
                + ImmutableSortedMap.naturalOrder().put("k3", "v3").put("k2", "v2").put("k1", "v1").build());
        this.log.debug("ImmutableSortedMap = "
                + new ImmutableSortedMap.Builder<String, String>(Ordering.natural()).put("k3", "v3").put("k2", "v2")
                .put("k1", "v1").build());
        this.log.debug("ImmutableSortedMap = "
                + new ImmutableSortedMap.Builder<Color, String>(enumCompare).put(Color.BLUE, "LightBlue")
                .put(Color.GREEN, "LightGreen").put(Color.RED, "Crimson").build());

    }

    @Test
    public void test007ImmutableListMultimap() {
        this.log.debug("ImmutableListMultimap = "
                + ImmutableListMultimap.builder().put("k1", "v1").put("k1", "v1").put("k1", "v2").put("k1", "v3")
                .put("k2", "v1").build());
        this.log.debug("values = "
                + ImmutableListMultimap.builder().put("k1", "v1").put("k1", "v1").put("k1", "v2").put("k1", "v3")
                .put("k2", "v1").build().keys());
        this.log.debug("values = "
                + ImmutableListMultimap.builder().put("k1", "v1").put("k1", "v1").put("k1", "v2").put("k1", "v3")
                .put("k2", "v1").build().values());
        this.log.debug("get(k1) = "
                + ImmutableListMultimap.builder().put("k1", "v1").put("k1", "v1").put("k1", "v2").put("k1", "v3")
                .put("k2", "v1").build().get("k1"));
    }

    @Test
    public void test008ImmutableSetMultimap() {
        this.log.debug("ImmutableSetMultimap = "
                + ImmutableSetMultimap.builder().put("k1", "v1").put("k1", "v1").put("k1", "v2").put("k1", "v3")
                .put("k2", "v1").build());
        this.log.debug("entries = "
                + ImmutableSetMultimap.builder().put("k1", "v1").put("k1", "v1").put("k1", "v2").put("k1", "v3")
                .put("k2", "v1").build().entries());
        this.log.debug("keys = "
                + ImmutableSetMultimap.builder().put("k1", "v1").put("k1", "v1").put("k1", "v2").put("k1", "v3")
                .put("k2", "v1").build().keys());
        this.log.debug("values = "
                + ImmutableSetMultimap.builder().put("k1", "v1").put("k1", "v1").put("k1", "v2").put("k1", "v3")
                .put("k2", "v1").build().values());
        this.log.debug("get(k1) = "
                + ImmutableSetMultimap.builder().put("k1", "v1").put("k1", "v1").put("k1", "v2").put("k1", "v3")
                .put("k2", "v1").build().get("k1"));
    }

    @Test
    public void test009ImmutableBiMap() {
        this.log.debug("ImmutableBiMap = " + ImmutableBiMap.of("k1", "v1", "k2", "v2", "k3", "v3"));
        this.log.debug("ImmutableBiMap = " + ImmutableBiMap.of("k1", "v1", "k2", "v2", "k3", "v3").inverse());
    }

    @Test
    public void test010ImmutableClassToInstanceMap() {
        this.log.debug("ImmutableClassToInstanceMap = "
                + ImmutableClassToInstanceMap.builder().put(String.class, "s1").put(Integer.class, 3).build());
    }

    @Test
    public void test011ImmutableTable() {
        ImmutableTable.Builder<String, String, Integer> tableBuilder = new ImmutableTable.Builder<>();
        // create a 9x9 table
        for (int i = 1; i <= 9; i++) {
            for (int j = 1; j <= 9; j++) {
                tableBuilder.put("Row" + i, "Column" + j, i * j);
            }
        }
        ImmutableTable<String, String, Integer> table = tableBuilder.build();
        this.log.debug("ImmutableTable = " + table);
        this.log.debug("ImmutableSet<Cell<String, String, Integer>>: table.cellSet() = " + table.cellSet());
        this.log.debug("table.get(Row7, Column7) = " + table.get("Row7", "Column7"));
        this.log.debug("ImmutableMap<String, Integer> : table.column(Column3) = " + table.column("Column3"));
        this.log.debug("ImmutableMap<String, Integer> : table.row(Row3) = " + table.row("Row3"));
        this.log.debug("ImmutableSet<String> : table.columnKeySet() = " + table.columnKeySet());
        this.log.debug("ImmutableSet<String> : table.rowKeySet() = " + table.rowKeySet());
        this.log.debug("table.contains(Row7, Column7) = " + table.contains("Row7", "Column7"));
        this.log.debug("table.containsColumnColumn7) = " + table.containsColumn("Column7"));
        this.log.debug("table.containsRow(Row7) = " + table.containsRow("Row7"));
        this.log.debug("table.containsValue(81) = " + table.containsValue(81));
    }
}
