package org.alfresco.utility.exception;

import org.alfresco.utility.TasProperties;

public class ServerReachableAlfrescoIsNotRunningException extends Exception {
	private static final long serialVersionUID = 1L;

	public ServerReachableAlfrescoIsNotRunningException(TasProperties properties) {
		super(String.format("Server {%s} is reachabled, but Alfresco {%s} is NOT running.", properties.getServer(),
				properties.getFullServerUrl()));
	}
}
