package cn.edu.bjtu.ebosgatewaymodify.controller;

import cn.edu.bjtu.ebosgatewaymodify.dao.IpAddressService;
import cn.edu.bjtu.ebosgatewaymodify.dao.PasswordService;
import cn.edu.bjtu.ebosgatewaymodify.entity.IpAddress;
import cn.edu.bjtu.ebosgatewaymodify.entity.Password;
import cn.edu.bjtu.ebosgatewaymodify.service.JudgeIp;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequestMapping("/api/modify")
@RestController
public class GatewayModifyController {

    @Autowired
    JudgeIp judgeIp;
    @Autowired
    IpAddressService ipAddressService;
    @Autowired
    PasswordService passwordService;

    @CrossOrigin
    @PostMapping("/ipv4")
    public String update(String name,String ip,String netmask,String gateway) throws IOException, InterruptedException {
        if (!judgeIp.ipCheck(ip)){
            return "ipv4地址输入不规范，请重新输入！";
        }
        if (!judgeIp.ipCheck(gateway)){
            return "网关地址输入不规范，请重新输入！";
        }
        String mask = judgeIp.getMask(netmask);
        if (!judgeIp.checkMask(mask)){
            return "子网掩码输入不规范，请重新输入！";
        }
        if (!judgeIp.checkSameSegment(ip,gateway,netmask)){
            return "ip地址和网关不匹配，请重新输入！";
        }

        IpAddress data = ipAddressService.find(name);
        if (data == null){
           ipAddressService.save(name,ip,gateway,netmask);
        }else {
           ipAddressService.delIp(name);
           ipAddressService.save(name,ip,gateway,netmask);
        }

        int num = judgeIp.getMaskNum(mask);

        String ipAddress = ip + "/" + num;

        String command = "";
        command = "/opt/ipv4update.sh " + name + " " + ip +
                " " + netmask + " " + gateway + " " + ipAddress;
        String[] cmdArray = new String[]{"/bin/sh", "-c", command};

        Process process = Runtime.getRuntime().exec(cmdArray);
        process.waitFor();
        System.out.println("修改完成！");
        return "修改成功！";
    }

    @CrossOrigin
    @PostMapping("/ipv6")
    public String update6(String name,String ip,String gateway) throws IOException, InterruptedException {

        String[] ipv6s = ip.split("/");
        String ipv6 = ipv6s[0];
        int num = Integer.parseInt(ipv6s[ipv6s.length-1]);
        if (!judgeIp.isValidIpv6Addr(ipv6)){
            return "ipv6地址输入不规范，请重新输入！";
        }
        if (!judgeIp.isValidIpv6Addr(gateway)){
            return "网关地址输入不规范，请重新输入！";
        }
        if (!judgeIp.checkSameSegment6(ipv6,gateway,num)){
            return "ip地址和网关不匹配，请重新输入！";
        }

        IpAddress data = ipAddressService.find(name);
        String mask = judgeIp.getMask(data.getNetmask());
        int num4 = judgeIp.getMaskNum(mask);
        String ip4 = data.getIp() + "/" + num4;

        String command = "";
        command = "/opt/ipv6update.sh " + name + " " + ipv6 + " " + num + " " +
                 gateway + " " + data.getIp() + " " + data.getNetmask() + " " +
                 data.getGateway() + " " + ip4 + " " + ip;
        String[] cmdArray = new String[]{"/bin/sh", "-c", command};
        Process process = Runtime.getRuntime().exec(cmdArray);
        process.waitFor();
        System.out.println("修改完成！");
        return "修改成功！";
    }

    @CrossOrigin
    @PostMapping("/password")
    public String password(String username,String oldPassword, String newPassword){

        Password data = passwordService.find(username);
        if (data == null){
            return "此用户不存在！";
        }

        if(!oldPassword.equals(data.getPassword())){
            return "原密码输入有误，请重新输入！";
        }else {
            System.out.println("修改完成！");
            return passwordService.modify(data.getUsername(),newPassword);
        }
    }

    @CrossOrigin
    @PostMapping("/login")
    public JSONObject login(String username, String password){

        Password data = passwordService.find(username);
        JSONObject js = new JSONObject();

        if (data == null){
            js.put("status",401.1);
            js.put("message","请输入用户名和密码！");
            return js;
        }
        boolean flag = passwordService.login(username,password);
        if(flag){
            js.put("status",200);
            js.put("message","登录成功！");
            return js;
        }else {
            js.put("status",401.1);
            js.put("message","密码有误，登录失败！");
            return js;
        }
    }

    @CrossOrigin
    @GetMapping("/ping")
    public String ping(){
        return "pong";
    }
}
