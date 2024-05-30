package com.javatechie.redis.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.javatechie.redis.subscriber.Receiver;

@Configuration
public class RedisConfig {

	@Bean
	public JedisConnectionFactory connectionFactory() {
		RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
		configuration.setHostName("localhost");
		configuration.setPort(6379);
		return new JedisConnectionFactory(configuration);
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory());
//		template.setKeySerializer(new StringRedisSerializer());
//		template.setHashKeySerializer(new StringRedisSerializer());
		template.setHashKeySerializer(new JdkSerializationRedisSerializer());
		template.setValueSerializer(new GenericToStringSerializer<Object>(Object.class));
//		template.setEnableTransactionSupport(true);
//		template.afterPropertiesSet();

		return template;
	}

	// given the topic name where the message is published and receiver
	@Bean
	public ChannelTopic topic() {
		return new ChannelTopic("pubsub-javatechie-channel");
	}

	//configure the class from where the message is consumed
	@Bean
	public MessageListenerAdapter messageListenerAdapter() {
		return new MessageListenerAdapter(new Receiver());
	}

	//Configure the container who can manage the above Topic and Adapter
	@Bean
	public RedisMessageListenerContainer redisMessageListenerContainer() {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory());
		container.addMessageListener(messageListenerAdapter(), topic());
		return container;
	}
}
