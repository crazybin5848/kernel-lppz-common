package com.lppz.diamond.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lppz.diamond.utils.PageUtil;
import com.lppz.diamond.web.service.ConfigService;
import com.lppz.diamond.web.service.ModuleService;
import com.lppz.diamond.web.service.ProjectService;

@Controller
public class ProfileController extends BaseController {
	@Autowired
	private ModuleService moduleService;
	@Autowired
	private ConfigService configService;
	@Autowired
	private ProjectService projectService;
	
	private static final int LIMIT = 15;
	
	@RequestMapping("/profile/{type}/{projectId}")
	public String profile(@PathVariable("type") String type, @PathVariable("projectId") Long projectId, 
			Long moduleId, ModelMap modelMap, @RequestParam(defaultValue="1")int page) {
		modelMap.addAttribute("modules", moduleService.queryModules(projectId));
		modelMap.addAttribute("configs", configService.queryConfigs(projectId, moduleId, PageUtil.getOffset(page, LIMIT), LIMIT));
		modelMap.addAttribute("moduleId", moduleId);
		modelMap.addAttribute("project", projectService.queryProject(projectId));
		
		long recordCount = configService.queryConfigCount(projectId, moduleId);
		modelMap.addAttribute("totalPages", PageUtil.pageCount(recordCount, LIMIT));
		modelMap.addAttribute("currentPage", page);
		
		return "profile/" + type;
	}
	
	@RequestMapping("/profile/preview/{projectCode}/{type}")
	public String preview(@PathVariable("type") String type, @PathVariable("projectCode") String projectCode, 
			Long projectId, ModelMap modelMap) {
		String config = configService.queryConfigs(projectCode, type, "preview");
		
		modelMap.addAttribute("project", projectService.queryProject(projectId));
		modelMap.addAttribute("message", config);
		return "profile/preview";
	}
	
	@RequestMapping("/profile/preview/{projectCode}/{type}/{moduleId}")
	public String preview(@PathVariable("type") String type, @PathVariable("projectCode") String projectCode
			,@PathVariable("moduleId") String moduleId, 
			Long projectId, ModelMap modelMap) {
		String config = configService.queryConfigsForView(projectCode, type, "preview", moduleId);
		
		modelMap.addAttribute("project", projectService.queryProject(projectId));
		modelMap.addAttribute("message", config);
		return "profile/preview";
	}
}
