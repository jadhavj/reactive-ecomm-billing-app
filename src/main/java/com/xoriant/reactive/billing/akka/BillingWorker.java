package com.xoriant.reactive.billing.akka;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.xoriant.reactive.billing.akka.models.Order;
import com.xoriant.reactive.billing.util.Mongo;

import akka.actor.UntypedActor;

public class BillingWorker extends UntypedActor {

	Map<String, Integer> cache = new HashMap<String, Integer>();

	@Override
	public void onReceive(Object message) throws JsonProcessingException {
		if (message instanceof Order) {
			Order order = (Order) message;

			// Mark order as pending
			order.setStatus("pending");
			Mongo.datastore().save(order);

			// Ship the ordered products
			order.setStatus("shipping");
			Mongo.datastore().save(order);

			// Mark order as shipped
			order.setStatus("shipped");
			Mongo.datastore().save(order);

			// Mark the order as processed
			order.setStatus("processed");
			Mongo.datastore().save(order);
		} else {
			unhandled(message);
		}
	}

}