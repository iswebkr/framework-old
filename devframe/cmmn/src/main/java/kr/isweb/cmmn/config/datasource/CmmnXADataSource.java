package kr.isweb.cmmn.config.datasource;

import java.util.Properties;

import javax.sql.DataSource;

import com.atomikos.jdbc.AtomikosDataSourceBean;

import kr.isweb.cmmn.config.datasource.router.RoutingKey;
import kr.isweb.cmmn.config.datasource.transaction.CmmnTransactionAdvice;

public class CmmnXADataSource extends CmmnTransactionAdvice {
	protected DataSource mysqlDataSource() {
		AtomikosDataSourceBean  dataSource = new AtomikosDataSourceBean();

		Properties properties = new Properties();
		properties.setProperty("user",  env.getProperty("spring.jta.atomikos.datasource.mysql.username"));
		properties.setProperty("password",  env.getProperty("spring.jta.atomikos.datasource.mysql.password"));
		properties.setProperty("URL",  env.getProperty("spring.jta.atomikos.datasource.mysql.url"));

		dataSource.setXaDataSourceClassName(env.getProperty("spring.jta.atomikos.datasource.mysql.driver-class-name"));
		dataSource.setUniqueResourceName(RoutingKey.MYSQL.name());
		dataSource.setPoolSize(2);
		dataSource.setMaxPoolSize(10);
		dataSource.setXaProperties(properties);

		return dataSource;
	}

	protected DataSource oracleDataSource() {
		AtomikosDataSourceBean  dataSource = new AtomikosDataSourceBean();

		Properties properties = new Properties();
		properties.setProperty("user",  env.getProperty("spring.jta.atomikos.datasource.oracle.username"));
		properties.setProperty("password",  env.getProperty("spring.jta.atomikos.datasource.oracle.password"));
		properties.setProperty("URL",  env.getProperty("spring.jta.atomikos.datasource.oracle.url"));

		dataSource.setXaDataSourceClassName(env.getProperty("spring.jta.atomikos.datasource.oracle.driver-class-name"));
		dataSource.setUniqueResourceName(RoutingKey.ORACLE.name());
		dataSource.setPoolSize(2);
		dataSource.setXaProperties(properties);

		return dataSource;
	}
}
