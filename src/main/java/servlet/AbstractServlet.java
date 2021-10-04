package servlet;

import db.ProductDatabase;
import html.ResponseBodyManager;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AbstractServlet extends HttpServlet {
    private static final String DEFAULT_CONTENT_TYPE = "text/html";
    private static final int DEFAULT_STATUS = HttpServletResponse.SC_OK;
    protected final ProductDatabase db;
    protected final ResponseBodyManager responseBodyManager;

    public AbstractServlet(ProductDatabase db) {
        this.db = db;
        this.responseBodyManager = new ResponseBodyManager();
    }

    // TODO write test with a few get-products request
    protected void setResponseBody(HttpServletResponse response) throws IOException {
        response.getWriter().println(responseBodyManager.getResponseBodyAndFlush());
    }

    protected void setResponseContentType(HttpServletResponse response) {
        response.setContentType(DEFAULT_CONTENT_TYPE);
    }

    protected void setResponseStatus(HttpServletResponse response) {
        response.setStatus(DEFAULT_STATUS);
    }

    protected void sendResponse(HttpServletResponse response) throws IOException {
        setResponseBody(response);
        setResponseStatus(response);
        setResponseContentType(response);
    }
}
