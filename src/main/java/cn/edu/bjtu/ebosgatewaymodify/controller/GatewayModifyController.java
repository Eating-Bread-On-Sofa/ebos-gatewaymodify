package cn.edu.bjtu.ebosgatewaymodify.controller;

import cn.edu.bjtu.ebosgatewaymodify.dao.IpAddressService;
import cn.edu.bjtu.ebosgatewaymodify.dao.PasswordService;
import cn.edu.bjtu.ebosgatewaymodify.entity.IpAddress;
import cn.edu.bjtu.ebosgatewaymodify.entity.Password;
import cn.edu.bjtu.ebosgatewaymodify.service.JudgeIp;
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
    public String update(String name,String ip,String netmask,String gateway,String dns) throws IOException, InterruptedException {
        if (!judgeIp.ipCheck(ip)){
            return "ipv4地址输入不规范，请重新输入！";
        }
        if (!judgeIp.ipCheck(gateway)){
            return "网关地址输入不规范，请重新输入！";
        }
        if (!judgeIp.ipCheck(dns)){
            return "DNS地址输入不规范，请重新输入！";
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
           ipAddressService.save(name,ip,gateway,netmask,dns);
        }else {
           ipAddressService.delIp(name);
           ipAddressService.save(name,ip,gateway,netmask,dns);
        }

        int num = judgeIp.getMaskNum(mask);

        String ipAddress = ip + "/" + num;

        String command = "";
        command = "/opt/ipv4update.sh " + name + " " + ipAddress + " " + gateway + " " +dns;
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
        command = "/opt/ipv6update.sh " + name + " " + ip4 + " " + ip + " "
                + data.getGateway() + " " + gateway + " " + data.getDns();
        String[] cmdArray = new String[]{"/bin/sh", "-c", command};
        Process process = Runtime.getRuntime().exec(cmdArray);
        process.waitFor();
        System.out.println("修改完成！");
        return "修改成功！";
    }

    @CrossOrigin
    @PostMapping("/password")
    public String password(String username,String oldPassword, String newPassword) throws IOException, InterruptedException {

        if (!(username.equals("ebos") || username.equals("root"))){
            return "此用户不存在！";
        }
        Password data = passwordService.find(username);
        if(!oldPassword.equals(data.getPassword())){
            return "原密码输入有误，请重新输入！";
        }else {
            passwordService.delete(username);
            passwordService.save(username, newPassword);
            String command = "";
            command = "/opt/password.sh " + username + " " + newPassword;
            String[] cmdArray = new String[]{"/bin/sh", "-c", command};
            Process process = Runtime.getRuntime().exec(cmdArray);
            process.waitFor();
            System.out.println("修改完成！");
            return "修改成功！";
        }
    }

    @CrossOrigin
    @GetMapping("/ping")
    public String ping(){
        return "pong";
    }
}
