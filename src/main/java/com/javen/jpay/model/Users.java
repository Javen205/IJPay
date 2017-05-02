package com.javen.jpay.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

/**
 * 授权获取到的用户信息
 * @author Javen
 */
public class Users extends Model<Users> {

	private static final long serialVersionUID = 6204222383226990020L;
	
	static Log log = Log.getLog(Users.class);
	
	public static final Users me = new Users();
	
	public boolean save(String openId,String nickName,String unionid,String headimgurl,String country,String city,String province ,int sex){
		
		log.error("openId:"+openId+" nickName:"+nickName+" unionid:"+unionid+ " headimgurl:"+headimgurl+" country:"+country+" city:"+city+" province:"+province+" sex:"+sex);
		
		/**
		 * 1、判断openId 是否存在 
		 *    如果存在就update
		 *    如果不存在就保存
		 */
		Users user = findByOpenId(openId);
		if (user!=null) {
			user.set("nickName", nickName);
			user.set("unionid", unionid);
			user.set("headimgurl", headimgurl);
			user.set("country", country);
			user.set("city", city);
			user.set("province", province);
			user.set("sex", sex);
			user.set("updateTime", new Date());
			return user.update();
		}else {
			if (StrKit.notBlank(openId)) {
				Users me = new Users();
				me.set("openId", openId);
				me.set("nickName", nickName);
				me.set("unionid", unionid);
				me.set("headimgurl", headimgurl);
				me.set("country", country);
				me.set("city", city);
				me.set("province", province);
				me.set("sex", sex);
				me.set("updateTime", new Date());
				return me.save();
			}
		}
		return false;
	}
	
	public List<Users> getAll(){
		return me.find("select * from users");
	}

	/**
	 * 所有 sql 与业务逻辑写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
	 */
	public Page<Users> paginate(int pageNumber, int pageSize) {
		return paginate(pageNumber, pageSize, "select *", "from users order by id asc");
	}
	
	public Users findByOpenId(String openId){
		return this.findFirst("select * from users where openId=?", openId);
	}

	/**
	 * 根据map参数查找
	 * @param paras
	 * @return 
	 */
	public List<Users> findByMap(Map<String, Object> paras) {
		StringBuilder sql = new StringBuilder("select * from users ");
		if (paras.containsKey("order")) {
			sql.append(" ORDER BY ");
			sql.append(paras.get("order"));
			sql.append(" ");
		}
		if (paras.containsKey("limit")) {
			sql.append(" LIMIT ");
			sql.append(paras.get("limit"));
		}
		return this.find(sql.toString());
	}

}
