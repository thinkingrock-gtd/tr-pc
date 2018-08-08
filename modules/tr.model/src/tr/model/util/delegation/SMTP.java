package tr.model.util.delegation;

import org.apache.commons.lang.StringUtils;

/**
 * SMTP fields.
 */
final class SMTP {

    public final String host;
    public final int port;
    public final String user;
    public final String pass;
    public final boolean ssl;

    SMTP(String host, int port, String user, String pass, boolean ssl) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.pass = pass;
        this.ssl = ssl;
    }

    boolean valid() {
        return port > 0
            && StringUtils.isNotBlank(host)
            && StringUtils.isNotBlank(user)
            && StringUtils.isNotBlank(pass);
    }
}
