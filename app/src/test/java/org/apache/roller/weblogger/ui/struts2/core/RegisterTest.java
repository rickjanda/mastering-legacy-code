package org.apache.roller.weblogger.ui.struts2.core;

import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.apache.roller.weblogger.WebloggerException;
import org.apache.roller.weblogger.business.*;
import org.apache.roller.weblogger.business.jpa.JPAPersistenceStrategy;
import org.apache.roller.weblogger.business.jpa.JPAPropertiesManagerImpl;
import org.apache.roller.weblogger.business.jpa.JPAUserManagerImpl;
import org.apache.roller.weblogger.business.jpa.JPAWebloggerImpl;
import org.apache.roller.weblogger.config.WebloggerRuntimeConfig;
import org.apache.roller.weblogger.pojos.RuntimeConfigProperty;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.sql.DriverManager;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(JMockit.class)
public class RegisterTest {

    @Mocked
    WebloggerFactory webloggerFactory;

    @Mocked
    DriverManager driverManager;

    @Mocked
    Persistence persistence;

    @Mocked
    EntityManager entityManager;

    final DatabaseProvider dbProvider;
    final JPAPersistenceStrategy persistenceStrategy;
    final UserManager userManager;
    final PropertiesManager propertiesManager;
    final Weblogger weblogger;

    public RegisterTest() throws Exception {
        dbProvider = new DatabaseProvider();
        persistenceStrategy = new JPAPersistenceStrategy(dbProvider) {};
        userManager = new JPAUserManagerImpl(persistenceStrategy) {};
        propertiesManager = new JPAPropertiesManagerImpl(persistenceStrategy) {};
        weblogger = new JPAWebloggerImpl(persistenceStrategy, null, null, null, null, null, null, null, null, propertiesManager, null, null, userManager, null, null, null, null, null, null, null) {
        };
    }

    @Test
    public void save__registration_disabled(@Mocked final TypedQuery<Long> query) throws WebloggerException {


        new Expectations() {{
            WebloggerFactory.getWeblogger();
            result = weblogger;

            entityManager.find(RuntimeConfigProperty.class, "users.registration.enabled");
            result = new RuntimeConfigProperty("users.registration.enabled", "false");

            query.getResultList();
            result = Collections.singletonList(1L);
        }};

        String actual = new Register().save();
        assertEquals("disabled", actual);
    }

    @Test
    public void save__registration_enabled() throws WebloggerException {

        new Expectations() {{
            WebloggerFactory.getWeblogger();
            result = weblogger;

            entityManager.find(RuntimeConfigProperty.class, "users.registration.enabled");
            result = new RuntimeConfigProperty("users.registration.enabled", "false");

        }};

        Register register = new Register();
        ProfileBean profileBean = register.getBean();
        profileBean.setUserName("aUsername");
        profileBean.setPasswordText("aPasswordText");
        profileBean.setPasswordConfirm("aPasswordText");

        String actual = register.save();
        assertEquals("disabled", actual);
    }

}
