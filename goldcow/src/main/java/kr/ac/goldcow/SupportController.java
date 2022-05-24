package kr.ac.goldcow;

import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.CookieGenerator;

import kr.ac.goldcow.model.Project;
import kr.ac.goldcow.model.Reward;
import kr.ac.goldcow.model.Support;
import kr.ac.goldcow.model.User;
import kr.ac.goldcow.service.ProjectService;
import kr.ac.goldcow.service.RewardService;
import kr.ac.goldcow.service.SupportService;
import kr.ac.goldcow.service.UserService2;

import java.sql.Timestamp;
@Controller
@RequestMapping("/support")
public class SupportController {
	@Autowired
	@Qualifier("supportService")
	private SupportService supportService;//�Ŀ� ����
	
	@Autowired
	@Qualifier("rewardService")
	private RewardService rewardService;//���� ����
	
	@Autowired
	@Qualifier("userService")
	private UserService2 userService;//���� ����
	
	@Autowired
	private ProjectService projectService;//������Ʈ ����
	//////////////////////////////////////////////////////////////////////////////////
	//																				//
	//								<<�Ŀ��� �ݾ��� �Է�>>								//
	//								<<���������� �����>>								//
	//																				//
	//////////////////////////////////////////////////////////////////////////////////
	@RequestMapping(value="addmoney", method=RequestMethod.GET)
	public ModelAndView addMoney(HttpServletRequest request) {
		//�Ŀ��ϰ���� ������Ʈ�� ��ȣ�� �˱����� ������Ʈ
		ModelAndView model = new ModelAndView();//��� ��������ֱ����� ��ü
		Support support = new Support();//�Ŀ���ü
		support.setProjectno(Long.parseLong(request.getParameter("projectno")));//�Ŀ������� ������Ʈ ��ȣ ����
		model.addObject("support", support);//�Ŀ� ����
		model.setViewName("e_addmoney");//�� �̸� ����
		return model;//��û������ ����
	}
	//////////////////////////////////////////////////////////////////////////////////
	//																				//
	//								<<������ ���>>									//
	//																				//
	//////////////////////////////////////////////////////////////////////////////////
	@RequestMapping(value="supportedit", method=RequestMethod.POST)
	public ModelAndView addMoney(@ModelAttribute Support support) throws Exception {
		ModelAndView model = new ModelAndView();//��� ��������ֱ����� ��ü
		List<Reward> rewards = rewardService.getRewards(support.getProjectno());//������Ʈ ��ȣ�κ��� ������ �̾ƿ�.
		model.addObject("rewards", rewards);//������ ����
		model.addObject("support", support);//�Ŀ����� ����
		model.setViewName("e_supportedit");//������ȭ������ ����
		return model;
	}
	//////////////////////////////////////////////////////////////////////////////////
	//																				//
	//						<<���ο� �Ŀ� ���>>											//
	//																				//
	//////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings("deprecation")
	@RequestMapping(value="registersupport.do", method=RequestMethod.POST)
	public String registerSupport(@ModelAttribute Support support, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Long userNo = null;
		User user = new User();
		Project project = new Project();
		//----------------��Ű�κ��� ���� ��ȣ �˾Ƴ���--------------------
		Cookie[] getCookie = request.getCookies();
		if(getCookie != null){
			for(int i=0; i<getCookie.length; i++){
				Cookie c = getCookie[i];
				if (c.getName().equals("no")) {
					userNo = Long.parseLong(c.getValue());
				}
			}
		}
		//----------------��Ű�κ��� ���� ��ȣ �˾Ƴ���--------------------
		support.setSupportDate(new Timestamp(new java.util.Date().getTime()));//����ð� ����
		support.setUserNo(userNo);//�Ŀ��� ������ȣ ����
		support.setCanceled(false);//�Ŀ��� ���� ��ҵ��� ����
		user.setNo(userNo);//
		user.setMoney(support.getAmount());
		project.setNo(support.getProjectno());
		project.setNowamount(support.getAmount());
		projectService.addMoney(project);//������Ʈ�� �Ŀ��� ����
		userService.subMoney(user);//������ �Ӵ� ����
		supportService.saveSupport(support);//���ο� �Ŀ��� ���
		CookieGenerator cg = new CookieGenerator();
		user = userService.getUserData(userNo);
		cg.setCookieName("money");
		cg.addCookie(response,String.valueOf(user.getMoney()));//��Ű�� ��ȭ
		return "redirect:/";//Ȩ�������� ���ư�
		
	}
	//////////////////////////////////////////////////////////////////////////////////
	//																				//
	//								<<�Ŀ����>>										//
	//																				//
	//////////////////////////////////////////////////////////////////////////////////
	@RequestMapping(value="supportcancel.do", method=RequestMethod.GET)
	public String supportCancel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Support support;
		Project project;
		User user;
		support = supportService.supportCancel(Long.parseLong(request.getParameter("no")));//�Ŀ��� ����ϰ� �Ŀ������� ������.
		user = new User();
		project = new Project();
		user.setNo(support.getUserNo());//�Ŀ������κ��� ������ȣ ����
		user.setMoney(support.getAmount());//�Ŀ������κ��� ������ �ݾ� ����
		userService.addMoney(user);//������ �� ������
		project.setNo(support.getProjectno());//�Ŀ������κ��� ������Ʈ ��ȣ ����
		project.setNowamount(support.getAmount());//�Ŀ������κ��� ������ �ݾ� ����
		projectService.subMoney(project);//������Ʈ ���� �ݾ� ����
		CookieGenerator cg = new CookieGenerator();//��Ű������ ���� ��ü
		user = userService.getUserData(support.getUserNo());//������ ���� �ݾ��� �ٽ� ������.
		cg.setCookieName("money");//���������� �ݾ��� ����
		cg.addCookie(response,String.valueOf(user.getMoney()));//��Ű����
		return "redirect:/";//Ȩ�������� �̵�
	}
	//////////////////////////////////////////////////////////////////////////////////
	//																				//
	//								<<���� �Ŀ��� ����Ʈ>>								//
	//																				//
	//////////////////////////////////////////////////////////////////////////////////
	@RequestMapping(value="mysupportlist", method=RequestMethod.GET)
	public ModelAndView mySupportList(HttpServletRequest request) throws Exception {
		ModelAndView model = new ModelAndView();//��� ������ ���� ��ü
		//----------------��Ű�κ��� ���� ��ȣ �˾Ƴ���--------------------
		String userNo = null;
		Cookie[] getCookie = request.getCookies();
		if(getCookie != null){
		for(int i=0; i<getCookie.length; i++){
			Cookie c = getCookie[i];
				if (c.getName().equals("no")) {
					userNo = c.getValue();
				}
			}
		}
		//----------------��Ű�κ��� ���� ��ȣ �˾Ƴ���--------------------
		List<Support> supports = supportService.getSupportsByUserNo(Long.parseLong(userNo));
		//�˾Ƴ� ������ȣ�� �Ŀ������ �̾Ƴ�.
		PageManager pm = new PageManager(supports.size(),2,Integer.parseInt(request.getParameter("page")),2);
		//���������� 2���� �Ŀ� ���
		pm.processPage();//����¡ ó��
		if(pm.getEndNum() != -1) {//�Ŀ��� �ϳ��� ���� �ʴٸ�
		supports = supports.subList(pm.getStartNum(), pm.getEndNum()+1);//������������ ����Ҹ�ŭ �Ŀ������ �߸�
		}
		model.addObject("supports", supports);//�Ŀ���� ����
		model.addObject("pm", pm);//������ �Ŵ��� ����
		model.setViewName("e_mysupportlist");//�� �̸� ����
		return model;//��û�� ���� ����
	}
	//////////////////////////////////////////////////////////////////////////////////
	//																				//
	//						<<���� ����� ������Ʈ������ �Ŀ��� ���>>							//
	//																				//
	//////////////////////////////////////////////////////////////////////////////////
	@RequestMapping(value="myprojectssupportlist", method=RequestMethod.GET)
	public ModelAndView myprojectsSupportList(HttpServletRequest request) throws Exception {
		ModelAndView model = new ModelAndView();//��� ������ ���� ��ü
		List<Support> supports = supportService.getSupportsByProjectno(Long.parseLong(request.getParameter("no")));
		//������Ʈ ��ȣ�κ��� �Ŀ���� ����
		PageManager pm = new PageManager(supports.size(),2,Integer.parseInt(request.getParameter("page")),2);
		//���������� 2���� �Ŀ� ���
		pm.processPage();//����¡ ó��
		if(pm.getEndNum() != -1) {//�Ŀ��� �ϳ��� ���� �ʴٸ�
		supports = supports.subList(pm.getStartNum(), pm.getEndNum()+1);//������������ ����Ҹ�ŭ �Ŀ������ �߸�
		}
		model.addObject("supports", supports);//�Ŀ���� ����
		model.addObject("pm", pm);//������ �Ŵ��� ����
		model.addObject("no", request.getParameter("no"));//���� �������� ���� ������Ʈ ��ȣ ����
		model.setViewName("e_myprojectssupportlist");//�� �̸� ����
		return model;//��û�� ���� ����
	}

	
}
