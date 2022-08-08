package com.ll.exam.sbb;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

    @RequestMapping("/sbb")
    // 아래 함수의 리턴값을 문자열화 해서 브라우저 응답의 바디에 담는다.
    // => 아래 함수의 리턴값은 그대로 브라우저에 표시된다.

    @ResponseBody
    public String index() {

        System.out.println("Hello");    // 서버에서 실행되는 부분

        return "안녕하세요. 오 빠르다 !";    // 먼 미래에 브라우저에서 보여지는 부분
    }
}
