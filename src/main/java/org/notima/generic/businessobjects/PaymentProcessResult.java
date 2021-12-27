package org.notima.generic.businessobjects;

public class PaymentProcessResult {

	public enum ResultCode {
		NOT_PROCESSED,
		OK,
		OK_WITH_WARNING,
		OK_WITH_RETRY_RECORDS,
		FAILED
	}

	private ResultCode	resultCode = ResultCode.NOT_PROCESSED;
	
	private StringBuffer	textResult;

	public PaymentProcessResult() {};
	
	public PaymentProcessResult(ResultCode code) {
		resultCode = code;
	}
	
	public ResultCode getResultCode() {
		return resultCode;
	}

	public void setResultCode(ResultCode resultCode) {
		this.resultCode = resultCode;
	}

	public StringBuffer getTextResult() {
		return textResult;
	}

	public void setTextResult(StringBuffer textResult) {
		this.textResult = textResult;
	}
	
	
	
}
