package sample.guava.basic;


import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Sets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.HashSet;
import java.util.stream.Collectors;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ObjectsTest {
    Logger log = LogManager.getLogger(this.getClass().getName());

    @Test
    public void test001GuavaEqual() {
        Assert.assertFalse(Objects.equal(null, ""));
        Assert.assertFalse(Objects.equal("a", "b"));
        Assert.assertTrue(Objects.equal(null, null));
    }

    @Test
    public void test002Jdk7Equals() {
        // same api supported by jdk 7
        Assert.assertFalse(java.util.Objects.equals(null, ""));
        Assert.assertFalse(java.util.Objects.equals("a", "b"));
        Assert.assertTrue(java.util.Objects.equals(null, null));
    }

    @Test
    public void test003Jdk7DeepEquals() {
        char[] chars = "abc".toCharArray();
        char[] anotherChars = "abc".toCharArray();
        char[] differentChars = "cde".toCharArray();
        // jdk 7 uses Arrays.deepEquals0()
        Assert.assertTrue(java.util.Objects.deepEquals(chars, anotherChars));
        Assert.assertFalse(java.util.Objects.deepEquals(chars, differentChars));
    }

    @Test(expected = NullPointerException.class)
    public void test004FirstNonNull() {
        Assert.assertEquals(MoreObjects.firstNonNull("a", "b"), "a");
        Assert.assertEquals(MoreObjects.firstNonNull("a", null), "a");
        Assert.assertEquals(MoreObjects.firstNonNull(null, "b"), "b");
        // throws NullPointerException
        Assert.assertNotNull(MoreObjects.firstNonNull(null, null));
    }

    @Test
    public void test005GuavaHashCodeAndToString() {
        class Person implements Comparable<Person> {
            String name;
            Integer age;
            String occupation;

            Person(String name, Integer age, String occupation) {
                super();
                this.name = name;
                this.age = age;
                this.occupation = occupation;
            }

            // @Override
            // public int hashCode() {
            // final int prime = 31;
            // int result = 1;
            // result = prime * result + ((age == null) ? 0 : age.hashCode());
            // result = prime * result + ((name == null) ? 0 : name.hashCode());
            // result = prime * result + ((occupation == null) ? 0 :
            // occupation.hashCode());
            // return result;
            // }

            @Override
            public boolean equals(Object obj) {
                if (this == obj)
                    return true;
                if (obj == null)
                    return false;
                if (getClass() != obj.getClass())
                    return false;
                Person other = (Person) obj;
                if (age == null) {
                    if (other.age != null)
                        return false;
                } else if (!age.equals(other.age))
                    return false;
                if (name == null) {
                    if (other.name != null)
                        return false;
                } else if (!name.equals(other.name))
                    return false;
                if (occupation == null) {
                    if (other.occupation != null)
                        return false;
                } else if (!occupation.equals(other.occupation))
                    return false;
                return true;
            }

            public int compareTo(Person person) {
                Person p = Optional.fromNullable(person).or(new Person("Peter", 20, "Student"));
                return ComparisonChain.start().compare(this.name, p.name).compare(this.age, p.age)
                        .compare(this.occupation, p.occupation).result();
            }

            public int hashCode() {
                return Objects.hashCode(name, age, occupation);
            }

            public String toString() {
                return MoreObjects.toStringHelper(this).add("name", name).add("age", age).add("occupation", occupation)
                        .toString();
            }
        }

        Person student = new Person("John", 20, "Student");
        this.log.debug("student.hashCode() == " + student.hashCode());
        this.log.debug("student.toString() == " + student.toString());
        this.log.debug("student.compareTo() == " + student.compareTo(new Person("Sunny", 20, "Student")));
    }

    @Test
    public void test006Jdk7HashCodeAndToString() {
        class Person {
            String name;
            Integer age;
            String occupation;

            Person(String name, Integer age, String occupation) {
                this.name = name;
                this.age = age;
                this.occupation = occupation;
            }

            public int hashCode() {
                // jdk 7 also uses Arrays.hashCode()
                return java.util.Objects.hash(name, age, occupation);
            }

            public String toString() {
                // never do this, cause stackOverFlow
                // return java.util.Objects.toString(this);
                return MoreObjects.toStringHelper(this).add("name", name).add("age", age).add("occupation", occupation)
                        .toString();
            }
        }

        Person student = new Person("John", 20, "Student");
        this.log.debug("student.hashCode() == " + student.hashCode());
        this.log.debug("student.hashCode() == " + student.toString());
    }

    @Test
    public void testEqualsWithoutOverridingHashCode() {
        class PersonWithoutOverridingHashCode {
            String name;
            Integer age;
            String occupation;

            PersonWithoutOverridingHashCode(String name, Integer age, String occupation) {
                this.name = name;
                this.age = age;
                this.occupation = occupation;
            }

            @Override
            public boolean equals(Object obj) {
                if (this == obj)
                    return true;
                if (obj == null)
                    return false;
                if (getClass() != obj.getClass())
                    return false;
                PersonWithoutOverridingHashCode other = (PersonWithoutOverridingHashCode) obj;
                if (age == null) {
                    if (other.age != null)
                        return false;
                } else if (!age.equals(other.age))
                    return false;
                if (name == null) {
                    if (other.name != null)
                        return false;
                } else if (!name.equals(other.name))
                    return false;
                if (occupation == null) {
                    if (other.occupation != null)
                        return false;
                } else if (!occupation.equals(other.occupation))
                    return false;
                return true;
            }
        }
        PersonWithoutOverridingHashCode studentJohn = new PersonWithoutOverridingHashCode("John", 20, "Student");
        PersonWithoutOverridingHashCode yongPeter = new PersonWithoutOverridingHashCode("Peter", 20, "Student");
        PersonWithoutOverridingHashCode samePeter = new PersonWithoutOverridingHashCode("Peter", 20, "Student");
        HashSet<PersonWithoutOverridingHashCode> people = Sets.newHashSet(yongPeter);
        Assertions.assertThat(yongPeter.hashCode()).isNotEqualTo(samePeter.hashCode());
        // AssertJ uses its own way to check contains
        Assertions.assertThat(people).contains(samePeter);
        Assertions.assertThat(people.contains(samePeter)).isFalse();
    }

    @Test
    public void testEqualsOnMutableFields() {
        class PersonWithMutableFields {
            String name;
            Integer age;
            String occupation;

            PersonWithMutableFields(String name, Integer age, String occupation) {
                this.name = name;
                this.age = age;
                this.occupation = occupation;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setAge(Integer age) {
                this.age = age;
            }

            public void setOccupation(String occupation) {
                this.occupation = occupation;
            }

            public int hashCode() {
                return Objects.hashCode(name, age, occupation);
            }

            @Override
            public boolean equals(Object obj) {
                if (this == obj)
                    return true;
                if (obj == null)
                    return false;
                if (getClass() != obj.getClass())
                    return false;
                PersonWithMutableFields other = (PersonWithMutableFields) obj;
                if (age == null) {
                    if (other.age != null)
                        return false;
                } else if (!age.equals(other.age))
                    return false;
                if (name == null) {
                    if (other.name != null)
                        return false;
                } else if (!name.equals(other.name))
                    return false;
                if (occupation == null) {
                    if (other.occupation != null)
                        return false;
                } else if (!occupation.equals(other.occupation))
                    return false;
                return true;
            }
        }
        PersonWithMutableFields studentJohn = new PersonWithMutableFields("John", 20, "Student");
        PersonWithMutableFields yongPeter = new PersonWithMutableFields("Peter", 20, "Student");
        PersonWithMutableFields samePeter = new PersonWithMutableFields("Peter", 20, "Student");
        HashSet<PersonWithMutableFields> people = Sets.newHashSet(yongPeter);
        people.add(studentJohn);
        Assertions.assertThat(people.contains(studentJohn)).isTrue();
        // change of age field means also the change of hashCode
        studentJohn.setAge(21);
        // people.contains(studentJohn) returns false because hashCode has been changed
        Assertions.assertThat(people.contains(studentJohn)).isFalse();
        // List is using Object.equals() only to check contains, the change of hashCode would not affect equals()
        Assertions.assertThat(people.parallelStream().collect(Collectors.toList()).contains(studentJohn)).isTrue();
    }
}
