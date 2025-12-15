package com.cgi.parizek.matej;

import com.microsoft.azure.functions.*;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@SuperBuilder
public class TestHttpRequestMessage<T> implements HttpRequestMessage<T> {

    private final T body;
    private final Map<String, String> headers;
    private final Map<String, String> queryParams;
    private final HttpMethod method;
    private final URI uri;

    public TestHttpRequestMessage(T body, HttpMethod method, URI uri) {
        this.body = body;
        this.headers = new HashMap<>();
        this.queryParams = new HashMap<>();
        this.method = method;
        this.uri = uri;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return method;
    }

    @Override
    public URI getUri() {
        return uri;
    }

    @Override
    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public T getBody() {
        return body;
    }

    @Override
    public Map<String, String> getQueryParameters() {
        return queryParams;
    }

    @Override
    public HttpResponseMessage.Builder createResponseBuilder(HttpStatus status) {
        return new HttpResponseMessageMockBuilder().status(status);
    }

    @Override
    public HttpResponseMessage.Builder createResponseBuilder(HttpStatusType status) {
        return new HttpResponseMessageMockBuilder().status(status);
    }
}

class HttpResponseMessageMockBuilder implements HttpResponseMessage.Builder {

    private HttpStatusType status;
    private Object body;
    private final Map<String, String> headers = new HashMap<>();


    @Override
    public HttpResponseMessage.Builder status(HttpStatusType status) {
        this.status = status;
        return this;
    }

    @Override
    public HttpResponseMessage.Builder header(String key, String value) {
        headers.put(key, value);
        return this;
    }

    @Override
    public HttpResponseMessage.Builder body(Object body) {
        this.body = body;
        return this;
    }

    @Override
    public HttpResponseMessage build() {
        HttpStatusType finalStatus = this.status;
        Object finalBody = this.body;
        Map<String, String> finalHeaders = new HashMap<>(this.headers);

        return new HttpResponseMessage() {
            @Override
            public HttpStatusType getStatus() {
                return finalStatus;
            }

            @Override
            public Object getBody() {
                return finalBody;
            }

            @Override
            public String getHeader(String key) {
                return finalHeaders.get(key);
            }

            @Override
            public int getStatusCode() {
                return finalStatus.value();
            }
        };
    }
}
