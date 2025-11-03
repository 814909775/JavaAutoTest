
Feature: 订舱平台UI Auto - 新建订舱数据
  Background:
  # 登录步骤之后，可以封装成一步，或者直接保存设置chrome arguments保存user data
#    Given UIClient opens "HGJBooking-Email"
#    Then UIClient is on "HGJ Login" page
#    Given UIClient type "Cbusol814@163.com" into "Mailbox"
#    And UIClient type "Q13817759419Q" into "Password"
#    And UIClient clicks "Login"
#    Then UIClient is on "HGJ Company" page
#    And UIClient clicks "MainCompany"
#    Then UIClient is on "User Center" page
#    When UIClient clicks "订舱平台"
    Given UIClient login HGJBooking via API
    Then UIClient is on "订舱主页面" page


  Scenario Outline: <港口> + <船司> + 普通货物 - 没有AMS
    When UIClient clicks "订舱管理标签"
    And UIClient clicks "订单列表标签"
    And UIClient clicks "新建订舱"
    Then UIClient sees "快速订舱dialog"
    When UIClient type "<港口>" then select in "选择港口" input
    And UIClient type "<船司>" then select in "选择船司" input
    And UIClient clicks "确定"
    Then UIClient is on "订舱表单" page
    #Then UIClient sees "" in  ""
    When UIClient selects "<签单方式>" in "签单方式" input
    And UIClient selects "<付款方式>" in "付款方式" input
    And UIClient selects "<运输条款>" in "运输条款" input
    And UIClient selects "整箱" in "拼整标记" input
    And UIClient selects "否" in "SOC箱" input
    And UIClient type "<包装单位>" then select in "包装单位" input
    And UIClient type "New York" then select in "卸货港" input
    And UIClient type "20GP" then select in "箱型" input
    And UIClient clicks "同卸货港"
    And UIClient clicks "自动生成"
    #这些都是输入框，下拉框和输入后下拉得用其他的
    And UIClient type data into related fields
    ##要不要将填写的数据维护在其他地方
      | Content                         | fieldname            |
      | Curtis-Shipper-<船司>             | shipperName          |
      | China SuZhou#{timestamp}        | shipperAddress       |
      | Felix-Xu-<船司>                   | consigneeName        |
      | America NewYork#{timestamp}     | consigneeAddress     |
      | Kelly Zhou-<船司>                 | notifyName           |
      | America NewYork                 | notifyAddress        |
      | Zoey Wang                       | secondNotifyName     |
      | America NewYork#{timestamp}     | secondNotifyAddress  |
      | bill#{timestamp}                | billNo               |
      | 3                               | billFNum             |
      | 3                               | billSNum             |
      | CON.1234455                     | contractNo           |
      | ABCDEF                          | mrCodeFront          |
      | 123                             | mrCodeEnd            |
      | This is test order#{timestamp}  | bookingRemark        |
#    |       |contactPerson      |
#    |       |phone              |
      | 34234234                        | qq                   |
      | Book123#{timestamp}             | bookingNo            |
      | Po123                           | poNo                 |
      | Seller1#{timestamp}             | shippingCompanySales |
      | No Fees#{timestamp}             | freightRateDesc      |
      | this is mark#{timestamp}        | marks                |
      | This is product name in English | englishProductName   |
      | 823459333                       | hsCode               |
      | This is product name in Chinese | chineseProductName   |
      | 10                              | number               |
      | 10                              | grossWeight          |
      | 10                              | volume               |
      | 10                              | netWeight            |
      | 1                               | containerNumber      |
    #|   10    |avgGrossWeight      |
    Then UIClient get value from "委托编号" and save to "@最新委托编号"
    When UIClient clicks "船期"
    Then UIClient sees "选择船期dialog"
    When UIClient type "<船司>" into "船名"
    And UIClient type "hang" into "航次"
    And UIClient type "" into "ETD"
    And UIClient clicks "航线代码"
    And UIClient clicks "直接填入"
    When UIClient clicks "暂存草稿"
    And UIClient clicks "保存"
    Then UIClient is on "草稿箱" page
    And UIClient sees "@最新委托编号"

  Examples:
    | 港口 | 船司  | 付款方式               | 运输条款      | 签单方式             | 包装单位 |
    | 上海 | MSK | Freight Prepaid-预付 | CY-CY     | SWB-Seaways Bill | BG   |
    | 上海 | CMA | Freight Collect-到付 | DOOR-DOOR | OBL-正本提单         | JR   |
  #  | 上海 | HPL | Freight Collect-到付 | CY-RAMP   | OBL-正本提单         | 大口瓶  |
  #  | 上海 | OOCL | Freight Prepaid-预付 | CY-CY     | SWB-Seaways Bill | VA   |
  #  | 上海 | COSCO | Freight Prepaid-预付 | CY-CY     | SWB-Seaways Bill | 桶    |

  Scenario Outline: <港口> + <船司> + 普通货物 - 无HBL
