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
import javax.jms.MessageConsumer;
import javax.jms.Session;
import org.apache.qpid.jms.JmsConnectionFactory;
import org.springframework.beans.factory.annotation.Value;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

    @Override
    public void run(String... args) throws Exception {
        final String uri = makeEventHubUri();
        final ConnectionFactory factory = new JmsConnectionFactory(uri);
        //final Destination queue = (Destination) context.lookup("myQueueLookup");

        final Connection connection = factory.createConnection(policyName, policyKey);
        //connection.setExceptionListener(new MyExceptionListener());
        connection.start();

        final Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        //final MessageConsumer messageConsumer = session.createConsumer(queue);
    }

    @Value("${azure.eventHub.policyName}")
    private String policyName;

    @Value("${azure.eventHub.policyKey}")
    private String policyKey;

    @Value("${azure.eventHub.domainName}")
    private String domainName;

    private String makeEventHubUri() {
        final String template = "amqps://%s.servicebus.windows.net";
        return String.format(template, domainName);
    }
}
