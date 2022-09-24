package com.ijpay.demo.entity;

import com.alibaba.fastjson.JSON;

/**
 * @author Javen
 */
public class H5SceneInfo {
	private H5 h5_info;

	public H5 getH5Info() {
		return h5_info;
	}

	public void setH5Info(H5 h5_info) {
		this.h5_info = h5_info;
	}


	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}


	public static class H5 {
		private String type;
		private String app_name;
		private String bundle_id;
		private String package_name;
		private String wap_url;
		private String wap_name;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getApp_name() {
			return app_name;
		}

		public void setApp_name(String app_name) {
			this.app_name = app_name;
		}

		public String getBundle_id() {
			return bundle_id;
		}

		public void setBundle_id(String bundle_id) {
			this.bundle_id = bundle_id;
		}

		public String getPackage_name() {
			return package_name;
		}

		public void setPackage_name(String package_name) {
			this.package_name = package_name;
		}

		public String getWap_url() {
			return wap_url;
		}

		public void setWap_url(String wap_url) {
			this.wap_url = wap_url;
		}

		public String getWap_name() {
			return wap_name;
		}

		public void setWap_name(String wap_name) {
			this.wap_name = wap_name;
		}
	}
}
