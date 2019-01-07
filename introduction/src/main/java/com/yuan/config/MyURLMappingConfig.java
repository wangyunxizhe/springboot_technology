package com.yuan.config;

import com.yuan.dao.UserDao;
import com.yuan.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;

import java.util.Collection;

/**
 * 路由器函数 配置
 * <p>
 * 另一种接受页面请求的方式
 */
@Configuration
public class MyURLMappingConfig {

    /**
     * Servlet 规范
     * 请求接口：ServletRequest 或者 HttpServletRequest
     * 响应接口：ServletResponse 或者 HttpServletResponse
     * <p>
     * Spring 5.0 重新定义了服务请求的响应接口
     * 请求接口：ServerRequest
     * 响应接口：ServerResponse
     * 即可支持 Servlet 规范，也可以支持自定义，比如 Netty （Web Server）
     * 以本例
     * 定义 GET 请求，并且放回所有的用户对象 URI：/person/find/all
     * Flux 是 0 - N 个对象集合
     * Mono 是 0 - 1 个对象集合
     * Reactive 中的 Flux 或者 Mono 是异步处理（非阻塞）
     * 集合对象基本上是同步处理（阻塞）
     * Flux 或者 Mono 都是 Publisher
     */
    @Bean
    @Autowired
    public RouterFunction<ServerResponse> personFindAll(UserDao userDao) {
        return RouterFunctions.route(RequestPredicates.GET("/person/find/all"),
                request -> {
                    Collection<User> users = userDao.findAll();
                    Flux<User> userFlux = Flux.fromIterable(users);
                    return ServerResponse.ok().body(userFlux, User.class);
                });
    }

}
