package kr.ac.goldcow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import kr.ac.goldcow.model.Reward;

public interface RewardRepositoryDataJpa extends JpaRepository<Reward, Long> {
	List<Reward> getRewardsByProjectNo (@Param("projectNo") long projectNo);
}
