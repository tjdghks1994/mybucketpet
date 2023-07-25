package com.mybucketpet.service.login;

import com.mybucketpet.util.RandomKey;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;
    @Value("${mail.full.email}")
    private String FULL_EMAIL;
    @Value("${mail.send.name}")
    private String SEND_NAME;

    public String sendAuthCode(String to) throws MessagingException {
        String authCode = RandomKey.createKey();

        MimeMessage message = createMessage(to, authCode);
        javaMailSender.send(message);

        return authCode;
    }

    private MimeMessage createMessage(String to, String authCode) throws MessagingException {
        log.debug("보내는 대상 = {}", to);
        log.debug("인증 번호 = {}", authCode);
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.addRecipients(Message.RecipientType.TO, to); // 보내는 대상
            message.setSubject("[마이버킷 펫] 인증코드 안내");

            String msgg = "";
            msgg += "<div style='margin:100px;'>";
            msgg += "<h1> 마이버킷 펫 </h1>";
            msgg += "<h1> 인증코드를 확인해주세요. </h1>";
            msgg += "<br>";
            msgg += authCode + "</strong>"; // 메일에 인증번호 넣기
            msgg += "<br/> ";
            msgg += "<p>이메일 인증 절차에 따라 이메일 인증코드를 발급해드립니다.<p>";
            msgg += "<br>";
            msgg += "</div>";
            message.setText(msgg, "utf-8", "html");// 내용, charset 타입, subtype
            // 보내는 사람의 이메일 주소, 보내는 사람 이름
            message.setFrom(new InternetAddress(FULL_EMAIL, SEND_NAME));// 보내는 사람

        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new MessagingException("메일을 보내는 도중 오류가 발생했습니다!", e);
        }

        return message;
    }

}
