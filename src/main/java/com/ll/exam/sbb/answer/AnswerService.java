package com.ll.exam.sbb.answer;

import com.ll.exam.sbb.question.Question;
import com.ll.exam.sbb.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service    // 큰 의미는 사실 없음
public class AnswerService {

    private final AnswerRepository answerRepository;

    public void create(Question question, String content, SiteUser author) {
        Answer answer = new Answer();

        answer.setContent(content);
        answer.setCreateDate(LocalDateTime.now());
        question.addAnswer(answer);

        this.answerRepository.save(answer);
    }
}
