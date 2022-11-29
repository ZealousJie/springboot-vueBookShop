package com.example.demo.service;

//这里应该导入springSecurity的UserDetails 暂时用这个代替
import com.alipay.api.domain.UserDetails;

/**
 * @ author zealousJie
 * @ version 1.0
 */
public interface UserDetailsService {
    UserDetails loadUserByUsername(String username) throws Exception;
}
