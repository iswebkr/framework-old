package kr.isweb.batch.config.init;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import kr.isweb.cmmn.config.mybatis.CmmnMyBatisDataSource;

@Configuration
@EnableBatchProcessing
@PropertySource(value = {
		"classpath:application-${spring.profiles.active}.properties"
})
@MapperScan(value = { "kr.isweb.batch.**.mapper" })
public class SampleBatchConfiguration extends CmmnMyBatisDataSource {

	@Value("org/springframework/batch/core/schema-drop-mysql.sql")
	private Resource dropReopsitoryTables;

	@Value("org/springframework/batch/core/schema-mysql.sql")
	private Resource dataReopsitorySchema;

	@Bean
	public DataSourceInitializer dataSourceInitializer() {
		ResourceDatabasePopulator databasePopulcator = new ResourceDatabasePopulator();
		databasePopulcator.addScript(dropReopsitoryTables);
		databasePopulcator.addScript(dataReopsitorySchema);
		databasePopulcator.setIgnoreFailedDrops(true);

		DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
		dataSourceInitializer.setDataSource(dataSource());
		dataSourceInitializer.setDatabasePopulator(databasePopulcator);

		return dataSourceInitializer;
	}

	@Bean
	public JobLauncher getJobLauncher() {
		SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
		jobLauncher.setJobRepository(_jobRepository());
		return jobLauncher;
	}

	private JobRepository _jobRepository() {
		JobRepository jobRepository = null;
		JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
		try {
			factory.setDataSource(dataSource());
			factory.setTransactionManager(new ResourcelessTransactionManager());
			factory.afterPropertiesSet();
			jobRepository = (JobRepository) factory.getObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jobRepository;
	}
}
