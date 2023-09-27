package http;

import java.io.InputStream;
import java.util.logging.Logger;

public class Constants {
    public static final Logger logger = Logger.getLogger("HttpLogger");
    public static final String HTTP_VERSION = "HTTP/1.1";
    public static final String CRLF = "\r\n";
    public static final String SPACE = " ";
    public static final String OK = "200 OK";
    public static final String INTERNAL_SERVER_ERROR = "500 Internal Server Error";
    public static final String BAD_REQUEST = "400 Bad Request";
    public static final String UNPROCESSABLE_ENTITY = "422 Unprocessable Entity";
//    public static final String HTTP_RESPONSE_FORMAT = "%s %s".concat(CRLF).concat("%s").concat();

    public void readData(InputStream inputStream) {

    }

}
