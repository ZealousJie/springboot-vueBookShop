package com.example.demo.service;

import com.example.demo.common.dto.NewEmailDto;
import com.example.demo.vo.UserVO;

import javax.mail.MessagingException;

/**
 * @ author zealousJie
 * @ version 1.0
 */
public interface NewService {

    void sendMail(NewEmailDto newEmailDto, UserVO userVO) throws MessagingException;
}
