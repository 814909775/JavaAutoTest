@test
Feature: 订舱平台 - 新建订舱数据
  Scenario: test api login
  # 登录步骤之后，可以封装成一步，或者直接保存设置chrome arguments保存user data
#    Given UIClient opens "HGJBooking-Phone"
    Given UIClient login HGJBooking via API
    Then UIClient is on "订舱主页面" page
    When UIClient clicks "订舱管理标签"
    And UIClient clicks "订单列表标签"

#    Given UIClient type "Cbusol814@163.com" into "Mailbox"
#    And UIClient type "Q13817759419Q" into "Password"
#    And UIClient clicks "Login"
#    Then UIClient is on "HGJ Company" page
#    And UIClient clicks "MainCompany"
#    Then UIClient is on "User Center" page
