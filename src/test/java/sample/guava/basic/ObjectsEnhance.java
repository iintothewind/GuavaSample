package sample.guava.basic;

import com.google.common.base.Objects;

public class ObjectsEnhance {

	public static boolean allNull(Object... objects) {
		for (Object object : objects) {
			if (!Objects.equal(null, object)) {
				return false;
			}
		}
		return true;
	}

	public static boolean allNotNull(Object... objects) {
		for (Object object : objects) {
			if (Objects.equal(null, object)) {
				return false;
			}
		}
		return true;
	}
}
