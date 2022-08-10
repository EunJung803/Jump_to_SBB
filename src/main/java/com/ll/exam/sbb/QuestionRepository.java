package com.ll.exam.sbb;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {

    Question findBySubject(String s);

    Question findBySubjectAndContent(String s, String s1);

    List<Question> findBySubjectLike(String s);
}
