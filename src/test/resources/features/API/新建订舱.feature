@testapi
Feature: 新建订舱
  Scenario: sdfsdf
    Given APIClient设置请求头
        | header-name  | header-value     |
        | app-name     | whale_common_pc  |
    #登录接口
    And APIClient发起用户登录APIPOST请求
       | BaseUri                     | Path                                         | Body |
       | https://beta-apisix.hgj.com | /whale-user-center/pass/login/password-login |login.json|
    Then APIClient校验用户登录API响应结果
      | 验证项 | 期望值 |
      | 状态码 | 200 |
      | 返回字段含 | secret |
      | 返回字段含 | userId |
      | 返回字段含 | enterpriseInfos |
      | 字段值等于 | code=200 |
      | 字段类型为 | code=Integer |
    #选择企业
#    And APIClient发起选择企业APIPOST请求
#      | BaseUri                     | Path                                         | Body |
#      | https://beta-apisix.hgj.com | /whale-user-center/pass/login/password-login |  {"userId": "%s","enterpriseId": "%s","secret": "%s"}    |
