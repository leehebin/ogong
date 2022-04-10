package com.ogong.web.study;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ogong.common.Search;
import com.ogong.service.banana.BananaService;
import com.ogong.service.domain.Banana;
import com.ogong.service.domain.Calendar;
import com.ogong.service.domain.CamStudyMember;
import com.ogong.service.domain.GroupStudyMember;
import com.ogong.service.domain.LearningHistory;
import com.ogong.service.domain.Notice;
import com.ogong.service.domain.Study;
import com.ogong.service.domain.User;
import com.ogong.service.integration.IntegrationService;
import com.ogong.service.learningHistory.LearningHistoryService;
import com.ogong.service.study.CamStudyService;
import com.ogong.service.study.StudyService;
import com.ogong.service.studyroom.StudyroomService;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/selfStudy/*")
public class SelfStudyController {
	
	@Autowired
	private StudyService studyService;
	
	@Autowired
	private StudyroomService studyroomService;
	
	@Autowired
	private CamStudyService camStudyService;
	
	@Autowired
	private LearningHistoryService learningHistoryService;
	
	@Autowired
	private BananaService bananaService;
	
	@Autowired
	private IntegrationService integrationService;		
	
	@Value("8")
	int pageSize;
	
	public SelfStudyController(){
		System.out.println(this.getClass());
	}
	
	@GetMapping("addStudy")
	public String addStudy(@RequestParam("studyType") String studyType, Model model) throws Exception{
		
		System.out.println("/studyController/addStudy : GET");
		
		model.addAttribute("studyType", studyType);
		
		return "/studyView/addSelfStudy";
	}
	
	@PostMapping("addStudy")
	public String addStudy(@ModelAttribute("study") Study study,
										@RequestParam("studyType") String studyType, 
										GroupStudyMember gsm,
										Calendar calendar, HttpSession session) throws Exception{
			
		System.out.println("/studyController/addStudy : POST");
		
		User user = new User();
		//user.setEmail(((User)session.getAttribute("user")).getEmail());
		user.setEmail("user01");
		
		study.setStudyMaker(user);
		studyService.addStudy(study);
		
		//바나나
		User bananaUser = new User();
		Banana banana = new Banana();
		if(study.getStudyRoomGrade().equals("basic")) {
			banana.setBananaEmail(user);
			banana.setBananaAmount(-20);
			banana.setBananaHistory("Basic 등급 스터디 개설로 인한 바나나 소모 ");
			banana.setBananaCategory("2");
			bananaService.addBanana(banana);
			bananaUser.setEmail(user.getEmail());
			bananaUser.setBananaCount(20);
			bananaService.updateUseBanana(bananaUser);
			user.setBananaCount(user.getBananaCount()-20);
		}else if(study.getStudyRoomGrade().equals("premium")) {
			banana.setBananaEmail(user);
			banana.setBananaAmount(-50);
			banana.setBananaHistory("Premium 등급 스터디 개설로 인한 바나나 소모 ");
			banana.setBananaCategory("2");
			bananaService.addBanana(banana);
			bananaUser.setEmail(user.getEmail());
			bananaUser.setBananaCount(50);
			bananaService.updateUseBanana(bananaUser);
			user.setBananaCount(user.getBananaCount()-50);
		}
		
		if(studyType.equals("group")) {
			gsm.setStudy(study);
			gsm.setMember(user);
			gsm.setStudyRole("3");
			
			if(study.getStudyType().equals("group")) {
				gsm.setApprovalFlag("1");
			}
			//studyroomService.addGSMember(gsm);
			
			calendar.setStudy(study);
			calendar.setCalendarStartDate(study.getStudyStartDate());
			calendar.setCalendarEndDate(study.getStudyEndDate());
			
			studyroomService.addCalendar(calendar);
			
			return "redirect:/study/getStudy?studyNo="+study.getStudyNo();
		}
		
		return "redirect:https://wnstjqtest.herokuapp.com/"+ study.getStudyNo()+"/"+user.getEmail();
	}
	
	@GetMapping("getStudy")
	public String getStudy(@RequestParam("studyNo") int studyNo,
										@RequestParam("studyType") String studyType, Model model) throws Exception{
		
		System.out.println("studyController/getStudy : GET");
		
		Study study = new Study();
		study = studyService.getStudy(studyNo);
		
		model.addAttribute("study", study);
		System.out.println(studyType);
		if(studyType.equals("self")) {
			return "/studyView/entranceSelfStudy";
		}else {
			return "/studyView/getStudy";
		}	
	}
	
	@Transactional
	@GetMapping("entranceStudy/{studyNo}")
	@ResponseBody
	public String entranceStudy(@PathVariable int studyNo, HttpSession session) throws Exception{
				
		CamStudyMember csm = new CamStudyMember();
		User user = ((User)session.getAttribute("user"));
				
		if(camStudyService.getCamStudyMember(studyNo, user.getEmail()) == null) {
			csm.setEmail(user.getEmail());
			csm.setNickname(user.getNickname());
			csm.setStudyNo(studyNo);
			
			studyService.entranceStudy(studyNo);
			camStudyService.addCamStudyMember(csm);
			
			return "success";
		}
		
		return "fail";
	}
	
	@RequestMapping("listStudy")
	public String listStudy(@ModelAttribute("search") Search search, Model model) throws Exception{
		
		System.out.println("/studyController/listStudy");
		
		if(search.getCurrentPage() == 0) {
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		Map<String, Object> map = studyService.getStudyList(search);
		System.out.println("map : "+map);
		
		model.addAttribute("list", map.get("list"));
		model.addAttribute("totalCount", map.get("totalCount"));
		model.addAttribute("studyType", search.getStudyType());
		model.addAttribute("search", search);
		
		return "/studyView/listSelfStudy";
	}
	
	@Transactional
	@PostMapping("leaveStudy")
	@ResponseBody
	public void leaveStudy(@RequestBody LearningHistory learningHistory) throws Exception{
		System.err.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		String learningTime = (camStudyService.getCamStudyMember(learningHistory.getStudyNo(), learningHistory.getEmail())).getLearningTime();
		if(!learningTime.equals("00:00:00")) {
			int userTargetTime = (camStudyService.getUser(learningHistory.getEmail())).getUserTargetTime();
			String todayLearningTime = (learningHistoryService.getTodayLearningTime(learningHistory.getEmail())).substring(1, 2);
			if(Integer.parseInt(todayLearningTime) < userTargetTime) {
				learningHistoryService.addLearningHistory(learningHistory);
				String todayLearningTimePlus = (learningHistoryService.getTodayLearningTime(learningHistory.getEmail())).substring(1, 2);
				
				if(Integer.parseInt(todayLearningTimePlus) >= userTargetTime) {
					//바나나 1개 update, 바나나기록 insert
					Banana banana = new Banana();
					User user = camStudyService.getUser(learningHistory.getEmail());
					Notice notice = new Notice();
					//바나나 기록 저장
					banana.setBananaEmail(camStudyService.getUser(learningHistory.getEmail()));
					banana.setBananaAmount(5);
					banana.setBananaHistory("목표시간 완료로 인한 바나나 적립");
					banana.setBananaCategory("1");
					bananaService.addBanana(banana);
					//회원 바나나 업데이트
					user.setBananaCount(5);
					bananaService.updateAcquireBanana(user);
					//목표시간 완료 알림처리
					notice.setNoticeUser(user);
					notice.setNoticeCategory("6");
					notice.setNoticeCondition("2");
					integrationService.addNotice(notice);
				}
			} else {
				learningHistoryService.addLearningHistory(learningHistory);
			}
		}
		
		camStudyService.deleteCamStudyMember(learningHistory);
		studyService.leaveStudy(learningHistory.getStudyNo());
	}
	
}