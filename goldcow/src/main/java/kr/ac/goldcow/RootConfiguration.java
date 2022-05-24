package kr.ac.goldcow;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebMvc
@PropertySource("classpath:environment.properties")
@EnableJpaRepositories(
		basePackages = "kr.ac.goldcow.repository",
		entityManagerFactoryRef = "entityManagerFactory",
		transactionManagerRef = "jpaTransactionManager"
		)
@EnableTransactionManagement
public class RootConfiguration extends WebMvcConfigurerAdapter  {
	@Autowired
	private Environment env;
	@Bean
	public DataSource dataSource() {
	DriverManagerDataSource dataSource = new DriverManagerDataSource ();
	dataSource.setDriverClassName(env.getProperty("dataSource.driverClassName"));
	dataSource.setUrl(env.getProperty("dataSource.url"));
	dataSource.setUsername(env.getProperty("dataSource.username"));
	dataSource.setPassword((env.getProperty("dataSource.password")));
	return dataSource;
	}
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
	LocalContainerEntityManagerFactoryBean lef =
	new LocalContainerEntityManagerFactoryBean();
	lef.setDataSource(dataSource());
	lef.setJpaVendorAdapter(jpaVendorAdapter());
	lef.setPackagesToScan("kr.ac.goldcow.model");
	lef.afterPropertiesSet();
	return lef;
	}
	@Bean
	public PlatformTransactionManager jpaTransactionManager() {
	JpaTransactionManager jpaTransactionManager =
	new JpaTransactionManager();
	jpaTransactionManager.setEntityManagerFactory(
	entityManagerFactory().getObject());
	return jpaTransactionManager;
	}
	@Bean
	public JpaVendorAdapter jpaVendorAdapter() {
	HibernateJpaVendorAdapter hibernateJpaVendorAdapter =
	new HibernateJpaVendorAdapter();
	hibernateJpaVendorAdapter.setDatabase(Database.MYSQL);
	return hibernateJpaVendorAdapter;
	}
}
