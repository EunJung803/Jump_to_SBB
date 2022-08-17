package com.ll.exam.sbb;

public class DataNotFoundException extends RuntimeException {
    // RuntimeException으로 해야 해당 요청을 바로 중지시켜버릴 수 있다
    // RuntimeException으로 달지 않으면 Service 부분에서 try catch문이 필수임

    public DataNotFoundException(String question_not_found) {
    }
}
