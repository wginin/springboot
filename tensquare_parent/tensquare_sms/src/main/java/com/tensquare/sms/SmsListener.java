package com.tensquare.sms;

import com.aliyuncs.exceptions.ClientException;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Map;
/**
 * 短信微服务监听类  监听sms消息队列
 */
@Component //将监听类实例化放入spring容器中
@RabbitListener(queues = "sms")//监听哪个消息队列
public class SmsListener {

    @Autowired
    private SmsUtil smsUtil;

    @Value("${aliyun.sms.template_code}")
    private String templateCode;

    @Value("${aliyun.sms.sign_name}")
    private String signName;

    @RabbitHandler //接收到消息 进行业务处理  发送短信
    public void sendMsg(Map<String,String> map){
        //将手机号码和 验证码打印出来保证环境没有问题
        System.out.println("手机号："+map.get("mobile"));
        System.out.println("验证码："+map.get("code"));

        //调用短信工具类 {"code":""}
        try {
            smsUtil.sendSms(map.get("mobile"),templateCode,signName,"{\"code\":"+map.get("code")+"}");
            System.out.println("短信发送成功了。。。");
        } catch (ClientException e) {
            e.printStackTrace();
        }


    }
}
