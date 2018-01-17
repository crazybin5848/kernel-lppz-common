/**
 * Copyright 2006-2015 handu.com
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lppz.dubbo.monitor.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.lppz.dubbo.monitor.DisruptorDubboMonitorService;
import com.lppz.util.logback.LogBackKafkaVo;

/**
 * LogBackController Controller
 *
 * @author yulei
 */
@Controller
@RequestMapping("/logback")
public class LogBackController {
	
	private static final Integer rows=15;
	
    @Autowired
    private DisruptorDubboMonitorService dubboMonitorService;

    @SuppressWarnings("rawtypes")
	@RequestMapping(method = RequestMethod.GET)
    public String home(Model model,LogBackKafkaVo vo,Integer page) {
    	model.addAttribute("data", dubboMonitorService.getLogBackList(vo,page,rows));
    	model.addAttribute("vo", vo);
    	
        return "logback/list";
    }
}
