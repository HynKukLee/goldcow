package kr.ac.goldcow.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.ac.goldcow.model.User;
import kr.ac.goldcow.repository.UserRepositoryDataJpa;
@Service("userService")
@Transactional
public class UserServiceDataJpa implements UserService2 {
	@Autowired
	UserRepositoryDataJpa repository;
	@Override
	@Transactional(rollbackFor = NotFoundException.class)
	public void saveUser(User user) throws Exception{
		// TODO Auto-generated method stub
		repository.save(user);
	}
	@Override
	public List<User> checkEmail(String email) throws Exception{
		// TODO Auto-generated method stub
		return repository.getUsersByEmail(email);
	}
	@Override
	public List<User> login(String email, String password) throws Exception {
		// TODO Auto-generated method stub
		return repository.getUsersByEmailAndPassword(email, password);
	}
	@Override
	public void registerBusiness(User user) throws Exception {
		// TODO Auto-generated method stub
		User entity = repository.findById(user.getNo()).get();
		entity.setBusinessNo(user.getBusinessNo());
		entity.setBusiness(user.isBusiness());
	}
	@Override
	public void addMoney(User user) throws Exception {
		// TODO Auto-generated method stub
		User entity = repository.findById(user.getNo()).get();
		entity.setMoney(entity.getMoney()+user.getMoney());
	}
	@Override
	public User getUserData(Long userNo) throws Exception {
		// TODO Auto-generated method stub
		return repository.findById(userNo).get();
	}
	@Override
	public void subMoney(User user) throws Exception {
		// TODO Auto-generated method stub
		User entity = repository.findById(user.getNo()).get();
		entity.setMoney(entity.getMoney()-user.getMoney());
	}

}
