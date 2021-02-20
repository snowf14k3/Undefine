package me.skids.margeleisgay.auth.impl;

import cn.snowflake.rose.utils.auth.HWIDUtils;
import cn.snowflake.rose.utils.auth.HttpUtils;
import me.skids.margeleisgay.auth.AuthModule;

public class CheckHWID implements AuthModule {
	private String selfHWID;
	private String targetHWID;
	@Override
	public void onEnable() {
		selfHWID = HWIDUtils.getHWID();
		targetHWID = HttpUtils.httpRequest("https://gitee.com/cnsnowflake/seasonclient/raw/master/season/hwid.txt");
	}

	@Override
	public void onDisable() {
		
	}

	@Override
	public boolean run() {
		if(!targetHWID.contains(selfHWID)) {
			return false;
		}
		return true;
	}

}
