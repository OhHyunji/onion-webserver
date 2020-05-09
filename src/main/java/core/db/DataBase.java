package core.db;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.Maps;

import core.model.User;

public class DataBase {
	private static final Map<String, User> users = Maps.newHashMap();

	static {
		users.put("a", new User("a", "a", "a", "a@gmail.com"));
		users.put("b", new User("b", "b", "b", "b@gmail.com"));
		users.put("c", new User("c", "c", "c", "c@gmail.com"));
		users.put("d", new User("d", "d", "d", "d@gmail.com"));
		users.put("e", new User("e", "e", "e", "e@gmail.com"));
	}

	public static void addUser(User user) {
		users.put(user.getUserId(), user);
	}

	public static Optional<User> findByUserId(String userId) {
		return Optional.ofNullable(users.get(userId));
	}

	public static Collection<User> findAll() {
		return users.values();
	}

	public static void update(User user) {
		users.put(user.getUserId(), user);
	}
}
