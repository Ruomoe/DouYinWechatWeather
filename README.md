# DouYinWechatWeather
抖音微信公众号推送天气通知
# 使用方法
在微信公众平台创建一个公众号(或者接口测试号)本篇教程使用接口测试号进行演示<br>
将代码中 ```"appid"``` ```"appSecret"``` ```"weatherapiurl"``` 更换为自己的<br>
在微信公众平台创建信息模板 标题自拟 可看下方示例 创建后可看到模板id<br>
扫描微信公众平台的二维码关注你自己的微信公众号可在右侧查看到你的微信openid<br>
将代码中 ```initSendMap``` 方法中 openid <-> template_id 的键值对更换为自己的<br>
运行即可收到通知 默认15分钟推送一次 可自行去main方法中修改<br>
模板中设置如下 标题自拟<br>
```
{{first.DATA}}
今天天气: {{keyword1.DATA}}~
今天温度: {{keyword2.DATA}}℃
我们爱上彼此: {{keyword3.DATA}}天
珂珂生日还有: {{keyword4.DATA}}天
{{remark.DATA}} 
```
# 常见问题
1. 我将代码Clone后 appid appSecret 天气接口Url 都替换成功了 并且浏览器测试可以访问为什么运行程序报错 SSLXXXXXX 啥的? <br>
A: 可能是证书出现了问题 可以尝试将天气接口/情话接口的url中 https 替换成 http<br><br>
2. 模板如何填写?<br>
A: 在你的微信公众平台中创建信息模板 键值对 值 填入信息模板ID (例如: ```sendMap.put("openid", "模板ID");```)
