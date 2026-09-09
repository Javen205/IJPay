# IJPay 中的 Stripe 支付

## 官方参考文档

- [Stripe 控制台](https://dashboard.stripe.com)(测试/生产模式切换入口)
- [Stripe API 文档](https://docs.stripe.com/api)
- [Stripe 测试环境(沙箱)](https://docs.stripe.com/testing)
- [测试卡号清单](https://docs.stripe.com/testing#cards)
- [Stripe CLI](https://docs.stripe.com/stripe-cli)

## 沙箱环境说明

:::: tip 测试模式(沙箱)要点
- 新注册的 Stripe 账号默认处于**测试模式(Test mode)**,沙箱内的收款、退款均为虚拟交易,不会产生真实账单
- Dashboard 右上角的开关可切换「测试模式 / 生产模式」,两者互不相通
- 测试环境获取的密钥以 `_test_` 结尾(`sk_test_`、`pk_test_`),生产环境以 `_live_` 结尾(`sk_live_`、`pk_live_`)
- **配置参数时务必确认处于测试模式,防止误用生产密钥产生真实扣款**
::::

## 两种收款方式的区别

IJPay Stripe Demo 的收款演示页 `/stripe.html` 提供两种收款方式,它们使用相同的测试卡与金额,区别在于「支付页面/收银流程由谁来做、支付结果由谁掌握」。

### 方式一:确认支付(PaymentIntent + Elements)

对应后端 `POST /stripe/createPaymentIntent` + 前端 `stripe.confirmCardPayment`。

```
后端 createPaymentIntent → 返回 client_secret
        ↓
前端把卡片元素(卡号输入框) + client_secret 交给 Stripe.js
        ↓
Stripe 直接扣款 → 前端拿到 paymentIntent 结果
```

特点:

- **收银页面是你自己网站的**——卡号输入框由 Stripe Elements 渲染并嵌入你的页面
- 页面风格、字段、支付后动作完全由你自定义,适合与下单页等业务 UI 深度融合
- 支付成功后**不跳转页面**,`PaymentIntent`(以 `pi_` 开头)ID 直接可用,可传给查询/退款接口
- 3DS 验证、错误提示等交互需要自行处理,前端工作量相对较大

### 方式二:Checkout 托管收银台

对应后端 `POST /stripe/createCheckoutSession`,浏览器整页跳转到 Stripe 托管收银台。

```
后端 createCheckoutSession → 返回托管页面 url
        ↓
浏览器整页跳转到 checkout.stripe.com 的 Stripe 托管页面完成支付
        ↓
支付完成 → 自动回跳到你配置的 success_url
        ↓(可选) Webhook 推送 checkout.session.completed
```

特点:

- **收银页面由 Stripe 官方托管**,你的站点只需一个「去支付」按钮,前端工作量极低
- 自动包含 Apple Pay/Google Pay、3DS、发票、货币换算等能力,合规与安全由 Stripe 持续维护
- 需要**公网 HTTPS 域名**(`stripe.domain`),否则无法配置 `success_url` 回跳
- 回跳地址携带 `session_id`(`cs_test_…`),但**建议以 Webhook 通知为准**更新订单状态

### 对比一览

| 维度 | 确认支付(PaymentIntent+Elements) | Checkout 托管收银台 |
| --- | --- | --- |
| 收银界面 | 你的网站(内嵌卡片元素) | Stripe 托管页面(整页跳转) |
| 前端工作量 | 高(自绘 UI 与交互) | 极低(一个跳转按钮) |
| 页面定制能力 | 完全可控 | 有限(Stripe 页面) |
| 支付后行为 | 留在当前页,直接使用 `pi_…` | 跳走再回跳,靠 session/Webhook 获取结果 |
| 是否需公网域名 | 不需要 | 必须(HTTPS) |
| 额外能力 | 需自行处理 3DS 等 | 内置 Apple Pay/3DS/发票等 |
| 适用场景 | 深度定制支付 UI 的业务 | 快速标准收款、订阅等 |

### 选型建议

- 初次联调建议先跑通「确认支付」,理解 PaymentIntent 完整生命周期
- 正式业务若仅做标准收款,优先选 Checkout,可显著减少 UI 与合规成本

## 添加模块依赖

[添加模块依赖](../maven.md)

## 配置说明

:::: tip IJPay 中 Stripe 支付需要配置的参数如下:
- appId: 应用标识(自定义,用于多商户/多配置隔离)
- apiKey: 后端密钥,测试环境以 `sk_test_` 开头
- publishableKey: 前端可发布密钥,测试环境以 `pk_test_` 开头
- webhookSecret: Webhook 签名密钥,以 `whsec_` 开头
- sandBox: 是否为沙箱(测试)环境
- domain: 外网 HTTPS 域名,Checkout 成功/取消回跳及 Webhook 使用
::::

对应配置文件示例:

```properties
stripe.appId=ijpay-stripe-demo
stripe.apiKey=sk_test_xxx
stripe.publishableKey=pk_test_xxx
stripe.webhookSecret=whsec_xxx
stripe.sandBox=true
stripe.domain=https://your.domain.com
```

## 参数如何获取

以下操作均在浏览器登录 [Stripe 控制台](https://dashboard.stripe.com) 后完成,并**确认右上角处于测试模式**。

### 1. appId

| 参数 | appId |
| --- | --- |
| 前缀 | 无(自定义字符串) |
| 获取方式 | **无需在控制台获取**。自行定义一个应用标识即可 |

- 用于区分同一套系统接入的多个 Stripe 商户/账号(多租户隔离)
- IJPay 的 `StripeApiConfigKit` 会以 appId 为键缓存配置,相同 appId 调用相同密钥
- 单商户场景随便填写,如 `ijpay-stripe-demo`

### 2. apiKey(Secret Key)

| 参数 | apiKey |
| --- | --- |
| 前缀 | 测试:`sk_test_` / 生产:`sk_live_` |
| 获取方式 | 控制台 **开发者 → API 密钥** |

获取步骤:
1. 登录控制台后,点击左下角「开发者(Developers)」入口(新版 UI 为左下角齿轮图标)
2. 进入 **API 密钥(API keys)** 页面
3. 找到 **Secret key(密钥)**,点击右侧「⋯」或「Reveal secret key」显示完整值
4. 确认以 `sk_test_` 开头后复制,填入配置:

```properties
stripe.apiKey=sk_test_51xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
```

::: warning 安全提醒
`apiKey` 是后端密钥,拥有该账号全部 API 权限,**绝不能暴露到前端页面或提交到公开仓库**。泄露后请立即在控制台点击「Roll secret key」轮换。
:::

### 3. publishableKey(Publishable Key)

| 参数 | publishableKey |
| --- | --- |
| 前缀 | 测试:`pk_test_` / 生产:`pk_live_` |
| 获取方式 | 控制台 **开发者 → API 密钥** |

获取步骤:
1. 仍在 **API 密钥** 页面
2. 找到 **Publishable key(可发布密钥)**
3. 明文可直接复制,确认以 `pk_test_` 开头:

```properties
stripe.publishableKey=pk_test_51xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
```

::: tip 用途
该密钥会被下发到前端用于初始化 Stripe.js(如 IJPay Demo 中的 `/stripe/config` 接口),用于创建卡片元素、发起确认支付。它可以公开,但请使用**受限密钥(restricted key)**限制其权限。
:::

### 4. webhookSecret(Webhook 签名密钥)

| 参数 | webhookSecret |
| --- | --- |
| 前缀 | `whsec_` |
| 获取方式 | 控制台 **开发者 → Webhook**,创建端点后显示;或使用 Stripe CLI `stripe listen` 生成 |

Stripe 通过 Webhook 把支付结果(支付成功、失败等)推送到你的服务器。IJPay 使用该密钥对回调内容验签(`StripeApi.constructEvent`),**不配置则回调永远验签失败**。

#### 方式一:本地联调(Stripe CLI,推荐先使用)

无需公网域名,CLI 会自动转发沙箱事件到本机:

```bash
# macOS
brew install stripe/stripe-cli/stripe

# Debian/Ubuntu
curl -s https://packages.stripe.dev/api/security/key/public | gpg --dearmor | sudo tee /etc/apt/keyrings/stripe.gpg
echo 'deb [signed-by=/etc/apt/keyrings/stripe.gpg] https://packages.stripe.dev/stripe-cli-debian-local/ stable main' | sudo tee /etc/apt/sources.list.d/stripe.list
sudo apt update && sudo apt install stripe

# 登录授权一次
stripe login

# 将沙箱事件转发到本地 Demo(端口与 application.yml 保持一致)
stripe listen --forward-to localhost:80/stripe/webhook
```

执行 `stripe listen` 后终端会打印签名密钥:

```
Your webhook signing secret is whsec_XXXXXXXXXXXXXXXXXXXXXXXX (^C to quit)
```

复制该 `whsec_…` 填入配置即可。Ctrl+C 退出后该密钥失效,重新运行需更新配置。

#### 方式二:正式端点(需已配置好 domain 对应的公网 HTTPS 域名)

1. 控制台进入 **开发者 → Webhook**
2. 点击 **Add endpoint / 添加端点**
3. Endpoint URL 填写:`https://你的域名/stripe/webhook`
4. 监听事件至少勾选:
   - `payment_intent.succeeded`(支付成功)
   - `payment_intent.payment_failed`(支付失败)
   - `checkout.session.completed`(结账完成)
5. 点击 **Add endpoint** 创建
6. 点击刚创建的端点 → 「Reveal signing secret / 显示签名密钥」,复制 `whsec_…`:

```properties
stripe.webhookSecret=whsec_XXXXXXXXXXXXXXXXXXXXXXXXXXXX
```

::: tip 验证
配置完成后,可在控制台 **开发者 → 事件(Events)** 中点击「Send test webhook」向你的端点推送一条测试事件,确认能正常接收并验签通过。
:::

### 5. sandBox

| 参数 | sandBox |
| --- | --- |
| 类型 | Boolean |
| 获取方式 | 手动填写 |

- `true`:测试模式(开发环境,使用 `sk_test_`/`pk_test_` 密钥)
- `false`:生产模式(使用 `sk_live_`/`pk_live_` 密钥)

```properties
stripe.sandBox=true
```

### 6. domain

| 参数 | domain |
| --- | --- |
| 类型 | URL(必须为公网 HTTPS) |
| 获取方式 | 自己的服务器域名,或使用内网穿透工具获得 |

用途:
- Checkout 托管收银台支付成功后回跳 `domain/stripe/success`
- Checkout 取消后回跳 `domain/stripe/cancel`
- 正式 Webhook 端点地址 `domain/stripe/webhook`

**Stripe 要求 `success_url` 必须是公网可访问的 HTTPS 地址**,不能填写 `localhost`。

#### 方式一:自有服务器域名

如 `https://pay.example.com`,需将域名解析指向运行 Demo 的机器,并确保 80/443 端口可达。

#### 方式二:本地开发使用内网穿透

以 cpolar 为例(ngrok 同理 `ngrok http 80`):

```bash
cpolar http 80
```

运行后得到类似 `https://xxxx.r6.cpolar.top` 的公网地址:

```properties
stripe.domain=https://xxxx.r6.cpolar.top
```

::: warning 注意
内网穿透地址每次重启会变化,需要同步更新本配置以及 Webhook 端点/`stripe listen` 的转发地址。
:::

## 实例化配置

```java {}
public StripeApiConfig getConfig() {
    StripeApiConfig config = new StripeApiConfig();
    config.setAppId(stripeBean.getAppId());
    config.setApiKey(stripeBean.getApiKey());
    config.setWebhookSecret(stripeBean.getWebhookSecret());
    config.setSandBox(stripeBean.getSandBox());
    StripeApiConfigKit.putApiConfig(config);
    return config;
}
```

多商户场景可按 appId 分别放入配置,通过 `StripeApiConfigKit.setThreadLocalApiConfig(config)` 切换当前线程使用的配置。

## 验证配置是否生效

配置完成后,重启应用并验证:

```bash
# 1. 直接用 curl 验证 apiKey 是否有效(返回 balance 对象即有效)
curl https://api.stripe.com/v1/balance -u sk_test_51xxx:

# 2. 启动 Demo 后访问配置回显接口
http://localhost/stripe/test

# 3. 启动 Webhook 转发
stripe listen --forward-to localhost:80/stripe/webhook
```

## 测试卡

沙箱支付请使用 Stripe 官方测试卡,金额均为虚拟交易:

| 卡号 | 场景 |
| --- | --- |
| `4242 4242 4242 4242` | 支付成功 |
| `4000 0000 0000 0002` | 支付被拒(declined) |
| `4000 0025 0000 3155` | 需要 3DS 验证(会弹出模拟 3DS 页面,点击 Complete) |

有效期填任意未来日期(如 12/30),CVC 与邮编任意。

更多卡号见 [Stripe 测试卡清单](https://docs.stripe.com/testing#cards)。

## 如何使用？

请参考 [JavaDoc 文档](https://apidoc.gitee.com/javen205/IJPay)或者看以下完整示例

## 完整示例

- [IJPay-Demo-SpringBoot](https://gitee.com/javen205/IJPay/tree/master/IJPay-Demo-SpringBoot)(`stripe.properties` 配置 + `/stripe.html` 演示页)

<Q url="tencent://message/?uin=572839485&Site=%E5%AE%A2%E6%9C%8D&Menu=yes" />
