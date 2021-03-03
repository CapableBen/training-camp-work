package org.geektimes.projects.user.repository;

import org.geektimes.function.ThrowableFunction;
import org.geektimes.projects.user.domain.User;
import org.geektimes.projects.user.sql.DBConnectionManager;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static org.apache.commons.lang.ClassUtils.wrapperToPrimitive;
import static org.geektimes.projects.user.sql.DBConnectionManager.CREATE_USERS_TABLE_DDL_SQL;
import static org.geektimes.projects.user.sql.DBConnectionManager.DROP_USERS_TABLE_DDL_SQL;

public class DatabaseUserRepository implements UserRepository {

    private static Logger logger = Logger.getLogger(DatabaseUserRepository.class.getName());

    /**
     * 通用处理方式
     */
    private static Consumer<Throwable> COMMON_EXCEPTION_HANDLER = e -> logger.log(Level.SEVERE, e.getMessage());

    public static final String INSERT_USER_DML_SQL = "INSERT INTO users(name,password,email,phoneNumber) VALUES (?,?,?,?)";

    public static final String QUERY_ALL_USERS_DML_SQL = "SELECT id,name,password,email,phoneNumber FROM users";

    private final DBConnectionManager dbConnectionManager = new DBConnectionManager();

//    public DatabaseUserRepository(DBConnectionManager dbConnectionManager) {
//        this.dbConnectionManager = dbConnectionManager;
//    }

    private Connection getConnection() {
        return dbConnectionManager.getConnection();
    }

    private static String[] of(String... values) { // String... == String[]
        return values;
    }

    private Connection lookupConnection() {
        Connection connection = null;
        try {
            Context context = new InitialContext();
            DataSource dataSource = (DataSource) context.lookup("jdbc/mysql");
            connection = dataSource.getConnection();
        } catch (Exception e) {
            e.fillInStackTrace();
        }
        return connection;
    }

    @Override
    public boolean save(User user) {
        String[] values = of(user.getName(), user.getPassword(), user.getEmail(), user.getPhoneNumber());

        try {
            String databaseURL = "jdbc:derby:/db/user-platform;create=true";
            Connection connection = DriverManager.getConnection(databaseURL);

//            String databaseURL = "jdbc:mysql://localhost:3306/back?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Hongkong";
//            Connection connection = DriverManager.getConnection(databaseURL, "root", "root");

//            Context context = new InitialContext();
//            DataSource derbySource = (DataSource) context.lookup("jdbc/derby");
//            Connection derbyConnection = derbySource.getConnection();
//            DataSource dataSource = (DataSource) context.lookup("jdbc/mysql");
//            Connection connection = dataSource.getConnection();

            Statement statement = connection.createStatement();

            DatabaseMetaData meta = connection.getMetaData();
            ResultSet resultSet = meta.getTables(null, null, null, new String[]{"TABLE"});
            HashSet<String> set = new HashSet<>();
            while (resultSet.next()) {
                set.add(resultSet.getString("TABLE_NAME"));
            }
            if (set.contains("users".toUpperCase())) {
                statement.execute(DROP_USERS_TABLE_DDL_SQL);
//                statement.execute(DROP_USERS_TABLE_DDL_MY_SQL);
            }
            statement.execute(CREATE_USERS_TABLE_DDL_SQL);
//            statement.execute(CREATE_USERS_TABLE_DDL_MY_SQL);

            StringBuilder builder = new StringBuilder("INSERT INTO users(name,password,email,phoneNumber) VALUES (");
            Stream.of(values).forEach(s -> builder.append("'").append(s).append("',"));
            builder.deleteCharAt(builder.length() - 1);
            builder.append(")");
            System.out.println("execute sql = " + builder.toString());

            statement.execute(builder.toString()); // false
            return true;
        } catch (SQLException e) {
            COMMON_EXCEPTION_HANDLER.accept(e);
            return false;
        }
    }


    @Override
    public boolean deleteById(Long userId) {
        return false;
    }

    @Override
    public boolean update(User user) {
        return false;
    }

    @Override
    public User getById(Long userId) {
        return null;
    }

    @Override
    public User getByNameAndPassword(String userName, String password) {
        return executeQuery("SELECT id,name,password,email,phoneNumber FROM users WHERE name=? and password=?",
                resultSet -> {
                    // TODO
                    return new User();
                }, COMMON_EXCEPTION_HANDLER, userName, password);
    }

    @Override
    public Collection<User> getAll() {
        return executeQuery("SELECT id,name,password,email,phoneNumber FROM users", resultSet -> {
            // BeanInfo -> IntrospectionException
            BeanInfo userBeanInfo = Introspector.getBeanInfo(User.class, Object.class);
            List<User> users = new ArrayList<>();
            while (resultSet.next()) { // 如果存在并且游标滚动 // SQLException
                User user = new User();
                for (PropertyDescriptor propertyDescriptor : userBeanInfo.getPropertyDescriptors()) {
                    String fieldName = propertyDescriptor.getName();
                    Class fieldType = propertyDescriptor.getPropertyType();
                    String methodName = resultSetMethodMappings.get(fieldType);
                    // 可能存在映射关系（不过此处是相等的）
                    String columnLabel = mapColumnLabel(fieldName);
                    Method resultSetMethod = ResultSet.class.getMethod(methodName, String.class);
                    // 通过放射调用 getXXX(String) 方法
                    Object resultValue = resultSetMethod.invoke(resultSet, columnLabel);
                    // 获取 User 类 Setter方法
                    // PropertyDescriptor ReadMethod 等于 Getter 方法
                    // PropertyDescriptor WriteMethod 等于 Setter 方法
                    Method setterMethodFromUser = propertyDescriptor.getWriteMethod();
                    // 以 id 为例，  user.setId(resultSet.getLong("id"));
                    setterMethodFromUser.invoke(user, resultValue);
                }
            }
            return users;
        }, e -> {
            // 异常处理
        });
    }

    /**
     * @param sql
     * @param function
     * @param <T>
     * @return
     */
    protected <T> T executeQuery(String sql, ThrowableFunction<ResultSet, T> function,
                                 Consumer<Throwable> exceptionHandler, Object... args) {
        Connection connection = getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                Class argType = arg.getClass();

                Class wrapperType = wrapperToPrimitive(argType);

                if (wrapperType == null) {
                    wrapperType = argType;
                }

                // Boolean -> boolean
                String methodName = preparedStatementMethodMappings.get(argType);
                Method method = PreparedStatement.class.getMethod(methodName, wrapperType);
                method.invoke(preparedStatement, i + 1, args);
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            // 返回一个 POJO List -> ResultSet -> POJO List
            // ResultSet -> T
            return function.apply(resultSet);
        } catch (Throwable e) {
            exceptionHandler.accept(e);
        }
        return null;
    }


    private static String mapColumnLabel(String fieldName) {
        return fieldName;
    }

    /**
     * 数据类型与 ResultSet 方法名映射
     */
    static Map<Class, String> resultSetMethodMappings = new HashMap<>();

    static Map<Class, String> preparedStatementMethodMappings = new HashMap<>();

    static {
        resultSetMethodMappings.put(Long.class, "getLong");
        resultSetMethodMappings.put(String.class, "getString");

        preparedStatementMethodMappings.put(Long.class, "setLong"); // long
        preparedStatementMethodMappings.put(String.class, "setString"); //
    }
}
