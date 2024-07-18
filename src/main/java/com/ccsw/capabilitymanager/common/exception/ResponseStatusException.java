package com.ccsw.capabilitymanager.common.exception;

import java.util.Collections;
import java.util.Map;

import org.springframework.core.NestedExceptionUtils;
import org.springframework.core.NestedRuntimeException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import com.ccsw.capabilitymanager.dataimport.model.ImportResponseDto;

/**
 * Base class for exceptions associated with specific HTTP response status codes.
 *
 * @author Rossen Stoyanchev
 * @author Juergen Hoeller
 * @since 5.0
 */
@SuppressWarnings("serial")
public class ResponseStatusException extends NestedRuntimeException {

	private final int status;

	@Nullable
	private final ImportResponseDto reason;



	/**
	 * Constructor with a response status and a reason to add to the exception
	 * message as explanation.
	 * @param status the HTTP status (required)
	 * @param reason the associated reason (optional)
	 */
	public ResponseStatusException(HttpStatus status, @Nullable ImportResponseDto reason) {
		super("");
		Assert.notNull(status, "HttpStatus is required");
		this.status = status.value();
		this.reason = reason;
	}
	/**
	 * Constructor with a response status and a reason to add to the exception
	 * message as explanation.
	 * @param status the HTTP status (required)
	 * @param reason the associated reason (optional)
	 */
	public ResponseStatusException(HttpStatus status, @Nullable String reasonString) {
		super("");
		Assert.notNull(status, "HttpStatus is required");
    	ImportResponseDto reason = new ImportResponseDto();
    	reason.setStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    	reason.setError(reasonString);
		reason.setMessage(reasonString);
		this.status = status.value();
		this.reason = reason;
	}

	/**
	 * Constructor with a response status and a reason to add to the exception
	 * message as explanation, as well as a nested exception.
	 * @param rawStatusCode the HTTP status code value
	 * @param reason the associated reason (optional)
	 * @param cause a nested exception (optional)
	 * @since 5.3
	 */
	public ResponseStatusException(int rawStatusCode, @Nullable ImportResponseDto reason, @Nullable Throwable cause) {
		super(null, cause);
		this.status = rawStatusCode;
		this.reason = reason;
	}


	/**
	 * Return the HTTP status associated with this exception.
	 * @throws IllegalArgumentException in case of an unknown HTTP status code
	 * @since #getRawStatusCode()
	 * @see HttpStatus#valueOf(int)
	 */
	public HttpStatus getStatus() {
		return HttpStatus.valueOf(this.status);
	}

	/**
	 * Return the HTTP status code (potentially non-standard and not resolvable
	 * through the {@link HttpStatus} enum) as an integer.
	 * @return the HTTP status as an integer value
	 * @since 5.3
	 * @see #getStatus()
	 * @see HttpStatus#resolve(int)
	 */
	public int getRawStatusCode() {
		return this.status;
	}

	/**
	 * Return headers associated with the exception that should be added to the
	 * error response, e.g. "Allow", "Accept", etc.
	 * <p>The default implementation in this class returns an empty map.
	 * @since 5.1.11
	 * @deprecated as of 5.1.13 in favor of {@link #getResponseHeaders()}
	 */
	@Deprecated
	public Map<String, String> getHeaders() {
		return Collections.emptyMap();
	}

	/**
	 * Return headers associated with the exception that should be added to the
	 * error response, e.g. "Allow", "Accept", etc.
	 * <p>The default implementation in this class returns empty headers.
	 * @since 5.1.13
	 */
	public HttpHeaders getResponseHeaders() {
		Map<String, String> headers = getHeaders();
		if (headers.isEmpty()) {
			return HttpHeaders.EMPTY;
		}
		HttpHeaders result = new HttpHeaders();
		getHeaders().forEach(result::add);
		return result;
	}

	/**
	 * The reason explaining the exception (potentially {@code null} or empty).
	 */
	@Nullable
	public ImportResponseDto getReason() {
		return this.reason;
	}


	@SuppressWarnings("deprecation")
	@Override
	public String getMessage() {
		HttpStatus code = HttpStatus.resolve(this.status);
		String msg =  this.reason.getError();
		return NestedExceptionUtils.buildMessage(msg, getCause());
	}

}