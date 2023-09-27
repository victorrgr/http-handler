package http;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static http.Constants.*;

public class HttpResponse {
    private final String httpVersion;
    private final String httpStatus;
    private final Set<String> headers;
    private final String content;

    public HttpResponse(String httpVersion, String httpStatus, Set<String> headers, String content) {
        this.httpVersion = httpVersion;
        this.httpStatus = httpStatus;
        if (headers == null || headers.isEmpty())
            this.headers = new HashSet<>();
        else
            this.headers = headers;
        this.content = content;
        process();
    }

    private void process() {
        if (content != null && !content.isBlank()) {
            headers.add("Content-Length: " + content.getBytes().length);
        }
    }

    public byte[] bundle() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(httpVersion);
        stringBuffer.append(SPACE);
        stringBuffer.append(httpStatus);
        stringBuffer.append(CRLF);
        stringBuffer.append(headers.stream()
                .collect(Collectors.joining(CRLF)));
        stringBuffer.append(CRLF);
        stringBuffer.append(CRLF);
        stringBuffer.append(content);
        var bundledResponse = stringBuffer.toString();
        logger.info(bundledResponse);
        return bundledResponse.getBytes();
    }

}
