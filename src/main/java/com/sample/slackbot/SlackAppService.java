package com.sample.slackbot;

import com.slack.api.bolt.App;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.methods.response.conversations.ConversationsListResponse;
import com.slack.api.model.Conversation;
import com.slack.api.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.IOException;

@PropertySource( "classpath:/application.properties" )
@Service
public class SlackAppService {
    private final App app;

    @Value( "${slack.bot.token}" )
    private String token;

    private final Logger log = LoggerFactory.getLogger( SlackAppService.class );

    public SlackAppService ( App app ) {
        this.app = app;
    }


    public Message sendMessage ( String channel, String message ) {
        log.info( "[SlackAppService] sendMessage called >>> {}, {} ", channel, message );

        // 1. 메시지를 보낼 채널 찾기
        String channelId = findConversation( channel );

        // 3. 메시지 전송
        ChatPostMessageResponse chatPostMessageResponse = publishMessage( channelId, message );

        assert chatPostMessageResponse != null;

        return chatPostMessageResponse.getMessage();

    }


    private String findConversation ( String name ) {
        var client = app.client();
        try {

            // call the conversations.list method with a valid token using the WebClient
            ConversationsListResponse result = client.conversationsList( r -> r.token( token ) );

            for ( Conversation channel : result.getChannels() ) {
                if ( channel.getName().equals( name ) ){
                    var conversationId = channel.getId();
                    // Print result
                    log.info( "Found conversation ID: {}", conversationId );
                    return conversationId;
                }
            }
        }
        catch ( Exception e ) {
            log.error( "error: {}", e.getMessage(), e );
        }

        return "";
    }


    /**
     * Post a message to a channel your app is in using ID and message text
     *
     * @return
     */
    private ChatPostMessageResponse publishMessage ( String id, String text ) {


        // you can get this instance via ctx.client() in a Bolt app
//        var client = Slack.getInstance().methods();
        var client = app.client();
        try {
            // Call the chat.postMessage method using the built-in WebClient
            var result = client.chatPostMessage( r -> r
                            // The token you used to initialize your app
                            .token( token ).channel( id ).text( text )
                    // You could also use a blocks[] array to send richer content
            );
            // Print result, which includes information about the message (like TS)
            log.info( "result {}", result );
            return result;
        }
        catch ( IOException | SlackApiException e ) {
            log.error( "error: {}", e.getMessage(), e );
        }
        return null;
    }
}
