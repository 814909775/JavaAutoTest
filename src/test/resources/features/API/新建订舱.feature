@testapi
Feature: 新建订舱 API Auto
  Background:
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
    And APIClient发起选择企业APIPOST请求
      | BaseUri                     | Path                                         | Body |
      | https://beta-apisix.hgj.com | /whale-user-center/pass/login/choose-enterprise|  {"userId": "%{userId}","enterpriseId": "%{enterpriseId}","secret": "%{secret}"}    |
    Then APIClient校验选择企业API响应结果
      | 验证项 | 期望值 |
      | 状态码 | 200 |
      | 字段值等于 | code=200 |
      | 返回字段含 | accessToken |
    Given APIClient设置请求头
      | header-name  | header-value     |
      | Access-Token  | %{Access-Token}   |

  Scenario: 新建订舱API测试-Positive
    When 用户发送GET请求带以下参数
      | BaseUri                     | Path                                                 | Body |
      | https://beta-apisix.hgj.com | /booking-open-order/access/order/productDelegationNo | null      |
    Then APIClient校验获取委托编号响应结果
      | 验证项   | 期望值  |
      | 状态码   | 200  |
      | 返回字段含 | data |
    And APIClient发起新建订舱POST请求
      | BaseUri                     | Path                                                | Body                     |
      | https://beta-apisix.hgj.com | /booking-open-order/access/order/bookingOrderHandle | OOCL-positive.json|
    Then APIClient校验新建订舱响应结果
      | 验证项   | 期望值  |
      | 状态码   | 200  |
      | 返回字段含 | data |
#    When 用户发送GET请求到 "/api/users" 带以下参数:
#      | page | 1 |
#      | size | 10 |
