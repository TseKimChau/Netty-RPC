package com.kimchau.rpc.common.bean;

/**
 * 封装 rpc 响应
 *
 * @author kimchau
 * @since 1.0.0
 */
public class RpcResponse {

    private String requestId;
    private int code;
    private String message;
    private Object data;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
