Feature: Login AI Booking
  Background:
  # 登录步骤之后，可以封装成一步，或者直接保存设置chrome arguments保存user data
    Given UIClient opens "HGJBooking"
    Then UIClient is on "HGJ Login" page
    Given UIClient type "Cbusol814@163.com" into "Mailbox"
    And UIClient type "Q13817759419Q" into "Password"
    And UIClient clicks "Login"
    Then UIClient is on "HGJ Company" page
    And UIClient clicks "MainCompany"
    Then UIClient is on "User Center" page
    When UIClient moves to "logistics"
    And UIClient clicks "AIBooking_Link"
    Then UIClient is on "AIBooking Dashboard" page


  Scenario: Create AI Booking by manual
    When UIClient clicks "NewBooking_Button"
    And UIClient clicks "ByManual"
    And UIClient clicks "OK" via CSS
    Then UIClient is on "AI Sheet" page
    When UIClient clicks "Generate Delegate Number"


