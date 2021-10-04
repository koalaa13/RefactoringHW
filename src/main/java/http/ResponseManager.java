package http;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ResponseManager {
    private final HttpServletResponse response;
    private final PrintWriter writer;
    private String header;
    private final List<String> lines;

    public ResponseManager(HttpServletResponse response) throws IOException {
        this.response = response;
        this.writer = response.getWriter();
        header = "";
        lines = new ArrayList<>();
    }

    /**
     * @param header      text of header
     * @param headerLevel if =1 will use \<h1> if =2 will use \<h2> and etc =0 for without tag usage
     */
    public void setHeader(String header, Integer headerLevel) {
        if (headerLevel == 0) {
            this.header = header;
        } else {
            this.header = "<h" + headerLevel + ">" + header + "</h" + headerLevel + ">";
        }
    }

    public void addLine(String line) {
        this.lines.add(line);
    }

    public void sendResponse() {
        writer.println("<html><body>");
        if (!header.isEmpty()) {
            writer.println(header);
        }
        for (String line : lines) {
            writer.println(line + "<br>");
        }
        writer.println("</body></html>");
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
