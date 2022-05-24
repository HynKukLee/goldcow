package kr.ac.goldcow;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import kr.ac.goldcow.service.ProjectService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {//홈페이지를 보여주는 컨트롤러
	@Autowired
	private ProjectService projectService;//신제품 목록을 보여주기 위한 프로젝트 서비스
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	/**
	 * Simply selects the home view to render by returning its name.
	 * @throws Exception 
	 */
	//////////////////////////////////////////////////////////////////////////////////
	//																				//
	//								홈페이지											//
	//																				//
	//////////////////////////////////////////////////////////////////////////////////
	@RequestMapping(value = "/", method = RequestMethod.GET)//추가적으로 아무것도없을때 연결
	public String home(Locale locale, Model model) throws Exception {
		logger.info("Welcome home! The client locale is {}.", locale);
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		String formattedDate = dateFormat.format(date);
		model.addAttribute("serverTime", formattedDate );
		model.addAttribute("projects",projectService.getProjects());//프로젝트서비스로부터 신프로젝트 목록을 받아 홈페이지로 보여줌.
		return "e_home";//홈페이지로 이동
	}
	@RequestMapping(value = "/login.do", method = RequestMethod.GET)
	public String login() {
		return "login";
	}
	
}
