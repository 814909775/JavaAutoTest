
Feature: Login Function
  Background:
  # 登录步骤之后，可以封装成一步，或者直接保存设置chrome arguments保存user data
    Given Agent opens "HGJBooking"
    Then Agent is on "HGJ_Login" page
    Given Agent type "Cbusol814@163.com" into "Mailbox"
    And Agent type "Q13817759419Q" into "Password"
    And Agent clicks "Login"
    Then Agent is on "HGJ_Company" page
    And Agent clicks "MainCompany"
    Then Agent is on "User_Center" page


  Scenario: Login Scenario1
    Given Agent clicks "BookingPlatform"
    # \"([^\"]+)\"



