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
	private SupportService supportService;//����Ʈ ����
	@Autowired
	private ProjectService projectService;//������Ʈ ����
	@Autowired
	@Qualifier("userService")
	private UserService2 userService;//���� ����
	//@Scheduled(cron="30 23 * * *")
	//���Ϲ� 11�� 30�п�����
	@Scheduled(cron="*/50 * * * * *")//50�ʸ��� ����
	   public void cronTest1(){
	        try {
	        	List<Support> supports = new ArrayList<Support>();//��ҵ� ��� �Ŀ����
	        	List<Long> numbers = projectService.expireProject();//��� ������Ʈ�� ����ϰ� �� ��ȣ�� ����
	        	for(long number : numbers) {//����� ��� ������Ʈ�� ��ȣ
	        		supports.addAll(supportService.getSupportsByProject(number));//��ҵ� ������Ʈ�� �Ŀ��ߴ� ��� ��� ������.
	        	}
	        	for(Support support : supports) {//��ҵ� �Ŀ� �ϳ��ϳ�����
	        		User user = new User();
	        		user.setNo(support.getUserNo());
	        		user.setMoney(support.getAmount());
	        		userService.addMoney(user);//������ �Ӵϸ� ������.
	        	}
	        	System.out.println("����Ǿ����ϴ�.");
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
}
