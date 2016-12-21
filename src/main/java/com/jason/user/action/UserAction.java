package com.jason.user.action;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jason.framework.json.JsonDocument;
import com.jason.user.dto.User;
import com.jason.user.service.IUserService;

@Controller
public class UserAction{

    @Autowired  
    private IUserService userService;  
      
    @RequestMapping(value="/showUser", produces = "text/html; charset=utf-8")
    public @ResponseBody String toIndex(HttpServletRequest request){  
        int userId = Integer.parseInt(request.getParameter("id"));  
        User user = userService.getUserById(userId);
        if(user != null) {
        	JsonDocument doc = new JsonDocument();
            doc.startObject();
            user.buildJson(doc);
            doc.endObject();
            return doc.toString();  
        }
        return null;
    }
}
