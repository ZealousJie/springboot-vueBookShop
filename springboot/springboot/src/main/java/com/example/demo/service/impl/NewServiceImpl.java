package com.example.demo.service.impl;

import com.example.demo.common.dto.NewEmailDto;
import com.example.demo.entity.Team;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.EventService;
import com.example.demo.service.NewService;
import com.example.demo.utils.JsonUtil;
import com.example.demo.vo.EventVO;
import com.example.demo.vo.UserVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;

/**
 * @ author zealousJie
 * @ version 1.0
 */
@Service
public class NewServiceImpl implements NewService {

    @Resource
    private EventService eventService;

    @Resource
    private UserMapper userMapper;
    @Resource
    private JavaMailSenderImpl javaMailSender;

    @Value("${spring.mail.username}")
    private String mail;

    @Override
    public void sendMail(NewEmailDto newEmailDto, UserVO userVO) throws MessagingException {

        EventVO eventVO = eventService.queryEventById(newEmailDto.getEventId(), userVO);
        List<Team> teamList = eventVO.getEventEntrants();
        //给所有的队伍的人发邮件

        for (Team team : teamList) {
            String teamMember = team.getTeamMember();
            List<String> uidList = JsonUtil.fromJsonList(teamMember);
            for (String id : uidList) {
                User user = userMapper.selectById(id);
                String email = user.getEmail();
                MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
                mimeMessageHelper.setSubject("参赛选手你好,您报名的比赛即将开始");
                mimeMessageHelper.setText(newEmailDto.getMailText());
                mimeMessageHelper.addAttachment("赛事流程图.jpg",newEmailDto.getFile());
                mimeMessageHelper.setTo(email);
                mimeMessageHelper.setFrom(mail);
                javaMailSender.send(mimeMessage);
            }
        }


    }
}
