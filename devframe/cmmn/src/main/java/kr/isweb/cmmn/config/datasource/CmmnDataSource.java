package kr.isweb.cmmn.config.datasource;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

import kr.isweb.cmmn.config.datasource.transaction.CmmnTransactionAdvice;

public class CmmnDataSource extends CmmnTransactionAdvice {
	protected DataSource mysqlDataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(env.getProperty("spring.datasource.mysql.driver-class-name"));
		dataSource.setUrl(env.getProperty("spring.datasource.mysql.url"));
		dataSource.setUsername(env.getProperty("spring.datasource.mysql.username"));
		dataSource.setPassword(env.getProperty("spring.datasource.mysql.password"));
		dataSource.setMaxIdle(10);
		dataSource.setMaxWaitMillis(1000);
		dataSource.setDefaultAutoCommit(Boolean.TRUE);
		dataSource.setTestOnBorrow(Boolean.TRUE);
		dataSource.setValidationQuery("SELECT 1");
		dataSource.setTestWhileIdle(Boolean.TRUE);
		dataSource.setNumTestsPerEvictionRun(1);
		dataSource.setTimeBetweenEvictionRunsMillis(3000);
		return dataSource;
	}

	protected DataSource oracleDataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(env.getProperty("spring.datasource.oracle.driver-class-name"));
		dataSource.setUrl(env.getProperty("spring.datasource.oracle.url"));
		dataSource.setUsername(env.getProperty("spring.datasource.oracle.username"));
		dataSource.setPassword(env.getProperty("spring.datasource.oracle.password"));
		dataSource.setMaxIdle(10);
		dataSource.setMaxWaitMillis(1000);
		dataSource.setDefaultAutoCommit(Boolean.TRUE);
		dataSource.setTestOnBorrow(Boolean.TRUE);
		dataSource.setValidationQuery("SELECT 1");
		dataSource.setTestWhileIdle(Boolean.TRUE);
		dataSource.setNumTestsPerEvictionRun(1);
		dataSource.setTimeBetweenEvictionRunsMillis(3000);
		return dataSource;
	}
}
