package sample.cafekiosk.spring.api;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class ApiResponse<T> {

	private int code;
	private HttpStatus status;
	private String message;
	private T data;

	public ApiResponse(final HttpStatus status, final String message, final T data) {
		this.code = status.value();
		this.status = status;
		this.message = message;
		this.data = data;
	}

	public static <T> ApiResponse<T> of(final HttpStatus status, String message, final T data) {
		return new ApiResponse<>(status, message, data);
	}

	public static <T> ApiResponse<T> of(final HttpStatus status, final T data) {
		return new ApiResponse<>(status, status.name(), data);
	}

	public static <T> ApiResponse<T> ok(final T data) {
		return of(HttpStatus.OK, data);
	}

}
