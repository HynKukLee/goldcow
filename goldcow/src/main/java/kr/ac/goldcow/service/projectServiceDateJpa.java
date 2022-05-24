package kr.ac.goldcow.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.ac.goldcow.model.Project;
import kr.ac.goldcow.repository.ProjectRepositoryDataJpa;
@Service("projectService")
@Transactional
public class projectServiceDateJpa implements ProjectService {
	@Autowired
	private ProjectRepositoryDataJpa repository;
	@Override
	@Transactional(rollbackFor = NotFoundException.class)
	public long saveProject (Project project) throws Exception {
		// TODO Auto-generated method stub
		Project saved = repository.save(project);
		return saved.getNo();
	}
	@Override
	public List<Project> getProjects() throws Exception {
		// TODO Auto-generated method stub
		List<Project> projects =  repository.findAll(new Sort(
				new Sort.Order(Sort.Direction.DESC, "no")
				));
		if(projects.size()>9) {
		projects = projects.subList(0,9);
		}
		return projects;
	}
	@Override
	public Project getProjct(long no) throws Exception {
		// TODO Auto-generated method stub
		Project project = repository.findById(no).get();
		Hibernate.initialize(project);
		return project;
	}
	@Override
	public List<Project> getProjectsByCategory(long category) throws Exception {
		// TODO Auto-generated method stub
		return repository.getProjectsByCategory(category, new Sort(
				new Sort.Order(Sort.Direction.DESC, "no")
				));
	}
	@Override
	public void addMoney(Project project) throws Exception {
		// TODO Auto-generated method stub
		Project entity = repository.findById(project.getNo()).get();
		entity.setNowamount(entity.getNowamount()+project.getNowamount());
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public List<Long> expireProject() throws Exception {
		// TODO Auto-generated method stub
		Date date = new Date();
		List<Long> numbers;
		numbers = new ArrayList<Long>();
		Timestamp timestamp = new Timestamp(date.getTime());
		List<Project> projects = repository.getProjectsByState(0l);
		for(Project project: projects) {
			if(project.getExpiredate().getYear() == timestamp.getYear()&&project.getExpiredate().getMonth() == timestamp.getMonth()&&project.getExpiredate().getDate() == timestamp.getDate()) {
				long result;
				result = expire(project.getNo());
				if(result != -1l) {
					numbers.add(result);
				}
			}
		}
		return numbers;
	}
	@Override
	public long expire(long no) throws Exception {
		// TODO Auto-generated method stub
		Project entity = repository.findById(no).get();
		if(entity.getAmount()<=entity.getNowamount()) {
			entity.setState(1l);
			return -1l;
		}else {
			entity.setState(2l);
		}
		return entity.getNo();
	}
	@Override
	public List<Project> getProjectsByUserNo(long userNo)throws Exception {
		// TODO Auto-generated method stub
		return repository.getProjectsByUserNo(userNo, new Sort(
				new Sort.Order(Sort.Direction.DESC, "no")
				));
	}
	@Override
	public void subMoney(Project project) throws Exception {
		// TODO Auto-generated method stub
		Project entity = repository.findById(project.getNo()).get();
		entity.setNowamount(entity.getNowamount()-project.getNowamount());
	}
	@Override
	public List<Project> searchProjects(String keyWord) throws Exception {
		// TODO Auto-generated method stub
		List<Project> results = repository.findByNameContains(keyWord);
		return results;
	}
	

}
