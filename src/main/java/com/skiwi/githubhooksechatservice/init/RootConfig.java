package com.skiwi.githubhooksechatservice.init;

import java.util.Properties;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.skiwi.githubhooksechatservice.mvc.beans.Statistics;

@Configuration
@EnableTransactionManagement
@ComponentScan("com.skiwi.githubhooksechatservice")
@PropertySource("classpath:application.properties")
public class RootConfig {
	
    private static final String PROPERTY_NAME_DATABASE_DRIVER = "db.driver";
    private static final String PROPERTY_NAME_DATABASE_PASSWORD = "db.password";
    private static final String PROPERTY_NAME_DATABASE_URL = "db.url";
    private static final String PROPERTY_NAME_DATABASE_USERNAME = "db.username";
	
    private static final String PROPERTY_NAME_HIBERNATE_DIALECT = "hibernate.dialect";
    private static final String PROPERTY_NAME_HIBERNATE_SHOW_SQL = "hibernate.show_sql";
    private static final String PROPERTY_NAME_ENTITYMANAGER_PACKAGES_TO_SCAN = "entitymanager.packages.to.scan";
    	
	@Resource
	private Environment env;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public com.skiwi.githubhooksechatservice.mvc.configuration.Configuration config() {
		com.skiwi.githubhooksechatservice.mvc.configuration.Configuration config = new com.skiwi.githubhooksechatservice.mvc.configuration.Configuration();
		config.setBotEmail(env.getRequiredProperty("env.botEmail"));
		config.setBotPassword(env.getRequiredProperty("env.botPassword"));
		config.setChatMaxBurst(Integer.parseInt(env.getRequiredProperty("env.chatMaxBurst")));
		config.setChatMinimumDelay(Integer.parseInt(env.getRequiredProperty("env.chatMinimumDelay")));
		config.setChatThrottle(Integer.parseInt(env.getRequiredProperty("env.chatThrottle")));
		config.setChatUrl(env.getRequiredProperty("env.chatUrl"));
		config.setDailyRooms(env.getRequiredProperty("env.dailyRooms"));
		config.setDeployGreetingEnabled(Boolean.parseBoolean(env.getRequiredProperty("env.deployGreetingEnabled")));
		config.setDeployGreetingText(env.getRequiredProperty("env.deployGreetingText"));
		config.setRoomId(env.getRequiredProperty("env.roomId"));
		config.setRootUrl(env.getRequiredProperty("env.rootUrl"));
		config.setUndeployGoodbyeEnabled(Boolean.parseBoolean(env.getRequiredProperty("env.undeployGoodbyeEnabled")));
		config.setUndeployGoodbyeText(env.getRequiredProperty("env.undeployGoodbyeText"));
		config.setUserMappings(env.getRequiredProperty("env.userMappings"));
		return config;
	}
	
	@Bean
	public MultipartResolver resolver() {
		CommonsMultipartResolver resolver = new CommonsMultipartResolver();
		resolver.setMaxUploadSize(-1);
		return resolver;
	}
	
	@Bean
	public Statistics stats() {
		return new Statistics();
	}
	
//	@Bean
//	public StartupBean startUp() {
//		return new StartupBean();
//	}
	
	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		
		dataSource.setDriverClassName(env.getRequiredProperty(PROPERTY_NAME_DATABASE_DRIVER));
		dataSource.setUrl(env.getRequiredProperty(PROPERTY_NAME_DATABASE_URL));
		dataSource.setUsername(env.getRequiredProperty(PROPERTY_NAME_DATABASE_USERNAME));
		dataSource.setPassword(env.getRequiredProperty(PROPERTY_NAME_DATABASE_PASSWORD));
		
		return dataSource;
	}
	
	@Bean
	public LocalSessionFactoryBean sessionFactory() {
		LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
		sessionFactoryBean.setDataSource(dataSource());
		sessionFactoryBean.setPackagesToScan(env.getRequiredProperty(PROPERTY_NAME_ENTITYMANAGER_PACKAGES_TO_SCAN));
		sessionFactoryBean.setHibernateProperties(hibProperties());
		return sessionFactoryBean;
	}
	
	private Properties hibProperties() {
		Properties properties = new Properties();
		properties.put("hbm2ddl.auto", "update");
		properties.put("hibernate.hbm2ddl.auto", "update");
		properties.put(PROPERTY_NAME_HIBERNATE_DIALECT, env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_DIALECT));
		properties.put(PROPERTY_NAME_HIBERNATE_SHOW_SQL, env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_SHOW_SQL));
		return properties;	
	}
	
	@Bean
	public HibernateTransactionManager transactionManager() {
		HibernateTransactionManager transactionManager = new HibernateTransactionManager();
		transactionManager.setSessionFactory(sessionFactory().getObject());
		return transactionManager;
	}
}
