package com.example.sample.service;

import com.example.sample.dao.UserDao;
import com.example.sample.dto.ChangePasswordBody;
import com.example.sample.dto.User;
import com.example.sample.library.UserCoreUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

	@Autowired
	PasswordService passwordService;
	private Map<String, User> users = UserDao.getUsers();

	public UserService() {

		users.put("nitishbector.it@gmail.com", new User(UserCoreUtils.generateUUID("nitishbector.it@gmail.com"), "nitishbector.it@gmail.com", UserCoreUtils.generatePasswordToken("nitishbector.it@gmail.com", "aB@#rtttridndjsijdijscijsAfnDFFKDX432")));
		users.put("nitish@gmail.com", new User(UserCoreUtils.generateUUID("nitish@gmail.com"), "nitish@gmail.com", UserCoreUtils.generatePasswordToken("nitishbector.it@gmail.com", "aB@#rtttridndjsijdijscijsAfnDFFKDX432")));
	}

	public List<User> getAllMessages() {
		return new ArrayList<User>(users.values());
	}

	public User getUser(String email) {
		return users.get(email);
	}

	public User addUser(User user) {

		if(users.containsKey(user.getEmail()))
			return null;

		String id = UserCoreUtils.generateUUID(user.getEmail());
		user.setPassword(UserCoreUtils.generatePasswordToken(user.getEmail(), user.getPassword()));
		User user1 = new User(id, user.getEmail(), user.getPassword());
		//user.setId(id);
		users.put(user1.getEmail(), user1);
		return user1;
	}

	public User updateUser(User user) {
		if (user.getEmail().isEmpty() || user.getEmail() == null)
			return null;
		users.put(user.getEmail(), user);
		return user;
	}

	public User removeUser(User user) {
		return users.remove(user.getEmail());
	}

	public List<User> getUsersPaginated(int start, int size){
		ArrayList<User> list = new ArrayList<>(users.values());
		if(start + size > list.size())
			return list.subList(start, list.size());
		return list.subList(start, start + size);
	}

	public boolean validateUser(ChangePasswordBody changeBody) {
		User user = users.get(changeBody.getEmailId());

		String oldPwd = UserCoreUtils.generatePasswordToken(changeBody.getEmailId(), changeBody.getOldPassword());

		if(users.get(changeBody.getEmailId()).getPassword().equals(oldPwd))
			return true;
		else
			return false;
	}

	public boolean validateNewPassword(ChangePasswordBody changeBody) {
		if(validateUser(changeBody)) {

		}

		return true;
	}

	public boolean changePassword(String oldPassword, String newPassword) {
		if(passwordService.validateNewPassword(oldPassword, newPassword)) {
			return true;
		}
		return false;
	}

	public String updatePassword(String email, String newPassword){
		users.get(email).setPassword(UserCoreUtils.generatePasswordToken(email, newPassword));
		return users.get(email).getPassword();
	}
}
