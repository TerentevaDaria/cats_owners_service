package ru.dterenteva.owner;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@EnableRabbit
//@Configuration
//public class RabbitConfiguration {
//
//    @Bean
//    public ConnectionFactory connectionFactory() {
//        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost");
//        connectionFactory.setUsername("user");
//        connectionFactory.setPassword("user");
//        return connectionFactory;
//    }
//
////    @Bean
////    public AmqpAdmin amqpAdmin() {
////        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory());
////        return rabbitAdmin;
////    }
//    @Bean
//    public DirectExchange directExchange() {
//        return new DirectExchange("ownerExchange");
//    }
//
//    @Bean
//    public Binding receiveBinding(Queue queue) {
//        return BindingBuilder.bind(queue).to(directExchange()).with("owner");
//    }
//
//    @Bean
//    public MessageConverter jsonMessageConverter() {
//        DefaultClassMapper defaultClassMapper = new DefaultClassMapper();
//        defaultClassMapper.setTrustedPackages("ru.dterenteva.*");
//        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
//        jackson2JsonMessageConverter.setClassMapper(defaultClassMapper);
//        return jackson2JsonMessageConverter;
//    }
//
////    @Bean
////    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
////        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
////        rabbitTemplate.setMessageConverter(jsonMessageConverter());
////        return rabbitTemplate;
////    }
//
//    @Bean
//    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
//        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
//        factory.setConnectionFactory(connectionFactory);
//        factory.setMessageConverter(jsonMessageConverter());
//        return factory;
//    }
//
//    @Bean
//    public Queue getOwnerQueue() {
//        return new Queue("getOwnerQueue");
//    }
//}
@Configuration
@EnableRabbit
public class RabbitConfiguration {
    public static final String EXCHANGE_NAME = "ownerExchange";

//    @Bean
//    public DirectExchange exchange() {
//        return new DirectExchange(EXCHANGE_NAME);
//    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost");
        connectionFactory.setUsername("user");
        connectionFactory.setPassword("user");
        return connectionFactory;
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        return factory;
    }

    @Bean
    public Queue getOwnerQueue() {
        return new Queue("getOwnerQueue");
    }

    @Bean
    public Queue getCatQueue() {
        return new Queue("getCatQueue");
    }

    @Bean
    public Queue getCatsByOwner() {
        return new Queue("getCatsByOwner");
    }

    @Bean
    public Queue getAllOwners() {
        return new Queue("getAllOwners");
    }

    @Bean
    public Queue deleteOwner() {
        return new Queue("deleteOwner");
    }

    @Bean
    public Queue addOwner() {
        return new Queue("addOwner");
    }

    @Bean Queue updateOwner() {
        return new Queue("updateOwner");
    }
}