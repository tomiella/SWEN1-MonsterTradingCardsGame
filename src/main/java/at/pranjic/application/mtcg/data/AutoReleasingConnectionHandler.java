package at.pranjic.application.mtcg.data;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;

public class AutoReleasingConnectionHandler implements InvocationHandler {
    private final Connection connection;
    private final ConnectionPool connectionPool;

    public AutoReleasingConnectionHandler(Connection connection, ConnectionPool connectionPool) {
        this.connection = connection;
        this.connectionPool = connectionPool;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("close".equals(method.getName())) {
            // Release the connection back to the pool instead of closing it
            connectionPool.releaseConnection(connection);
            return null;
        }
        return method.invoke(connection, args);
    }

    public static Connection wrap(Connection connection, ConnectionPool connectionPool) {
        return (Connection) Proxy.newProxyInstance(
                connection.getClass().getClassLoader(),
                new Class[]{Connection.class},
                new AutoReleasingConnectionHandler(connection, connectionPool)
        );
    }
}
