package com.ll.exam.sbb.question;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class QuestionController {
    @RequestMapping("/question/list")
    // 이 자리에 @ResponseBody 가 없으면 resources/templates 안에 있는 question_list.html 파일을 뷰로 사용한다
    public String list() {
        return "question_list";
    }
}
