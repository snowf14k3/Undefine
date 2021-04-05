package me.skids.margeleisgay.auth.impl;

import cn.snowflake.rose.utils.auth.HWIDUtils;
import cn.snowflake.rose.utils.auth.HttpUtils;
import cn.snowflake.rose.utils.auth.ShitUtil;
import me.skids.margeleisgay.auth.AuthModule;

public class CheckHWID implements AuthModule {
	private String selfHWID;
	private String targetHWID;

	public String getSelfHWID() {
		return selfHWID;
	}

	public String getTargetHWID() {
		return targetHWID;
	}

	@Override
	public void onEnable() {
		selfHWID = HWIDUtils.getHWID();
		targetHWID = HttpUtils.httpRequest("https://snowflake.coding.net/p/hwid/d/season/git/raw/master/hardwareid.txt?download=true");
	}

	@Override
	public void onDisable() {
		
	}
	public boolean method1(){
		if(!targetHWID.contains(selfHWID) &&
				!(targetHWID.indexOf(selfHWID.toString()) > -1) &&
				!ShitUtil.contains(targetHWID,selfHWID)
		) {
			return false;
		}
		return true;
	}

	@Override
	public boolean run() {
		return method1();
	}

}
