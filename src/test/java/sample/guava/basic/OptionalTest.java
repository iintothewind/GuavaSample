package sample.guava.basic;

import com.google.common.base.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class OptionalTest {
    Logger log = LogManager.getLogger(this.getClass().getName());

    /*
     * 使用Optional的意义在哪儿？
     *
     * 使用Optional除了赋予null语义，增加了可读性，最大的优点在于它是一种傻瓜式的防护。Optional迫使你积极思考引用缺失的情况，因为你必须显式地从Optional获取引用。直接使用null很容易让人忘掉某些情形，
     * 尽管FindBugs可以帮助查找null相关的问题，但是我们还是认为它并不能准确地定位问题根源。
     *
     * 如同输入参数，方法的返回值也可能是null。和其他人一样，你绝对很可能会忘记别人写的方法method(a,b)会返回一个null，就好像当你实现method(a,b)时，也很可能忘记输入参数a可以为null。
     * 将方法的返回类型指定为Optional，也可以迫使调用者思考返回的引用缺失的情形。
     *
     * 其他处理null的便利方法
     *
     * 当你需要用一个默认值来替换可能的null，请使用Objects.firstNonNull(T, T)
     * 方法。如果两个值都是null，该方法会抛出NullPointerException。Optional也是一个比较好的替代方案，例如：Optional.of(first).or(second).
     *
     * 还有其它一些方法专门处理null或空字符串：emptyToNull(String)，nullToEmpty(String)，isNullOrEmpty(String)。我们想要强调的是，这些方法主要用来与混淆null/
     * 空的API进行交互。当每次你写下混淆null/空的代码时，Guava团队都泪流满面。（好的做法是积极地把null和空区分开，以表示不同的含义，在代码中把null和空同等对待是一种令人不安的坏味道。
     */
    @Test
    public void testOptional() {
        this.log.debug("Optional.of()  Returns an Optional instance containing the given non-null reference. ");
        this.log.debug("Optional.of()  == " + Optional.of("test").get());
        Optional<Integer> possible = Optional.of(5);
        this.log.debug("possible.isPresent() == " + possible.isPresent());
        this.log.debug("possible.get() == " + possible.get());
        //this.logger.debug("Optional.of(null).get() == " + Optional.of(null).get());
        this.log.debug("Optional.absent() == " + Optional.absent().isPresent());
        this.log.debug("Optional.fromNullable(null).isPresent() == " + Optional.fromNullable(null).isPresent());
        this.log.debug("Optional.fromNullable(null).isPresent() == " + Optional.fromNullable("").isPresent());
        this.log.debug("Optional.fromNullable(null).or(\"defaultValue\")) == "
                + Optional.fromNullable(null).or("defaultValue"));
        this.log.debug("Optional.fromNullable(null).orNull() == " + Optional.fromNullable(null).orNull());
        this.log.debug("Optional.fromNullable(\"test\").asSet() == " + Optional.fromNullable("test").asSet());
        this.log.debug("Optional.fromNullable(null).asSet() == " + Optional.fromNullable(null).asSet());
    }

}
