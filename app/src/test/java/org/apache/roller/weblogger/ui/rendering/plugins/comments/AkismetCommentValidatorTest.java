package org.apache.roller.weblogger.ui.rendering.plugins.comments;

import org.apache.roller.weblogger.pojos.Weblog;
import org.apache.roller.weblogger.pojos.WeblogEntry;
import org.apache.roller.weblogger.pojos.WeblogEntryComment;
import org.apache.roller.weblogger.util.RollerMessages;
import org.junit.Test;

import java.io.*;
import java.net.URLConnection;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("RefusedBequest")
public class AkismetCommentValidatorTest {

    @Test
    public void validate_blockedComment() throws UnsupportedEncodingException {

        //arrange
        final Weblog weblog = new Weblog();
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        final AkismetCommentValidator validator = new AkismetCommentValidator("apiKeyValue", null) {

            @Override
            String getWeblogURL(Weblog website) {
                return "http://weblogurl.com";
            }

            @Override
            String getWebLoggerVersion() {
                return "1.0";
            }

            @Override
            OutputStream getOutputStreamFrom(URLConnection conn) throws IOException {
                return outputStream;
            }

            @Override
            InputStream getInputStreamFrom(URLConnection conn) throws IOException {
                return new ByteArrayInputStream(("true").getBytes());
            }
        };
        final WeblogEntryComment comment = new WeblogEntryComment();
        comment.setWeblogEntry(new WeblogEntry() {
            @Override
            public String getPermalink() {
                return "http://permalink.com";
            }

            @Override
            public Weblog getWebsite() {
                return weblog;
            }
        });
        final RollerMessages messages = new RollerMessages();

        // act
        final int retVal = validator.validate(comment, messages);

        //assert
        assertEquals(0, retVal);
    }

    @Test
    public void validate_nonblockedComment() throws UnsupportedEncodingException {

        //arrange
        final Weblog weblog = new Weblog();
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        final AkismetCommentValidator validator = new AkismetCommentValidator("apiKeyValue", null) {

            @Override
            String getWeblogURL(Weblog website) {
                return "http://weblogurl.com";
            }

            @Override
            String getWebLoggerVersion() {
                return "1.0";
            }

            @Override
            OutputStream getOutputStreamFrom(URLConnection conn) throws IOException {
                return outputStream;
            }

            @Override
            InputStream getInputStreamFrom(URLConnection conn) throws IOException {
                return new ByteArrayInputStream(("false").getBytes());
            }
        };
        final WeblogEntryComment comment = new WeblogEntryComment();
        comment.setWeblogEntry(new WeblogEntry() {
            @Override
            public String getPermalink() {
                return "http://permalink.com";
            }

            @Override
            public Weblog getWebsite() {
                return weblog;
            }
        });
        final RollerMessages messages = new RollerMessages();

        // act
        final int retVal = validator.validate(comment, messages);

        //assert
        assertEquals(100, retVal);
     }

    @Test
    public void validate_exceptionWasThrown() throws UnsupportedEncodingException {

        //arrange
        final Weblog weblog = new Weblog();
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        final AkismetCommentValidator validator = new AkismetCommentValidator("apiKeyValue", null) {

            @Override
            String getWeblogURL(Weblog website) {
                return "http://weblogurl.com";
            }

            @Override
            String getWebLoggerVersion() {
                return "1.0";
            }

            @Override
            OutputStream getOutputStreamFrom(URLConnection conn) throws IOException {
                return outputStream;
            }

            @Override
            InputStream getInputStreamFrom(URLConnection conn) throws IOException {
                throw new IOException("Simulated");
            }
        };
        final WeblogEntryComment comment = new WeblogEntryComment();
        comment.setWeblogEntry(new WeblogEntry() {
            @Override
            public String getPermalink() {
                return "http://permalink.com";
            }

            @Override
            public Weblog getWebsite() {
                return weblog;
            }
        });
        final RollerMessages messages = new RollerMessages();

        // act
        final int retVal = validator.validate(comment, messages);

        //assert
        assertEquals(0, retVal);
     }

}
