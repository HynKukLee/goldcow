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
	private SupportService supportService;//후원 서비스
	
	@Autowired
	@Qualifier("rewardService")
	private RewardService rewardService;//보상 서비스
	
	@Autowired
	@Qualifier("userService")
	private UserService2 userService;//유저 서비스
	
	@Autowired
	private ProjectService projectService;//프로젝트 서비스
	//////////////////////////////////////////////////////////////////////////////////
	//																				//
	//								<<후원할 금액을 입력>>								//
	//								<<보상선택으로 연결됨>>								//
	//																				//
	//////////////////////////////////////////////////////////////////////////////////
	@RequestMapping(value="addmoney", method=RequestMethod.GET)
	public ModelAndView addMoney(HttpServletRequest request) {
		//후원하고싶은 프로젝트의 번호를 알기위한 리퀘스트
		ModelAndView model = new ModelAndView();//뷰로 연결시켜주기위한 객체
		Support support = new Support();//후원객체
		support.setProjectno(Long.parseLong(request.getParameter("projectno")));//후원정보에 프로젝트 번호 저장
		model.addObject("support", support);//후원 저장
		model.setViewName("e_addmoney");//뷰 이름 지정
		return model;//요청에대한 응답
	}
	//////////////////////////////////////////////////////////////////////////////////
	//																				//
	//								<<보상목록 출력>>									//
	//																				//
	//////////////////////////////////////////////////////////////////////////////////
	@RequestMapping(value="supportedit", method=RequestMethod.POST)
	public ModelAndView addMoney(@ModelAttribute Support support) throws Exception {
		ModelAndView model = new ModelAndView();//뷰로 연결시켜주기위한 객체
		List<Reward> rewards = rewardService.getRewards(support.getProjectno());//프로젝트 번호로부터 보상목록 뽑아옴.
		model.addObject("rewards", rewards);//보상목록 전달
		model.addObject("support", support);//후원정보 전달
		model.setViewName("e_supportedit");//보상선택화면으로 연결
		return model;
	}
	//////////////////////////////////////////////////////////////////////////////////
	//																				//
	//						<<새로운 후원 등록>>											//
	//																				//
	//////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings("deprecation")
	@RequestMapping(value="registersupport.do", method=RequestMethod.POST)
	public String registerSupport(@ModelAttribute Support support, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Long userNo = null;
		User user = new User();
		Project project = new Project();
		//----------------쿠키로부터 유저 번호 알아내기--------------------
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
		support.setSupportDate(new Timestamp(new java.util.Date().getTime()));//현재시간 추출
		support.setUserNo(userNo);//후원에 유저번호 저장
		support.setCanceled(false);//후원이 아직 취소되지 않음
		user.setNo(userNo);//
		user.setMoney(support.getAmount());
		project.setNo(support.getProjectno());
		project.setNowamount(support.getAmount());
		projectService.addMoney(project);//프로젝트의 후원액 증가
		userService.subMoney(user);//유저의 머니 차감
		supportService.saveSupport(support);//새로운 후원의 등록
		CookieGenerator cg = new CookieGenerator();
		user = userService.getUserData(userNo);
		cg.setCookieName("money");
		cg.addCookie(response,String.valueOf(user.getMoney()));//쿠키값 변화
		return "redirect:/";//홈페이지로 돌아감
		
	}
	//////////////////////////////////////////////////////////////////////////////////
	//																				//
	//								<<후원취소>>										//
	//																				//
	//////////////////////////////////////////////////////////////////////////////////
	@RequestMapping(value="supportcancel.do", method=RequestMethod.GET)
	public String supportCancel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Support support;
		Project project;
		User user;
		support = supportService.supportCancel(Long.parseLong(request.getParameter("no")));//후원을 취소하고 후원정보를 가져옴.
		user = new User();
		project = new Project();
		user.setNo(support.getUserNo());//후원정보로부터 유저번호 저장
		user.setMoney(support.getAmount());//후원정보로부터 돌려줄 금액 저장
		userService.addMoney(user);//유저의 돈 돌려줌
		project.setNo(support.getProjectno());//후원정보로부터 프로젝트 번호 저장
		project.setNowamount(support.getAmount());//후원정보로부터 차감할 금액 저장
		projectService.subMoney(project);//프로젝트 현재 금액 차감
		CookieGenerator cg = new CookieGenerator();//쿠키저장을 위한 객체
		user = userService.getUserData(support.getUserNo());//유저의 현재 금액을 다시 가져옴.
		cg.setCookieName("money");//현재유저의 금액을 지정
		cg.addCookie(response,String.valueOf(user.getMoney()));//쿠키저장
		return "redirect:/";//홈페이지로 이동
	}
	//////////////////////////////////////////////////////////////////////////////////
	//																				//
	//								<<내가 후원한 리스트>>								//
	//																				//
	//////////////////////////////////////////////////////////////////////////////////
	@RequestMapping(value="mysupportlist", method=RequestMethod.GET)
	public ModelAndView mySupportList(HttpServletRequest request) throws Exception {
		ModelAndView model = new ModelAndView();//뷰로 연결을 위한 객체
		//----------------쿠키로부터 유저 번호 알아내기--------------------
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
		//----------------쿠키로부터 유저 번호 알아내기--------------------
		List<Support> supports = supportService.getSupportsByUserNo(Long.parseLong(userNo));
		//알아낸 유저번호로 후원목록을 뽑아냄.
		PageManager pm = new PageManager(supports.size(),2,Integer.parseInt(request.getParameter("page")),2);
		//한페이지에 2개의 후원 출력
		pm.processPage();//페이징 처리
		if(pm.getEndNum() != -1) {//후원이 하나도 없지 않다면
		supports = supports.subList(pm.getStartNum(), pm.getEndNum()+1);//한페이지에서 출력할만큼 후원목록을 추림
		}
		model.addObject("supports", supports);//후원목록 저장
		model.addObject("pm", pm);//페이지 매니저 저장
		model.setViewName("e_mysupportlist");//뷰 이름 지정
		return model;//요청에 대한 응답
	}
	//////////////////////////////////////////////////////////////////////////////////
	//																				//
	//						<<내가 등록한 프로젝트에대한 후원자 목록>>							//
	//																				//
	//////////////////////////////////////////////////////////////////////////////////
	@RequestMapping(value="myprojectssupportlist", method=RequestMethod.GET)
	public ModelAndView myprojectsSupportList(HttpServletRequest request) throws Exception {
		ModelAndView model = new ModelAndView();//뷰로 연결을 위한 객체
		List<Support> supports = supportService.getSupportsByProjectno(Long.parseLong(request.getParameter("no")));
		//프로젝트 번호로부터 후원목록 추출
		PageManager pm = new PageManager(supports.size(),2,Integer.parseInt(request.getParameter("page")),2);
		//한페이지에 2개의 후원 출력
		pm.processPage();//페이징 처리
		if(pm.getEndNum() != -1) {//후원이 하나도 없지 않다면
		supports = supports.subList(pm.getStartNum(), pm.getEndNum()+1);//한페이지에서 출력할만큼 후원목록을 추림
		}
		model.addObject("supports", supports);//후원목록 저장
		model.addObject("pm", pm);//페이지 매니저 저장
		model.addObject("no", request.getParameter("no"));//다음 페이지를 위한 프로젝트 번호 전달
		model.setViewName("e_myprojectssupportlist");//뷰 이름 지정
		return model;//요청에 대한 응답
	}

	
}
