package ba.unsa.etf.rpr.dao;

import ba.unsa.etf.rpr.domain.Idable;
import ba.unsa.etf.rpr.domain.Member;
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
    Map.Entry<String, String> prepareInsertParts(Map<String, Object> row, String idName){
        StringBuilder columns = new StringBuilder();
        StringBuilder questions = new StringBuilder();

        int counter = 0;
        for (Map.Entry<String, Object> entry: row.entrySet()) {
            counter++;
            if (entry.getKey().equals(idName)) continue;
            columns.append(entry.getKey());
            questions.append("?");
            if (row.size() != counter) {
                columns.append(",");
                questions.append(",");
            }
        }
        return new AbstractMap.SimpleEntry<>(columns.toString(), questions.toString());
    }
    String prepareUpdateParts(Map<String, Object> row, String idName){
        StringBuilder columns = new StringBuilder();
        int counter = 0;
        for (Map.Entry<String, Object> entry: row.entrySet()) {
            counter++;
            if (entry.getKey().equals(idName)) continue;
            columns.append(entry.getKey()).append("= ?");
            if (row.size() != counter) {
                columns.append(",");
            }
        }
        return columns.toString();
    }
    /**
     * method for executing queries that are known to return only one row
     * @param query
     * @param parameters
     * @return the object returned by the query
     * @throws LibraryException an exception is thrown if no row is returned
     */
    public T executeQueryUnique(String query, Object[] parameters) throws LibraryException{
        List<T> result = executeQuery(query, parameters);
        if (result != null && result.size() == 1){
            return result.get(0);
        }else {
            throw new LibraryException("Object not found");
        }
    }
    public abstract T getById(int id) throws LibraryException;
    public abstract void delete(T item) throws LibraryException;
    public abstract T add(T item) throws LibraryException;
    public abstract T searchById(int id) throws LibraryException;
    public abstract T update(T item) throws LibraryException;

    public List<T> getAll() throws LibraryException {
        return executeQuery("SELECT * FROM "+ table, null);
    }
    @Override
    public void viewAll() throws LibraryException {
        List<T> l = new ArrayList<>();
        l = getAll();
        for(T t : l) System.out.println(t);
    }
}