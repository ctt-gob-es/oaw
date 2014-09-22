package es.inteco.rastreador2.utils;

import es.inteco.common.Constants;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.form.PageForm;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

public final class Pagination {

    private Pagination() {
    }

    public static List<PageForm> createPagination(HttpServletRequest request, int numResult, int currentPage) {
        PropertiesManager properties = new PropertiesManager();
        return createPagination(request, numResult, properties.getValue(CRAWLER_PROPERTIES, "pagination.size"), currentPage, Constants.PAG_PARAM);
    }

    public static List<PageForm> createPagination(HttpServletRequest request, int numResult, String pageSize, int currentPage, String parameter) {
        PropertiesManager pmgr = new PropertiesManager();

        int numPages = (int) Math.ceil((float) numResult / (float) Integer.parseInt(pageSize));
        int numShowedPages = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "pagination.showed.pages"));
        int begin = 1;
        int end = numPages;

        if ((numPages > numShowedPages)) {
            if (currentPage <= numShowedPages / 2) {
                end = numShowedPages;
            } else {
                begin = currentPage - numShowedPages / 2;
                if ((currentPage + numShowedPages / 2) <= numPages) {
                    end = currentPage + numShowedPages / 2;
                } else {
                    begin = numPages - numShowedPages + 1;
                }
            }
        }
        return createPageList(request, currentPage, begin, end, parameter, numPages);
    }

    private static List<PageForm> createPageList(HttpServletRequest request, int currentPage, int begin, int end, String parameter, int numPages) {
        List<PageForm> pageFormList = new ArrayList<PageForm>();

        String path = request.getRequestURI() + "?";
        if (request.getQueryString() != null) {
            path += request.getQueryString();
        }
        path = removeParameterP(path, parameter);

        PageForm pForm;
        if (currentPage != 1) {
            if (begin != 1) {
                pForm = new PageForm(CrawlerUtils.getResources(request).getMessage("linkList.beginning"),
                        calculatePath(path, parameter).concat("1"), Constants.PAGINATION_PF_STYLE, true);
                pageFormList.add(pForm);
            }

            pForm = new PageForm(CrawlerUtils.getResources(request).getMessage("linkList.previous"),
                    calculatePath(path, parameter).concat(String.valueOf((currentPage - 1))), Constants.PAGINATION_PF_STYLE, true);
            pageFormList.add(pForm);
        }

        for (int i = begin; i <= end; i++) {
            pForm = new PageForm();
            pForm.setTitle(String.valueOf(i));
            if (i != currentPage) {
                pForm.setPath(calculatePath(path, parameter).concat(String.valueOf(i)));
                pForm.setStyleClass(Constants.PAGINATION_STYLE);
                pForm.setActive(true);
            } else {
                pForm.setStyleClass(Constants.PAGINATION_CURRENT_STYLE);
                pForm.setActive(false);
            }
            pageFormList.add(pForm);
        }

        if (currentPage != numPages) {
            pForm = new PageForm(CrawlerUtils.getResources(request).getMessage("linkList.following"),
                    calculatePath(path, parameter).concat(String.valueOf((currentPage + 1))), Constants.PAGINATION_PF_STYLE, true);
            pageFormList.add(pForm);

            if (end < numPages) {
                pForm = new PageForm(CrawlerUtils.getResources(request).getMessage("linkList.end"),
                        calculatePath(path, parameter).concat(String.valueOf(numPages)), Constants.PAGINATION_PF_STYLE, true);
                pageFormList.add(pForm);
            }
        }

        return pageFormList;
    }

    private static String calculatePath(String path, String param) {
        if (path.charAt(path.length() - 1) == '?') {
            path = path.concat(param + "=");
        } else {
            path = path.concat("&" + param + "=");
        }
        return path;
    }

    private static String removeParameterP(String parameterP, String parameter) {
        return parameterP.replaceAll("&*" + parameter + "=\\d+", "");
    }

    public static int getPage(HttpServletRequest request, String param) {
        int pagina = 1;
        if (request.getParameter(param) != null) {
            pagina = Integer.parseInt(request.getParameter(param));
        }
        return pagina;
    }

}
