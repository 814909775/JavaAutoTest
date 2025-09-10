
Feature: Login Booking Platform
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


  Scenario: HGJ Booking - Client Login
    Given Agent clicks "BookingPlatform"
    Then Agent is on "Booking Dashboard" page
    # \"([^\"]+)\"



