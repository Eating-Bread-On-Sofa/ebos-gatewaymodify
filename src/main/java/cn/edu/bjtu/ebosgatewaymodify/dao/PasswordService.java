package cn.edu.bjtu.ebosgatewaymodify.dao;

import cn.edu.bjtu.ebosgatewaymodify.entity.Password;
import org.springframework.stereotype.Service;

@Service
public interface PasswordService {
    String modify(String username,String password);
    Boolean login(String username,String password);
    Password find(String username);
}
