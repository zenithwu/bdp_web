package com.stylefeng.guns.core.constant;

/**
 * 任务状态的枚举
 *
 * @author zenith
 */
public enum JobStatus {

    INPUT(0, "启用"),
    SQL(1, "禁用");

    int code;
    String name;

    JobStatus(int code, String message) {
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


    public static JobStatus ObjOf(Integer status) {
        if (status == null) {
            status=0;
        }
        for (JobStatus s : JobStatus.values()) {
            if (s.getCode() == status) {
                return s;
            }
        }
        return null;
    }
}
