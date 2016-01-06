package sample.guava.collections;

import com.google.common.base.Preconditions;
import com.google.common.collect.ForwardingList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class FowardingTest {
    Logger log = LogManager.getLogger(this.getClass().getName());

    LoggingList<String> loggingList = new LoggingList<>();

    @Before
    public void add() {
        this.loggingList.add("1");
        this.loggingList.add("2");
        this.loggingList.add("3");
        this.loggingList.add("4");
        this.loggingList.add("5");
    }

    @Test
    public void get() {
        for (int i = 0; i < this.loggingList.size(); i++) {
            this.log.debug(this.loggingList.get(i));
        }
    }
}

class LoggingList<E> extends ForwardingList<E> {
    Logger log = LogManager.getLogger(this.getClass().getName());
    private List<E> list = null;

    public LoggingList() {
        this.list = new ArrayList<E>();
    }

    @Override
    protected List<E> delegate() {
        Preconditions.checkNotNull(list);
        return list;
    }

    @Override
    public void add(int index, E element) {
        this.log.info("add element: " + element + " at index " + index);
        this.delegate().add(index, element);
    }

    @Override
    public E get(int index) {
        this.log.info("get element at index " + index);
        return this.delegate().get(index);
    }

    @Override
    public E remove(int index) {
        this.log.info("remove element at index " + index);
        return this.delegate().remove(index);
    }

    @Override
    public E set(int index, E element) {
        this.log.info("set element: " + element + " at index " + index);
        return this.delegate().set(index, element);
    }
}