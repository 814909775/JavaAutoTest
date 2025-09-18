Feature: Login AI Booking
  Background:
  # 登录步骤之后，可以封装成一步，或者直接保存设置chrome arguments保存user data
    Given Agent opens "HGJBooking"
    Then Agent is on "HGJ Login" page
    Given Agent type "Cbusol814@163.com" into "Mailbox"
    And Agent type "Q13817759419Q" into "Password"
    And Agent clicks "Login"
    Then Agent is on "HGJ Company" page
    And Agent clicks "MainCompany"
    Then Agent is on "User Center" page
    When Agent moves to "logistics"
    And Agent clicks "AIBooking_Link"
    Then Agent is on "AIBooking Dashboard" page


  Scenario: Create AI Booking by manual
    When Agent clicks "NewBooking_Button"
    And Agent clicks "ByManual"
    And Agent clicks "OK" via CSS
    Then Agent is on "AI Sheet" page
    When Agent clicks "Generate Delegate Number"


