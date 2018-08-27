package com.stylefeng.guns.core.constant;

/**
 * 任务类型的枚举
 *
 * @author zenith
 */
public enum JobType {

    INPUT(1, "数据接入","input_data.html"),
    SQL(2, "SQL计算","sql_exec.html"),
    PROC(3, "程序执行","proc_exec.html"),
    OUTPUT(4, "数据推送","output_data.html");

    int code;
    String name;
    String page;

    JobType(int code, String message,String page) {
        this.code = code;
        this.name = message;
        this.page=page;
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

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }



    public static JobType ObjOf(Integer status) {
        if (status == null) {
            return null;
        } else {
            for (JobType s : JobType.values()) {
                if (s.getCode() == status) {
                    return s;
                }
            }
            return null;
        }
    }
}
