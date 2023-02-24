package ba.unsa.etf.rpr.dao;

import ba.unsa.etf.rpr.domain.Idable;
import ba.unsa.etf.rpr.exceptions.LibraryException;

import java.sql.*;
import java.util.*;


public abstract class AbstractDao<T extends Idable> implements Dao<T> {
    private static Connection connection = null;
    private String table;

    public AbstractDao(String table) {
        this.table = table;
        createConnection();
    }

    private static void createConnection(){
        if(AbstractDao.connection==null) {
            try {
                Properties p = new Properties();
                p.load(ClassLoader.getSystemResource("application.properties").openStream());
                String url = p.getProperty("db.connection_string");
                String username = p.getProperty("db.username");
                String password = p.getProperty("db.password");
                AbstractDao.connection = DriverManager.getConnection(url, username, password);
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                Runtime.getRuntime().addShutdownHook(new Thread(){
                    @Override
                    public void run(){
                        try {
                            connection.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }
    public static Connection getConnection(){
        return AbstractDao.connection;
    }
    public abstract T row2object(ResultSet rs) throws LibraryException;

    public abstract Map<String, Object> object2row(T object);
    public List<T> executeQuery(String query, Object[] parameters) throws LibraryException {
        try {
            PreparedStatement stmt = getConnection().prepareStatement(query);
            if (parameters != null){
                for(int i = 1; i <= parameters.length; i++){
                    stmt.setObject(i, parameters[i-1]);
                }
            }
            ResultSet rs = stmt.executeQuery();
            ArrayList<T> resultList = new ArrayList<>();
            while (rs.next()) {
                resultList.add(row2object(rs));
            }
            return resultList;
        } catch (SQLException e) {
            throw new LibraryException(e.getMessage(), e);
        }
    }
    public T executeQueryUnique(String query, Object[] parameters) throws LibraryException{
        List<T> result = executeQuery(query, parameters);
        if (result != null && result.size() == 1){
            return result.get(0);
        }else{
            throw new LibraryException("Object not found");
        }
    }
}
