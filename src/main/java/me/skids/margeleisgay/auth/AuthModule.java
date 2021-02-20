package me.skids.margeleisgay.auth;

public interface AuthModule {
	abstract void onEnable();
	abstract void onDisable();
	abstract boolean run();
}
