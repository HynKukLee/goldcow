package kr.ac.goldcow;

import java.io.File;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

//import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import kr.ac.goldcow.model.Comment;
import kr.ac.goldcow.model.MultiRowReward;
import kr.ac.goldcow.model.Project;
import kr.ac.goldcow.model.Reward;
import kr.ac.goldcow.model.Snstype;
import kr.ac.goldcow.service.ProjectService;
import kr.ac.goldcow.service.RewardService;
import kr.ac.goldcow.service.SnstypeService;
import okhttp3.Request;

@Controller
@RequestMapping("/project")//프로젝트에 관한 요청을 이곳에서 처리
public class ProjectController {
	@Autowired
	private ProjectService projectService;//프로젝트 서비스
	@Autowired
	private SnstypeService snstypeService;//sns타입 목록 서비스
	@Autowired
	private RewardService rewardService;//보상 서비스
	//////////////////////////////////////////////////////////////////////////////////
	//																				//
	//								<<프로젝트 등록폼 출력>>								//
	//																				//
	//////////////////////////////////////////////////////////////////////////////////
	@RequestMapping(value="projectedit", method=RequestMethod.GET)
	public ModelAndView projectEdit() {
		ModelAndView model = new ModelAndView();//뷰로 연결시켜주기위한 객체
		Reward reward = new Reward();//보상 등록을 위한 보상객체
		Project project = new Project();//프로젝트 등록을 위한 프로젝트 객체
        Map< Long, String > categories = new HashMap<Long, String>();//카테고리의 리스트
        categories.put(0l, "잡화");//카테고리의 리스트 추가
        categories.put(1l, "식품");//카테고리의 리스트 추가
        categories.put(2l, "게임");//카테고리의 리스트 추가
        categories.put(3l, "영화");//카테고리의 리스트 추가
		model.addObject("reward", reward);//보상객체 전달
		model.addObject("project", project);//프로젝트 객체 전달
		model.addObject("categories", categories);//카테고리 목록 전달
		model.setViewName("e_projectedit");//뷰 이름 지정
		return model;//요청에 대한 응답
	}
	//////////////////////////////////////////////////////////////////////////////////
	//																				//
	//								<<신규 프로젝트 등록>>								//
	//																				//
	//////////////////////////////////////////////////////////////////////////////////
	@RequestMapping(value="create.do", method=RequestMethod.POST)
	public String add(@ModelAttribute MultiRowReward rewards, @ModelAttribute("project") Project project, BindingResult bindingResult, 
			MultipartHttpServletRequest mhsq, HttpServletRequest request) throws Exception {
		//여러개의 보상목록을 받기위한 리워드스, 프로젝트 정보, 제대로 주입되었는지, 사진목록, 유저의 넘버를 알기위한 리퀘스트
		if(bindingResult.hasErrors()) {//주입이 잘 되었는지 확인
			return "projectedit";//안됐으면 프로젝트 에디트로 되돌아감.
		}
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
	    Calendar cal = Calendar.getInstance();//날짜계산을 위한 캘린더 객체
	    cal.setTime(new Date());//캘린더에 현재시간 저장
	    cal.add(Calendar.DATE, 15);//현재로부터 15일 뒤를 만료일로 지정
		project.setUserNo(Long.parseLong(userNo));//주입된 프로젝트에 유저넘버 저장
		project.setNowamount(0l);//현재 모금액 0원으로 설정
		project.setExpiredate(new Timestamp(new java.util.Date(cal.getTimeInMillis()).getTime()));
		//cal로부터 시간을 추출해 프로젝트 만료일로 설정.
		long projectNo = projectService.saveProject(project);//리워드에 프로젝트의 번호를 저장하기위해 프로젝트를 저장하고 그 결과생성된 프로젝트 번호를 가져옴.
		List<MultipartFile> images = mhsq.getFiles("image");//프로젝트의 대표사진을 가져옴.
		MultipartFile image = images.get(0);//프로젝트의 대표사진 저장
		if(!(image == null)) {
			try {
				//String root = request.getSession().getServletContext().getRealPath("/");
				String root = "C:\\";
				//String filepath = root + "resources/" + image.getOriginalFilename();
				String filepath = root + "resources/" + String.valueOf(projectNo) + ".jpg";//리소스에 프로젝트 번호로 저장
				File file = new File(filepath);
				FileUtils.writeByteArrayToFile(file, image.getBytes());
				System.out.println("파일이 저장됨 :" + filepath);
			}catch(Exception ex) {
				System.out.println(ex);
			}
		}//저장 끝
		if(!(rewards.getRewards() == null)){//보상이 하나라도 달려있다면
			for(int i = 0; i < rewards.getRewards().size(); i++) {//보상의 수만큼 반복
				rewards.getRewards().get(i).setProjectNo(projectNo);//보상의 각각 객체에 프로젝트 이름 저장
			}
			List<Reward> resultRewards = rewardService.save(rewards.getRewards());//보상을 전부 저장하고 그 목록을 받아옴.
			if(images.size()>1) {//보상 이미지 저장
				for(int i = 1; i < images.size(); i++) {//보상이미지 수만큼 
					image = images.get(i);
					if(!(image == null)) {
						try {
							//String root = request.getSession().getServletContext().getRealPath("/");
							//String filepath = root + "resources/" + image.getOriginalFilename();
							String root = "C:\\";
							String filepath = root + "resources/rewards/"+resultRewards.get(i-1).getNo()+ ".jpg";
							//보상의 번호로 사진저장
							File file = new File(filepath);
							FileUtils.writeByteArrayToFile(file, image.getBytes());
							System.out.println("파일이 저장됨 :" + filepath);
						}
						catch(Exception ex) {
							System.out.println(ex);
						}
					}
				}
			}
		}
		return "redirect:/";//홈페이지로 이동.
	}
	//테스트용으로 사용함.
	/*@RequestMapping(value="projectlist", method=RequestMethod.GET)
	public ModelAndView projectList() throws Exception {
		ModelAndView model = new ModelAndView();
		List<Project> projects = projectService.getProjects();
		model.addObject("projects", projects);
		model.setViewName("projectlist");
		return model;
	}*/
	//////////////////////////////////////////////////////////////////////////////////
	//																				//
	//								<<카테고리별 프로젝트 리스트>>							//
	//																				//
	//////////////////////////////////////////////////////////////////////////////////
	@RequestMapping(value="projectcategory", method=RequestMethod.GET)
	public ModelAndView projectCategory(HttpServletRequest request) throws Exception {
		//카테고리 구분을 위한 리퀘스트 객체
		ModelAndView model = new ModelAndView();//뷰로 연결시켜주기위한 객체
		List<Project> projects = projectService.getProjectsByCategory(Long.parseLong(request.getParameter("category")));
		//프로젝트카테고리를 매개변수로 프로젝트 리스트를 프로젝트 서비스에서 받아옴.
		PageManager pm = new PageManager(projects.size(),6,Integer.parseInt(request.getParameter("page")),10);
		//한 페이지에 나오는 프로젝트 최대 6개. 페이지 리스트는 10개마다 끊음.
		String category = String.valueOf(request.getParameter("category"));//다음 페이지로 넘겨주기위한 카테고리 변수
		pm.processPage();//페이지 계산
		if(pm.getEndNum() != -1) {//프로젝트가 하나도 없지 않다면
			projects = projects.subList(pm.getStartNum(), pm.getEndNum()+1);//한페이지에 출력할 만큼만 자름.
		}
		model.addObject("projects", projects);//프로젝트목록전달
		model.addObject("categorynum", category);//다음페이지를 위한 카테고리번호
		model.addObject("pm", pm);//페이징 처리를 위한 페이지매니저 전달
		model.setViewName("projectcategory");//뷰 이름:projectcategory 지정.
		return model;//요청 응답.
	}
	//////////////////////////////////////////////////////////////////////////////////
	//																				//
	//								<<내 프로젝트의 리스트>>								//
	//																				//
	//////////////////////////////////////////////////////////////////////////////////
	@RequestMapping(value="myprojectlist", method=RequestMethod.GET)
	public ModelAndView myProjectList(HttpServletRequest request) throws Exception {
		//요청한 회원의 번호를 알기위한 리퀘스트
		ModelAndView model = new ModelAndView();//뷰로 연결시켜주기위한 객체
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
		List<Project> projects = projectService.getProjectsByUserNo(Long.parseLong(userNo));
		//유저의 번호로부터 해당 유저가 등록한 프로젝트 목록 가져옴.
		PageManager pm = new PageManager(projects.size(),2,Integer.parseInt(request.getParameter("page")),2);
		//한 페이지에 나오는 프로젝트 최대 2개. 페이지 리스트는 2개마다 끊음.
		pm.processPage();//페이지 계산
		if(pm.getEndNum() != -1) {//프로젝트가 하나도 없지 않다면
			projects = projects.subList(pm.getStartNum(), pm.getEndNum()+1);//한페이지에 출력할 만큼만 자름.
		}
		model.addObject("projects", projects);//나의 프로젝트 목록 전달
		model.addObject("pm", pm);//페이징 처리를 위한 페이지매니저 전달
		model.setViewName("e_myprojectlist");//뷰 이름 지정
		return model;//요청에 대한 응답
	}
	//////////////////////////////////////////////////////////////////////////////////
	//																				//
	//								<<프로젝트 상세화면>>								//
	//																				//
	//////////////////////////////////////////////////////////////////////////////////
	@Transactional
	@RequestMapping(value="projectdetail", method=RequestMethod.GET)//프로젝트 상세화면 요청
	public ModelAndView projectDetail(HttpServletRequest request) throws Exception {
		//프로젝트 번호를 받기위한 리퀘스트
		ModelAndView model = new ModelAndView();//뷰로 연결시켜주기위한 객체
		Comment comment = new Comment();//댓글등록 폼을 만들어주기 위한 댓글객체
		List<Snstype> snstype = snstypeService.getSnstypes();//sns연결버튼을 만들기 위한 SNS리스트
		Project project = projectService.getProjct(Long.parseLong(request.getParameter("projectNo")));
		List<Reward> rewards = rewardService.getRewards(Long.parseLong(request.getParameter("projectNo")));//프로젝트 번호로부터 보상목록 뽑아옴.
		//프로젝트 상세화면을 표시하기 위한 프로젝트 객체
		List<Comment> comments = project.getComment();//***프로젝트 정보로부터 댓글목록을 뽑아옴*****
		model.addObject("project", project);//프로젝트 상세정보
		model.addObject("snstype", snstype);//SNS 타입들
		model.addObject("rewards", rewards);//보상목록 전달
		model.addObject("comment", comment);//댓글객체(댓글폼을 만들기 위함.)
		model.addObject("comments", comments);//댓글목록
		model.setViewName("y_projectdetail");//프로젝트 상세화면 뷰
		return model;//뷰이름과 정보 객체를 전달.
	}
	@Transactional
	@RequestMapping(value="search", method=RequestMethod.GET)//프로젝트 상세화면 요청
	public ModelAndView searchProject(HttpServletRequest request) throws Exception {
		//카테고리 구분을 위한 리퀘스트 객체
		ModelAndView model = new ModelAndView();//뷰로 연결시켜주기위한 객체
		List<Project> projects = projectService.searchProjects(request.getParameter("keyWord"));
		PageManager pm = new PageManager(projects.size(),6,Integer.parseInt(request.getParameter("page")),10);
		//한 페이지에 나오는 프로젝트 최대 6개. 페이지 리스트는 10개마다 끊음.
		String keyWord = String.valueOf(request.getParameter("keyWord"));//다음 페이지로 넘겨주기위한 카테고리 변수
		pm.processPage();//페이지 계산
		if(pm.getEndNum() != -1) {//프로젝트가 하나도 없지 않다면
			projects = projects.subList(pm.getStartNum(), pm.getEndNum()+1);//한페이지에 출력할 만큼만 자름.
		}
		model.addObject("projects", projects);//프로젝트목록전달
		model.addObject("keyWord", keyWord);//다음페이지를 위한 카테고리번호
		model.addObject("pm", pm);//페이징 처리를 위한 페이지매니저 전달
		model.setViewName("e_searchResult");//뷰 이름:projectcategory 지정.
		return model;//요청 응답.
	}
}
