package com.ijpay.wxpay.model.v3;

import com.ijpay.core.model.BaseModel;
import lombok.*;
import lombok.experimental.Accessors;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CloseOrderModel extends BaseModel {
    private String appid;
    private String sub_appid;
    private String mchid;
    private String sub_mchid;
    private String out_trade_no;
    private String nonce_str;
    private String sign;
    private String sign_type;


}
