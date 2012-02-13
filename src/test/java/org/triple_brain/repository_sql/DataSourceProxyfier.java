package org.triple_brain.repository_sql;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * @author Vincent Blouin
 */
public class DataSourceProxyfier {

    private DataSourceProxyfier() {
    }

    @SuppressWarnings({"unchecked"})
    static DataSource proxify(final DataSource ds) {
        final Constructor<Connection> proxyCtor;
        try {
            Class<Connection> proxyClass = (Class<Connection>) Proxy.getProxyClass(Thread.currentThread().getContextClassLoader(), new Class<?>[]{Connection.class});
            proxyCtor = proxyClass.getConstructor(InvocationHandler.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return (DataSource) Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                new Class<?>[]{DataSource.class, Marker.class},
                new InvocationHandler() {
                    final Map<Connection, Throwable> opened = new IdentityHashMap<Connection, Throwable>();

                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        if (method.getName().equals("getOpenedConnections")) {
                            return opened;
                        }
                        Object o = method.invoke(ds, args);
                        if (method.getName().equals("getConnection")) {
                            Throwable t = new Throwable();
                            //System.out.println(new StringBuilder(Thread.currentThread().getName() + " => getConnection()\n")/*.append(asString(t))*/);
                            synchronized (opened) {
                                opened.put((Connection) o, t);
                            }
                            return proxyCtor.newInstance(new IH((Connection) o, opened));
                        }
                        return o;
                    }
                });
    }

    private static final class IH implements InvocationHandler {
        final Connection connection;
        final Map<Connection, Throwable> opened;

        private IH(Connection connection, Map<Connection, Throwable> opened) {
            this.connection = connection;
            this.opened = opened;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getName().equals("close")) {
                synchronized (opened) {
                    Throwable t = opened.remove(connection);
                    //System.out.println(new StringBuilder(Thread.currentThread().getName() + " => close()\n")/*.append(asString(t))*/);
                }
            }
            return method.invoke(connection, args);
        }
    }

    static String asString(Throwable t) {
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    public static interface Marker {
        Map<Connection, Throwable> getOpenedConnections();
    }
}
