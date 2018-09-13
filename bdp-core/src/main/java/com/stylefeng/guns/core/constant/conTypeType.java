package com.stylefeng.guns.core.constant;

/**
 * 任务类型的枚举
 *
 * @author zenith
 */
public enum conTypeType {

    RDBMS(0),
    FTP(1);

    int code;

    conTypeType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }



    public static conTypeType ObjOf(Integer status) {
        if (status == null) {
            return null;
        } else {
            for (conTypeType s : conTypeType.values()) {
                if (s.getCode() == status) {
                    return s;
                }
            }
            return null;
        }
    }
}
