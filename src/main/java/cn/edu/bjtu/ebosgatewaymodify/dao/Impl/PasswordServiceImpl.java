package cn.edu.bjtu.ebosgatewaymodify.dao.Impl;

import cn.edu.bjtu.ebosgatewaymodify.dao.PasswordService;
import cn.edu.bjtu.ebosgatewaymodify.entity.Password;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class PasswordServiceImpl implements PasswordService {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public void save(String username, String password) {
        Password passwd = new Password();
        passwd.setUsername(username);
        passwd.setPassword(password);
        mongoTemplate.save(passwd);
    }

    @Override
    public Password find(String username) {
        Query query = Query.query(Criteria.where("username").is(username));
        Password result = mongoTemplate.findOne(query,Password.class,"password");
        return result;
    }

    @Override
    public void delete(String username) {
        Query query = Query.query(Criteria.where("username").is(username));
        mongoTemplate.remove(query,Password.class,"password");
    }
}
