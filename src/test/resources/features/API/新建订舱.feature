Feature: 新建订舱
  Background:
    Given APIClient设置请求头
        | header-name  | header-value     |
        | Content-Type | application/json |
        | App-Name     | whale_common_pc  |
    And APIClient发起如下请求
      | Method | BaseUri                     | Path                                         | Body |
      | Post   | https://beta-apisix.hgj.com | /whale-user-center/pass/login/password-login |{"username": "","account": "%s","countryCode": "86","loginType": 1,"registerType": 1,"password":"%s","rememberMe": true,"param": {"openId": "","unionId": "","client": ""},"captchaData": {}}      |
