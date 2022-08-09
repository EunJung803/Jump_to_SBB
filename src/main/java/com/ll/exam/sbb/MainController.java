package com.ll.exam.sbb;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    /*
    // 내가 짜고있던 코드
    // (근데 article을 쌓아야하는데 이렇게 하면 쌓이지 않는다. article 객체 list로 저장해둬야함 !!)
    int id = 0;
    String articleTitle = "";
    String articleBody = "";
    @GetMapping("/addArticle")
    @ResponseBody
    public String addArticle(String title, String body) {
        id++;
        articleTitle = title;
        articleBody = body;

        return "%d번 글이 등록되었습니다.".formatted(id);
    }

    @GetMapping("/article/{id}")
    @ResponseBody
    public Article showArticle(@PathVariable int id) {
        Article article = new Article();
        article.writeArticle(id, articleTitle, articleBody);
        return article;
    }
     */

    private List<Article> articles = new ArrayList<>();
    @GetMapping("/addArticle")
    @ResponseBody
    public String addArticle(String title, String body) {
        Article article = new Article(title, body);

        articles.add(article);

        return "%d번 게시물이 생성되었습니다.".formatted(article.getId());
    }

    @GetMapping("/article/{id}")
    @ResponseBody
    public Article getArticle(@PathVariable int id) {
        Article article = articles
                .stream()
                .filter(a -> a.getId() == id) // 1번
                .findFirst()
                .orElse(null);

        return article;
    }

    @GetMapping("/modifyArticle")
    @ResponseBody
    public String modifyArticle(int id, String title, String body) {
        Article article = articles
                .stream()
                .filter(a -> a.getId() == id) // 1번
                .findFirst()
                .orElse(null);  // 값이 없으면 null을 넣어라

        if (article == null) {
            return "%d번 게시물은 존재하지 않습니다.".formatted(id);
        }

        article.setTitle(title);
        article.setBody(body);

        return "%d번 게시물을 수정하였습니다.".formatted(article.getId());
    }

    @GetMapping("/removeArticle")
    @ResponseBody
    public String removeArticle(int id, String title, String body) {
        Article article = articles
                .stream()
                .filter(a -> a.getId() == id) // 1번
                .findFirst()
                .orElse(null);  // 값이 없으면 null을 넣어라

        if (article == null) {
            return "%d번 게시물은 존재하지 않습니다.".formatted(id);
        }

        articles.remove(article);

        return "%d번 게시물을 삭제하였습니다.".formatted(article.getId());
    }

    /*
    // @Pathvariable로 처리하는 방법 (수정, 삭제)

    @GetMapping("/modifyArticle/{id}")
    @ResponseBody
    public String modifyArticle(@PathVariable int id, String title, String body) {
        Article article = articles
                .stream()
                .filter(a -> a.getId() == id) // 1번
                .findFirst()
                .orElse(null);
        if (article == null) {
            return "%d번 게시물은 존재하지 않습니다.".formatted(id);
        }
        article.setTitle(title);
        article.setBody(body);

        return "%d번 게시물을 수정하였습니다.".formatted(article.getId());
    }

    @GetMapping("/deleteArticle/{id}")
    @ResponseBody
    public String deleteArticle(@PathVariable int id) {
        Article article = articles
                .stream()
                .filter(a -> a.getId() == id) // 1번
                .findFirst()
                .orElse(null);

        if (article == null) {
            return "%d번 게시물은 존재하지 않습니다.".formatted(id);
        }

        articles.remove(article);

        return "%d번 게시물을 삭제하였습니다.".formatted(article.getId());
    }
     */
}

@AllArgsConstructor
@Getter
@Setter
class Article {
    private static int lastId = 0;
    private int id;
    private String title;
    private String body;

    public Article(String title, String body) {
        this(++lastId, title, body);
    }
}
