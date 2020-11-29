package com.smoke.service.utils.Enum;

public enum CmdEnum {

    // 正常
    NORMAL(0),
    // 报警
    ALARM(1),
    // START NET LONG LINK
    START(3),
    // STOP NET LONG LINK
    STOP(4),
    // SOS
    SOS(4),
    // NO SOS
    NONE_SOS(5),
    // 测试
    TEST(7),
    // 空
    ENPTY(8);


    private final int code;

    CmdEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
