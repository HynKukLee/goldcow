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

@Controller//컨트롤러임
@RequestMapping("comment")//comment로 진입
public class CommentController {
	@Autowired
	private ProjectService projectService;//댓글에 프로젝트의 정보를 입력하기 위한 프로젝트 서비스(다대일 관계를 위해)
	@Autowired
	private CommentService commentService;//댓글을 저장하기 위한 댓글 서비스
	@Autowired
	@Qualifier("userService")
	private UserService2 userService;//댓글의 유저의 정보를 저장하기 위한 유저 서비스(다대일 관계를 위해)
	//////////////////////////////////////////////////////////////////////////////////
	//																				//
	//								새로운 댓글 저장										//
	//																				//
	//////////////////////////////////////////////////////////////////////////////////
	@RequestMapping(value="register.do", method=RequestMethod.POST)//register.do로 들어오는 포스트 요청을 받아서 처리한다.
	public String add(@ModelAttribute("comment") Comment comment, HttpServletRequest request) throws Exception {
		//댓글 모델을 자동주입, 누가썼는지 알기위해 쿠키를 알기위한 리퀘스트
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
		comment.setUser(userService.getUserData(userNo));//댓글에 유저 정보를 저장(다대일 관계를 위해)
		comment.setProject(projectService.getProjct(Long.parseLong(request.getParameter("projectno"))));//댓글에 프로젝트 정보를 저장(다대일 관계를 위해)
		commentService.saveComment(comment);//폼으로부터 받아온 댓글을 서비스를 통해 저장
		return "redirect:/project/projectdetail?projectNo="+request.getParameter("projectno");
		//프로젝트 상세화면으로 복귀. 매개변수로 받았던 프로젝트 번호 사용.
	}
}
