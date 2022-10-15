package com.portfolio.gascharge.error.exception.enums;

public class NotFoundMatchedEnum extends RuntimeException {

    public NotFoundMatchedEnum() {super("일치하는 Enum 값을 찾을 수 없습니다.");}

    public NotFoundMatchedEnum(Class clazz, String code) {super(code + " 값과 매치되는" + clazz.getSimpleName() + " 이름의 Enum 을 찾을 수 없습니다");}
}
