package org.alfresco.utility.data;

import org.alfresco.utility.Utility;
import org.alfresco.utility.model.UserModel;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.testng.Assert;

import javax.mail.*;
import javax.mail.search.SearchTerm;
import java.util.Properties;

import static org.alfresco.utility.report.log.Step.STEP;

@Service
@Scope(value = "prototype")
public class DataEmail extends TestData<DataEmail>
{
    private boolean found = false;
    private Folder folder = null;
    private Store store = null;
    private Message[] messages;

    /**
     * Helper method that connects to a IMAPS host {@code host} with the credentials from {@code userModel}
     */
    private void connectToHost(UserModel userModel, String host) throws Exception
    {
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imaps");

        Session session = Session.getDefaultInstance(props, null);

        store = session.getStore("imaps");
        store.connect(host, userModel.getUsername(), userModel.getPassword());

        folder = store.getFolder("inbox");

        if(!folder.isOpen())
            folder.open(Folder.READ_WRITE);
    }

    /**
     * Helper method that searches in the current folder for messages that have the subject {@code subject}
     */
    private Message[] findMessagesBySubject(String subject) throws Exception
    {
        SearchTerm searchTerm = new SearchTerm() {
            @Override
            public boolean match(Message message) {
                try {
                    if (message.getSubject().equals(subject)) {
                        return true;
                    }
                } catch (MessagingException ex) {
                    ex.printStackTrace();
                }
                return false;
            }
        };

        return folder.search(searchTerm);
    }

    /**
     * Helper method that deletes the messages provided
     */
    private void deleteMessages(Message[] messages) throws Exception
    {
        for (Message message : messages)
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
     * e.g. assertEmailHasBeenReceived(userModel, "imap.gmail.com", "messageSubject") will conect to "imap.gmail.com" with
     * the username and password from userModel, will search for a message with the subject "messageSubject", assert that it exists
     * and return the message
     */
    public Message[] assertEmailHasBeenReceived(UserModel userModel, String host, String subject) throws Exception
    {
        try
        {
            int retry = 0;

            connectToHost(userModel, host);

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

            return messages;
        }
        finally
        {
            deleteMessages(messages);
            closeResources();
        }
    }
}
