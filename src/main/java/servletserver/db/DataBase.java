package servletserver.db;

import com.google.common.collect.Maps;
import servletserver.model.User;

import java.util.Collection;
import java.util.Map;

public class DataBase {
	private static Map<String, User> users = Maps.newHashMap();

	public static void addUser(User user) {
		users.put(user.getUserId(), user);
	}

	public static User findByUserId(String userId) {
		return users.get(userId);
	}

	public static Collection<User> findAll() {
		return users.values();
	}
}
