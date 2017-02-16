package org.alfresco.utility.data;

import static org.alfresco.utility.report.log.Step.STEP;

import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;
import javax.mail.search.AndTerm;
import javax.mail.search.FlagTerm;
import javax.mail.search.SearchTerm;

import org.alfresco.utility.Utility;
import org.alfresco.utility.exception.TestConfigurationException;
import org.alfresco.utility.model.UserModel;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.testng.Assert;

@Service
@Scope(value = "prototype")
public class DataEmail extends TestData<DataEmail>
{
    private boolean found = false;
    private Folder folder = null;
    private Store store = null;

    /**
     * Helper method that connects to a IMAPS host {@code host} with the credentials from {@code userModel}
     */
    private void connectToHost(UserModel userModel, String host, int port, String protocol) throws Exception
    {
        STEP(String.format("DATAEMAIL: Connect to IMAP with %s/%s using port %d and host %s", userModel.getUsername(), userModel.getPassword(), port, host));

        Properties props = new Properties();
        props.setProperty("mail.store.protocol", protocol);

        Session session = Session.getDefaultInstance(props, null);

        store = session.getStore(protocol);

        try
        {
            store.connect(host, port, userModel.getUsername(), userModel.getPassword());
        }
        catch (MessagingException authEx)
        {
            LOG.info("User failed to connect to IMAP Server [{}], port [{}]", host, port);
            throw new TestConfigurationException(String.format("User failed to connect to IMAP server %s", authEx.getMessage()));
        }

        folder = store.getFolder("inbox");

        if(!folder.isOpen())
            folder.open(Folder.READ_WRITE);
    }

    /**
     * Helper method that searches in the current folder for messages that have the subject {@code subject}
     */
    private Message[] findMessagesBySubject(String subject) throws Exception
    {
        STEP(String.format("DATAEMAIL: Search for messages with subject '%s' in folder '%s'", subject, folder.getName()));

        SearchTerm subjectSearchTerm = new SearchTerm()
        {
            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            @Override
            public boolean match(Message message)
            {
                try
                {
                    if (message.getSubject().equals(subject))
                        return true;
                }
                catch (MessagingException me)
                {
                    me.printStackTrace();
                }
                return false;
            }
        };

        FlagTerm unseenFlagTerm = new FlagTerm(new Flags(Flags.Flag.SEEN), false);

        return folder.search(new AndTerm(subjectSearchTerm, unseenFlagTerm));
    }

    /**
     * Helper method that deletes the messages provided
     */
    private void deleteMessages() throws Exception
    {
        STEP(String.format("DATAEMAIL: Delete all messages from folder %s", folder.getName()));

        for (Message message : folder.getMessages())
            message.setFlag(Flags.Flag.DELETED, true);

        folder.close(true);
        folder.open(Folder.READ_WRITE);
    }

    /**
     * Helper method that closes the current folder and the store
     */
    private void closeResources() throws Exception
    {
        if (folder != null && folder.isOpen())
            folder.close(true);

        if (store != null)
            store.close();
    }

    /**
     * Assert that a message with subject {@code @subject} has been received and returns the message
     *
     * e.g. assertEmailHasBeenReceived(userModel, "imap.gmail.com", "imaps", messageSubject") will connect to "imap.gmail.com" with
     * the username and password from userModel, will search for a message with the subject "messageSubject", assert that it exists
     * and return the message
     */
    public Message[] assertEmailHasBeenReceived(UserModel userModel, String host, int port, String protocol, String subject) throws Exception
    {
        Message[] messages = null;
        ArrayList<Message> messageArrayList = new ArrayList<>();

        try
        {
            int retry = 0;

            connectToHost(userModel, host, port, protocol);

            while (retry < 15)
            {
                messages = findMessagesBySubject(subject);

                if (messages.length == 0)
                {
                    retry++;
                    Utility.waitToLoopTime(1);
                }
                else
                {
                    found = true;
                    break;
                }
            }

            Assert.assertTrue(found, String.format("Message with subject '%s' has not been found", subject));

            for (Message message:messages)
            {
                Message copyOfMessage = new MimeMessage((MimeMessage) message);
                messageArrayList.add(copyOfMessage);
            }

            return messageArrayList.toArray(messages);
        }
        finally
        {
            deleteMessages();
            closeResources();
        }
    }
}
