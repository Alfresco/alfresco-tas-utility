package org.alfresco.utility.exception;

import org.alfresco.utility.TasProperties;

public class ServerUnreachableException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ServerUnreachableException(TasProperties properties) {
		super(String.format("Server {%s} is unreachable.", properties.getServer()));
	}
}
