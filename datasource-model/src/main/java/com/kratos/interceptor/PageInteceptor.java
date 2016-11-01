package com.kratos.interceptor;

import com.kratos.model.Page;
import org.apache.ibatis.binding.MapperMethod.ParamMap;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;


/**
 * @author zengping
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class})})
public class PageInteceptor implements Interceptor {

	@SuppressWarnings({ "rawtypes" })
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		StatementHandler statementHandler = (StatementHandler) invocation
				.getTarget();
		BoundSql targetSql = statementHandler.getBoundSql();
		Object paramObj = targetSql.getParameterObject();
		Page page = null;
		if (paramObj instanceof Page) {
			page = (Page) paramObj;
		} else if (paramObj instanceof ParamMap) {
			for (Object obj : ((ParamMap) paramObj).values()) {
				if (obj instanceof Page) {
					page = (Page) obj;
					break;
				}
			}
		} else {
			return invocation.proceed();
		}

		if (null == page) {
			return invocation.proceed();
		}
		Connection connection = (Connection) invocation.getArgs()[0];
		MetaObject metaStatementHandler = SystemMetaObject.forObject(statementHandler);
		MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");
		executePageCount(connection, mappedStatement, targetSql, page);
	    String pageSql = this.buildPageSql(targetSql, page);
	    metaStatementHandler.setValue("delegate.boundSql.sql", pageSql);
	    Object obj = invocation.proceed();
	    return obj;
	}

	@SuppressWarnings({ "rawtypes" })
	private void executePageCount(Connection connection, MappedStatement mappedStatement, BoundSql boundSql, Page page) {
		String countSql = buildCountSql(boundSql);
		PreparedStatement countStmt = null;
		ResultSet rs = null;
		try {
			countStmt = connection.prepareStatement(countSql);
			BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(),countSql, boundSql.getParameterMappings(),
					boundSql.getParameterObject());
			ParameterHandler parameterHandler = new DefaultParameterHandler(
					mappedStatement, boundSql.getParameterObject(), countBS);
			parameterHandler.setParameters(countStmt);
			rs = countStmt.executeQuery();
			rs.next();
			int totalCount = rs.getInt(1);
			page.setTotal(totalCount);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				countStmt.close();
			} catch (SQLException e) {
			}
		}
	}

	private String buildPageSql(BoundSql sql, Page<?> page) {
		int start = page.getStart();
		int limit = page.getSize();
		StringBuilder pageSql = new StringBuilder(sql.getSql().trim());
		pageSql.append(" LIMIT ").append(start).append(", ").append(limit);
		return pageSql.toString();
	}

	private String buildCountSql(BoundSql sql) {
		StringBuilder countSql = new StringBuilder(sql.getSql().trim());
		countSql.insert(0, "SELECT COUNT(0) FROM (");
		countSql.append(") AS _PAGE_TBL");
		return countSql.toString();
	}

	@Override
	public Object plugin(Object target) {
		if (target instanceof StatementHandler) {
			return Plugin.wrap(target, this);
		} else {
			return target;
		}

	}

	@Override
	public void setProperties(Properties properties) {}

}
