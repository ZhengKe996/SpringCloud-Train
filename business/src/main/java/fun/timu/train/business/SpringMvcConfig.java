package fun.timu.train.business;

import fun.timu.train.commo.interceptor.MemberInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {

    @Resource
    MemberInterceptor memberInterceptor;

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(memberInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
//                        "/business/member/send-code"
                );
    }
}
