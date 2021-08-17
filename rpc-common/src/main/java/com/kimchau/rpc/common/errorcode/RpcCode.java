package com.kimchau.rpc.common.errorcode;

import org.apache.http.HttpStatus;

/**
 * RPC 错误码
 *
 * @author kimchau
 * @since 1.0.0
 */
public enum RpcCode {

    /**
     * HTTP Mapping: 200 OK
     * @description 无错误。
     */
    OK(0, "OK", HttpStatus.SC_OK),
    /**
     * HTTP Mapping: 499 Client Closed Request
     * @description 请求被客户端取消。
     */
    // CANCELLED(1, "The operation was cancelled, typically by the caller.", HttpStatus.BAD_REQUEST),
    /**
     * HTTP Mapping: 500 Internal Server Error
     * @description 出现未知的服务器错误。通常是服务器错误。
     */
    UNKNOWN(2, "Unknown error",HttpStatus.SC_INTERNAL_SERVER_ERROR),
    /**
     * HTTP Mapping: 400 Bad Request
     * @description 客户端指定了无效参数。
     */
    INVALID_ARGUMENT(3, "The client specified an invalid argument",HttpStatus.SC_BAD_REQUEST),
    /**
     * HTTP Mapping: 504 Gateway Timeout
     * @description 超出请求时限。仅当调用者设置的时限比方法的默认时限短（即请求的时限不足以让服务器处理请求）并且请求未在时限范围内完成时，才会发生这种情况。
     */
    DEADLINE_EXCEEDED(4, "The deadline expired before the operation could complete",HttpStatus.SC_GATEWAY_TIMEOUT),
    /**
     * HTTP Mapping: 404 Not Found
     * @description 找不到指定的资源，或者请求由于未公开的原因（例如白名单）而被拒绝。
     */
    NOT_FOUND(5, "Some requested entity (e.g., file or directory) was not found",HttpStatus.SC_NOT_FOUND),
    /**
     * HTTP Mapping: 409 Conflict
     * @description 客户端尝试创建的资源已存在。
     */
    ALREADY_EXISTS(6, "The entity that a client attempted to create (e.g., file or directory) already exists",HttpStatus.SC_CONFLICT),
    /**
     * HTTP Mapping: 403 Forbidden
     * @description 客户端权限不足。可能的原因包括 OAuth 令牌的覆盖范围不正确、客户端没有权限或者尚未为客户端项目启用 API。
     */
    PERMISSION_DENIED(7, "The caller does not have permission to execute the specified operation",HttpStatus.SC_FORBIDDEN),
    /**
     * HTTP Mapping: 401 Unauthorized
     * @description 由于 OAuth 令牌丢失、无效或过期，请求未通过身份验证。
     */
    UNAUTHENTICATED(16, "The request does not have valid authentication credentials for the operation",HttpStatus.SC_UNAUTHORIZED),
    /**
     * HTTP Mapping: 429 Too Many Requests
     * @description 资源配额不足或达到速率限制。
     */
    RESOURCE_EXHAUSTED(8, "Some resource has been exhausted, perhaps a per-user quota, or perhaps the entire file system is out of space",429),
    /**
     * HTTP Mapping: 400 Bad Request
     * @description 请求无法在当前系统状态下执行，例如删除非空目录。
     */
    FAILED_PRECONDITION(9, "The operation was rejected because the system is not in a state required for the operation's execution",HttpStatus.SC_BAD_REQUEST),
    /**
     * HTTP Mapping: 409 Conflict
     * @description 并发冲突，例如读取/修改/写入冲突。
     */
    ABORTED(10, "The operation was aborted, typically due to a concurrency issue such as a sequencer check failure or transaction abort",HttpStatus.SC_CONFLICT),
    /**
     * HTTP Mapping: 400 Bad Request
     * @description 客户端指定了无效范围。
     */
    OUT_OF_RANGE(11, "The operation was attempted past the valid range",HttpStatus.SC_BAD_REQUEST),
    /**
     * HTTP Mapping: 501 Not Implemented
     * @description API 方法未通过服务器实现。
     */
    UNIMPLEMENTED(12, "The operation is not implemented or is not supported/enabled in this service",HttpStatus.SC_NOT_IMPLEMENTED),
    /**
     * HTTP Mapping: 500 Internal Server Error
     * @description 出现内部服务器错误。通常是服务器错误。
     */
    INTERNAL(13, "Internal errors",HttpStatus.SC_INTERNAL_SERVER_ERROR),
    /**
     * HTTP Mapping: 503 Service Unavailable
     * @description 服务不可用。通常是服务器已关闭。
     */
    UNAVAILABLE(14, "The service is currently unavailable",HttpStatus.SC_SERVICE_UNAVAILABLE),
    /**
     * HTTP Mapping: 500 Internal Server Error
     * @description 出现不可恢复的数据丢失或数据损坏。客户端应该向用户报告错误。
     */
    DATA_LOSS(15, "Unrecoverable data loss or corruption",HttpStatus.SC_INTERNAL_SERVER_ERROR)
    ;

    private final int code;
    private final String message;
    private final int httpCode;

    RpcCode(int code, String message, int httpCode) {
        this.code = code;
        this.message = message;
        this.httpCode = httpCode;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public int getHttpCode() {
        return httpCode;
    }
}
