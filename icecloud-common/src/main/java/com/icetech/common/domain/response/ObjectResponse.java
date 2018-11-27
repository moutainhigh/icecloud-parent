package com.icetech.common.domain.response;

import lombok.Data;

@Data
public class ObjectResponse<T> extends Response {

    public ObjectResponse(String code, String msg){
        setCode(code);
        setMsg(msg);
    }

	public ObjectResponse() {
	}

	public ObjectResponse(String code, String msg, T data){
		setCode(code);
		setMsg(msg);
		setData(data);
	}
	
	private T data;

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

    @Override
    public String toString() {
        return "ObjectResponse{" +
                "code=" + super.getCode() +
                ",msg=" + super.getMsg() +
                ",data=" + data +
                '}';
    }
}
