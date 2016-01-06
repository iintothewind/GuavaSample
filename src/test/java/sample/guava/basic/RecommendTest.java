package sample.guava.basic;

import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class RecommendTest {
    Logger log = LogManager.getLogger(this.getClass().getName());
    private final char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private final Random random = new Random();
    private SetMultimap<String, String> userFriendsMap;

    @Before
    public void setUp() {
        this.userFriendsMap = this.generateUserFriends();
    }

    @Test
    public void testRecommend() {
        SetMultimap<String, String> recommend1 = this.recommend1(userFriendsMap);
        SetMultimap<String, String> recommend2 = this.recommend2(userFriendsMap);
        this.log.debug("users: " + userFriendsMap);
        this.log.debug("recommend1: " + recommend1);
        this.log.debug("recommend2: " + recommend2);
        Assert.assertEquals(true, recommend1.equals(recommend2));
    }

    @Test
    public void testRecommend1() {
        SetMultimap<String, String> recommend1 = null;
        for (int i = 0; i < 10000; i++) {
            recommend1 = this.recommend1(userFriendsMap);
        }
        Stopwatch watch = Stopwatch.createStarted();
        for (int i = 0; i < 20000; i++) {
            recommend1 = this.recommend1(userFriendsMap);
        }
        Long ms = watch.elapsed(TimeUnit.MICROSECONDS);
        this.log.debug("recommend1 costs in mcs: " + ms / 20000L);
    }

    @Test
    public void testRecommend2() {

        SetMultimap<String, String> recommend2 = null;
        for (int i = 0; i < 10000; i++) {
            recommend2 = this.recommend2(userFriendsMap);
        }
        Stopwatch watch = Stopwatch.createStarted();
        for (int i = 0; i < 20000; i++) {
            recommend2 = this.recommend2(userFriendsMap);
        }
        Long ms = watch.elapsed(TimeUnit.MICROSECONDS);
        this.log.debug("recommend2 costs in mcs: " + ms / 20000L);
    }

    private int random(int max) {
        return random.nextInt(max);
    }

    private char randomLetter() {
        return alphabet[random(26)];
    }

    private Set<String> randomFriends(String user) {
        // final int maxFriendsNumber = 26;
        // at least one friend
        final int maxFriendsNumber = 1 + random(26);
        Set<String> friends = Sets.newHashSet();
        for (int i = 0; i < maxFriendsNumber; i++) {
            friends.add("user-" + String.valueOf(randomLetter()));
        }
        friends.remove(user);
        return friends;
    }

    private SetMultimap<String, String> generateUserFriends() {
        String user = null;
        ImmutableSetMultimap.Builder<String, String> builder = ImmutableSetMultimap.builder();
        for (int i = 0; i < alphabet.length; i++) {
            user = "user-" + String.valueOf(alphabet[i]);
            Set<String> friends = randomFriends(user);
            // friends number must greater than 1 or no key for user is
            // generated
            builder.putAll(user, friends);
        }
        return builder.build();
    }

    private SetMultimap<String, String> recommend1(SetMultimap<String, String> map) {
        Preconditions.checkNotNull(map);
        Set<String> users = map.keySet();
        Set<String> friends = null;
        Set<String> tempFriends = null;
        SetMultimap<String, String> recommendMap = HashMultimap.create();
        for (String user : users) {
            friends = map.get(user);
            for (String friend : friends) {
                if (!map.get(friend).contains(user)) {
                    recommendMap.put(friend, user);
                }
            }
        }

        return recommendMap;
    }

    private SetMultimap<String, String> recommend2(SetMultimap<String, String> map) {
        Preconditions.checkNotNull(map);
        Set<String> users = map.keySet();
        Set<String> friends = null;
        Set<String> tempFriends = null;
        SetMultimap<String, String> recommendMap = HashMultimap.create();
        for (String user : users) {
            friends = map.get(user);
            Set<String> potentialFriends = Sets.difference(users, friends);
            for (String potentialFriend : potentialFriends) {
                if (map.get(potentialFriend).contains(user)) {
                    recommendMap.put(user, potentialFriend);
                }
            }
        }

        return recommendMap;
    }
}
