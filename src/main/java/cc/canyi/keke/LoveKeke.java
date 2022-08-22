package cc.canyi.keke;


import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.StaticLog;
import lombok.SneakyThrows;

import java.text.SimpleDateFormat;
import java.util.*;

public class LoveKeke {
    /**
     * 推送Map
     * K -> wechat open id
     * V -> wechat message template id
     */
    private static final HashMap<String, String> sendMap = new HashMap<>();

    /**
     * wechat appid
     */
    private static final String appid = "appid";

    /**
     * wechat appSecret
     */
    private static final String appSecret = "appSecret";

    /**
     * wechat token get url
     */
    private static final String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";

    /**
     * wechat message send post url
     */
    private static final String sendUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send";

    /**
     * weather API url
     */
    private static final String weatherUrl = "weatherapiurl";

    /**
     * love message API url
     */
    private static final String qinghuaUrl = "https://api.1314.cool/words/api.php?return=json";


    /**
     * data format
     */
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    /**
     * cache love date
     */
    private static Date loveDate;
    /**
     * cache birthday
     */
    private static Date birthday;


    /**
     * init send map
     */
    public static void initSendMap() {
        //XrBv0DQMYFRTpd4-RzOFQV8ZX4NTdHhgXO_xFZjs2Jg 珂珂模板

        //openid <-> template-id
        sendMap.put("oV9Yf5gtOff4YWXDwKloR_RoIVGc", "XrBv0DQMYFRTpd4-RzOFQV8ZX4NTdHhgXO_xFZjs2Jg");
        sendMap.put("oV9Yf5rQB45li7LZeVazoJbAuDYk", "XrBv0DQMYFRTpd4-RzOFQV8ZX4NTdHhgXO_xFZjs2Jg");

        //骚年
        sendMap.put("oV9Yf5u805dwCY7IJBDj3v-hVfRU", "Acskbzb-dNsTIO6oYh7wXruA12aCbAI_ToI6-u3Ove4");
    }

    /**
     * main
     * @param args param
     */
    @SneakyThrows
    public static void main(String[] args) {

        //init send map
        initSendMap();

        //init love day and birthday
        loveDate = dateFormat.parse("2021-09-13");
        birthday = dateFormat.parse("2022-12-25");

        //params
        Map<String, Object> params = new HashMap<>();

        params.put("appid", appid);
        params.put("secret", appSecret);

        //timer schedule
        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //get token
                String token = JSONUtil.parseObj(HttpUtil.get(url, params)).getStr("access_token");
                System.out.println("Token " + token);
                send(token);
                StaticLog.info("Success send.");
            }
        }, 0, 1000 * 60 * 15);

    }

    public static void send(String token) {
        for(String openid : sendMap.keySet()) {
            //join token
            String realUrl = sendUrl + "?access_token=" + token;

            //generate json data
            JSONObject json = generateJsonData(openid);

            //post
            HttpUtil.post(realUrl, json.toString());

            //log
            StaticLog.info("send data " + json.toString());

            StaticLog.info("Send to " + openid + " success.");
        }
    }

    @SneakyThrows
    public static JSONObject generateJsonData(String openid) {
        JSONObject json = JSONUtil.createObj();
        //根据不同的openid 生成不同的推送信息
        if(openid.equals("oV9Yf5u805dwCY7IJBDj3v-hVfRU")) {
            Date loveDate = dateFormat.parse("2006-12-23");
            Date birthday = dateFormat.parse("2022-12-23");
            json.set("touser", openid);
            json.set("template_id", sendMap.get(openid));

            JSONObject arrayObject = JSONUtil.createObj();

            arrayObject.set("first", vcObject("嗨嗨害 来了嗷~"));

            JSONObject weatherObj = getWeather("保定");
            arrayObject.set("keyword1", vcObject(weatherObj.getStr("wea")));
            arrayObject.set("keyword2", vcObject(weatherObj.getStr("tem_day")));

            String loveDays = DateUtil.between(loveDate, new Date(), DateUnit.DAY) + "";
            String birthdayDays = DateUtil.between(new Date(), birthday, DateUnit.DAY) + "";
            arrayObject.set("keyword3", vcObject(loveDays));
            arrayObject.set("keyword4", vcObject(birthdayDays));
            JSONObject qinghuaObj = getQingHua();
            arrayObject.set("remark", vcObject("\n" + qinghuaObj.getStr("word"), "#f00"));

            json.set("data", arrayObject);
        }else {
            json.set("touser", openid);
            json.set("template_id", sendMap.get(openid));

            JSONObject arrayObject = JSONUtil.createObj();

            arrayObject.set("first", vcObject("嗨嗨害 来了嗷~"));

            JSONObject weatherObj = getWeather("邯郸");
            arrayObject.set("keyword1", vcObject(weatherObj.getStr("wea")));
            arrayObject.set("keyword2", vcObject(weatherObj.getStr("tem_day")));

            String loveDays = DateUtil.between(loveDate, new Date(), DateUnit.DAY) + "";
            String birthdayDays = DateUtil.between(new Date(), birthday, DateUnit.DAY) + "";
            arrayObject.set("keyword3", vcObject(loveDays));
            arrayObject.set("keyword4", vcObject(birthdayDays));
            JSONObject qinghuaObj = getQingHua();
            arrayObject.set("remark", vcObject("\n" + qinghuaObj.getStr("word"), "#f00"));

            json.set("data", arrayObject);
        }
        return json;
    }


    public static JSONObject vcObject(String value) {
        return vcObject(value, "#000");
    }

    public static JSONObject vcObject(String value, String color) {
        JSONObject object = JSONUtil.createObj();
        object.set("value", value);
        object.set("color", color);
        return object;
    }


    public static JSONObject getWeather(String area) {
        return JSONUtil.parseObj(HttpUtil.get(weatherUrl + area));
    }


    public static JSONObject getQingHua() {
        return JSONUtil.parseObj(HttpUtil.get(qinghuaUrl).replace("<br>", ""));
    }

}
