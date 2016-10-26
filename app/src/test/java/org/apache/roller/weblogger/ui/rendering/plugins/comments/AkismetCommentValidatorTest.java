package org.apache.roller.weblogger.ui.rendering.plugins.comments;

import org.apache.roller.weblogger.pojos.Weblog;
import org.apache.roller.weblogger.pojos.WeblogEntry;
import org.apache.roller.weblogger.pojos.WeblogEntryComment;
import org.apache.roller.weblogger.util.RollerMessages;
import org.junit.Test;

import java.io.*;
import java.net.URLConnection;

import static org.junit.Assert.*;

public class AkismetCommentValidatorTest {


    @Test
    public void validate() {

        AkismetCommentValidator akismetCommentValidator = new AkismetCommentValidator() {


            @Override
            String getWeblogURL(Weblog website) {
                return "http://test.weblog.url";
            }

            @Override
            String getVersion() {
                return "1.0";
            }

            @Override
            OutputStream getOutputStream(URLConnection conn) throws IOException {
                return new ByteArrayOutputStream();
            }

            @Override
            InputStream getInputStream(URLConnection conn) throws IOException {
                return new ByteArrayInputStream("true".getBytes());
            }
        };
        WeblogEntryComment comment = new WeblogEntryComment();
        WeblogEntry weblogEntry = new WeblogEntry() {
            @Override
            public String getPermalink() {
                return "http://test.perma.link";
            }
        };
        comment.setWeblogEntry(weblogEntry);

        akismetCommentValidator.validate(comment, new RollerMessages());

    }

}
