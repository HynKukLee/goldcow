package kr.ac.goldcow.service;

import java.util.List;
import kr.ac.goldcow.model.User;

public interface UserService2 {
	public void saveUser(User user) throws Exception;
	public List<User> checkEmail(String email) throws Exception;
	public List<User> login(String email, String password) throws Exception;
	public void registerBusiness(User user) throws Exception;
	public void addMoney(User user) throws Exception;
	public void subMoney(User user) throws Exception;
	public User getUserData(Long userNo) throws Exception;
}
