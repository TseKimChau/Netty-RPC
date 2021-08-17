package com.kimchau.rpc.common.codec;

/**
 * 自定义传输协议（粘包拆包）
 *
 * @author kimchau
 * @since 1.0.0
 */
@Deprecated
public class MessageProtocol {

    private int length;

    private byte[] content;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
