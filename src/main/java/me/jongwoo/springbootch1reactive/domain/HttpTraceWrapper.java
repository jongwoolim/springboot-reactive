package me.jongwoo.springbootch1reactive.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.boot.actuate.trace.http.HttpTrace;
import org.springframework.data.annotation.Id;


@Getter
public class HttpTraceWrapper {

    @Id
    private String id;

    private HttpTrace httpTrace;

    public HttpTraceWrapper(HttpTrace httpTrace) {
        this.httpTrace = httpTrace;
    }
}
