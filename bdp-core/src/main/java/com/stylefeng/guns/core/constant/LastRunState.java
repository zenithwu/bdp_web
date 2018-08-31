package com.stylefeng.guns.core.constant;

/**
 * 最后运行状态枚举类
 * @author Administrator
 *
 */
public enum LastRunState {
	
	  FAIL(0, "失败"),
	  	SUCCESS(1, "成功"),
	    RUNNING(2, "运行中");
		
	    int code;
	    String name;

	    LastRunState(int code, String message) {
	        this.code = code;
	        this.name = message;
	    }

	    public int getCode() {
	        return code;
	    }

	    public void setCode(int code) {
	        this.code = code;
	    }

	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }


	    public static LastRunState ObjOf(Integer status) {
	        if (status == null) {
	            status = 1;
	        }
	        for (LastRunState s : LastRunState.values()) {
	            if (s.getCode() == status) {
	                return s;
	            }
	        }
	        return null;
	    }
}
