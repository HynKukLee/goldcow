package kr.ac.goldcow.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import kr.ac.goldcow.model.Support;

public interface SupportRepositoryDataJpa  extends JpaRepository<Support, Long>{
public List<Support> getSupportsByProjectnoAndIsCanceled(@Param("projectno")long no,@Param("isCanceled")boolean cancel);
public List<Support> getSupportsByUserNoAndIsCanceled(@Param("userNo")long no,@Param("isCanceled")boolean cancel, Sort sort);
public List<Support> getSupportsByProjectnoAndIsCanceled(@Param("projectno")long no, @Param("isCanceled")boolean cancel, Sort sort);
}
