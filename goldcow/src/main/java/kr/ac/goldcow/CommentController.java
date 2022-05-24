package kr.ac.goldcow;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import kr.ac.goldcow.model.Comment;
import kr.ac.goldcow.model.User;
import kr.ac.goldcow.service.CommentService;
import kr.ac.goldcow.service.ProjectService;
import kr.ac.goldcow.service.UserService2;

@Controller//��Ʈ�ѷ���
@RequestMapping("comment")//comment�� ����
public class CommentController {
	@Autowired
	private ProjectService projectService;//��ۿ� ������Ʈ�� ������ �Է��ϱ� ���� ������Ʈ ����(�ٴ��� ���踦 ����)
	@Autowired
	private CommentService commentService;//����� �����ϱ� ���� ��� ����
	@Autowired
	@Qualifier("userService")
	private UserService2 userService;//����� ������ ������ �����ϱ� ���� ���� ����(�ٴ��� ���踦 ����)
	//////////////////////////////////////////////////////////////////////////////////
	//																				//
	//								���ο� ��� ����										//
	//																				//
	//////////////////////////////////////////////////////////////////////////////////
	@RequestMapping(value="register.do", method=RequestMethod.POST)//register.do�� ������ ����Ʈ ��û�� �޾Ƽ� ó���Ѵ�.
	public String add(@ModelAttribute("comment") Comment comment, HttpServletRequest request) throws Exception {
		//��� ���� �ڵ�����, ��������� �˱����� ��Ű�� �˱����� ������Ʈ
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
		comment.setUser(userService.getUserData(userNo));//��ۿ� ���� ������ ����(�ٴ��� ���踦 ����)
		comment.setProject(projectService.getProjct(Long.parseLong(request.getParameter("projectno"))));//��ۿ� ������Ʈ ������ ����(�ٴ��� ���踦 ����)
		commentService.saveComment(comment);//�����κ��� �޾ƿ� ����� ���񽺸� ���� ����
		return "redirect:/project/projectdetail?projectNo="+request.getParameter("projectno");
		//������Ʈ ��ȭ������ ����. �Ű������� �޾Ҵ� ������Ʈ ��ȣ ���.
	}
}
