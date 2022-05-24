package kr.ac.goldcow;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import kr.ac.goldcow.model.Snstype;
import kr.ac.goldcow.model.Support;
import kr.ac.goldcow.service.SnstypeService;

@Controller
@RequestMapping("/sns")
public class SNSController {
	@Autowired
	private SnstypeService snstypeService;
	//////////////////////////////////////////////////////////////////////////////////
	//																				//
	//								!!!!사용안함!!!!!!!!!!								//
	//																				//
	//////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////
	//																				//
	//								<<공유할수있는 SNS 목록 가져옴>>						//
	//																				//
	//////////////////////////////////////////////////////////////////////////////////
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String publishSNS(HttpServletRequest request) {
		String url = null;
		Snstype snstype = snstypeService.getSNS(Long.parseLong(request.getParameter("no")));
		url = snstype.getUrl() + "?" +snstype.getParameter() + "=" + request.getRequestURL();
		return "redirect:"+url;
	}
}
