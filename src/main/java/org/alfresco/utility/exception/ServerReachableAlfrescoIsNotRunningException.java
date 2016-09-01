package org.alfresco.utility.exception;

import org.alfresco.utility.TasProperties;

public class ServerReachableAlfrescoIsNotRunningException extends Exception {
	private static final long serialVersionUID = 1L;

	public ServerReachableAlfrescoIsNotRunningException(TasProperties properties) {
		super(String.format("Server {} is reachabled, but Alfresco {} is NOT running.", properties.getServer(),
				properties.getFullServerUrl()));
	}
}
