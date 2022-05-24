package kr.ac.goldcow.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.ac.goldcow.model.Support;
import kr.ac.goldcow.repository.SupportRepositoryDataJpa;

@Service("supportService")
@Transactional
public class SupportServiceDataJPA implements SupportService {
	@Autowired
	private SupportRepositoryDataJpa repository;

	@Override
	public void saveSupport(Support support) {
		// TODO Auto-generated method stub
		repository.save(support);
	}

	@Override
	public List<Support> getSupportsByProject(long no) {
		// TODO Auto-generated method stub
		List<Support> supports = repository.getSupportsByProjectnoAndIsCanceled(no,false);
		for(Support support : supports) {
			Support entity = repository.findById(support.getNo()).get();
			entity.setCanceled(true);
		}
		return supports;
	}

	@Override
	public List<Support> getSupportsByUserNo(long no) {
		// TODO Auto-generated method stub
		List<Support> supports =  repository.getSupportsByUserNoAndIsCanceled(no, false, new Sort(
				new Sort.Order(Sort.Direction.DESC, "no")
				));
		
		return supports;
	}

	@Override
	public Support supportCancel(long no) {
		// TODO Auto-generated method stub
		Support entity = repository.findById(no).get();
		entity.setCanceled(true);
		return repository.findById(no).get();
	}

	@Override
	public List<Support> getSupportsByProjectno(long no) {
		// TODO Auto-generated method stub
		return repository.getSupportsByProjectnoAndIsCanceled(no, false, new Sort(
				new Sort.Order(Sort.Direction.DESC, "no")
				));
	}
}
