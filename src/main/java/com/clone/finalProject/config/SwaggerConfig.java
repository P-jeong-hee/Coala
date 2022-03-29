package com.clone.finalProject.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

//스웨거 적용하면 로그인 시(토큰은 정상발급함) 프론트 단에서 상태 코드 200임에도 에러로 잡힘, 해결 진행 중

//@EnableWebMvc
//@EnableSwagger2
//@Configuration
//public class SwaggerConfig implements WebMvcConfigurer {
//
//    @Bean
//    public Docket api()  {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(apiInfo())
//                .select()
////                .apis(RequestHandlerSelectors.basePackage("com.clone.finalProject.controller"))
//                .apis(RequestHandlerSelectors.any())
//                .paths(PathSelectors.any())
//                .build();
//    }
//
//    private ApiInfo apiInfo() {
//        return new ApiInfoBuilder()
//                .title("COala API Test With Swagger")
//                .description("항해5기 A반 8조 COala API입니다")
//                .version("2.9.2")
//                .build();
//    }
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/swagger-ui.html")
//                .addResourceLocations("classpath:/META-INF/resources/");
//
//        registry.addResourceHandler("/webjars/**")
//                .addResourceLocations("classpath:/META-INF/resources/webjars/");
//        // -- Static resources
//        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
//        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css");
//        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/");
//        registry.addResourceHandler("/images/**").addResourceLocations("classpath:/static/images/");
//    }
//}
