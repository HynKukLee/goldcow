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
	private UserService2 userService;//유저 서비스
	private BusinessController bc = new BusinessController();//사업자 등록을 위한 객체
	//////////////////////////////////////////////////////////////////////////////////
	//																				//
	//								<<회원가입 폼 출력>>									//
	//																				//
	//////////////////////////////////////////////////////////////////////////////////
	@RequestMapping(value="edit.do", method=RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView model = new ModelAndView();//뷰로 연결하기 위한 객체
		User user = new User();
		model.addObject("user", user);//폼을 출력하기위해 객체 생성
		model.setViewName("e_createuser");//회원가입 폼 이름
		return model;//요청에대한 응답
	}
	//////////////////////////////////////////////////////////////////////////////////
	//																				//
	//								<<비동기식으로 이메일 체크>>							//
	//																				//
	//////////////////////////////////////////////////////////////////////////////////
	@ResponseBody
	@RequestMapping(value="/checkemail.do", method=RequestMethod.POST)
	  public   String checkEmail(HttpServletRequest request, Model model) throws Exception {
		List<User> users;//검색결과
        String email = request.getParameter("email");//유저가 등록하길 원하는 이메일
        users = userService.checkEmail(email);//이메일이 중복된 이메일인지 검사
        if (users.size() == 0) {//중복되지 않은 이메일이라면 
        	return "0";//0리턴
        }else {
        	return "1";//아니면 1리턴
        }
    }
	//테스트를 위한 메서드
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
		System.out.println("집에가자");
		return "redirect:/";
	}*/
	//////////////////////////////////////////////////////////////////////////////////
	//																				//
	//								<<회원가입 저장>>									//
	//																				//
	//////////////////////////////////////////////////////////////////////////////////
	@RequestMapping(value="create.do", method=RequestMethod.POST)
	public String add(@ModelAttribute("user") User user) throws Exception {
		userService.saveUser(user);//자동으로 주입된 유저객체를 저장
		return "redirect:/";
	}
	//////////////////////////////////////////////////////////////////////////////////
	//																				//
	//								<<로그인 폼 출력>>									//
	//																				//
	//////////////////////////////////////////////////////////////////////////////////
	@RequestMapping(value="loginedit", method=RequestMethod.GET)
	public ModelAndView loginedit() {
		ModelAndView model = new ModelAndView();//뷰로 연결하기 위한 객체
		User user = new User();//폼 출력을 위한 객체
		model.addObject("user", user);//유저객체 전달
		model.setViewName("e_login");//로그인폼 이름 저장
		return model;//로그인폼으로 이동
	}
	//////////////////////////////////////////////////////////////////////////////////
	//																				//
	//								<<로그인>>										//
	//																				//
	//////////////////////////////////////////////////////////////////////////////////
	@RequestMapping(value="login.do", method=RequestMethod.POST)
	public String login(@ModelAttribute("user") User user, HttpServletResponse response) throws Exception {
		User returnUser; 
		List<User> users;
		users = userService.login(user.getEmail(),user.getPassword());//전달받은 이메일과 패스워드로 로그인
		if(users.size() == 0) {//로그인정보가 틀렸다면
			return "e_login";//다시 로그인폼으로 돌아감.
		}
		returnUser = users.get(0);//로그인한 유저의 상세정보
		CookieGenerator cg = new CookieGenerator();//쿠키를 저장하기위한 객체
		cg.setCookieName("name");
		cg.addCookie(response,returnUser.getName());
		cg.setCookieName("money");
		cg.addCookie(response,String.valueOf(returnUser.getMoney()));
		cg.setCookieName("no");
		cg.addCookie(response,String.valueOf(returnUser.getNo()));
		cg.setCookieName("isbusiness");
		cg.addCookie(response,String.valueOf(returnUser.isBusiness()));//이름과 현재 사이버머니, 번호, 사업자회원 여부 쿠키로 저장
		return "redirect:/";//홈페이지로 이동
	}
	//////////////////////////////////////////////////////////////////////////////////
	//																				//
	//								<<로그아웃>>										//
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
		cg.addCookie(response,null);//쿠키값 전부삭제
		return "redirect:/";//홈페이지로 이동
	}
	//////////////////////////////////////////////////////////////////////////////////
	//																				//
	//								<<사업자등록 폼 출력>>								//
	//																				//
	//////////////////////////////////////////////////////////////////////////////////
	@RequestMapping(value="businessedit", method=RequestMethod.GET)
	public String businessedit() {
		return "e_businessedit";//사업자등록 폼 출력
	}
	//////////////////////////////////////////////////////////////////////////////////
	//																				//
	//								<<사업자등록저장>>									//
	//																				//
	//////////////////////////////////////////////////////////////////////////////////
	@RequestMapping(value="checkbusiness.do", method=RequestMethod.GET)//사업자인지 체크함
	public String checkbusiness(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String userNo = null;
		String businessNo = request.getParameter("businessno");//유저가 입력한 사업자등록번호
		String businessName = request.getParameter("businessname");//유저가 입력한 사업장 이름
		User user = new User(); 
		//----------------쿠키로부터 유저 번호 알아내기--------------------
		Cookie[] getCookie = request.getCookies();
		if(getCookie != null){
			for(int i=0; i<getCookie.length; i++){
				Cookie c = getCookie[i];
					if (c.getName().equals("no")) {
						userNo = c.getValue();
					}
			}
		}
		//----------------쿠키로부터 유저 번호 알아내기--------------------
		//String returnName  = bc.checkbusiness(businessNo, businessName);//사업자등록조회 요청
		String returnName = "return";
        if (returnName == null) {//사업자가 아닐경우
        	return "e_businessedit";//사업자등록폼으로 이동
        }
        user.setNo(Long.parseLong(userNo));
        user.setBusinessNo(businessNo);
        user.setBusiness(true);
        userService.registerBusiness(user);
        CookieGenerator cg = new CookieGenerator();//유저를 업데이트하고 쿠키값 변경
		cg.setCookieName("isbusiness");
		cg.addCookie(response,"true");
		return "redirect:/";//홈페이지로 이동
	}
	//////////////////////////////////////////////////////////////////////////////////
	//																				//
	//								<<사이버머니 구매 폼 출력>>							//
	//																				//
	//////////////////////////////////////////////////////////////////////////////////
	@RequestMapping(value="money", method=RequestMethod.GET)
	public ModelAndView money(HttpServletRequest request) throws Exception {
		ModelAndView model = new ModelAndView();
		//----------------쿠키로부터 유저 번호 알아내기--------------------
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
		//----------------쿠키로부터 유저 번호 알아내기--------------------
		User user = userService.getUserData(userNo);
		model.addObject("user", user);//유저의 정보를 저장
		model.setViewName("e_money");//사이버머니 구매폼
		return model;//뷰로 이동
	}
	//////////////////////////////////////////////////////////////////////////////////
	//																				//
	//								<<사이버머니 충전>>									//
	//																				//
	//////////////////////////////////////////////////////////////////////////////////
	@RequestMapping(value="addmoney.do", method=RequestMethod.POST)
	public String addMoney(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User user = new User();
		//----------------쿠키로부터 유저 번호 알아내기--------------------
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
		//----------------쿠키로부터 유저 번호 알아내기--------------------
		Long money =Long.parseLong(request.getParameter("amount"));//유저가 얼마나 충전했는지 알아냄
		user.setNo(userNo);
		user.setMoney(money);
		userService.addMoney(user);//유저의 사이버머니 충전
		CookieGenerator cg = new CookieGenerator();//쿠키저장을 위한 객체
		user = userService.getUserData(userNo);
		cg.setCookieName("money");
		cg.addCookie(response,String.valueOf(user.getMoney()));//현재 유저의 금액쿠키에 저장
		return "redirect:/";//홈페이지로 이동.
	}


	
	}

