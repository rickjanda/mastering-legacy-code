package org.apache.roller.weblogger.ui.struts2.core;

import com.opensymphony.xwork2.Action;
import org.apache.commons.lang3.StringUtils;
import org.apache.roller.weblogger.business.UserManager;
import org.apache.roller.weblogger.business.Weblogger;
import org.apache.roller.weblogger.config.WebloggerRuntimeConfigInstance;
import org.apache.roller.weblogger.pojos.User;
import org.apache.roller.weblogger.util.MailUtilInstance;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RegisterTest {

    @Test
    public void save_successful() throws Exception {
        ProfileBean profileBean1 = new ProfileBean();
        profileBean1.setUserName("Peterchen");
        profileBean1.setPasswordText("This is an amazing text.");
        profileBean1.setOpenIdUrl("http://peterchenopenidurl.ch");
        profileBean1.setPasswordConfirm(profileBean1.getPasswordText());
        ProfileBean profileBean = profileBean1;
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class, RETURNS_DEEP_STUBS);
        WebloggerRuntimeConfigInstance webloggerRuntimeConfigInstance = mock(WebloggerRuntimeConfigInstance.class);
        Register register = spy(new Register(mock(MailUtilInstance.class), webloggerRuntimeConfigInstance));
        Weblogger weblogger = mock(Weblogger.class, RETURNS_DEEP_STUBS);

        doReturn(weblogger).when(register).getWeblogger();
        doReturn(true).when(webloggerRuntimeConfigInstance).getBooleanProperty("users.registration.enabled");
        doReturn(true).when(webloggerRuntimeConfigInstance).getBooleanProperty("user.account.email.activation");
        when(weblogger.getUserManager().getUserByUserName("Peterchen", null)).thenReturn(null);
        when(weblogger.getUserManager().getUserByOpenIdUrl("http://peterchenopenidurl.ch")).thenReturn(null);
        when(weblogger.getUserManager().getUserByActivationCode(anyString())).thenReturn(null);

        register.setServletRequest(httpServletRequest);
        register.setBean(profileBean);

        // act
        String saveResult = register.save();

        verify(weblogger.getUserManager()).addUser(userArgumentCaptor.capture());
        User savedUser = userArgumentCaptor.getValue();
        assertEquals(Action.SUCCESS, saveResult);
        assertEquals("ProfileBean{id='null', userName='Peterchen', password='null', screenName='null', fullName='null', emailAddress='null', locale='null', timeZone='null', openIdUrl='http://peterchenopenidurl.ch', passwordText='This is an amazing text.', passwordConfirm='This is an amazing text.'}", profileBean.getStateAsString());
        assertEquals("User{, userName='Peterchen', password='This is an amazing text.', openIdUrl='http://peterchenopenidurl.ch', screenName='null', fullName='null', emailAddress='null', enabled=false}", savedUser.getStateAsString());
        assertTrue(StringUtils.isNotEmpty(savedUser.getId()));
        assertNotNull(savedUser.getDateCreated());
    }

}
