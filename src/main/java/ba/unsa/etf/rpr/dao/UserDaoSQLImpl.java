package ba.unsa.etf.rpr.dao;

import ba.unsa.etf.rpr.domain.Movie;
import ba.unsa.etf.rpr.domain.User;
import ba.unsa.etf.rpr.exception.MovieException;

import java.sql.*;
import java.util.Map;
import java.util.TreeMap;

/**
 * MySQL implementation of the DAO
 * @author Emina Efendic
 */
public class UserDaoSQLImpl extends AbstractDao<User> implements UserDao{

    private static UserDaoSQLImpl instance = null;
    private UserDaoSQLImpl() {
        super("users");
    }

    /**
     * @author Emina Efendic
     * @return QuoteDaoSQLImpl
     * We don't need more than one object for CRUD operations on table 'quotes' so we will use Singleton
     * This method will call private constructor in instance==null and then return that instance
     */
    public static UserDaoSQLImpl getInstance(){
        if(instance==null)
            instance = new UserDaoSQLImpl();
        return instance;
    }

    public static void removeInstance(){
        if(instance!=null)
            instance=null;
    }

    @Override
    public User row2object(ResultSet rs) throws MovieException {
        try {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setName(rs.getString("name"));
            user.setPhone(rs.getString("phone"));
            user.setEmail(rs.getString("email"));
            user.setAdmin(rs.getBoolean("admin"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            return user;
        } catch (SQLException e) {
            throw new MovieException(e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> object2row(User object) {
        Map<String, Object> item = new TreeMap<String, Object>();
        item.put("id", object.getId());
        item.put("name", object.getName());
        item.put("phone", object.getPhone());
        item.put("email", object.getEmail());
        item.put("admin", object.isAdmin());
        item.put("username", object.getUsername());
        item.put("password", object.getPassword());
        return item;
    }
    @Override
    public boolean findUsername(String usernameField) throws MovieException{
        String insert = "SELECT count(username) from users where username='" + usernameField +"'";
        try {
            PreparedStatement stmt = getConnection().prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) { // result set is iterator.
                return rs.getInt(1) != 0;
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public boolean isAdmin(String usernameField) throws MovieException {
        String insert = "SELECT username from users where admin=1";
        try {
            PreparedStatement stmt = getConnection().prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) { // result set is iterator.
                if(rs.getString(1).equals(usernameField)) return true;
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Integer checkUsernamePassword(String usernameTextField, String passwordField) throws MovieException{
        String insert = "SELECT id from users where username='" + usernameTextField + "' AND password='"
                + passwordField + "'";
        try {
            PreparedStatement stmt = getConnection().prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) { // result set is iterator.
                return rs.getInt(1);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
