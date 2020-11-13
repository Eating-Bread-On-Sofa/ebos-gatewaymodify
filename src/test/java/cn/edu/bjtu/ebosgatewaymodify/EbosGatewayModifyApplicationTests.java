package cn.edu.bjtu.ebosgatewaymodify;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EbosGatewayModifyApplicationTests {

    public  StringBuilder getIpV4Bytes(String ipOrMask){
        try{
            String[] addrs = ipOrMask.split("\\.");
            int length = addrs.length;
            String[] addr = new String[length];
            StringBuilder result = new StringBuilder();
            for (int index = 0; index < length; index++){
                addr[index] = Integer.toBinaryString((Integer.parseInt(addrs[index]) & 0xff));
                result.append(addr[index]);
            }
            return result;
        }
        catch (Exception e)
        {
        }
        return new StringBuilder();
    }

    @Test
    public void main() {
        String result = getIpV4Bytes("255.255.128.0").toString();
        int[] temp = new int[result.length()];
        int jo = 0;
        for(int i=0;i<result.length();i++){
            temp[i] = Integer.parseInt(result.substring(i, i + 1));
        }
        for(int j=0;j<result.length();j++){
            if(temp[j]==1){
                temp[j]= 0;
                jo++;
            }else {
                break;
            }
        }
        for(int k=0;k<result.length();k++){
            if(temp[k]==1){
                System.out.println("掩码出错");
                break;
            }
        }
        System.out.println("掩码正确");
        System.out.println(jo);
    }
}
