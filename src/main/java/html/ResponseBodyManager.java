package html;

import java.util.ArrayList;
import java.util.List;

public class ResponseBodyManager {
    private String header;
    private final List<String> lines;

    public ResponseBodyManager() {
        header = "";
        lines = new ArrayList<>();
    }

    /**
     * @param header      text of header
     * @param headerLevel if =1 will use \<h1> if =2 will use \<h2> and etc =0 for without tag usage
     */
    public void setHeader(String header, int headerLevel) {
        if (headerLevel == 0) {
            this.header = header;
        } else {
            this.header = "<h" + headerLevel + ">" + header + "</h" + headerLevel + ">";
        }
    }

    public void addLine(String line) {
        this.lines.add(line);
    }

    public String getResponseBody() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body>").append('\n');
        if (!header.isEmpty()) {
            sb.append(header).append('\n');
        }
        for (String line : lines) {
            sb.append(line).append("<br>").append('\n');
        }
        sb.append("</body></html>").append('\n');
        return sb.toString();
    }

    public String getResponseBodyAndFlush() {
        String res = getResponseBody();
        flush();
        return res;
    }

    public void flush() {
        header = "";
        lines.clear();
    }
}