#    Given UIClient clicks "订舱平台"
#    Then UIClient is on "订舱主页面" page
    When UIClient clicks "订舱管理标签"
    And UIClient clicks "订单列表标签"
    And UIClient clicks "新建订舱"
    Then UIClient sees "快速订舱dialog"
    When UIClient type "<港口>" then select in "选择港口" input
    And UIClient type "<船司>" then select in "选择船司" input
    And UIClient clicks "确定"
    Then UIClient is on "订舱表单" page
    #Then UIClient sees "" in  ""
    When UIClient selects "<签单方式>" in "签单方式" input
    And UIClient selects "<付款方式>" in "付款方式" input
    And UIClient selects "<运输条款>" in "运输条款" input
    And UIClient selects "整箱" in "拼整标记" input
    And UIClient selects "否" in "SOC箱" input
    And UIClient type "<包装单位>" then select in "包装单位" input
    And UIClient type "New York" then select in "卸货港" input
    And UIClient type "20GP" then select in "箱型" input
    And UIClient clicks "同卸货港"
    And UIClient clicks "自动生成"
    #这些都是输入框，下拉框和输入后下拉得用其他的
    And UIClient type data into related fields
    ##要不要将填写的数据维护在其他地方
      | Content                         | fieldname            |
      | Curtis-Shipper-<船司>             | shipperName          |
      | China SuZhou#{timestamp}        | shipperAddress       |
      | Felix-Xu-<船司>                   | consigneeName        |
      | America NewYork#{timestamp}     | consigneeAddress     |
      | Kelly Zhou-<船司>                 | notifyName           |
      | America NewYork                 | notifyAddress        |
      | Zoey Wang                       | secondNotifyName     |
      | America NewYork#{timestamp}     | secondNotifyAddress  |
      | bill#{timestamp}                | billNo               |
      | 3                               | billFNum             |
      | 3                               | billSNum             |
      | CON.1234455                     | contractNo           |
      | ABCDEF                          | mrCodeFront          |
      | 123                             | mrCodeEnd            |
      | This is test order#{timestamp}  | bookingRemark        |
#    |       |contactPerson      |
#    |       |phone              |
      | 34234234                        | qq                   |
      | Book123#{timestamp}             | bookingNo            |
      | Po123                           | poNo                 |
      | Seller1#{timestamp}             | shippingCompanySales |
      | No Fees#{timestamp}             | freightRateDesc      |
      | this is mark#{timestamp}        | marks                |
      | This is product name in English | englishProductName   |
      | 823459333                       | hsCode               |
      | This is product name in Chinese | chineseProductName   |
      | 10                              | number               |
      | 10                              | grossWeight          |
      | 10                              | volume               |
      | 10                              | netWeight            |
      | 1                               | containerNumber      |
    #|   10    |avgGrossWeight      |
    Then UIClient get value from "委托编号" and save to "@最新委托编号"
    When UIClient clicks "船期"
    Then UIClient sees "选择船期dialog"
    When UIClient type "<船司>" into "船名"
    And UIClient type "hang" into "航次"
    And UIClient type "" into "ETD"
    And UIClient clicks "航线代码"
    And UIClient clicks "直接填入"
    When UIClient clicks "暂存草稿"
    And UIClient clicks "保存"
    Then UIClient is on "草稿箱" page
    And UIClient sees "@最新委托编号"

    Examples:
      | 港口 | 船司  | 付款方式               | 运输条款      | 签单方式             | 包装单位 |
      | 上海 | WHL | Freight Prepaid-预付 | CY-CY     | SWB-Seaways Bill | BG   |
      | 上海 | ONE | Freight Collect-到付 | DOOR-DOOR | OBL-正本提单         | JR   |
      | 上海 | MSC | Freight Prepaid-预付 | CY-CY     | SWB-Seaways Bill | BG|


    # \"([^\"]+)\"
#WHL , one ,msc有ams


