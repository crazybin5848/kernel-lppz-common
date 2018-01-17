package com.lppz.diamond.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lppz.diamond.utils.PageUtil;
import com.lppz.diamond.utils.SessionHolder;
import com.lppz.diamond.web.model.Project;
import com.lppz.diamond.web.model.User;
import com.lppz.diamond.web.service.ProjectService;

@Controller
public class IndexContoller extends BaseController {
	
	@Autowired
	private ProjectService projectService;
	
	private static final int LIMIT = 10;
	
	@RequestMapping("/index")
	public void index(ModelMap modelMap, @RequestParam(defaultValue="1") int page) {
		User user = (User) SessionHolder.getSession().getAttribute("sessionUser");
		List<Project> projects = projectService.queryProjectForUser(user, PageUtil.getOffset(page, LIMIT), LIMIT);
		for(Project project : projects) {
			List<String> roles = projectService.queryRoles(project.getId(), user.getId());
			project.setRoles(roles);
		}
		modelMap.addAttribute("projects", projects);
		long recordCount = projectService.queryProjectCountForUser(user);
		modelMap.addAttribute("totalPages", PageUtil.pageCount(recordCount, LIMIT));
		modelMap.addAttribute("currentPage", page);
	}
}
