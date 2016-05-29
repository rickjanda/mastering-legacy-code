package org.apache.roller.weblogger.ui.core.filters;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import org.apache.roller.weblogger.util.IPBanList;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.junit.Assert.*;

@RunWith(JMockit.class)
public class IPBanFilterTest {

    @Test
    public void doFilter(
            @Mocked final HttpServletRequest request,
            @Mocked final HttpServletResponse response,
            @Mocked final FilterChain chain,
            @Mocked(stubOutClassInitialization = false) final IPBanList ipBanList
    ) throws IOException, ServletException {

        new Expectations() {{
            ipBanList.isBanned(anyString); result = false;
        }};

        new IPBanFilter().doFilter(request, response, chain);

        new Verifications() {{
            chain.doFilter(request, response); times = 1;
        }};
    }

    @Test
    public void doFilter2(
            @Mocked final HttpServletRequest request,
            @Mocked final HttpServletResponse response,
            @Mocked final FilterChain chain,
            @Mocked(stubOutClassInitialization = false) final IPBanList ipBanList
    ) throws IOException, ServletException {

        new Expectations() {{
            ipBanList.isBanned(anyString); result = true;
        }};

        new IPBanFilter().doFilter(request, response, chain);

        new Verifications() {{
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            chain.doFilter(request, response); times = 0;
        }};
    }
}
