package org.alfresco.utility.dsl;

/**
 * @author Paul Brodner
 *
 * Extend this class if you are implementing a protocol 
 * that can be used within one mapped network drive
 * 
 * see CifsWrapper.java protocol implementation
 */
public abstract class DSLProtocolWithNetworkDrive<Client> extends DSLProtocol<Client>
{
    private boolean actionExecutedOnMappedDrive = false;

    protected boolean isActionExecutedOnMappedDrive()
    {
        return actionExecutedOnMappedDrive;
    }
    
    protected void setActionExecutedOnMappedDrive(boolean actionExecutedOnMappedDrive)
    {
        this.actionExecutedOnMappedDrive = actionExecutedOnMappedDrive;
    }
    
    /**
     * Define the code for initializing the network drive using your Client protocol
     * 
     * public CifsWrapper usingNetworkDrive() throws Exception
        {
            if (!cifsNetworkDrive.inOSEnvironment().isNetworkDriveMounted())
                cifsNetworkDrive.inOSEnvironment().mount();
    
            setCurrentSpace(cifsNetworkDrive.inOSEnvironment().getLocalVolumePath());
            setActionExecutedOnMappedDrive(true);
            return this;
        }
     * @return
     * @throws Exception
     */
    public abstract Client usingNetworkDrive() throws Exception;
}
