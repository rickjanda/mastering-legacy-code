package org.apache.roller.weblogger.ui.rendering.plugins.comments;

import org.apache.roller.weblogger.pojos.Weblog;
import org.apache.roller.weblogger.pojos.WeblogEntry;
import org.apache.roller.weblogger.pojos.WeblogEntryComment;
import org.apache.roller.weblogger.util.RollerMessages;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;

import java.io.*;
import java.net.URLConnection;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@SuppressWarnings("RefusedBequest")
public class AkismetCommentValidatorTest {

    @Test
    public void validate_blockedComment() throws IOException {

        //arrange
        final Weblog weblog = new Weblog();
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        final AkismetCommentValidator validator = spy(new AkismetCommentValidator("apiKeyValue", null) );

        final ByteArrayInputStream streamWithTrue = new ByteArrayInputStream("true".getBytes());
        doReturn(streamWithTrue).when(validator).getInputStreamFrom(Matchers.<URLConnection>anyObject());
        doReturn(outputStream).when(validator).getOutputStreamFrom(Matchers.<URLConnection>anyObject());
        doReturn("1.0").when(validator).getWebLoggerVersion();
        doReturn("http://weblogurl.com").when(validator).getWeblogURL(weblog);

        final WeblogEntryComment comment = new WeblogEntryComment();
        final WeblogEntry weblogEntry = spy(new WeblogEntry());
        doReturn("http://permalink.com").when(weblogEntry).getPermalink();
        doReturn(weblog).when(weblogEntry).getWebsite();
        comment.setWeblogEntry(weblogEntry);

        final RollerMessages messages = new RollerMessages();

        //act
        final int retVal = validator.validate(comment, messages);

        //assert
        assertEquals(0, retVal);

        assertEquals("blog=http://weblogurl.com&user_ip=null&user_agent=null&referrer=null&" +
                        "permalink=http://permalink.com&comment_type=comment&comment_author=null&" +
                        "comment_author_email=null&comment_author_url=null&comment_content=null",
                outputStream.toString("UTF-8"));
        assertEquals("comment.validator.akismetMessage : ", messages.toString());

        ArgumentCaptor<URLConnection> urlConnectionCaptor = ArgumentCaptor.forClass(URLConnection.class);
        verify(validator).getInputStreamFrom(urlConnectionCaptor.capture());
        assertEquals("http://apiKeyValue.rest.akismet.com/1.1/comment-check", urlConnectionCaptor.getValue().getURL().toString());

        verify(validator).getWeblogURL(weblog);
    }

    @Test
    public void validate_nonblockedComment() throws IOException {

        //arrange
        final Weblog weblog = new Weblog();
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        final AkismetCommentValidator validator = spy(new AkismetCommentValidator("apiKeyValue", null) );

        final ByteArrayInputStream streamWithFalse = new ByteArrayInputStream("false".getBytes());
        doReturn(streamWithFalse).when(validator).getInputStreamFrom(Matchers.<URLConnection>anyObject());
        doReturn(outputStream).when(validator).getOutputStreamFrom(Matchers.<URLConnection>anyObject());
        doReturn("1.0").when(validator).getWebLoggerVersion();
        doReturn("http://weblogurl.com").when(validator).getWeblogURL(weblog);

        final WeblogEntryComment comment = new WeblogEntryComment();
        final WeblogEntry weblogEntry = spy(new WeblogEntry());
        doReturn("http://permalink.com").when(weblogEntry).getPermalink();
        doReturn(weblog).when(weblogEntry).getWebsite();
        comment.setWeblogEntry(weblogEntry);

        final RollerMessages messages = new RollerMessages();

        //act
        final int retVal = validator.validate(comment, messages);

        //assert
        assertEquals(100, retVal);

        assertEquals("blog=http://weblogurl.com&user_ip=null&user_agent=null&referrer=null&" +
                        "permalink=http://permalink.com&comment_type=comment&comment_author=null&" +
                        "comment_author_email=null&comment_author_url=null&comment_content=null",
                outputStream.toString("UTF-8"));
        assertEquals("", messages.toString());

        ArgumentCaptor<URLConnection> urlConnectionCaptor = ArgumentCaptor.forClass(URLConnection.class);
        verify(validator).getInputStreamFrom(urlConnectionCaptor.capture());
        assertEquals("http://apiKeyValue.rest.akismet.com/1.1/comment-check", urlConnectionCaptor.getValue().getURL().toString());

        verify(validator).getWeblogURL(weblog);
    }

    @Test
    public void validate_exceptionWasThrown() throws IOException {

        //arrange
        final Weblog weblog = new Weblog();
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        final AkismetCommentValidator validator = spy(new AkismetCommentValidator("apiKeyValue", null) );

        doThrow(new IOException("simulated")).when(validator).getInputStreamFrom(Matchers.<URLConnection>anyObject());
        doReturn(outputStream).when(validator).getOutputStreamFrom(Matchers.<URLConnection>anyObject());
        doReturn("1.0").when(validator).getWebLoggerVersion();
        doReturn("http://weblogurl.com").when(validator).getWeblogURL(weblog);

        final WeblogEntryComment comment = new WeblogEntryComment();
        final WeblogEntry weblogEntry = spy(new WeblogEntry());
        doReturn("http://permalink.com").when(weblogEntry).getPermalink();
        doReturn(weblog).when(weblogEntry).getWebsite();
        comment.setWeblogEntry(weblogEntry);

        final RollerMessages messages = new RollerMessages();

        //act
        final int retVal = validator.validate(comment, messages);

        //assert
        assertEquals(0, retVal);
    }
}
