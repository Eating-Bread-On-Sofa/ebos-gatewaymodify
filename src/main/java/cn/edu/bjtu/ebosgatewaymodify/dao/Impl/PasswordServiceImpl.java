package cn.edu.bjtu.ebosgatewaymodify.dao.Impl;

import cn.edu.bjtu.ebosgatewaymodify.dao.PasswordService;
import cn.edu.bjtu.ebosgatewaymodify.entity.Password;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class PasswordServiceImpl implements PasswordService {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public String modify(String username, String password) {
        Query query = new Query(Criteria.where("_id").is(username));
        Update update = new Update();
        update.set("password",password);
        UpdateResult result = mongoTemplate.upsert(query,update,Password.class,"password");
        long count = result.getModifiedCount();
        if (count > 0){
            return "修改成功！";
        }
        return "修改失败！";
    }

    @Override
    public Boolean login(String username, String password) {
        boolean flag = false;
        Password passwd = mongoTemplate.findById(username, Password.class,"password");
        assert passwd != null;
        if (passwd.getPassword().equals(password)){
            flag = true;
        }
        return flag;
    }

    @Override
    public Password find(String username) {
        return mongoTemplate.findById(username, Password.class,"password");
    }
}
