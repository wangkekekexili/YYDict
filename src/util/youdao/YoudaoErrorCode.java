package util.youdao;

public enum YoudaoErrorCode {
	NORMAL(0, "normal"),
	INPUT_TOO_LONG(20, "input too long"),
	PROCESS_ERROR(30, "process error"),
	UNSUPPORTED_LANGUAGE(40, "unsupported language"),
	INVALID_API_KEY(50, "invalid api key");
	
	private int errorCode;
	private String description;
	
	private YoudaoErrorCode(int errorCode, String descripion) {
		this.errorCode = errorCode;
		this.description = descripion;
	}
	
	public int getErrorCode() {
		return errorCode;
	}
	
	public String getDescription() {
		return description;
	}
	
	@Override
	public String toString() {
		return Integer.toString(errorCode) + ": " + description;
	}
	
}
