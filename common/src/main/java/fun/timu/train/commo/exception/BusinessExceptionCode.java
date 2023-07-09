package fun.timu.train.commo.exception;

public enum BusinessExceptionCode {

    MEMBER_MOBILE_EXIST("手机号已注册");

    private String desc;

    BusinessExceptionCode(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
