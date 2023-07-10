package fun.timu.train.member.enums;

import lombok.Getter;

public enum PassengerTypeEnum {

    ADULT("1", "成人"),
    CHILD("2", "儿童"),
    STUDENT("3", "学生");

    @Getter
    private final String code;
    @Getter
    private final String desc;

    PassengerTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
