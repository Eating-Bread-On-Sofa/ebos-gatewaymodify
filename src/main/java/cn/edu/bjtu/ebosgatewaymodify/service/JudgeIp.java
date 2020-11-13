package cn.edu.bjtu.ebosgatewaymodify.service;

import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class JudgeIp {

    private static final String IPV4_REGEX = "((\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3})";

    public boolean ipCheck(String ip){

        if (ip != null && !ip.isEmpty()) {
            // 定义正则表达式
            String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                    + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." +"(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                    + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
            // 判断ip地址是否与正则表达式匹配
            if (ip.matches(regex)) {
                // 返回判断信息
                return true;
            } else {
                // 返回判断信息
                return false;
            }
        }
        return false;
    }

    public String getMask(String mask){
        try{
            String[] addrs = mask.split("\\.");
            int length = addrs.length;
            String[] addr = new String[length];
            StringBuilder result = new StringBuilder();
            for (int index = 0; index < length; index++){
                addr[index] = Integer.toBinaryString((Integer.parseInt(addrs[index]) & 0xff));
                result.append(addr[index]);
            }
            return result.toString();
        } catch (Exception e) {}
        return "";
    }

    public boolean checkMask(String mask){
        int[] temp = new int[mask.length()];
        for(int i=0;i<mask.length();i++){
            temp[i] = Integer.parseInt(mask.substring(i, i + 1));
        }
        for(int j=0;j<mask.length();j++){
            if(temp[j]==1){
                temp[j]= 0;
            }else {
                break;
            }
        }
        for(int k=0;k<mask.length();k++){
            if(temp[k]==1){
                return false;
            }
        }
        return true;
    }

    public int getMaskNum(String mask){
        int[] temp = new int[mask.length()];
        int result = 0;
        for(int i=0;i<mask.length();i++){
            temp[i] = Integer.parseInt(mask.substring(i, i + 1));
        }
        for(int j=0;j<mask.length();j++){
            if(temp[j]==1){
                result++;
            }else {
                break;
            }
        }
        return result;
    }

    public boolean checkSameSegment(String ip1,String ip2, String mask){
        int maskInt = getIpV4Value(mask);
        // 判断IPV4是否合法
        if(!ipV4Validate(ip1)){
            return false;
        }
        if(!ipV4Validate(ip2)){
            return false;
        }
        int ipValue1 = getIpV4Value(ip1);
        int ipValue2 = getIpV4Value(ip2);
        return (maskInt & ipValue1) == (maskInt & ipValue2);
    }

    public static boolean ipV4Validate(String ipv4){
        return ipv4Validate(ipv4,IPV4_REGEX);
    }

    private static boolean ipv4Validate(String addr,String regex){
        if(addr == null)
        {
            return false;
        }
        else
        {
            return Pattern.matches(regex, addr.trim());
        }
    }

    public static byte[] getIpV4Bytes(String ipOrMask){
        try{
            String[] addrs = ipOrMask.split("\\.");
            int length = addrs.length;
            byte[] addr = new byte[length];
            for (int index = 0; index < length; index++){
                addr[index] = (byte) (Integer.parseInt(addrs[index]) & 0xff);
            }
            return addr;
        }
        catch (Exception e)
        {
        }
        return new byte[4];
    }

    public static int getIpV4Value(String ipOrMask){
        byte[] addr = getIpV4Bytes(ipOrMask);
        int address1  = addr[3] & 0xFF;
        address1 |= ((addr[2] << 8) & 0xFF00);
        address1 |= ((addr[1] << 16) & 0xFF0000);
        address1 |= ((addr[0] << 24) & 0xFF000000);
        return address1;
    }



    //关于判断ipv6地址合法性
    public boolean isValidIpv6Addr(String ipAddr) {

        String regex = "(^((([0-9A-Fa-f]{1,4}:){7}(([0-9A-Fa-f]{1,4}){1}|:))"
                + "|(([0-9A-Fa-f]{1,4}:){6}((:[0-9A-Fa-f]{1,4}){1}|"
                + "((22[0-3]|2[0-1][0-9]|[0-1][0-9][0-9]|"
                + "([0-9]){1,2})([.](25[0-5]|2[0-4][0-9]|"
                + "[0-1][0-9][0-9]|([0-9]){1,2})){3})|:))|"
                + "(([0-9A-Fa-f]{1,4}:){5}((:[0-9A-Fa-f]{1,4}){1,2}|"
                + ":((22[0-3]|2[0-1][0-9]|[0-1][0-9][0-9]|"
                + "([0-9]){1,2})([.](25[0-5]|2[0-4][0-9]|"
                + "[0-1][0-9][0-9]|([0-9]){1,2})){3})|:))|"
                + "(([0-9A-Fa-f]{1,4}:){4}((:[0-9A-Fa-f]{1,4}){1,3}"
                + "|:((22[0-3]|2[0-1][0-9]|[0-1][0-9][0-9]|"
                + "([0-9]){1,2})([.](25[0-5]|2[0-4][0-9]|[0-1][0-9][0-9]|"
                + "([0-9]){1,2})){3})|:))|(([0-9A-Fa-f]{1,4}:){3}((:[0-9A-Fa-f]{1,4}){1,4}|"
                + ":((22[0-3]|2[0-1][0-9]|[0-1][0-9][0-9]|"
                + "([0-9]){1,2})([.](25[0-5]|2[0-4][0-9]|"
                + "[0-1][0-9][0-9]|([0-9]){1,2})){3})|:))|"
                + "(([0-9A-Fa-f]{1,4}:){2}((:[0-9A-Fa-f]{1,4}){1,5}|"
                + ":((22[0-3]|2[0-1][0-9]|[0-1][0-9][0-9]|"
                + "([0-9]){1,2})([.](25[0-5]|2[0-4][0-9]|"
                + "[0-1][0-9][0-9]|([0-9]){1,2})){3})|:))"
                + "|(([0-9A-Fa-f]{1,4}:){1}((:[0-9A-Fa-f]{1,4}){1,6}"
                + "|:((22[0-3]|2[0-1][0-9]|[0-1][0-9][0-9]|"
                + "([0-9]){1,2})([.](25[0-5]|2[0-4][0-9]|"
                + "[0-1][0-9][0-9]|([0-9]){1,2})){3})|:))|"
                + "(:((:[0-9A-Fa-f]{1,4}){1,7}|(:[fF]{4}){0,1}:((22[0-3]|2[0-1][0-9]|"
                + "[0-1][0-9][0-9]|([0-9]){1,2})"
                + "([.](25[0-5]|2[0-4][0-9]|[0-1][0-9][0-9]|([0-9]){1,2})){3})|:)))$)";

        if (ipAddr == null) {
            System.out.println("IPv6 address is null ");
            return false;
        }
        ipAddr = Normalizer.normalize(ipAddr, Normalizer.Form.NFKC);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(ipAddr);

        boolean match = matcher.matches();
        if (!match) {
            System.out.println("Invalid IPv6 address = " + ipAddr);
        }

        return match;
    }

    public int[] getipv6(String ipv6,int num){
        try{
            String[] addrs = ipv6.split(":");
            int length = addrs.length;
            String[] addr = new String[length];
            StringBuilder result = new StringBuilder();
            for (int index = 0; index < length; index++){
                addr[index] = Integer.toBinaryString((Integer.parseInt(addrs[index])));
                result.append(addr[index]);
            }
            int[] temp = new int[num];
            for(int i=0;i<num;i++){
                temp[i] = Integer.parseInt(result.toString().substring(i, i + 1));
            }
            return temp;
        } catch (Exception e) {}
        return new int[]{0};
    }

    public boolean checkSameSegment6(String ipv6,String gateway,int num) {
        int[] ipaddress6 = getipv6(ipv6,num);
        int[] gateway6 = getipv6(gateway,num);
        for (int j=0;j<num;j++){
            if (!(ipaddress6[j] == gateway6[j])){
                return false;
            }
        }
        return true;
    }


}
