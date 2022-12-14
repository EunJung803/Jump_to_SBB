package com.ll.exam.sbb;

import com.ll.exam.sbb.answer.Answer;
import com.ll.exam.sbb.answer.AnswerRepository;
import com.ll.exam.sbb.question.Question;
import com.ll.exam.sbb.question.QuestionRepository;
import com.ll.exam.sbb.user.SiteUser;
import com.ll.exam.sbb.user.UserRepository;
import com.ll.exam.sbb.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class AnswerRepositoryTests {

    @Autowired
    private UserService userService;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;


    @BeforeEach
    void beforeEach() {
        clearData();
        createSampleData();
    }

    public static void clearData(UserRepository userRepository, AnswerRepository answerRepository, QuestionRepository questionRepository) {
        UserServiceTests.clearData(userRepository, answerRepository, questionRepository);
    }

    private void clearData() {
        clearData(userRepository, answerRepository, questionRepository);
    }

    private void createSampleData() {
        QuestionRepositoryTests.createSampleData(userService, questionRepository);

        Question q = questionRepository.findById(1L).get();  // 1번 글을 가져와서

        // 매번 두개의 데이터 생성

        Answer a1 = new Answer();
        a1.setContent("sbb는 질문답변 게시판 입니다.");
        a1.setQuestion(q);
        a1.setCreateDate(LocalDateTime.now());
        a1.setAuthor(new SiteUser(1L));
        q.addAnswer(a1);
        answerRepository.save(a1);

//        q.getAnswerList().add(a1);

        Answer a2 = new Answer();
        a2.setContent("sbb에서는 주로 스프링부트관련 내용을 다룹니다.");
        a2.setQuestion(q);
        a2.setCreateDate(LocalDateTime.now());
        a2.setAuthor(new SiteUser(2L));
        q.addAnswer(a1);
        answerRepository.save(a2);

//        q.getAnswerList().add(a2);

        questionRepository.save(q);
    }

    @Test
    @Transactional
    @Rollback(false)
    void 저장() {
        Question q = questionRepository.findById(2L).get();

        Answer a1 = new Answer();
        a1.setContent("네 자동으로 생성됩니다.");
        a1.setCreateDate(LocalDateTime.now());
        a1.setAuthor(new SiteUser(2L));
        q.addAnswer(a1);

        Answer a2 = new Answer();
        a2.setContent("네네~ 맞아요!");
        a2.setCreateDate(LocalDateTime.now());
        a2.setAuthor(new SiteUser(2L));
        q.addAnswer(a2);

        questionRepository.save(q);     // question이 알아서 자기가 추가된 answer, 삭제된 answer 등등을 관리하여 저장
    }

    @Test
    @Transactional
    @Rollback(false)
    void 조회() {
        Answer a = this.answerRepository.findById(1L).get();     // 1번 질문의 답변을 가져와서
        assertThat(a.getContent()).isEqualTo("sbb는 질문답변 게시판 입니다.");     // 비교
    }

    @Test
    @Transactional
    @Rollback(false)
    void 관련된_question_조회() {
        Answer a = this.answerRepository.findById(1L).get();
        Question q = a.getQuestion();

        assertThat(q.getId()).isEqualTo(1);
    }

    @Test
    @Transactional  // 한 묶음, 이 함수가 끝날 때까지 DB연결을 끊지 않음 (근데 이렇게 하면 이 테스트의 결과를 DB에 적용했다가 빼버림)
    @Rollback(false)    // DB에 결과를 남겨둘 수 있도록
    void question으로부터_관련된_질문들_조회() {
        // SELECT * FROM question WHERE id = 1
        Question q = questionRepository.findById(1L).get();  // 1번 글을 가져옴
        // DB 연결이 끊김

        // SELECT * FROM answer WHERE question_id = 1
        List<Answer> answerList = q.getAnswerList();    // DB 연결이 끊겨서 가져오지 못함 ( 늦어버림 )

        assertThat(answerList.size()).isEqualTo(2); // 2개의 답변이 달려있음 1번 글에는
        assertThat(answerList.get(0).getContent()).isEqualTo("sbb는 질문답변 게시판 입니다."); // 그 2개의 답변 중 첫번째 답변이 이와 같음
    }   // 이 테스트가 안되는 이유 : Lazily initialize -> 따라서 Question에 있는 OneToMany의 fetch타입을 Eager로 변경해줌
}
