package kr.isweb.cmmn.config.datasource.transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import kr.isweb.cmmn.config.mybatis.config.CmmnMyBatisConfiguration;

@Aspect
@EnableAspectJAutoProxy
public class CmmnTransactionAdvice extends CmmnMyBatisConfiguration {
	@Bean
	public TransactionInterceptor txAdvice(TransactionManager transactionManager) {
		TransactionInterceptor txAdvice = new TransactionInterceptor();
		List<RollbackRuleAttribute> rollbackRules = new ArrayList<>();
		rollbackRules.add(new RollbackRuleAttribute(Exception.class));

		// readonly Transaction attribute
		RuleBasedTransactionAttribute readonlyTransactionAttribute = new RuleBasedTransactionAttribute();
		readonlyTransactionAttribute.setReadOnly(Boolean.TRUE);
		readonlyTransactionAttribute.setIsolationLevel(TransactionDefinition.ISOLATION_DEFAULT);
		readonlyTransactionAttribute.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

		// write Transaction attribute
		RuleBasedTransactionAttribute writeTransactionAttribute = new RuleBasedTransactionAttribute();
		writeTransactionAttribute.setIsolationLevel(TransactionDefinition.ISOLATION_DEFAULT);
		writeTransactionAttribute.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		writeTransactionAttribute.setRollbackRules(rollbackRules);

		// transactionAttributes
		Properties transactionAttributes = new Properties();

		// readonly transactionAttributes
		transactionAttributes.setProperty("select*",  readonlyTransactionAttribute.toString());
		transactionAttributes.setProperty("get*",  readonlyTransactionAttribute.toString());
		transactionAttributes.setProperty("search*",  readonlyTransactionAttribute.toString());
		transactionAttributes.setProperty("retrieve*",  readonlyTransactionAttribute.toString());
		transactionAttributes.setProperty("find*",  readonlyTransactionAttribute.toString());
		transactionAttributes.setProperty("read*",  readonlyTransactionAttribute.toString());
		transactionAttributes.setProperty("count*",  readonlyTransactionAttribute.toString());
		transactionAttributes.setProperty("list*",  readonlyTransactionAttribute.toString());

		// write transactionAttributes
		transactionAttributes.setProperty("do*",  writeTransactionAttribute.toString());
		transactionAttributes.setProperty("export*",  writeTransactionAttribute.toString());
		transactionAttributes.setProperty("insert*",  writeTransactionAttribute.toString());
		transactionAttributes.setProperty("write*",  writeTransactionAttribute.toString());
		transactionAttributes.setProperty("save*",  writeTransactionAttribute.toString());
		transactionAttributes.setProperty("add*",  writeTransactionAttribute.toString());
		transactionAttributes.setProperty("create*",  writeTransactionAttribute.toString());
		transactionAttributes.setProperty("regist*",  writeTransactionAttribute.toString());
		transactionAttributes.setProperty("set*",  writeTransactionAttribute.toString());
		transactionAttributes.setProperty("update*",  writeTransactionAttribute.toString());
		transactionAttributes.setProperty("modify*",  writeTransactionAttribute.toString());
		transactionAttributes.setProperty("edit*",  writeTransactionAttribute.toString());
		transactionAttributes.setProperty("change*",  writeTransactionAttribute.toString());
		transactionAttributes.setProperty("delete*",  writeTransactionAttribute.toString());
		transactionAttributes.setProperty("remove*",  writeTransactionAttribute.toString());
		transactionAttributes.setProperty("terminate*",  writeTransactionAttribute.toString());
		transactionAttributes.setProperty("drop*",  writeTransactionAttribute.toString());

		txAdvice.setTransactionAttributes(transactionAttributes);
		txAdvice.setTransactionManager(transactionManager);

		return txAdvice;
	}

	@Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

	@Bean
	public Advisor txAdviceAdvisor(TransactionInterceptor txAdvice) {
		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		pointcut.setExpression("within(@org.springframework.stereotype.Service *)");
		return new DefaultPointcutAdvisor(pointcut, txAdvice);
	}
}
