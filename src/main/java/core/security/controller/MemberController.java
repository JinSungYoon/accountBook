package core.security.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import core.security.dto.MemberDto;
import core.security.service.MemberService;


@Controller
@AllArgsConstructor
public class MemberController {
	  private MemberService memberService;

	    // 메인 페이지
	    @GetMapping("/")
	    public String index() {
	        return "/index";
	    }

	    // 회원가입 페이지
	    @GetMapping("/signup")
	    public String dispSignup() {
	        return "/signup";
	    }

	    // 회원가입 처리
	    @PostMapping("/signup")
	    public String execSignup(MemberDto memberDto) {
	        memberService.joinUser(memberDto);
	        return "redirect:/user/login";
	    }

	    // 로그인 페이지
	    @GetMapping("/user/login")
	    public String dispLogin() {
	        return "/user/login";
	    }

	    // 로그인 결과 페이지
	    @GetMapping("/user/login/result")
	    public String dispLoginResult() {
	        return "/user/loginSuccess";
	    }

	    // 로그아웃 결과 페이지
	    @GetMapping("/user/logout/result")
	    public String dispLogout() {
	        return "/user/logout";
	    }

	    // 접근 거부 페이지
	    @GetMapping("/user/denied")
	    public String dispDenied() {
	        return "/denied";
	    }

	    // 내 정보 페이지
	    @GetMapping("/user/info")
	    public String dispMyInfo() {
	        return "/user/myinfo";
	    }

	    // 어드민 페이지
	    @GetMapping("/admin")
	    public String dispAdmin() {
	        return "/user/admin";
	    }
}
