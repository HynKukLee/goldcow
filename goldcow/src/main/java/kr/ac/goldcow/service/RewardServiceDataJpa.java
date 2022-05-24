package kr.ac.goldcow.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.ac.goldcow.model.Reward;
import kr.ac.goldcow.repository.RewardRepositoryDataJpa;
@Service("rewardService")
@Transactional
public class RewardServiceDataJpa implements RewardService {

	@Autowired
	private RewardRepositoryDataJpa repository;
	
	@Transactional(rollbackFor = NotFoundException.class)
	@Override
	public List<Reward> save(List<Reward> rewards) throws Exception {
		// TODO Auto-generated method stub
		return repository.saveAll(rewards);
	}

	@Override
	public List<Reward> getRewards(long projectNo) throws Exception {
		// TODO Auto-generated method stub
		return repository.getRewardsByProjectNo(projectNo);
	}

}
