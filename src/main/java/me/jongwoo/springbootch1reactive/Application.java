package me.jongwoo.springbootch1reactive;

import me.jongwoo.springbootch1reactive.domain.HttpTraceWrapper;
import me.jongwoo.springbootch1reactive.repository.HttpTraceWrapperRepository;
import me.jongwoo.springbootch1reactive.repository.SpringDataHttpTraceRepository;
import org.bson.Document;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.trace.http.HttpTrace;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.convert.NoOpDbRefResolver;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import java.net.URI;
import java.util.Collections;
import java.util.Map;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        // 해당 스레드가 블로킹 메소드 호출을 허용하는지 검사한다.
//        BlockHound.install();

        SpringApplication.run(Application.class, args);
    }

//    @Bean
//    Jackson2JsonMessageConverter jackson2JsonMessageConverter(){
//        return new Jackson2JsonMessageConverter();
//    }

//    @Bean
//    HttpTraceRepository springDataTraceRepository(HttpTraceWrapperRepository repository){
//        return new SpringDataHttpTraceRepository(repository);
//    }
//
//    @Bean
//    public MappingMongoConverter mappingMongoConverter(MongoMappingContext context){
//        MappingMongoConverter mappingMongoConverter =
//                new MappingMongoConverter(NoOpDbRefResolver.INSTANCE, context);
//
//        mappingMongoConverter.setCustomConversions(
//                new MongoCustomConversions(Collections.singletonList(CONVERTER)));
//        return mappingMongoConverter;
//
//    }
//
//    static Converter<Document, HttpTraceWrapper> CONVERTER =
//            new Converter<Document, HttpTraceWrapper>() {
//                @Override
//                public HttpTraceWrapper convert(Document document) {
//                    Document httpTrace = document.get("httpTrace", Document.class);
//                    Document request = httpTrace.get("request", Document.class);
//                    Document response = httpTrace.get("response", Document.class);
//
//                    return new HttpTraceWrapper(new HttpTrace(
//                            new HttpTrace.Request(
//                                    request.getString("method"),
//                                    URI.create(request.getString("uri")),
//                                    request.get("headers", Map.class),
//                                    null
//                            ),
//                            new HttpTrace.Response(
//                                    response.getInteger("status"),
//                                    response.get("headers", Map.class)),
//                            httpTrace.getDate("timestamp").toInstant(),
//                            null,
//                            null,
//                            httpTrace.getLong("timeTaken")));
//                }
//            };

//    @Bean
//    HttpTraceRepository httpTraceRepository(){
//        return new InMemoryHttpTraceRepository();
//    }

}
