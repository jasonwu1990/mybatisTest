package com.jason.user.action;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jason.user.dto.User;
import com.jason.user.service.IUserService;

@Controller
@RequestMapping("/avatar")
public class UserAction{

    @Autowired  
    private IUserService userService;  
      
    @RequestMapping("/showUser")
    public String toIndex(HttpServletRequest request,Model model){  
        int userId = Integer.parseInt(request.getParameter("id"));  
        User user = userService.getUserById(userId);  
        model.addAttribute("user", user);  
        return "user";  
    }
}
