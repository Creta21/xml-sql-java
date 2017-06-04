package com.test.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ControllerTest {
    //StackOverflow Example.
    //https://stackoverflow.com/questions/33796218/content-type-application-x-www-form-urlencodedcharset-utf-8-not-supported-for
    @RequestMapping(value="/", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> home(){ 
        
        return new ResponseEntity<>("home Page", HttpStatus.OK); 
    }
    
    @RequestMapping(value="/page/{name}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> pageGET(@PathVariable("name") String name){
        
        return new ResponseEntity<>("Hello " + name, HttpStatus.OK); 
    }
    
    @RequestMapping(value="/page", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> pageForm(){
        
        String form = "<h1> What's your name?</h1>"+
                        "<form method=\"POST\" action=\"/page\"> "+
                        "<input type=\"text\" name=\"name\" >"+
                         "<input type=\"submit\" value=\"submit\">" +
                         "</form>";
        
        return new ResponseEntity<>(form, HttpStatus.OK); 
    }
    
    @RequestMapping(value="/page", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE , produces = MediaType.TEXT_HTML_VALUE)
    public @ResponseBody ResponseEntity<String> pageFormPOST(@RequestBody MultiValueMap<String, String> paramMap){
        
        String formName = paramMap.get("name").get(0);
        
        String form = "<h1> What's your name?</h1>"+
                        "<form method=\"POST\"> "+
                        "<input type=\"text\" name=\"name\" value=\" "+ formName + "\" >"+
                         "<input type=\"submit\" value=\"submit\">" +
                         "</form>" +
                         (formName != null ? "<h2>Hello " + formName + "</h2>" : "");
        
        return new ResponseEntity<>(form , HttpStatus.OK);
        
    }
}