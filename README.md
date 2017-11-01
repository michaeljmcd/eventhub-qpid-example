# eventhub-qpid-example

This repo has a minimal example for connecting to Azure Event Hubs
(<https://azure.microsoft.com/en-us/services/event-hubs/>) over the AMQP
protocol
(<https://docs.microsoft.com/en-us/azure/service-bus-messaging/service-bus-amqp-protocol-guide>)
using Apache Qpid's (<https://qpid.apache.org/>) JMS connector.

The command line application itself is written in Java and Spring Boot, though
this is unlikely to be important.

The connection settings are specified under `application.properties`, so be
sure set these before running the example.

The example connects to the two partitions of a single consumer group. The
properties used are:

    azure.eventHub.policyName 
    azure.eventHub.policyKey 
    azure.eventHub.domainName 
    azure.eventHub.queue.0 
    azure.eventHub.queue.1

The easiest way to provide these values is to examine your Event Hub namespace
in the Azure portal. After navigating to the Event Hub namespace, pull up the
"Connection Strings" list off of the main page. `azure.eventHub.policyName` will be the name
of the policy listed on that page. Typical examples are "Producer" and "Consumer". Since this
example reads from the hub, "Consumer" is generally the one you want.

`azure.eventHub.policyKey` is the key corresponding to the policy. Click on the policy
and copy either the primary or secondary key.

For `azure.eventHub.domainName`, examine your connection string beneath the keys and look for
the subdomain of `servicebus.windows.net` this will be the value here.

The two queue values are the combination of event hub, consumer group and partition to use. 
They aren't queues, in the traditional sense, but a combination of parameters. The general
format is:

    <event hub>/ConsumerGroups/<Consumer Group - $Default is common>/Partions/<Partition number - 0 or 1 by default> 

The example itself is licensed under the MIT license.
