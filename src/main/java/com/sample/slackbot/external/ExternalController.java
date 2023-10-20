package com.sample.slackbot.external;

import com.sample.slackbot.SlackAppService;
import com.slack.api.model.Message;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/external")
public class ExternalController {
    private final Logger log = LoggerFactory.getLogger( ExternalController.class );

    private final SlackAppService slackAppService;


    @RequestMapping("/hello")
    public void hello( @RequestBody Map<String, String>data, HttpServletRequest request ){

        log.info(" @@@@@@@@@@@@@@@@@@@@@@@@@ HELLO @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

        if ( data.get( "channelName" ) == null || data.get( "message" ) == null ) {
            log.error("XXXXXXXXXXXXXXXXXXXXXXXXXXXX   {}, {}", data.get( "channelName" ), data.get( "message" ) );
            return;
        }
        Message message = slackAppService.sendMessage( data.get( "channelName" ), data.get( "message" ) );

        log.info( "message : {}", message );

    }


}
