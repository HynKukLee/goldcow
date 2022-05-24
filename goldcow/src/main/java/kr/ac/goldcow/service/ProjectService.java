package kr.ac.goldcow.service;

import java.util.List;

import kr.ac.goldcow.model.Project;

public interface ProjectService {
public long saveProject(Project project) throws Exception;
public List<Project> getProjects() throws Exception;
public List<Project> searchProjects(String keyWord) throws Exception;
public List<Project> getProjectsByCategory(long category) throws Exception;
public Project getProjct(long no) throws Exception;
public void addMoney(Project project) throws Exception;
public void subMoney(Project project) throws Exception;
public List<Long> expireProject()throws Exception;
public long expire(long no)throws Exception;
public List<Project> getProjectsByUserNo(long userNo)throws Exception;
}
