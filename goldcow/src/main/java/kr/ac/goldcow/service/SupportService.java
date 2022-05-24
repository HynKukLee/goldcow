package kr.ac.goldcow.service;

import java.util.List;

import kr.ac.goldcow.model.Support;

public interface SupportService {
	public void saveSupport(Support support);
	public List<Support> getSupportsByProject(long no);
	public List<Support> getSupportsByUserNo(long no);
	public List<Support> getSupportsByProjectno(long no);
	public Support supportCancel(long no);
}
