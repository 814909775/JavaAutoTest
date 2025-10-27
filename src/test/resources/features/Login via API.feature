@test
Feature: 订舱平台 - 新建订舱数据
  Scenario: test api login
  # 登录步骤之后，可以封装成一步，或者直接保存设置chrome arguments保存user data
#    Given Agent opens "HGJBooking-Phone"
    Given Agent login HGJBooking via API
    Then Agent is on "订舱主页面" page
    When Agent clicks "订舱管理标签"
    And Agent clicks "订单列表标签"

#    Given Agent type "Cbusol814@163.com" into "Mailbox"
#    And Agent type "Q13817759419Q" into "Password"
#    And Agent clicks "Login"
#    Then Agent is on "HGJ Company" page
#    And Agent clicks "MainCompany"
#    Then Agent is on "User Center" page
