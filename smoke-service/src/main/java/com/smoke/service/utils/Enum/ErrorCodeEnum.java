package com.smoke.service.utils.Enum;

public enum ErrorCodeEnum {


    /**
     * Uac 10011041 error code enum.
     */
    GL99990100(9999100, "参数异常"),

    GL404(9999404, "访问路径不存在"),

    GL9999417(9999417, "操作失败"),

    GL99990101(9999101, "token is invalid"),

    GL99990401(9999401, "无访问权限"),

    UAC10011041(10011041, "token已过期,请重新登录"),

    PUB10000001(10000001, "文件上传失败"),
    PUB10000006(10000006, "文件不能为空"),
    PUB10000008(10000008, "文件名格式不正确, 请使用后缀名为.XLSX的文件"),
    PUB10000009(10000009, "OneNet添加设备失败"),
    PUB10000010(10000010, "OneNet删除设备失败"),
    PUB10000011(10000011, "OneNet获取状态失败"),
    PUB10000000(10000100, "------");


    private int code;
    private String msg;

    /**
     * Msg string.
     *
     * @return the string
     */
    public String msg() {
        return msg;
    }

    /**
     * Code int.
     *
     * @return the int
     */
    public int code() {
        return code;
    }

    ErrorCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * Gets enum.
     *
     * @param code the code
     * @return the enum
     */
    public static ErrorCodeEnum getEnum(int code) {
        for (ErrorCodeEnum ele : ErrorCodeEnum.values()) {
            if (ele.code() == code) {
                return ele;
            }
        }
        return null;
    }

}
