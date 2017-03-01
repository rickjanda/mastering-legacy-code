package org.apache.roller.weblogger.ui.rendering.plugins.comments;

import org.apache.roller.weblogger.business.Weblogger;
import org.apache.roller.weblogger.pojos.Weblog;
import org.apache.roller.weblogger.pojos.WeblogEntry;
import org.apache.roller.weblogger.pojos.WeblogEntryComment;
import org.junit.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;

@SuppressWarnings("RefusedBequest")
public class AkismetCommentValidator2Test {

    @Test
    public void validate() throws IOException {

        //arrange
        Weblog weblog = new Weblog();

        Weblogger webloggerMock = mock(Weblogger.class, RETURNS_DEEP_STUBS);
        when(webloggerMock.getVersion()).thenReturn("1.0");
        when(webloggerMock.getUrlStrategy().getWeblogURL(weblog, null, true)).thenReturn("http://weblogurl.com");

        AkismetCommentValidator2 validatorSpy = spy(new AkismetCommentValidator2("apiKeyValue", null) );
        doReturn(webloggerMock).when(validatorSpy).getWeblogger();

        WeblogEntry weblogEntrySpy = spy(new WeblogEntry());
        weblogEntrySpy.setWebsite(weblog);
        doReturn("http://permalink.com").when(weblogEntrySpy).getPermalink();

        WeblogEntryComment comment = new WeblogEntryComment();
        comment.setWeblogEntry(weblogEntrySpy);

        //act
        validatorSpy.validate(comment, null);
    }

}
