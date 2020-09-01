package com.xue;

import java.util.ArrayList;

public class ModList<T> extends ArrayList<T> {
	
	public boolean needInterupt = true;
	@Override
	public boolean add(Object object) {
		boolean res = super.add((T) object);
		if (needInterupt) {
			Utils.logerror("mod add method");
			
			int index = -1;
			for(int i = 0; i < super.size(); i++) {
				if (super.get(i).getClass().getName().equals("cn.snowflake.rose.classTransformer.ClassTransformer")) {
					index = i;
					break;
				}
			}
			
			Utils.logerror("find index %s", index);
			
			Object myClassTransformer = null;
			if (index >= 0) {
				myClassTransformer = (Object) super.remove(index);
			}
			
			if (myClassTransformer != null) {
				super.add(super.size(), (T) myClassTransformer);
			}
			
			Utils.logerror("interupt add in mod list ");
			for(int i = 0; i < super.size(); i++) {
				Utils.logerror(super.get(i).toString());
			}
		}
		return res;
	}
}
