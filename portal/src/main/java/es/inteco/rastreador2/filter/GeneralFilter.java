package es.inteco.rastreador2.filter;

import es.inteco.common.Constants;
import es.inteco.rastreador2.utils.CrawlerUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class GeneralFilter implements Filter {

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        if (((HttpServletRequest) request).getSession().getAttribute(Constants.LANGUAGE_LIST) == null) {
            ((HttpServletRequest) request).getSession().setAttribute(Constants.LANGUAGE_LIST, CrawlerUtils.getLanguages());
        }

        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
    }

}
