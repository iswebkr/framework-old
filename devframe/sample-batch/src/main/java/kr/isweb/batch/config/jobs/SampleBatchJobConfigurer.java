package kr.isweb.batch.config.jobs;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

public class SampleBatchJobConfigurer {

	@Autowired
	JobBuilderFactory jobs;

	@Autowired
	StepBuilderFactory steps;

	@Bean
	protected Step sampleStep1() {
		return steps.get("step01").tasklet((contribution, chunkContext)->{
			System.out.println(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
			return RepeatStatus.FINISHED;
		}).build();
	}

	@Bean(name="sampleJob")
	public Job sampleJob() {
		return jobs.get("sampleJob").start(sampleStep1()).build();
	}
}
