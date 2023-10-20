package com.sample.slackbot;

import com.slack.api.bolt.App;
import com.slack.api.bolt.AppConfig;
import com.slack.api.bolt.socket_mode.SocketModeApp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;


@PropertySource( "classpath:/application.properties" )
@Configuration
public class SlackServerConfig {
    private Logger log = LoggerFactory.getLogger( SlackServerConfig.class );

    @Value( "${slack.bot.token}" )
    private String token;

    @Value( "${slack.app.token}" )
    private String appToken;

    /*@Value( "${slack.signingSecret}" )
    private String signingSecret;*/

    SlackServerConfig ( Environment env ) {
        this.token = env.getProperty("slack.bot.token" );
        this.appToken = env.getProperty("slack.app.token" );
//        this.signingSecret = env.getProperty("slack.signingSecret" );

        log.info( "[SlackServerConfig] token = " + token );
        log.info( "[SlackServerConfig] appToken = " + appToken );
//        log.info( "[SlackServerConfig] signingSecret = " + signingSecret );
    }

    /**
     * ContextStartedEvent로 ApplicationContext가 시작될 때 호출
     * https://docs.spring.io/spring-framework/reference/core/beans/context-introduction.html#context-functionality-events
     * https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#context-functionality-events-annotation
     */
    @Bean
    @EventListener( ContextStartedEvent.class )
    public void startSocketModeApp() throws Exception {
        log.info( "#################### startSocketModeApp called" );

    }


    @Bean
    public AppConfig loadSingleWorkspaceAppConfig() {
        log.info( "#################### AppConfig called" );
        return AppConfig.builder()
                .singleTeamBotToken( token )
//                .signingSecret( signingSecret )
                .build();
    }

    @Bean
    public App initSlackApp( AppConfig config ) throws Exception {
        log.info( "#################### initSlackApp called" );

        var app = new App( config );

        app.command("/hello", (req, ctx) -> {
          return ctx.ack(":wave: 안녕하세요! 에러봇이에요 :robot_face:");
        });

        app.client().chatPostMessage( req -> req
                .channel( "C061JLBJUG5" )
                .text( "socketMode test!" ) );

        new SocketModeApp( appToken, app ).startAsync(); // start()는 쓰레드를 block한다. startAsync()는 쓰레드를 block하지 않는다.

        return app;
    }



}
