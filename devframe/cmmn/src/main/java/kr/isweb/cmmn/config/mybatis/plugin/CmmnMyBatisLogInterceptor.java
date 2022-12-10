package kr.isweb.cmmn.config.mybatis.plugin;

import java.lang.reflect.Field;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;

import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

@Intercepts({
	@Signature(type = StatementHandler.class, method = "query", args = { Statement.class, ResultHandler.class }),
	@Signature(type = StatementHandler.class, method = "update", args = { Statement.class }), })
public class CmmnMyBatisLogInterceptor implements Interceptor {

	private static final Logger logger = LoggerFactory.getLogger(CmmnMyBatisLogInterceptor.class);

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Object result = null;
		RoutingStatementHandler handler = (RoutingStatementHandler) invocation.getTarget();
		StatementHandler delegate = (StatementHandler) getFieldValue(handler, "delegate");
		MappedStatement mappedStatement = (MappedStatement) getFieldValue(delegate, "mappedStatement");

		Object parameterObject = delegate.getParameterHandler().getParameterObject();
		Configuration configuration = mappedStatement.getConfiguration();
		BoundSql boundSql = mappedStatement.getBoundSql(parameterObject);

		String sql = getSql(boundSql, parameterObject, configuration);

		String queryInfo = "\n";
		queryInfo += "***********************************************************************************\n";
		queryInfo += "RESOURCE\t: " + getResourceFileName(mappedStatement.getResource()) + "\n";
		queryInfo += "METHOD\t\t: " + invocation.getMethod().getName() + "\n";
		queryInfo += "STATEMENT TYPE\t: " + mappedStatement.getStatementType() + "\n";
		queryInfo += "COMMAND TYPE\t: " + mappedStatement.getSqlCommandType() + "\n";
		queryInfo += "DATABASE ID\t: " + mappedStatement.getDatabaseId() + "\n";
		queryInfo += "QUERY ID\t: " + mappedStatement.getId() + "\n";
		queryInfo += "===================================================================================\n";
		queryInfo += "\t\t" + sql + "\n";
		queryInfo += "===================================================================================";

		logger.info("{}", queryInfo);

		StopWatch timer = new StopWatch();
		timer.start();
		result = invocation.proceed();
		timer.stop();

		logger.info(">>> Totaly query execution time : {} Seconds", timer.getTotalTimeSeconds());

		return result;
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	private String getSql(BoundSql boundSql, Object parameterObject, Configuration configuration) {
		String sql = boundSql.getSql();
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();

		if(parameterMappings != null) {
			for (ParameterMapping parameterMapping : parameterMappings) {
				if(parameterMapping.getMode() != ParameterMode.OUT) {
					Object value;
					String propertyName = parameterMapping.getProperty();
					if (boundSql.hasAdditionalParameter(propertyName)) {
						value = boundSql.getAdditionalParameter(propertyName);
					} else if (parameterObject == null) {
						value = null;
					} else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
						value = parameterObject;
					} else {
						MetaObject metaObject = configuration.newMetaObject(parameterObject);
						value = metaObject.getValue(propertyName);
					}
					sql = replacePlaceholder(sql, value);
				}
			}
		}

		return sql;
	}

	private String replacePlaceholder(String sql, Object propertyValue) {
		String result;
		DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (propertyValue != null) {
			if (propertyValue instanceof String) {
				result = "'" + propertyValue + "'";
			} else if (propertyValue instanceof Date) {
				result = "'" + DATE_FORMAT.format(propertyValue) + "'";
			} else {
				result = propertyValue.toString();
			}
		} else {
			result = "null";
		}
		return sql.replaceFirst("\\?", Matcher.quoteReplacement(result));
	}

	private Object getFieldValue(Object obj, String fieldName) {
		Object result = null;
		Field field = getObjectField(obj.getClass(), fieldName);
		if (field != null) {
			field.setAccessible(true);
			try {
				result = field.get(obj);
			} catch (IllegalArgumentException e) {
				logger.error(e.getLocalizedMessage());
			} catch (IllegalAccessException e) {
				logger.error(e.getLocalizedMessage());
			}
		}
		return result;
	}

	private Field getObjectField(Class<?> clazz, String fieldName) {
		Class<?> tmpClass = clazz;
		do {
			for (Field field : tmpClass.getDeclaredFields()) {
				String canfidateName = field.getName();
				if (!canfidateName.equals(fieldName)) {
					continue;
				}
				field.setAccessible(true);
				return field;
			}
			tmpClass = tmpClass.getSuperclass();
		} while (clazz != null);
		return null;
	}

	private String getResourceFileName(String resourcePath) {
		return resourcePath.substring(resourcePath.lastIndexOf("/") + 1, resourcePath.lastIndexOf("]"));
	}
}
