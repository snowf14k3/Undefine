package me.skids.margeleisgay.auth.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import me.skids.margeleisgay.auth.AuthModule;

public class CheckVMProcess implements AuthModule {
	private ArrayList<String> targetProcess;
	private Process process = null;
	@Override
	public void onEnable() {
		targetProcess = new ArrayList<>();
		targetProcess.add("vmware.exe");
		targetProcess.add("VBoxService.exe");
		try {
			process = Runtime.getRuntime().exec("cmd.exe /c tasklist");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDisable() {
		process = null;
	}

	@Override
	public boolean run() {
		try {
		BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line = new String();
			while ((line = input.readLine()) != null) {
				for (String target : targetProcess) {
					if(line.contains(target)) {
						return false;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

}
