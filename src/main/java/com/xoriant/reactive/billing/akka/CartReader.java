package com.xoriant.reactive.billing.akka;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.xoriant.reactive.billing.akka.models.Order;
import com.xoriant.reactive.billing.util.Mongo;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.actor.UntypedActor;

public class CartReader extends UntypedActor {

	public CartReader() {
		
		ActorSelection selection = Application.system().actorSelection("akka.tcp://ClusterSystem@10.20.3.84:2551/user/billingService");

		try {
			String QUEUE_NAME = "orders";
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("10.20.3.90");
			factory.setUsername("test");
			factory.setPassword("test");
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();

			channel.queueDeclare(QUEUE_NAME, false, false, false, null);
			System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

			Consumer consumer = new DefaultConsumer(channel) {
				public void handleDelivery(String consumerTag,
						Envelope envelope, AMQP.BasicProperties properties,
						byte[] body) throws IOException {

					String message = new String(body, "UTF-8");
					Order order = Mongo.getEntityFromJson(message, Order.class);
					
					selection.tell(order, getSelf());
				}
			};
			channel.basicConsume(QUEUE_NAME, true, consumer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onReceive(Object message) throws Exception {
	}

}
