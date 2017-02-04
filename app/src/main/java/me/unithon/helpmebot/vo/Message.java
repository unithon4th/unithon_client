package me.unithon.helpmebot.vo;

/**
 * Created by Kim on 2017-02-04.
 */

public class Message {
    /**
     * 메세지 유형
     * 0 : 유저
     * 1 : 텍스트
     * 2 : 사진
     */
    private int type;
    // 메세지 내용
    private String message;

    public Message(int type, String message) {
        this.type = type;
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
