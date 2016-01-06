package sample.guava.collections;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class TablesTest {
    Logger log = LogManager.getLogger(this.getClass().getName());

    @Test
    public void buildImmutableTable() {
        ImmutableTable.Builder<String, String, Integer> tableBuilder = new ImmutableTable.Builder<>();
        // create a 9x9 table
        for (int i = 1; i <= 9; i++) {
            for (int j = 1; j <= 9; j++) {
                tableBuilder.put("Row" + i, "Column" + j, i * j);
            }
        }
        ImmutableTable<String, String, Integer> table = tableBuilder.build();
        this.log.debug("ImmutableTable = " + table);
    }

    @Test
    public void createHashBasedTable() {
        Table<String, String, Integer> hashBasedTable = HashBasedTable.create();
        for (int i = 1; i <= 9; i++) {
            for (int j = 1; j <= 9; j++) {
                hashBasedTable.put("Row" + i, "Column" + j, i * j);
            }
        }
    }

    @Test
    public void createTreeBasedTable() {
        Table<String, String, Integer> treeBasedTable = TreeBasedTable.create();
        for (int i = 1; i <= 9; i++) {
            for (int j = 1; j <= 9; j++) {
                treeBasedTable.put("Row" + i, "Column" + j, i * j);
            }
        }
    }

    @Test
    public void transformValues() {
        Table<String, String, Integer> table = HashBasedTable.create();
        for (int i = 1; i <= 9; i++) {
            for (int j = 1; j <= 9; j++) {
                table.put("Row" + i, "Column" + j, i * j);
            }
        }

        Table<String, String, String> transformation = Tables.transformValues(table, new Function<Integer, String>() {
            @Override
            public String apply(Integer input) {
                Preconditions.checkNotNull(input);
                return input.toString();
            }
        });

        this.log.debug("Table<String, String, String> Tables.transformValues == " + transformation);
    }

    @Test
    public void transpose() {
        Table<String, String, Integer> table = HashBasedTable.create();
        for (int i = 1; i <= 9; i++) {
            for (int j = 1; j <= 9; j++) {
                table.put("Row" + i, "Column" + j, i * j);
            }
        }

        Table<String, String, Integer> transposed = Tables.transpose(table);

        this.log.debug("Table<String, String, Integer> Tables.transpose() == " + transposed);
    }
}
