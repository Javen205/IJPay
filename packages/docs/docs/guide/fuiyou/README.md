# IJPay 中的富友支付

## 官方参考文档

- [富友支付开放平台-聚合支付 API](https://fundwx.fuiou.com/doc/#/aggregatePay/api)
- [富友支付-聚合支付介绍(含测试环境参数)](https://fundwx.fuiou.com/doc/#/aggregatePay/introduction)

## 支持的功能

:::: tip 富友支付(聚合支付)说明
富友聚合支付可聚合微信支付、支付宝支付、银联(云闪付)等主流支付方式,IJPay 基于其「聚合支付」接口封装,支持统一下单、条码支付、公众号/小程序下单、退款、关闭/撤销、提现、历史订单查询等能力。
::::

IJPay 富友模块主要 API(`com.ijpay.fuiyou.FuiYouPayApi`):

| 方法 | 官方接口 | 说明 |
| --- | --- | --- |
| `unifiedOrder(params)` | 2.1 统一下单 | 扫码支付/收银台下单,返回二维码链接 |
| `microPay(params)` | 2.2 条码支付 | 用户付款码当面付(被扫) |
| `orderQuery(params)` | 2.3 订单查询 | 查询订单支付状态 |
| `refund(params)` | 2.4 退款申请 | 发起退款 |
| `wxPreCreate(params)` | 2.6 公众号/服务窗统一下单 | 公众号、小程序、APP、云闪付等交易方式(见 `FuiYouTradeType`) |
| `queryWithdrawAmt(params)` | 2.8 查询可提现资金 | 资金余额查询 |
| `queryFeeAmt(params)` | 2.9 查询提现手续费 | 提现手续费试算 |
| `withdraw(params)` | 2.10 发起提现 | 余额提现到结算卡 |
| `queryTransferAmt(params)` | 2.11 资金划拨查询 | 按日期查询划拨流水 |
| `hisTradeQuery(params)` | 2.12 历史订单查询 | 历史交易查询 |
| `refundQuery(params)` | 2.13 退款查询(3 日内) | 退款结果查询 |
| `closeOrder(params)` | 2.14 关闭订单 | 关闭未支付订单 |
| `cancelOrder(params)` | 2.15 撤销订单 | 撤销当日交易 |
| `auth2Openid(params)` | 2.16 获取 openid | 服务商模式,返回拼接好的授权跳转 URL |
| `getOpenidByAuthCode(params)` | 2.17 银联授权码获取 user_id | 云闪付 user_id 获取 |

辅助能力:

- `FuiYouPayApi.generateMchntOrderNo(prefix)`:按官方规则生成商户订单号(前缀 + `yyyyMMdd` 日期 + 10 位随机数,最长不超过 30 位)
- `FuiYouPayCore.createSign / verifySign / verifyNotify / verifyNotifyByFullSign`:MD5 签名与验签
- `FuiYouOrderType`、`FuiYouTradeType`、`FuiYouTransStat`、`FuiYouApiType` 等枚举:`order_type`/`trade_type`/交易状态/接口路径定义

## 添加模块依赖

[添加模块依赖](../maven.md)

## 配置说明

:::: tip IJPay 中富友支付需要配置的参数如下:
- mchntCd: 商户号
- mchntKey: 商户密钥
- domain: 接口环境域名(测试/生产)
::::

对应配置文件示例:

```properties
# 富友支付官方测试环境参数(来源:富友聚合支付文档-测试环境参数)
# 测试环境订单前缀:1066
fuiyou.mchntCd=0002900F5829371
fuiyou.mchntKey=f00dac5077ea11e754e14c9541bc0170
# 接口环境 test 测试环境 / prod 生产环境
fuiyou.domain=test
```

## 参数如何获取

富友支付为**审核制接入**,需先向富友申请开通聚合支付,获取商户号与商户密钥。

### 1. mchntCd(商户号)

| 参数 | mchntCd |
| --- | --- |
| 获取方式 | 开通富友聚合支付后由富友分配 |

- 测试环境可直接使用官方文档提供的测试商户号(见富友文档「测试环境参数」章节,官方公开的测试商户号 `0002900F5829371` 等)
- 生产环境商户号为富友分配的真实商户号

```properties
fuiyou.mchntCd=0002900F5829371
```

### 2. mchntKey(商户密钥)

| 参数 | mchntKey |
| --- | --- |
| 获取方式 | 富友商户后台/开通验收后由富友分配 |

- 测试环境使用官方文档提供的测试密钥即可联调
- 生产环境密钥由富友验收通过后分配;**建议使用网易邮箱等稳定邮箱申请,避免收不到富友通知邮件**
- 该密钥用于接口 MD5 签名与回调验签,务必妥善保管,禁止泄露

```properties
fuiyou.mchntKey=f00dac5077ea11e754e14c9541bc0170
```

### 3. domain(接口环境)

| 参数 | domain |
| --- | --- |
| 取值范围 | `test` 测试环境 / `prod` 生产环境 |
| 获取方式 | 手动填写 |

- IJPay 内置 `FuiYouDomainEnum` 对应官方域名:
  - 测试:`https://aipaytest.fuioupay.com`
  - 生产:`https://aipay-cloud.fuioupay.com`
- 配置文件填写 `test` 或 `prod` 即可自动切换;如需自定义域名,可在代码中直接传入 `FuiYouDomain` 实现

```properties
fuiyou.domain=test
```

### 其他注意点

- **订单前缀**:测试环境订单前缀固定为 `1066`;生产环境使用富友分配的商户号编码前缀(4 或 5 位)
- **term_id(终端号)**:Demo 中测试使用 `88888888`,正式接入需使用富友分配的终端号
- **金额单位**:所有金额字段单位均为**分**
- **APPID**:公众号/小程序交易需在富友配置对应的 APPID,测试环境官方提供 `wxfa089da95020ba1a`、`wx5ac8eb4651fe544f`

## 实例化配置

```java {}
// 方式一:直接构建(推荐)
FuiYouPayApiConfig config = FuiYouPayApiConfig.builder()
    .mchntCd("0002900F5829371")          // 商户号
    .mchntKey("f00dac5077ea11e754e14c9541bc0170")  // 商户密钥
    .domain(FuiYouDomainEnum.TEST)       // 测试环境
    .build();
FuiYouPayApiConfigKit.putApiConfig(config);
```

多商户场景可按商户号分别放入配置,并通过 `FuiYouPayApiConfigKit.setThreadLocalFuiYouPayApiConfig(config)` 切换当前线程使用的商户配置。

## 发起统一下单

```java {}
Map<String, String> params = new HashMap<>(16);
params.put("order_type", "ALIPAY");                                  // 订单类型
params.put("order_amt", "1");                                        // 金额(分)
params.put("mchnt_order_no", FuiYouPayApi.generateMchntOrderNo("1066")); // 商户订单号
params.put("txn_begin_ts", DateUtil.format(new Date(), "yyyyMMddHHmmss"));
params.put("goods_des", "商品描述");
params.put("term_id", "88888888");
params.put("term_ip", "127.0.0.1");
params.put("notify_url", "https://你的域名/fuiYou/payNotify"); // 支付结果通知地址

// 内部自动补全 mchnt_cd/random_str/version 并生成 sign
String result = FuiYouPayApi.unifiedOrder(params);
```

公众号/服务窗场景调用 `FuiYouPayApi.wxPreCreate(params)`,`trade_type` 可通过 `FuiYouTradeType` 枚举获取(如 `FuiYouTradeType.JSAPI.getType()`)。

## 异步通知验签

富友支付结果以异步通知推送,最多通知 5 次、间隔 30 秒,**商户应答返回 `"1"` 后停止通知**。回调 JSON 由 IJPay 的 `FuiYouNotify` 自动解析:

```java {}
String readData = HttpKit.readData(request);
FuiYouNotify notify = JSONUtil.toBean(readData, FuiYouNotify.class);

// 验签 sign(mchnt_cd|mchnt_order_no|settle_order_amt|order_amt|txn_fin_ts|reserved_fy_settle_dt|random_str)
boolean verifyNotify = FuiYouPayCore.verifyNotify(notify.getSign(), notify.getMchnt_cd(),
    notify.getMchnt_order_no(), notify.getSettle_order_amt(), notify.getOrder_amt(),
    notify.getTxn_fin_ts(), notify.getReserved_fy_settle_dt(), notify.getRandom_str(),
    apiConfig.getMchntKey());

// 验签 full_sign(result_code|result_msg|mchnt_cd|mchnt_order_no|settle_order_amt|order_amt|txn_fin_ts|reserved_fy_settle_dt|random_str)
boolean verifyFullSign = FuiYouPayCore.verifyNotifyByFullSign(notify.getFull_sign(),
    notify.getResult_code(), notify.getResult_msg(), notify.getMchnt_cd(), notify.getMchnt_order_no(),
    notify.getSettle_order_amt(), notify.getOrder_amt(), notify.getTxn_fin_ts(),
    notify.getReserved_fy_settle_dt(), notify.getRandom_str(), apiConfig.getMchntKey());

// 验签通过且 result_code 为 0000(成功)后处理业务,注意判重(同一订单号可能收到多次通知)
if (verifyNotify && "0000".equals(notify.getResult_code())) {
    // TODO 更新订单状态
    return "1";
}
return "0";
```

## 如何使用？

请参考 [JavaDoc 文档](https://apidoc.gitee.com/javen205/IJPay)或者看以下完整示例

## 完整示例

- [IJPay-Demo-SpringBoot](https://gitee.com/javen205/IJPay/tree/master/IJPay-Demo-SpringBoot)(`fuiyou.properties` 配置 + `/fuiYou` 系列接口)

<Q url="tencent://message/?uin=572839485&Site=%E5%AE%A2%E6%9C%8D&Menu=yes" />
