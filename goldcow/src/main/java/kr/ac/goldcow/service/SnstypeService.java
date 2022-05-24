package kr.ac.goldcow.service;

import java.util.List;

import kr.ac.goldcow.model.Snstype;

public interface SnstypeService {
	public List<Snstype> getSnstypes();
	public Snstype getSNS(long no);
}
