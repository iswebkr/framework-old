package kr.isweb.cmmn.config.mybatis.config;

import java.util.HashSet;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.ibatis.session.AutoMappingBehavior;
import org.apache.ibatis.session.AutoMappingUnknownColumnBehavior;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.LocalCacheScope;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import kr.isweb.cmmn.config.mybatis.plugin.CmmnMyBatisLogInterceptor;

public class CmmnMyBatisConfiguration {

	@Autowired
	protected Environment env;

	protected Configuration mybatisConfiguration() {
		Configuration configuration = new Configuration();
		Set<String> lazyLoadTriggerMethods = new HashSet<>();
		lazyLoadTriggerMethods.add("equals");
		lazyLoadTriggerMethods.add("clone");
		lazyLoadTriggerMethods.add("hashCode");
		lazyLoadTriggerMethods.add("toString");

		configuration.setMapUnderscoreToCamelCase(Boolean.TRUE);
		configuration.setCacheEnabled(Boolean.TRUE);
		configuration.setLazyLoadingEnabled(Boolean.TRUE);
		configuration.setMultipleResultSetsEnabled(Boolean.TRUE);
		configuration.setUseColumnLabel(Boolean.TRUE);
		configuration.setUseGeneratedKeys(Boolean.FALSE);
		configuration.setAutoMappingBehavior(AutoMappingBehavior.PARTIAL);
		configuration.setAutoMappingUnknownColumnBehavior(AutoMappingUnknownColumnBehavior.NONE);
		configuration.setDefaultExecutorType(ExecutorType.SIMPLE);
		configuration.setDefaultStatementTimeout(25);
		configuration.setDefaultFetchSize(100);
		configuration.setSafeRowBoundsEnabled(Boolean.FALSE);
		configuration.setMapUnderscoreToCamelCase(Boolean.TRUE);
		configuration.setLocalCacheScope(LocalCacheScope.SESSION);
		configuration.setJdbcTypeForNull(JdbcType.NULL);
		configuration.setLazyLoadTriggerMethods(lazyLoadTriggerMethods);
		return configuration;
	}

	@Bean
	public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Throwable {
		SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
		sessionFactory.setDataSource(dataSource);
		sessionFactory.setTransactionFactory(new SpringManagedTransactionFactory());
		sessionFactory.setMapperLocations(pathMatchingResourcePatternResolver.getResources("classpath*:/**/*.sql.xml"));
		sessionFactory.setConfiguration(mybatisConfiguration());
		sessionFactory.setPlugins(new CmmnMyBatisLogInterceptor());
		return sessionFactory.getObject();
	}

	@Bean
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) throws Throwable {
		SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory);
		return sqlSessionTemplate;
	}
}
