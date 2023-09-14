package com.local.vincent.contoroller;

import javax.servlet.http.HttpServletRequest;

import com.local.vincent.dao.MessageDao;
import com.local.vincent.dao.PasswordDao;
import com.local.vincent.dao.UserDao;
import com.local.vincent.dao.UserMessageDao;
import com.local.vincent.entity.User;
import com.local.vincent.service.MessageService;
import com.local.vincent.service.PasswordService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class LiffContoroller {
    private final UserDao userDao;
    private final UserMessageDao userMessageDao;
    private final MessageDao messageDao;
    private final PasswordDao passwordDao;
    private final MessageService messageService;
    private final PasswordService passwordService;

    @Autowired
    public LiffContoroller(UserDao userDao, UserMessageDao userMessageDao, MessageDao messageDao, PasswordDao passwordDao, PasswordService passwordService, MessageService messageService) {
        this.userDao = userDao;
        this.userMessageDao = userMessageDao;
        this.messageDao = messageDao;
        this.passwordDao = passwordDao;
        this.messageService = messageService;
        this.passwordService = passwordService;
    }

    /*liffアプリ*/
    @GetMapping("/liffappMain")
    public ModelAndView hello(HttpServletRequest request, ModelAndView mav) {
        mav.setViewName("liffappMain");
        mav.addObject("titleMessage", "Vincent");
        return mav;
    }

    // ユーザー全件検索
    @GetMapping("/getAllUsers")
    public void getAllUsers(){
        System.out.println("テスト"+userDao.getAllEntities().get(0));
    }

    // ユーザーID検索
    @GetMapping("/getUserById")
    public void getUserById(@RequestParam("id") Integer id){
        User user = userDao.getUserById(id);
        System.out.println(user.getId());
    }

    // lineユーザーID検索
    @GetMapping("/getUserByLineUserId")
    public void getUserByLineUserId(@RequestParam("lineUserId") String lineUserId){
        User user = userDao.findBylineUserId(lineUserId);
        System.out.println(user.getId());
    }

    /**
     * ユーザー新規登録
     */
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/insertUser")
    public void insertUser(@RequestBody String lineUserId) {
        userDao.insertUserWithSeq(lineUserId);
    }

    /**
     * ユーザー削除
     */
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/deleteUser")
    public void deleteUser(@RequestBody String lineUserId) {
        userDao.deleteUser(lineUserId);
    }

    // ユーザーメッセージ中間テーブル全件検索
    @GetMapping("/getAllUserMessages")
    public void getAllUserMessages(){
        System.out.println(userMessageDao.getAllEntities().get(0));
    }

    // メッセージ全件検索
    @GetMapping("/getAllMessages")
    public void getAllMessages(){
        System.out.println(messageDao.getAllEntities().get(0).getMessageText());
    }

    /**
     * メッセージ登録
     */
    @GetMapping("/insertMessage")
    public void insertMessage(@RequestParam("lineUserId") String lineUserId, @RequestParam("messageText") String messageText){
        // パスワード作成
        String password = passwordService.generateRandomPassword();

        // パスワード登録
        Integer passwordId = passwordDao.insertPassword(password);

        // メッセージ作成
        String fixedMessageText = messageService.fixMessage(messageText);

        // daoに詰めてインサート
        messageDao.insertMessage(lineUserId, fixedMessageText, passwordId);
    }

    // パスワード全件検索
    @GetMapping("/getAllPasswords")
    public void getAllPasswords(){
        System.out.println(passwordDao.getAllEntities().get(0).getPassword());
    }
}
