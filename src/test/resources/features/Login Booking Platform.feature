@test
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
    When Agent clicks "Booking Manager Tab"
    And Agent clicks "Booking List Tab"
    And Agent clicks "New Booking Link"
    Then Agent sees "Quick Booking Dialog"
    When Agent type "青岛" then select in "Port" input
    And Agent type "ASL" then select in "Carrier" input
    And Agent clicks "Submit"
    Then Agent is on "Booking Form" page
    #Then Agent sees "" in  ""
    When Agent selects "OBL-正本提单" in "签单方式" input
    And Agent selects "Freight Prepaid-预付" in "付款方式" input
    And Agent selects "CY-CY" in "运输条款" input
    And Agent selects "整箱" in "拼整标记" input
    And Agent selects "否" in "SOC箱" input
    And Agent clicks "自动生成"
    #这些都是输入框，下拉框和输入后下拉得用其他的
    And Agent type data into related fields
    ##要不要将填写的数据维护在其他地方
    |Content|fieldname|
    |  Curtis-Shipper     |shipperName|
    |  China SuZhou     |shipperAddress|
    |  Felix-Xu     |consigneeName |
    |  America NewYork     |consigneeAddress|
    |  Kelly Zhou     |notifyName      |
    |  America NewYork    |notifyAddress   |
    |   Zoey Wang    |secondNotifyName|
    |   America NewYork    |secondNotifyAddress|
    |  987654321     |billNo             |
    |   3    |billFNum           |
    |    3   |billSNum           |
    |  CON.1234455     |contractNo         |
    |  ABCDEF     |mrCodeFront        |
    |   123    |mrCodeEnd          |
    |   This is test order    |bookingRemark      |
#    |       |contactPerson      |
#    |       |phone              |
    |   34234234    |qq                 |
    | Book123      |bookingNo          |
    | Po123      |poNo               |
    |  Seller1     |shippingCompanySales|
    |   No Fees    |freightRateDesc     |
    |  this is mark     |marks               |
    |  This is product name in English     |englishProductName  |
    |  823459333     |hsCode              |
    |  This is product name in Chinese      |chineseProductName  |
    |   10    |number              |
    |   10    |grossWeight         |
    |   10    |volume              |
    |   10    |netWeight           |
    |   1    |containerNumber     |
    |   10    |avgGrossWeight      |
    Then Agent get value from "委托编号" and save to "@最新委托编号"
    When Agent clicks "暂存草稿"
    And Agent clicks "保存"
    Then Agent is on "草稿箱" page
    And Agent sees "@最新委托编号"


    # \"([^\"]+)\"



