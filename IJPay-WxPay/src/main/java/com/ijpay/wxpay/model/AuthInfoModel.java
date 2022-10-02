package com.ijpay.wxpay.model;

import com.ijpay.core.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 刷脸设备获取设备调用凭证
 *
 * @author Frank
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AuthInfoModel extends BaseModel {
	private String appid;
	private String mch_id;
	private String sub_appid;
	private String sub_mch_id;
	private String now;
	private String version;
	private String sign_type;
	private String nonce_str;
	private String store_id;
	private String store_name;
	private String device_id;
	private String rawdata;
	private String attach;
	private String sign;
}
