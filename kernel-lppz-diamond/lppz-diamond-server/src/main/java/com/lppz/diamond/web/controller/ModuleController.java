package com.lppz.diamond.web.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lppz.diamond.web.service.ModuleService;

@Controller
public class ModuleController extends BaseController {
	
	@Autowired
	private ModuleService moduleService;
	
	@RequestMapping("/module/save")
	public String save(String type, Long projectId, String name) {
		moduleService.save(projectId, name);
		return "redirect:/profile/" + type + "/" + projectId;
	}
	
	@RequestMapping("/module/delete/{type}/{projectId}/{moduleId}")
	public String delete(@PathVariable String type, @PathVariable Long projectId, 
			@PathVariable Long moduleId, HttpSession session) {
		boolean result = moduleService.delete(moduleId, projectId);
		if(!result) {
			session.setAttribute("message", "模块已经被配置项关联，不能删除！");
			return "redirect:/profile/" + type + "/" + projectId + "?moduleId=" + moduleId;
		} else {
			session.setAttribute("message", "删除成功！");
			return "redirect:/profile/" + type + "/" + projectId;
		}
	}
}
