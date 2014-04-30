package com.springapp.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class HelloController {
	@RequestMapping(method = RequestMethod.GET)
	public String printWelcome(ModelMap model) {
		model.addAttribute("message", "Hello world!");
		return "hello";
	}
    @RequestMapping(value="/owner/{ownerId}", method= RequestMethod.GET)
    public String sayHello(@PathVariable("ownerId") String theOwner, ModelMap model) {
        model.addAttribute("message", "Hello world!");
        model.addAttribute("owner", theOwner);
        model.put("table",new String("@@@@@@@@@@@@@@@@@"));
        return "hello";
    }
}