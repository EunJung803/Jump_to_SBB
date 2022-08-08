package com.ll.exam.sbb;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class MainController {
    private int value = -1;
    private String age = "";

    @RequestMapping("/sbb")
    // 아래 함수의 리턴값을 문자열화 해서 브라우저 응답의 바디에 담는다.
    // => 아래 함수의 리턴값은 그대로 브라우저에 표시된다.

    @ResponseBody
    public String index() {

        System.out.println("Hello");    // 서버에서 실행되는 부분

        return "안녕하세요. 오 빠르다 !";    // 먼 미래에 브라우저에서 보여지는 부분
    }

    @GetMapping("/page1")
    @ResponseBody
    public String showPage1() {
        return """
                <form method="POST" action="/page2">
                    <input type="number" name="age" placeholder="나이"/>
                    <input type="submit" value="page2로 POST 방식으로 이동"/>
                </form>
                """;
    }

    @PostMapping("/page2")
    @ResponseBody
    public String showPage2POST (@RequestParam(defaultValue = "0") int age) {
        return """
                <h1>입력된 나이 : %d</h1>
                <h1>안녕하세요, POST 방식으로 오셨군요.</h1>
                """.formatted(age);
    }

    @GetMapping("/page2")
    @ResponseBody
    public String showPage2GET (@RequestParam(defaultValue = "0") int age) {
        return """
                <h1>입력된 나이 : %d</h1>
                <h1>안녕하세요, POST 방식으로 오셨군요.</h1>
                """.formatted(age);
    }

    @GetMapping("/plus")
    @ResponseBody
    public int showPlus(int a, int b) {
        return a + b;
    }

    @GetMapping("/minus")
    @ResponseBody
    public int showMinus(int a, int b) {
        return a - b;
    }

    @GetMapping ("/increase")
    @ResponseBody
    public int increasePage () {
        value ++;
        return value;
    }

    @GetMapping("/gugudan")
    @ResponseBody
    public String showGugudan(int dan, int limit) {
        String gugu="";
        for(int i=1; i<=limit; i++) {
            gugu += "%d * %d = %d <br>".formatted(dan, i, dan*i);
        }
        return gugu;
    }

    // Stream 방식의 구구단
    @GetMapping("/gugudanstream")
    @ResponseBody
    public String showGugudan(Integer dan, Integer limit) {
        if (limit == null) {
            limit = 9;
        }

        if (dan == null) {
            dan = 9;
        }

        Integer finalDan = dan;
        return IntStream.rangeClosed(1, limit)
                .mapToObj(i -> "%d * %d = %d".formatted(finalDan, i, finalDan * i))
                .collect(Collectors.joining("<br>\n"));
    }

    @GetMapping("/mbti")
    @ResponseBody
    public String getMbti(String name) {
        switch(name){
            case "홍길동" :
                return "INFP";
            case "홍길순" :
                return "ENFP";
            case "임꺽정" :
                return "INFJ";
            case "김은정" :
                return "ISFJ";
        }
        return name;
    }

    // 새로운 방식의 switch문
    @GetMapping("/mbti/{name}")
    @ResponseBody
    public String showMbti(@PathVariable String name) {
        String rs =
                switch (name) {
                    case "홍길동" -> "INFP";
                    case "홍길순" -> "ENFP";
                    case "임꺽정" -> "INFJ";
                    case "김은정" -> "ISFJ";
                    default -> "모름";
        };
        return rs;
    }

    @GetMapping("/saveSessionAge/{input_age}")
    @ResponseBody
    public String saveSession(@PathVariable String input_age, HttpServletRequest req) {

        HttpSession session = req.getSession();
        session.setAttribute(age, input_age);

        return "세션 age=%s 등록".formatted(input_age);
    }

    @GetMapping("/getSessionAge")
    @ResponseBody
    public String getSession(HttpSession session) {
        String value = (String) session.getAttribute(age);

        return value;
    }
}
