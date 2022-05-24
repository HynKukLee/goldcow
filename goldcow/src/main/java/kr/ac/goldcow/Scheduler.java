package kr.ac.goldcow;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import kr.ac.goldcow.model.Support;
import kr.ac.goldcow.model.User;
import kr.ac.goldcow.service.ProjectService;
import kr.ac.goldcow.service.SupportService;
import kr.ac.goldcow.service.UserService2;

@Component
public class Scheduler {
	@Autowired
	@Qualifier("supportService")
	private SupportService supportService;//서포트 서비스
	@Autowired
	private ProjectService projectService;//프로젝트 서비스
	@Autowired
	@Qualifier("userService")
	private UserService2 userService;//유저 서비스
	//@Scheduled(cron="30 23 * * *")
	//매일밤 11시 30분에실행
	@Scheduled(cron="*/50 * * * * *")//50초마다 수행
	   public void cronTest1(){
	        try {
	        	List<Support> supports = new ArrayList<Support>();//취소될 모든 후원목록
	        	List<Long> numbers = projectService.expireProject();//모든 프로젝트를 취소하고 그 번호를 저장
	        	for(long number : numbers) {//취소한 모든 프로젝트의 번호
	        		supports.addAll(supportService.getSupportsByProject(number));//취소된 프로젝트에 후원했던 모든 기록 가져옴.
	        	}
	        	for(Support support : supports) {//취소된 후원 하나하나실행
	        		User user = new User();
	        		user.setNo(support.getUserNo());
	        		user.setMoney(support.getAmount());
	        		userService.addMoney(user);//유저의 머니를 돌려줌.
	        	}
	        	System.out.println("만료되었습니다.");
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
}
