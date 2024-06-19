package org.web.dto;

public class Result<T> {
    private Integer returnCode; //自定義與前端溝通(包含狀態、想拿取的資料)

    private T data;

    public Integer getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(Integer returnCode) {
        this.returnCode = returnCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Result() {
    }

    public Result(Integer returnCode) {
        this.returnCode = returnCode;
    }

    public Result(Integer returnCode, T data) {
        this.returnCode = returnCode;
        this.data = data;
    }
}
