package kr.ac.goldcow.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import kr.ac.goldcow.model.Project;

public interface ProjectRepositoryDataJpa extends JpaRepository<Project, Long>{
public List<Project> getProjectsByCategory(@Param("category") long category, Sort sort);
public List<Project> getProjectsByState(@Param("state") long state);
public List<Project> getProjectsByUserNo(@Param("userNo") long no, Sort sort);
public List<Project> findByNameContains(String keyWord);
}
