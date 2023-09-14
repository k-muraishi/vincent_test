package com.local.vincent.contoroller;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;

import com.local.vincent.dao.TestEntityDao;
import com.local.vincent.repository.TestEntityRepository;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.ConfirmTemplate;
import com.linecorp.bot.model.response.BotApiResponse;
import com.local.vincent.properties.LineplatformProperties;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class Push {

    private static final Logger log = LoggerFactory.getLogger(Push.class);
    private final LineMessagingClient client;
    private final LineplatformProperties lineplatformProperties;
    private final TestEntityDao testEntityDao;

    @Autowired
    public Push(LineMessagingClient lineMessagingClient, LineplatformProperties lineplatformProperties, TestEntityDao testEntityDao) {
        this.client = lineMessagingClient;
        this.lineplatformProperties = lineplatformProperties;
        this.testEntityDao = testEntityDao;
    }

    // テスト
    @GetMapping("/test")
    public ModelAndView hello(HttpServletRequest request, ModelAndView mav) {
        mav.setViewName("test");
        mav.addObject("test", "Get from " + request.getRequestURL() + " from controller");
        return mav;
    }

    // daoのテスト
    @GetMapping("/dao")
    public void daoTest () {
        System.out.println(testEntityDao.getAllEntities().get(0).getId());
    }

    // 時報をpushする
    @GetMapping("timetone")
    //@Scheduled(cron = "0 0 * * * *", zone = "Asia/Tokyo")// 毎時0分
    public  String pushTimeTone() {
        String text = DateTimeFormatter.ofPattern("a k:mm").format(LocalDateTime.now());
        try {
            PushMessage pMsg = new PushMessage(lineplatformProperties.getVincentUserId(), new TextMessage(text));
            BotApiResponse resp = client.pushMessage(pMsg).get();
            log.info("Sent message: {}", resp);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        return text;
    }

    // 確認メッセージをpush
    @GetMapping("confirm")
    public String pushConfirm() {
        String text = "質問だよ";
        try {
            Message msg = new TemplateMessage(text,
                    new ConfirmTemplate("どんなかんじ?",
                            new PostbackAction("いいかんじ", "CY"),
                            new PostbackAction("やばい", "CN")));
            PushMessage pMsg = new PushMessage(lineplatformProperties.getVincentUserId(), msg);
            BotApiResponse resp = client.pushMessage(pMsg).get();
            log.info("Sent mssages: {}", resp);
        } catch (InterruptedException | ExecutionException e) {
            throw  new RuntimeException(e);
        }
        return text;
    }

    // 友達のuserID一覧を取得
    @GetMapping("/getProfiles")
    public void getProfiles(){
        String apiUrl = "https://api.line.me/v2/bot/profiles";
        String accessToken = lineplatformProperties.getAccessToken();

        HttpClient httpClient = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl + "?userIds=" + lineplatformProperties.getVincentUserId()))
                .header("Authorization", "Bearer " + accessToken)
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

            if (response.statusCode() == 200) {
                String jsonResponse = response.body();
                System.out.println("Friend User IDs: " + jsonResponse);
            } else {
                System.err.println("Failed to fetch friend user IDs.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}