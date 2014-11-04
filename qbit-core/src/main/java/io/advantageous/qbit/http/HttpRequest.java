package io.advantageous.qbit.http;

import io.advantageous.qbit.message.Request;
import io.advantageous.qbit.util.MultiMap;


/**
 * Created by rhightower on 10/21/14.
 * @author rhightower
 */
public class HttpRequest implements Request<Object>{

    private final String uri;
    private final String remoteAddress;
    private final MultiMap<String, String> params;

    private final MultiMap<String, String> headers;
    private final String body;
    private final String method;
    private final HttpResponse response;

    private final long messageId;

    private final long timestamp;


    private  boolean handled;

    @Override
    public String address() {
        return uri;
    }

    @Override
    public String returnAddress() {
        return remoteAddress;
    }

    @Override
    public MultiMap<String, String> params() {
        return params;
    }

    @Override
    public MultiMap<String, String> headers() {
        return headers;
    }

    @Override
    public long timestamp() {
        return timestamp;
    }

    @Override
    public boolean isHandled() {
        return false;
    }

    @Override
    public void handled() {
        handled = true;
    }

    @Override
    public long id() {
        return messageId;
    }

    @Override
    public Object body() {
        return body;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    private static class RequestIdGenerator {
        private long value;
        private long inc() {return value++;}
    }

    private final static ThreadLocal<RequestIdGenerator> idGen = new ThreadLocal<RequestIdGenerator>(){
        @Override
        protected RequestIdGenerator initialValue() {
            return new RequestIdGenerator();
        }
    };


    public HttpRequest(final String uri, final String method, final MultiMap<String, String> params,
                       final MultiMap<String, String> headers,
                       final String body, final String remoteAddress, final HttpResponse response) {
        this.params = params;
        this.body = body;
        this.method = method;
        this.uri = uri;
        this.response = response;
        this.remoteAddress = remoteAddress;
        this.headers = headers;
        this.messageId = idGen.get().inc();
        this.timestamp = io.advantageous.qbit.util.Timer.timer().now();
    }

    public MultiMap<String, String> getParams() {
        return params;
    }

    public String getBody() {
        return body;
    }

    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }


    public HttpResponse getResponse() {
        return response;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public MultiMap<String, String> getHeaders() {
        return headers;
    }


    public long getMessageId() {
        return messageId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HttpRequest request = (HttpRequest) o;

        if (messageId != request.messageId) return false;
        if (timestamp != request.timestamp) return false;
        if (!method.equals(request.method)) return false;
        if (!uri.equals(request.uri)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = uri.hashCode();
        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
        return result;
    }
}