package cn.edu.bjtu.ebosgatewaymodify.dao;

import cn.edu.bjtu.ebosgatewaymodify.entity.IpAddress;
import cn.edu.bjtu.ebosgatewaymodify.entity.Password;
import org.springframework.stereotype.Service;

@Service
public interface PasswordService {
    void save(String username,String password);
    Password find(String username);
    void delete(String username);
}
