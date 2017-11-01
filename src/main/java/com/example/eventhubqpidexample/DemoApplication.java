package com.example.eventhubqpidexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.jms.MessageListener;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import org.apache.qpid.jms.JmsConnectionFactory;
import org.apache.qpid.jms.JmsQueue;
import org.springframework.beans.factory.annotation.Value;
import java.util.Map;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class DemoApplication implements CommandLineRunner {
    @Value("${azure.eventHub.policyName}")
    private String policyName;

    @Value("${azure.eventHub.policyKey}")
    private String policyKey;

    @Value("${azure.eventHub.domainName}")
    private String domainName;

    @Value("${azure.eventHub.queue.0}")
    private String queue0;

    @Value("${azure.eventHub.queue.1}")
    private String queue1;


	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

    @Override
    public void run(String... args) throws Exception {
        final String uri = makeEventHubUri();
        log.info("Attempting to connection to Event Hub with URI {}", uri);

        final ConnectionFactory factory = new JmsConnectionFactory(uri);
        final Connection connection = factory.createConnection();
        connection.start();

        final Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        attachExceptionListenerToSession(connection);
        attachListenersToSession(session);
    }

    private String makeEventHubUri() {
        final String template = "amqps://%s.servicebus.windows.net/?jms.username=%s&jms.password=%s&amqp.idleTimeout=1200000";
        return String.format(template, domainName, policyName, policyKey);
    }

    private void attachListenersToSession(final Session session) throws JMSException {
        attachListenerToSession(queue0, session);
        attachListenerToSession(queue1, session);
    }

    private void attachListenerToSession(final String queue, final Session session) throws JMSException {
        final Destination destination = new JmsQueue(queue);
        final MessageConsumer messageConsumer = session.createConsumer(destination);

        messageConsumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(final Message message) {
                log.info("Triggered message listener.");

                try {
                    log.info("Received message: {}", ((TextMessage)message).getText());
                } catch (JMSException e) {
                    log.error("Error attempting to examine message.", e);
                }
            }
        });
    }

    private void attachExceptionListenerToSession(final Connection connection) throws JMSException {
        connection.setExceptionListener(new ExceptionListener() {
            @Override
            public void onException(final JMSException exception) {
                log.error("Error: {}", exception);
            }
        });
    }
}
