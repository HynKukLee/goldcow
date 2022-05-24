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
@RequestMapping("/project")//������Ʈ�� ���� ��û�� �̰����� ó��
public class ProjectController {
	@Autowired
	private ProjectService projectService;//������Ʈ ����
	@Autowired
	private SnstypeService snstypeService;//snsŸ�� ��� ����
	@Autowired
	private RewardService rewardService;//���� ����
	//////////////////////////////////////////////////////////////////////////////////
	//																				//
	//								<<������Ʈ ����� ���>>								//
	//																				//
	//////////////////////////////////////////////////////////////////////////////////
	@RequestMapping(value="projectedit", method=RequestMethod.GET)
	public ModelAndView projectEdit() {
		ModelAndView model = new ModelAndView();//��� ��������ֱ����� ��ü
		Reward reward = new Reward();//���� ����� ���� ����ü
		Project project = new Project();//������Ʈ ����� ���� ������Ʈ ��ü
        Map< Long, String > categories = new HashMap<Long, String>();//ī�װ��� ����Ʈ
        categories.put(0l, "��ȭ");//ī�װ��� ����Ʈ �߰�
        categories.put(1l, "��ǰ");//ī�װ��� ����Ʈ �߰�
        categories.put(2l, "����");//ī�װ��� ����Ʈ �߰�
        categories.put(3l, "��ȭ");//ī�װ��� ����Ʈ �߰�
		model.addObject("reward", reward);//����ü ����
		model.addObject("project", project);//������Ʈ ��ü ����
		model.addObject("categories", categories);//ī�װ� ��� ����
		model.setViewName("e_projectedit");//�� �̸� ����
		return model;//��û�� ���� ����
	}
	//////////////////////////////////////////////////////////////////////////////////
	//																				//
	//								<<�ű� ������Ʈ ���>>								//
	//																				//
	//////////////////////////////////////////////////////////////////////////////////
	@RequestMapping(value="create.do", method=RequestMethod.POST)
	public String add(@ModelAttribute MultiRowReward rewards, @ModelAttribute("project") Project project, BindingResult bindingResult, 
			MultipartHttpServletRequest mhsq, HttpServletRequest request) throws Exception {
		//�������� �������� �ޱ����� �����彺, ������Ʈ ����, ����� ���ԵǾ�����, �������, ������ �ѹ��� �˱����� ������Ʈ
		if(bindingResult.hasErrors()) {//������ �� �Ǿ����� Ȯ��
			return "projectedit";//�ȵ����� ������Ʈ ����Ʈ�� �ǵ��ư�.
		}
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
	    Calendar cal = Calendar.getInstance();//��¥����� ���� Ķ���� ��ü
	    cal.setTime(new Date());//Ķ������ ����ð� ����
	    cal.add(Calendar.DATE, 15);//����κ��� 15�� �ڸ� �����Ϸ� ����
		project.setUserNo(Long.parseLong(userNo));//���Ե� ������Ʈ�� �����ѹ� ����
		project.setNowamount(0l);//���� ��ݾ� 0������ ����
		project.setExpiredate(new Timestamp(new java.util.Date(cal.getTimeInMillis()).getTime()));
		//cal�κ��� �ð��� ������ ������Ʈ �����Ϸ� ����.
		long projectNo = projectService.saveProject(project);//�����忡 ������Ʈ�� ��ȣ�� �����ϱ����� ������Ʈ�� �����ϰ� �� ��������� ������Ʈ ��ȣ�� ������.
		List<MultipartFile> images = mhsq.getFiles("image");//������Ʈ�� ��ǥ������ ������.
		MultipartFile image = images.get(0);//������Ʈ�� ��ǥ���� ����
		if(!(image == null)) {
			try {
				//String root = request.getSession().getServletContext().getRealPath("/");
				String root = "C:\\";
				//String filepath = root + "resources/" + image.getOriginalFilename();
				String filepath = root + "resources/" + String.valueOf(projectNo) + ".jpg";//���ҽ��� ������Ʈ ��ȣ�� ����
				File file = new File(filepath);
				FileUtils.writeByteArrayToFile(file, image.getBytes());
				System.out.println("������ ����� :" + filepath);
			}catch(Exception ex) {
				System.out.println(ex);
			}
		}//���� ��
		if(!(rewards.getRewards() == null)){//������ �ϳ��� �޷��ִٸ�
			for(int i = 0; i < rewards.getRewards().size(); i++) {//������ ����ŭ �ݺ�
				rewards.getRewards().get(i).setProjectNo(projectNo);//������ ���� ��ü�� ������Ʈ �̸� ����
			}
			List<Reward> resultRewards = rewardService.save(rewards.getRewards());//������ ���� �����ϰ� �� ����� �޾ƿ�.
			if(images.size()>1) {//���� �̹��� ����
				for(int i = 1; i < images.size(); i++) {//�����̹��� ����ŭ 
					image = images.get(i);
					if(!(image == null)) {
						try {
							//String root = request.getSession().getServletContext().getRealPath("/");
							//String filepath = root + "resources/" + image.getOriginalFilename();
							String root = "C:\\";
							String filepath = root + "resources/rewards/"+resultRewards.get(i-1).getNo()+ ".jpg";
							//������ ��ȣ�� ��������
							File file = new File(filepath);
							FileUtils.writeByteArrayToFile(file, image.getBytes());
							System.out.println("������ ����� :" + filepath);
						}
						catch(Exception ex) {
							System.out.println(ex);
						}
					}
				}
			}
		}
		return "redirect:/";//Ȩ�������� �̵�.
	}
	//�׽�Ʈ������ �����.
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
	//								<<ī�װ��� ������Ʈ ����Ʈ>>							//
	//																				//
	//////////////////////////////////////////////////////////////////////////////////
	@RequestMapping(value="projectcategory", method=RequestMethod.GET)
	public ModelAndView projectCategory(HttpServletRequest request) throws Exception {
		//ī�װ� ������ ���� ������Ʈ ��ü
		ModelAndView model = new ModelAndView();//��� ��������ֱ����� ��ü
		List<Project> projects = projectService.getProjectsByCategory(Long.parseLong(request.getParameter("category")));
		//������Ʈī�װ��� �Ű������� ������Ʈ ����Ʈ�� ������Ʈ ���񽺿��� �޾ƿ�.
		PageManager pm = new PageManager(projects.size(),6,Integer.parseInt(request.getParameter("page")),10);
		//�� �������� ������ ������Ʈ �ִ� 6��. ������ ����Ʈ�� 10������ ����.
		String category = String.valueOf(request.getParameter("category"));//���� �������� �Ѱ��ֱ����� ī�װ� ����
		pm.processPage();//������ ���
		if(pm.getEndNum() != -1) {//������Ʈ�� �ϳ��� ���� �ʴٸ�
			projects = projects.subList(pm.getStartNum(), pm.getEndNum()+1);//���������� ����� ��ŭ�� �ڸ�.
		}
		model.addObject("projects", projects);//������Ʈ�������
		model.addObject("categorynum", category);//������������ ���� ī�װ���ȣ
		model.addObject("pm", pm);//����¡ ó���� ���� �������Ŵ��� ����
		model.setViewName("projectcategory");//�� �̸�:projectcategory ����.
		return model;//��û ����.
	}
	//////////////////////////////////////////////////////////////////////////////////
	//																				//
	//								<<�� ������Ʈ�� ����Ʈ>>								//
	//																				//
	//////////////////////////////////////////////////////////////////////////////////
	@RequestMapping(value="myprojectlist", method=RequestMethod.GET)
	public ModelAndView myProjectList(HttpServletRequest request) throws Exception {
		//��û�� ȸ���� ��ȣ�� �˱����� ������Ʈ
		ModelAndView model = new ModelAndView();//��� ��������ֱ����� ��ü
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
		List<Project> projects = projectService.getProjectsByUserNo(Long.parseLong(userNo));
		//������ ��ȣ�κ��� �ش� ������ ����� ������Ʈ ��� ������.
		PageManager pm = new PageManager(projects.size(),2,Integer.parseInt(request.getParameter("page")),2);
		//�� �������� ������ ������Ʈ �ִ� 2��. ������ ����Ʈ�� 2������ ����.
		pm.processPage();//������ ���
		if(pm.getEndNum() != -1) {//������Ʈ�� �ϳ��� ���� �ʴٸ�
			projects = projects.subList(pm.getStartNum(), pm.getEndNum()+1);//���������� ����� ��ŭ�� �ڸ�.
		}
		model.addObject("projects", projects);//���� ������Ʈ ��� ����
		model.addObject("pm", pm);//����¡ ó���� ���� �������Ŵ��� ����
		model.setViewName("e_myprojectlist");//�� �̸� ����
		return model;//��û�� ���� ����
	}
	//////////////////////////////////////////////////////////////////////////////////
	//																				//
	//								<<������Ʈ ��ȭ��>>								//
	//																				//
	//////////////////////////////////////////////////////////////////////////////////
	@Transactional
	@RequestMapping(value="projectdetail", method=RequestMethod.GET)//������Ʈ ��ȭ�� ��û
	public ModelAndView projectDetail(HttpServletRequest request) throws Exception {
		//������Ʈ ��ȣ�� �ޱ����� ������Ʈ
		ModelAndView model = new ModelAndView();//��� ��������ֱ����� ��ü
		Comment comment = new Comment();//��۵�� ���� ������ֱ� ���� ��۰�ü
		List<Snstype> snstype = snstypeService.getSnstypes();//sns�����ư�� ����� ���� SNS����Ʈ
		Project project = projectService.getProjct(Long.parseLong(request.getParameter("projectNo")));
		List<Reward> rewards = rewardService.getRewards(Long.parseLong(request.getParameter("projectNo")));//������Ʈ ��ȣ�κ��� ������ �̾ƿ�.
		//������Ʈ ��ȭ���� ǥ���ϱ� ���� ������Ʈ ��ü
		List<Comment> comments = project.getComment();//***������Ʈ �����κ��� ��۸���� �̾ƿ�*****
		model.addObject("project", project);//������Ʈ ������
		model.addObject("snstype", snstype);//SNS Ÿ�Ե�
		model.addObject("rewards", rewards);//������ ����
		model.addObject("comment", comment);//��۰�ü(������� ����� ����.)
		model.addObject("comments", comments);//��۸��
		model.setViewName("y_projectdetail");//������Ʈ ��ȭ�� ��
		return model;//���̸��� ���� ��ü�� ����.
	}
	@Transactional
	@RequestMapping(value="search", method=RequestMethod.GET)//������Ʈ ��ȭ�� ��û
	public ModelAndView searchProject(HttpServletRequest request) throws Exception {
		//ī�װ� ������ ���� ������Ʈ ��ü
		ModelAndView model = new ModelAndView();//��� ��������ֱ����� ��ü
		List<Project> projects = projectService.searchProjects(request.getParameter("keyWord"));
		PageManager pm = new PageManager(projects.size(),6,Integer.parseInt(request.getParameter("page")),10);
		//�� �������� ������ ������Ʈ �ִ� 6��. ������ ����Ʈ�� 10������ ����.
		String keyWord = String.valueOf(request.getParameter("keyWord"));//���� �������� �Ѱ��ֱ����� ī�װ� ����
		pm.processPage();//������ ���
		if(pm.getEndNum() != -1) {//������Ʈ�� �ϳ��� ���� �ʴٸ�
			projects = projects.subList(pm.getStartNum(), pm.getEndNum()+1);//���������� ����� ��ŭ�� �ڸ�.
		}
		model.addObject("projects", projects);//������Ʈ�������
		model.addObject("keyWord", keyWord);//������������ ���� ī�װ���ȣ
		model.addObject("pm", pm);//����¡ ó���� ���� �������Ŵ��� ����
		model.setViewName("e_searchResult");//�� �̸�:projectcategory ����.
		return model;//��û ����.
	}
}
