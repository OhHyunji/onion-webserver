package webserver.db;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.Maps;

import webserver.model.User;

public class DataBase {
	private static final Map<String, User> users = Maps.newHashMap();

	public static void addUser(User user) {
		users.put(user.getUserId(), user);
	}

	public static Optional<User> findByUserId(String userId) {
		return Optional.ofNullable(users.get(userId));
	}

	public static Collection<User> findAll() {
		return users.values();
	}
}
