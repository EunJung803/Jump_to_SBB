package com.ll.exam.sbb.answer;

import com.ll.exam.sbb.question.Question;
import com.ll.exam.sbb.question.QuestionService;
import com.ll.exam.sbb.user.SiteUser;
import com.ll.exam.sbb.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RequestMapping("/answer")
@Controller
@RequiredArgsConstructor
public class AnswerController {

    private final QuestionService questionService;
    private final AnswerService answerService;
    private final UserService userService;

    @PostMapping("/create/{id}")
    public String detail(Principal principal, Model model, @PathVariable long id, @Valid AnswerForm answerForm, BindingResult bindingResult) {
        Question question = this.questionService.getQuestion(id);

        if ( bindingResult.hasErrors() ) {
            model.addAttribute("question", question);
            return "question_detail";
        }

        SiteUser siteUser = userService.getUser(principal.getName());

        // 답변 등록 시작
        answerService.create(question, answerForm.getContent(), siteUser);
        // 답변 등록 끝

        return "redirect:/question/detail/%d".formatted(id);
    }
}
