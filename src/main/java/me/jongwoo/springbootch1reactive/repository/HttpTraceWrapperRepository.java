package me.jongwoo.springbootch1reactive.repository;

import me.jongwoo.springbootch1reactive.domain.HttpTraceWrapper;
import org.springframework.data.repository.Repository;

import java.util.stream.Stream;

public interface HttpTraceWrapperRepository extends Repository<HttpTraceWrapper, String> {

    Stream<HttpTraceWrapper> findAll();

    void save(HttpTraceWrapper trace);
}
