package cn.edu.bjtu.ebosgatewaymodify.dao;

import cn.edu.bjtu.ebosgatewaymodify.entity.IpAddress;
import org.springframework.stereotype.Service;

@Service
public interface IpAddressService {

    void save(String name, String ip, String gateway, String netmask, String dns);
    IpAddress find(String name);
    void delIp(String name);

}

