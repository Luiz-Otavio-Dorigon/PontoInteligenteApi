package br.com.dorigon.pontoeletronico.api.configs;

import br.com.dorigon.pontoeletronico.api.security.utils.JwtTokenUtil;
import br.com.dorigon.pontoeletronico.api.utils.DebugUtils;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Luiz Otávio Dorigon <luiz.otavio.dorigon@gmail.com>  on 12/09/17.
 */
@Configuration
@Profile("dev")
@EnableSwagger2
public class SwaggerConfig {

    @Autowired
    private JwtTokenUtil mJwtTokenUtil;

    @Autowired
    private UserDetailsService mUserDetailsService;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("br.com.dorigon.pontoeletronico.api.controllers"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Ponto Inteligente API")
                .description("Documentação da API de acesso aos endpoints do Ponto Inteligente.")
                .version("1.0")
                .build();
    }

    @Bean
    public SecurityConfiguration security() {
        String token = "";
        try {
            UserDetails userDetails = this.mUserDetailsService.loadUserByUsername("admin@dorigon.com.br");
            token = this.mJwtTokenUtil.obterToken(userDetails);
        } catch (Exception e) {
            DebugUtils.log(this.getClass(),"Error to generate a token swagger!", e);
        }

        return new SecurityConfiguration(null, null, null, null, "Bearer " + token, ApiKeyVehicle.HEADER, "Authorization", ",");
    }
}
