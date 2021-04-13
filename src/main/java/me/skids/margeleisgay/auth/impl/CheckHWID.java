package me.skids.margeleisgay.auth.impl;

import cn.snowflake.rose.NativeMethod;
import cn.snowflake.rose.utils.auth.HWIDUtils;
import cn.snowflake.rose.utils.auth.HttpUtils;
import cn.snowflake.rose.utils.auth.ShitUtil;
import me.skids.margeleisgay.auth.AuthModule;

public class CheckHWID implements AuthModule {
	public String selfHWID;
	public String targetHWID;

	public String getSelfHWID() {
		return selfHWID;
	}

	public String getTargetHWID() {
		return targetHWID;
	}

	@Override
	public void onEnable() {
		selfHWID = HWIDUtils.getHWID();
		targetHWID = NativeMethod.method1("https://snowflake.coding.net/p/hwid/d/season/git/raw/master/hardwareid.txt?download=true");
	}

	@Override
	public void onDisable() {
		
	}


	@Override
	public boolean run() {
		if(!targetHWID.contains(selfHWID) &&
				!(targetHWID.indexOf(selfHWID.toString()) > -1) &&
				!ShitUtil.contains(targetHWID,selfHWID)
		) {
			return false;
		}
		return true;
	}

}
