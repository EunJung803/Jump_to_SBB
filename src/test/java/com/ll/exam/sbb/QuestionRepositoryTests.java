package com.ll.exam.sbb;

import com.ll.exam.sbb.question.Question;
import com.ll.exam.sbb.question.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class QuestionRepositoryTests {

    @Autowired
    private QuestionRepository questionRepository;
    private static int lastSampleDataId;

    @BeforeEach // 매번 테스트 하기 전에 실행된다.
    void beforeEach() {
        clearData();        // truncate
        createSampleData();     // insert into
    }

    public static int createSampleData(QuestionRepository questionRepository) {
        Question q1 = new Question();
        q1.setSubject("sbb가 무엇인가요?");
        q1.setContent("sbb에 대해서 알고 싶습니다.");
        q1.setCreateDate(LocalDateTime.now());
        questionRepository.save(q1);

        Question q2 = new Question();
        q2.setSubject("스프링부트 모델 질문입니다.");
        q2.setContent("id는 자동으로 생성되나요?");
        q2.setCreateDate(LocalDateTime.now());
        questionRepository.save(q2);

        return q2.getId();
    }

    private void createSampleData() {
        lastSampleDataId = createSampleData(questionRepository);
    }

    public static void clearData(QuestionRepository questionRepository) {
        questionRepository.deleteAll(); // DELETE FROM question;
        questionRepository.truncateTable();
    }

    private void clearData() {
        clearData(questionRepository);
    }

    @Test
    void 저장() {
        Question q1 = new Question();
        q1.setSubject("sbb가 무엇인가요?");
        q1.setContent("sbb에 대해서 알고 싶습니다.");
        q1.setCreateDate(LocalDateTime.now());
        questionRepository.save(q1);

        Question q2 = new Question();
        q2.setSubject("스프링부트 모델 질문입니다.");
        q2.setContent("id는 자동으로 생성되나요?");
        q2.setCreateDate(LocalDateTime.now());
        questionRepository.save(q2);

//        assertThat(q1.getId()).isGreaterThan(0);
//        assertThat(q2.getId()).isGreaterThan(q1.getId());

        assertThat(q1.getId()).isEqualTo(lastSampleDataId + 1);
        assertThat(q2.getId()).isEqualTo(lastSampleDataId + 2);
    }

    @Test
    void 삭제() {
        assertEquals(2, questionRepository.count());
        Optional<Question> oq = questionRepository.findById(1);
        assertTrue(oq.isPresent());
        Question q = oq.get();

        questionRepository.delete(q);

        assertEquals(1, questionRepository.count());
    }

    @Test
    void 수정() {
        Question q = this.questionRepository.findById(1).get();
        q.setSubject("수정된 제목");
        questionRepository.save(q);

        q = this.questionRepository.findById(1).get();

        assertThat(q.getSubject()).isEqualTo("수정된 제목");
    }

    @Test
    void findAll() {
        List<Question> all = questionRepository.findAll();
        assertThat(all.size()).isEqualTo(lastSampleDataId);

        Question q = all.get(0);
        assertThat(q.getSubject()).isEqualTo("sbb가 무엇인가요?");
    }

    @Test
    void findBySubject() {
        Question q = questionRepository.findBySubject("sbb가 무엇인가요?");
        assertThat(q.getId()).isEqualTo(1);
    }

    @Test
    void findBySubjectAndContent() {
        Question q = questionRepository.findBySubjectAndContent(
                "sbb가 무엇인가요?", "sbb에 대해서 알고 싶습니다.");
        assertThat(q.getId()).isEqualTo(1);
    }

    @Test
    void findBySubjectLike() {
        List<Question> qList = questionRepository.findBySubjectLike("sbb%");
        Question q = qList.get(0);

        assertThat(q.getSubject()).isEqualTo("sbb가 무엇인가요?");
    }

    @Test
    void createManySampleData() {
        boolean run = true;

        if (run == false) return;

        IntStream.rangeClosed(3, 300).forEach(id -> {
            Question q = new Question();
            q.setSubject("%d번 질문".formatted(id));
            q.setContent("%d번 질문의 내용".formatted(id));
            q.setCreateDate(LocalDateTime.now());
            questionRepository.save(q);
        });
    }

//    @Test
//    void testJpa0() {
//        Question q1 = new Question();
//        q1.setSubject("sbb가 무엇인가요?");
//        q1.setContent("sbb에 대해서 알고 싶습니다.");
//        q1.setCreateDate(LocalDateTime.now());
//        questionRepository.save(q1);
//
//        Question q2 = new Question();
//        q2.setSubject("스프링부트 모델 질문입니다.");
//        q2.setContent("id는 자동으로 생성되나요?");
//        q2.setCreateDate(LocalDateTime.now());
//        questionRepository.save(q2);
//
//        questionRepository.disableForeignKeyChecks();
//        questionRepository.truncate();
//        questionRepository.enableForeignKeyChecks();
//    }
//
//    @Test
//    void testJpa1() {
//        Question q1 = new Question();
//        q1.setSubject("sbb가 무엇인가요?");
//        q1.setContent("sbb에 대해서 알고 싶습니다.");
//        q1.setCreateDate(LocalDateTime.now());
//        questionRepository.save(q1);  // 첫번째 질문 저장
//
//        Question q2 = new Question();
//        q2.setSubject("스프링부트 모델 질문입니다.");
//        q2.setContent("id는 자동으로 생성되나요?");
//        q2.setCreateDate(LocalDateTime.now());
//        questionRepository.save(q2);  // 두번째 질문 저장
//
//        assertThat(q1.getId()).isGreaterThan(0);
//        assertThat(q2.getId()).isGreaterThan(q1.getId());
//    }
//
//    @Test
//    void testJpa2() {
//        // SELECT * FROM question
//        List<Question> all = questionRepository.findAll();
//        assertEquals(2, all.size());	// 2개로 컬럼 데이터 수정 완료
//
//        Question q = all.get(0);
//        assertEquals("sbb가 무엇인가요?", q.getSubject());
//    }
//
//    @Test
//    void testJpa3() {
//        Question q = questionRepository.findBySubject("sbb가 무엇인가요?");
//        assertEquals(1, q.getId());
//    }
//
//    @Test
//    void testJpa4() {
//        Question q = questionRepository.findBySubjectAndContent(
//                "sbb가 무엇인가요?", "sbb에 대해서 알고 싶습니다.");
//        assertEquals(1, q.getId());
//    }
//
//    @Test
//    void testJpa5() {
//        List<Question> qList = questionRepository.findBySubjectLike("sbb%");
//        Question q = qList.get(0);
//        assertEquals("sbb가 무엇인가요?", q.getSubject());
//    }
//
//    @Test
//    void testJpa6() {
//        Optional<Question> oq = questionRepository.findById(1);
//        assertTrue(oq.isPresent());
//        Question q = oq.get();
//        q.setSubject("수정된 제목");
//        this.questionRepository.save(q); // UPDATE
//    }
//
//    @Test
//    void testJpa7() {
//        assertEquals(2, questionRepository.count());
//        Optional<Question> oq = questionRepository.findById(1);
//        assertTrue(oq.isPresent());
//        Question q = oq.get();
//
//        questionRepository.delete(q);
//
//        assertEquals(1, questionRepository.count());
//    }

}
