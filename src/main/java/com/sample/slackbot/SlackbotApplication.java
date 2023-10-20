package com.sample.slackbot;

import com.slack.api.bolt.App;
import com.slack.api.bolt.AppConfig;
import com.slack.api.bolt.socket_mode.SocketModeApp;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.PropertySource;

import java.io.IOException;

@SpringBootApplication
@ServletComponentScan
public class SlackbotApplication {

    public static void main ( String[] args ) {

        SpringApplication.run( SlackbotApplication.class, args );

    }

}
