package kr.isweb.sample.config.mybatis;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import kr.isweb.cmmn.config.mybatis.CmmnMyBatisDataSource;

@Configuration
@MapperScan(value = { "kr.isweb.sample.**.mapper" })
public class SampleMyBatisDataSource extends CmmnMyBatisDataSource {
	@Bean
	public EntityManagerFactory entityManagerFactory(DataSource dataSource) {
		HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		adapter.setGenerateDdl(true);

		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setJpaVendorAdapter(adapter);
		em.setPackagesToScan("kr.isweb.cmmn.config.message.entity");
		em.setDataSource(dataSource);
		em.afterPropertiesSet();

		Properties jpaProperties = new Properties();
		jpaProperties.setProperty("hibernate.hbm2ddl.auto", "update");
		jpaProperties.setProperty("hibernate.show.sql", "true");
		jpaProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLInnoDBDialect");

		em.setJpaProperties(jpaProperties);

		return em.getObject();
	}
}
