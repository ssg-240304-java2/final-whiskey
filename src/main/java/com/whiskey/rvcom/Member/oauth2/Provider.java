package com.whiskey.rvcom.Member.oauth2;
import com.whiskey.rvcom.entity.member.LoginType;
public enum Provider {
    BASIC, Naver, Google;

    public LoginType toLoginType() {
        switch (this) {
            case Naver:
                return LoginType.NAVER;
            case Google:
                return LoginType.GOOGLE;
            default:
                return LoginType.BASIC;
        }
    }
}
