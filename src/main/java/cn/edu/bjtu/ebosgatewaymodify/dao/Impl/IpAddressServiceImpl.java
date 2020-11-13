package cn.edu.bjtu.ebosgatewaymodify.dao.Impl;

import cn.edu.bjtu.ebosgatewaymodify.dao.IpAddressService;
import cn.edu.bjtu.ebosgatewaymodify.entity.IpAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class IpAddressServiceImpl implements IpAddressService {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public void save(String name,String ip, String gateway, String netmask, String dns) {
        IpAddress ipAddress = new IpAddress();
        ipAddress.setName(name);
        ipAddress.setIp(ip);
        ipAddress.setGateway(gateway);
        ipAddress.setNetmask(netmask);
        ipAddress.setDns(dns);
        mongoTemplate.save(ipAddress);
    }

    @Override
    public IpAddress find(String name) {
        Query query = Query.query(Criteria.where("name").is(name));
        IpAddress result = mongoTemplate.findOne(query,IpAddress.class,"ipAddress");
        return result;
    }

    @Override
    public void delIp(String name) {
        Query query = Query.query(Criteria.where("name").is(name));
        mongoTemplate.remove(query,IpAddress.class,"ipAddress");
    }
}
