package kr.ac.goldcow.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.ac.goldcow.model.Snstype;
import kr.ac.goldcow.repository.SnstypeRepositoryDataJpa;
@Service("snstypeService")
@Transactional
public class SnstypeServiceDataJpa implements SnstypeService {
	@Autowired
	private SnstypeRepositoryDataJpa repository;
	@Override
	public List<Snstype> getSnstypes() {
		// TODO Auto-generated method stub
		return repository.findAll();
	}
	@Override
	public Snstype getSNS(long no) {
		// TODO Auto-generated method stub
		return repository.findById(no).get();
	}

}
