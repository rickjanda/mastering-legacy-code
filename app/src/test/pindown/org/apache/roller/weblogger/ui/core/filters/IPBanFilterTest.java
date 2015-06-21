package org.apache.roller.weblogger.ui.core.filters;

import org.junit.Test;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.*;


public class IPBanFilterTest {

    @Test
    public void doFilter_notBanned() throws Exception {

        final HttpServletRequest request = mock(HttpServletRequest.class);
        final FilterChain chain = mock(FilterChain.class);

        new IPBanFilter().doFilter(request, null, chain);

        verify(chain).doFilter(request, null);
    }

    @Test
    public void doFilter_banned() throws Exception {

        // arrange
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final String remoteAddress = "http://remote.address.com";
        doReturn(remoteAddress).when(request).getRemoteAddr();
        final FilterChain chain = mock(FilterChain.class);

        final IPBanFilter filter = spy(new IPBanFilter());
        doReturn(true).when(filter).isBanned(remoteAddress);

        final HttpServletResponse response = mock(HttpServletResponse.class);

        //act
        filter.doFilter(request, response, chain);

        //assert
        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
        verify(filter).isBanned(remoteAddress);
    }
}
