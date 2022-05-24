package kr.ac.goldcow.service;

import java.util.List;

import kr.ac.goldcow.model.Reward;

public interface RewardService {
public List<Reward> save(List<Reward> rewards) throws Exception;
public List<Reward> getRewards(long projectNo) throws Exception;
}
