package kr.ac.goldcow;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.CookieGenerator;

import kr.ac.goldcow.model.Project;
import kr.ac.goldcow.model.User;
import kr.ac.goldcow.model.MultiRowUser;
import kr.ac.goldcow.service.UserService2;
import kr.ac.goldcow.service.UserServiceDataJpa;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	@Qualifier("userService")
	private UserService2 userService;//���� ����
	private BusinessController bc = new BusinessController();//����� ����� ���� ��ü
	//////////////////////////////////////////////////////////////////////////////////
	//																				//
	//								<<ȸ������ �� ���>>									//
	//																				//
	//////////////////////////////////////////////////////////////////////////////////
	@RequestMapping(value="edit.do", method=RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView model = new ModelAndView();//��� �����ϱ� ���� ��ü
		User user = new User();
		model.addObject("user", user);//���� ����ϱ����� ��ü ����
		model.setViewName("e_createuser");//ȸ������ �� �̸�
		return model;//��û������ ����
	}
	//////////////////////////////////////////////////////////////////////////////////
	//																				//
	//								<<�񵿱������ �̸��� üũ>>							//
	//																				//
	//////////////////////////////////////////////////////////////////////////////////
	@ResponseBody
	@RequestMapping(value="/checkemail.do", method=RequestMethod.POST)
	  public   String checkEmail(HttpServletRequest request, Model model) throws Exception {
		List<User> users;//�˻����
        String email = request.getParameter("email");//������ ����ϱ� ���ϴ� �̸���
        users = userService.checkEmail(email);//�̸����� �ߺ��� �̸������� �˻�
        if (users.size() == 0) {//�ߺ����� ���� �̸����̶�� 
        	return "0";//0����
        }else {
        	return "1";//�ƴϸ� 1����
        }
    }
	//�׽�Ʈ�� ���� �޼���
	/*
	@RequestMapping(value="test", method=RequestMethod.GET)
	public ModelAndView test() {
		ModelAndView model = new ModelAndView();
		User user = new User();
		Project project = new Project();
		model.addObject("user", user);
		model.addObject("project", project);
		model.setViewName("test");
		return model;
	}
	@RequestMapping(value="teststart", method=RequestMethod.POST)
	public String teststart(@ModelAttribute MultiRowUser users, @ModelAttribute("project") Project project) {
		System.out.println(users.getUsers().size());
		System.out.println(project.getName());
		System.out.println("��������");
		return "redirect:/";
	}*/
	//////////////////////////////////////////////////////////////////////////////////
	//																				//
	//								<<ȸ������ ����>>									//
	//																				//
	//////////////////////////////////////////////////////////////////////////////////
	@RequestMapping(value="create.do", method=RequestMethod.POST)
	public String add(@ModelAttribute("user") User user) throws Exception {
		userService.saveUser(user);//�ڵ����� ���Ե� ������ü�� ����
		return "redirect:/";
	}
	//////////////////////////////////////////////////////////////////////////////////
	//																				//
	//								<<�α��� �� ���>>									//
	//																				//
	//////////////////////////////////////////////////////////////////////////////////
	@RequestMapping(value="loginedit", method=RequestMethod.GET)
	public ModelAndView loginedit() {
		ModelAndView model = new ModelAndView();//��� �����ϱ� ���� ��ü
		User user = new User();//�� ����� ���� ��ü
		model.addObject("user", user);//������ü ����
		model.setViewName("e_login");//�α����� �̸� ����
		return model;//�α��������� �̵�
	}
	//////////////////////////////////////////////////////////////////////////////////
	//																				//
	//								<<�α���>>										//
	//																				//
	//////////////////////////////////////////////////////////////////////////////////
	@RequestMapping(value="login.do", method=RequestMethod.POST)
	public String login(@ModelAttribute("user") User user, HttpServletResponse response) throws Exception {
		User returnUser; 
		List<User> users;
		users = userService.login(user.getEmail(),user.getPassword());//���޹��� �̸��ϰ� �н������ �α���
		if(users.size() == 0) {//�α��������� Ʋ�ȴٸ�
			return "e_login";//�ٽ� �α��������� ���ư�.
		}
		returnUser = users.get(0);//�α����� ������ ������
		CookieGenerator cg = new CookieGenerator();//��Ű�� �����ϱ����� ��ü
		cg.setCookieName("name");
		cg.addCookie(response,returnUser.getName());
		cg.setCookieName("money");
		cg.addCookie(response,String.valueOf(returnUser.getMoney()));
		cg.setCookieName("no");
		cg.addCookie(response,String.valueOf(returnUser.getNo()));
		cg.setCookieName("isbusiness");
		cg.addCookie(response,String.valueOf(returnUser.isBusiness()));//�̸��� ���� ���̹��Ӵ�, ��ȣ, �����ȸ�� ���� ��Ű�� ����
		return "redirect:/";//Ȩ�������� �̵�
	}
	//////////////////////////////////////////////////////////////////////////////////
	//																				//
	//								<<�α׾ƿ�>>										//
	//																				//
	//////////////////////////////////////////////////////////////////////////////////
	@RequestMapping(value="logout.do", method=RequestMethod.GET)
	public String logout(HttpServletResponse response) {
		CookieGenerator cg = new CookieGenerator();
		cg.setCookieName("name");
		cg.setCookieMaxAge(0);
		cg.addCookie(response,null);
		cg.setCookieName("money");
		cg.setCookieMaxAge(0);
		cg.addCookie(response,null);
		cg.setCookieName("no");
		cg.setCookieMaxAge(0);
		cg.addCookie(response,null);
		cg.setCookieName("isbusiness");
		cg.setCookieMaxAge(0);
		cg.addCookie(response,null);//��Ű�� ���λ���
		return "redirect:/";//Ȩ�������� �̵�
	}
	//////////////////////////////////////////////////////////////////////////////////
	//																				//
	//								<<����ڵ�� �� ���>>								//
	//																				//
	//////////////////////////////////////////////////////////////////////////////////
	@RequestMapping(value="businessedit", method=RequestMethod.GET)
	public String businessedit() {
		return "e_businessedit";//����ڵ�� �� ���
	}
	//////////////////////////////////////////////////////////////////////////////////
	//																				//
	//								<<����ڵ������>>									//
	//																				//
	//////////////////////////////////////////////////////////////////////////////////
	@RequestMapping(value="checkbusiness.do", method=RequestMethod.GET)//��������� üũ��
	public String checkbusiness(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String userNo = null;
		String businessNo = request.getParameter("businessno");//������ �Է��� ����ڵ�Ϲ�ȣ
		String businessName = request.getParameter("businessname");//������ �Է��� ����� �̸�
		User user = new User(); 
		//----------------��Ű�κ��� ���� ��ȣ �˾Ƴ���--------------------
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
		//String returnName  = bc.checkbusiness(businessNo, businessName);//����ڵ����ȸ ��û
		String returnName = "return";
        if (returnName == null) {//����ڰ� �ƴҰ��
        	return "e_businessedit";//����ڵ�������� �̵�
        }
        user.setNo(Long.parseLong(userNo));
        user.setBusinessNo(businessNo);
        user.setBusiness(true);
        userService.registerBusiness(user);
        CookieGenerator cg = new CookieGenerator();//������ ������Ʈ�ϰ� ��Ű�� ����
		cg.setCookieName("isbusiness");
		cg.addCookie(response,"true");
		return "redirect:/";//Ȩ�������� �̵�
	}
	//////////////////////////////////////////////////////////////////////////////////
	//																				//
	//								<<���̹��Ӵ� ���� �� ���>>							//
	//																				//
	//////////////////////////////////////////////////////////////////////////////////
	@RequestMapping(value="money", method=RequestMethod.GET)
	public ModelAndView money(HttpServletRequest request) throws Exception {
		ModelAndView model = new ModelAndView();
		//----------------��Ű�κ��� ���� ��ȣ �˾Ƴ���--------------------
		Long userNo = null;
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
		User user = userService.getUserData(userNo);
		model.addObject("user", user);//������ ������ ����
		model.setViewName("e_money");//���̹��Ӵ� ������
		return model;//��� �̵�
	}
	//////////////////////////////////////////////////////////////////////////////////
	//																				//
	//								<<���̹��Ӵ� ����>>									//
	//																				//
	//////////////////////////////////////////////////////////////////////////////////
	@RequestMapping(value="addmoney.do", method=RequestMethod.POST)
	public String addMoney(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User user = new User();
		//----------------��Ű�κ��� ���� ��ȣ �˾Ƴ���--------------------
		Long userNo = null;
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
		Long money =Long.parseLong(request.getParameter("amount"));//������ �󸶳� �����ߴ��� �˾Ƴ�
		user.setNo(userNo);
		user.setMoney(money);
		userService.addMoney(user);//������ ���̹��Ӵ� ����
		CookieGenerator cg = new CookieGenerator();//��Ű������ ���� ��ü
		user = userService.getUserData(userNo);
		cg.setCookieName("money");
		cg.addCookie(response,String.valueOf(user.getMoney()));//���� ������ �ݾ���Ű�� ����
		return "redirect:/";//Ȩ�������� �̵�.
	}


	
	}

