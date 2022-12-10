package kr.isweb.cmmn.config.mybatis;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;
import javax.transaction.UserTransaction;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;

import kr.isweb.cmmn.config.datasource.CmmnXADataSource;
import kr.isweb.cmmn.config.datasource.router.RoutingDataSource;
import kr.isweb.cmmn.config.datasource.router.RoutingKey;

/**
 * 아래와 같은 형태로 현재 파일을 상속받아 구현하는 DataSource 에서
 * Entity 객체를 사용해 자동으로 Message Table 을 생성하여 사용할 수 있음
 *
 * <code>
 * @Bean
 * public EntityManagerFactory entityManagerFactory(DataSource dataSource) {
 * 		HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
 * 		adapter.setGenerateDdl(true);
 *
 * 		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
 * 		em.setJpaVendorAdapter(adapter);
 * 		em.setPackagesToScan("kr.isweb.cmmn.config.message.entity");
 * 		em.setDataSource(dataSource);
 * 		em.afterPropertiesSet();
 *
 * 		Properties jpaProperties = new Properties();
 * 		jpaProperties.setProperty("hibernate.hbm2ddl.auto", "update");
 * 		jpaProperties.setProperty("hibernate.show.sql", "true");
 * 		jpaProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLInnoDBDialect");
 *
 * 		em.setJpaProperties(jpaProperties);
 *
 * 		return em.getObject();
 * }
 * </code>
 */
@EnableTransactionManagement(proxyTargetClass = true)
@MapperScan(value = { "kr.isweb.cmmn.**.mapper" })
public class CmmnMyBatisXADataSource extends CmmnXADataSource {
	@Bean
	@Primary
	public DataSource dataSource() {
		AbstractRoutingDataSource routingDataSources = new RoutingDataSource();
		Map<Object, Object> dataSourceMap = new HashMap<>();

		DataSource mysqlDataSource = mysqlDataSource();
		DataSource oracleDataSource = oracleDataSource();

		dataSourceMap.put(RoutingKey.MYSQL, mysqlDataSource);
		dataSourceMap.put(RoutingKey.ORACLE, oracleDataSource);

		routingDataSources.setTargetDataSources(dataSourceMap);
		routingDataSources.setDefaultTargetDataSource(mysqlDataSource);
		routingDataSources.afterPropertiesSet();

		return new LazyConnectionDataSourceProxy(routingDataSources);
	}

	@Bean
	public TransactionManager transactionManager() {
		JtaTransactionManager transactionManager = new JtaTransactionManager();
		transactionManager.setUserTransaction(atomikosUserTransaction());
		transactionManager.setTransactionManager(atomikosUserTransactionManager());
		transactionManager.setAllowCustomIsolationLevels(Boolean.TRUE);
		return transactionManager;
	}

	@Bean
	public UserTransaction atomikosUserTransaction() {
		UserTransactionImp userTransactoin = new UserTransactionImp();
		return userTransactoin;
	}

	@Bean
	public UserTransactionManager atomikosUserTransactionManager() {
		UserTransactionManager userTransactionManager = new UserTransactionManager();
		userTransactionManager.setForceShutdown(Boolean.TRUE);
		userTransactionManager.setStartupTransactionService(Boolean.TRUE);
		return userTransactionManager;
	}
}
