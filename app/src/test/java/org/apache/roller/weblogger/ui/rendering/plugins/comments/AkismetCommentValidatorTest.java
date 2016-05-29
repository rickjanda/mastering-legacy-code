package org.apache.roller.weblogger.ui.rendering.plugins.comments;

import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.apache.roller.util.RollerConstants;
import org.apache.roller.weblogger.business.URLStrategy;
import org.apache.roller.weblogger.business.Weblogger;
import org.apache.roller.weblogger.business.WebloggerFactory;
import org.apache.roller.weblogger.config.WebloggerConfig;
import org.apache.roller.weblogger.pojos.Weblog;
import org.apache.roller.weblogger.pojos.WeblogEntry;
import org.apache.roller.weblogger.pojos.WeblogEntryComment;
import org.apache.roller.weblogger.util.RollerMessages;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

@RunWith(JMockit.class)
public class AkismetCommentValidatorTest {

    @Mocked ResourceBundle resourceBundle;

    @Mocked(stubOutClassInitialization = true) WebloggerConfig webloggerConfig;

    @Mocked(stubOutClassInitialization = true) WebloggerFactory webloggerFactory;

    @Mocked URL url;
    @Mocked Weblog weblog;

    @Test
    public void getName() {

        new Expectations() {{
            WebloggerConfig.getProperty(anyString);
            result = "anApiKey";
            resourceBundle.getString(anyString);
            result = "aName";
        }};

        String actualName = new AkismetCommentValidator().getName();

        assertEquals("aName", actualName);
    }


    @Test
    public void validate_Exception_during_connetion_opening() throws IOException {

        new Expectations() {{
            WebloggerConfig.getProperty(anyString);
            result = "anApiKey";
            WebloggerFactory.getWeblogger().getUrlStrategy().getWeblogURL(weblog, null, true);
            result = "http://any.url";
            url = new URL("http://anApiKey.rest.akismet.com/1.1/comment-check");
            url.openConnection().getOutputStream();
            result = new Exception("Simulated connection error");
        }};

        WeblogEntryComment comment = new WeblogEntryComment();
        WeblogEntry entry = new WeblogEntry();
        comment.setWeblogEntry(entry);
        entry.setWebsite(weblog);
        int actual = new AkismetCommentValidator().validate(comment, null);
        assertEquals(0, actual);
    }


    @Test
    public void validate_akismet_returns_false() throws IOException {

        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        new Expectations() {{
            WebloggerConfig.getProperty(anyString);
            result = "anApiKey";
            WebloggerFactory.getWeblogger().getUrlStrategy().getWeblogURL(weblog, anyString, anyBoolean);
            result = "http://my.weblog.url";
            URLConnection urlConnection = url.openConnection();
            urlConnection.getOutputStream();
            result = byteArrayOutputStream;
            urlConnection.getInputStream();
            result = new ByteArrayInputStream("false".getBytes());
        }};

        WeblogEntryComment comment = new WeblogEntryComment();
        WeblogEntry entry = new WeblogEntry();
        comment.setWeblogEntry(entry);
        entry.setWebsite(weblog);
        int actual = new AkismetCommentValidator().validate(comment, null);
        assertEquals("blog=http://my.weblog.url&user_ip=null&user_agent=null&referrer=null&permalink=null&comment_type=comment&comment_author=null&comment_author_email=null&comment_author_url=null&comment_content=null", byteArrayOutputStream.toString());
        assertEquals(RollerConstants.PERCENT_100, actual);
    }

    @Test
    public void validate_akismet_returns_true() throws IOException {

        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        new Expectations() {{
            WebloggerConfig.getProperty(anyString);
            result = "anApiKey";
            WebloggerFactory.getWeblogger().getUrlStrategy().getWeblogURL(weblog, anyString, anyBoolean);
            result = "http://my.weblog.url";
            URLConnection urlConnection = url.openConnection();
            urlConnection.getOutputStream();
            result = byteArrayOutputStream;
            urlConnection.getInputStream();
            result = new ByteArrayInputStream("true".getBytes());
        }};

        WeblogEntryComment comment = new WeblogEntryComment();
        WeblogEntry entry = new WeblogEntry();
        comment.setWeblogEntry(entry);
        entry.setWebsite(weblog);
        RollerMessages messages = new RollerMessages();

        int actual = new AkismetCommentValidator().validate(comment, messages);

        assertEquals("blog=http://my.weblog.url&user_ip=null&user_agent=null&referrer=null&permalink=null&comment_type=comment&comment_author=null&comment_author_email=null&comment_author_url=null&comment_content=null", byteArrayOutputStream.toString());
        assertEquals(0, actual);
        assertEquals("comment.validator.akismetMessage : ", messages.toString());
    }
}

