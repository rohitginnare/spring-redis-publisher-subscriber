package com.javatechie.redis.publisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javatechie.redis.dto.Product;

@RestController
public class Publisher {

	@Qualifier("redisTemplate")
	@Autowired
	private RedisTemplate template;
	
	@Autowired
	private ChannelTopic topic;

	@RequestMapping("/publish")
	public String publish(@RequestBody Product product) {
		template.convertAndSend(topic.getTopic(), product.toString());

		return "Event Published";

	}

}
