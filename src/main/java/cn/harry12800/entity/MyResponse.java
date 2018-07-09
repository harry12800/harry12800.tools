package cn.harry12800.entity;

/**
 * 
 * @author Administrator
 *
 */
public class MyResponse {

	public int code;
	public String msg;
	public Object content;

	public int getCode() {
		return code;
	}

	public MyResponse setCode(int code) {
		this.code = code;
		return this;
	}

	public String getMsg() {
		return msg;
	}

	public MyResponse setMsg(String msg) {
		this.msg = msg;
		return this;
	}

	public Object getContent() {
		return content;
	}

	public MyResponse setContent(Object content) {
		this.content = content;
		return this;
	}

	public static MyResponse newOk() {
		return new MyResponse().setCode(EResponseCode.OK.code).setMsg(EResponseCode.OK.msg);
	}

	public static MyResponse newBad() {
		return new MyResponse().setCode(EResponseCode.BAD.code).setMsg(EResponseCode.BAD.msg);
	}

	public static MyResponse newNotFound() {
		return new MyResponse().setCode(EResponseCode.NOT_FOUND.code).setMsg(EResponseCode.NOT_FOUND.msg);
	}

	public static MyResponse newServerError() {
		return new MyResponse().setCode(EResponseCode.SERVER_ERROR.code).setMsg(EResponseCode.SERVER_ERROR.msg);
	}

	public static MyResponse newByResponseCode(EResponseCode code) {
		return new MyResponse().setCode(code.code).setMsg(code.msg);
	}

	public static enum EResponseCode {
		OK(10, "OK"), BAD(11, "BAD"), NOT_FOUND(12, "NOT_FOUND"), SERVER_ERROR(13, "SERVER_ERROR");
		EResponseCode(int code, String msg) {
			this.code = code;
			this.msg = msg;
		}

		public int code;
		public String msg;
	}

	@Override
	public String toString() {
		return "MyResponse [code=" + code + ", msg=" + msg + ", content=" + content + "]";
	}
	
}
