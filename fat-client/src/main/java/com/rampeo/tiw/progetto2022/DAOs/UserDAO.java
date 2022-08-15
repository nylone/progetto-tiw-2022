package com.rampeo.tiw.progetto2022.DAOs;

import com.rampeo.tiw.progetto2022.Beans.UserBean;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO extends AbstractDAO {
    public UserDAO() throws SQLException {
        super();
    }

    public Integer getUserCount() throws SQLException {
        String query = "SELECT COUNT(*) FROM user";
        try (PreparedStatement pstatement = getConnection().prepareStatement(query)) {
            try (ResultSet result = pstatement.executeQuery()) {
                if (!result.isBeforeFirst())
                    return null;
                else {
                    result.next();
                    return result.getInt(1);
                }
            }
        }
    }

    public List<UserBean> getOtherUsers(UserBean user) throws SQLException {
        String query = "SELECT id, email FROM user WHERE id<>?";
        try (PreparedStatement pstatement = getConnection().prepareStatement(query)) {
            pstatement.setLong(1, user.getId());
            try (ResultSet result = pstatement.executeQuery()) {
                if (!result.isBeforeFirst())
                    return List.of();
                else {
                    List<UserBean> userBeanList = new ArrayList<>();
                    while (result.next()) {
                        UserBean userBean = new UserBean();
                        userBean.setId(result.getLong("id"));
                        userBean.setEmail(result.getString("email"));
                        userBeanList.add(userBean);
                    }
                    return List.copyOf(userBeanList);
                }
            }
        }
    }

    public boolean checkUnique(String email) throws SQLException {
        String query = "SELECT  id FROM user  WHERE email=?";
        try (PreparedStatement pstatement = getConnection().prepareStatement(query)) {
            pstatement.setString(1, email);
            try (ResultSet result = pstatement.executeQuery()) {
                if (!result.isBeforeFirst()) // no results, credential check failed
                    return true;
                else {
                    result.next();
                    return false;
                }
            }
        }
    }

    public UserBean authenticate(String email, String pass) throws SQLException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] salt;
        String query = "SELECT salt FROM user  WHERE email=?";
        try (PreparedStatement pstatement = getConnection().prepareStatement(query)) {
            pstatement.setString(1, email);
            try (ResultSet result = pstatement.executeQuery()) {
                if (!result.isBeforeFirst()) // no results, credential check failed
                    return null;
                else {
                    result.next();
                    salt = result.getBytes("salt");
                }
            }
        }
        byte[] hash = hashPassword(pass, salt);
        query = "SELECT  id, email FROM user  WHERE email=? AND hash=?";
        try (PreparedStatement pstatement = getConnection().prepareStatement(query)) {
            pstatement.setString(1, email);
            pstatement.setBytes(2, hash);
            try (ResultSet result = pstatement.executeQuery()) {
                if (!result.isBeforeFirst()) // no results, credential check failed
                    return null;
                else {
                    result.next();
                    UserBean userBean = new UserBean();
                    userBean.setId(result.getLong("id"));
                    userBean.setEmail(result.getString("email"));
                    return userBean;
                }
            }
        }
    }

    private byte[] hashPassword(String pass, byte[] salt) throws InvalidKeySpecException, NoSuchAlgorithmException {
        KeySpec spec = new PBEKeySpec(pass.toCharArray(), salt, 65536, 512);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        return factory.generateSecret(spec).getEncoded();
    }

    public void addCredentials(String email, String pass) throws SQLException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] salt = generateSalt();
        byte[] hash = hashPassword(pass, salt);
        String query = "INSERT INTO user (email, hash, salt) values (?, ?, ?)";
        try (PreparedStatement pstatement = getConnection().prepareStatement(query)) {
            pstatement.setString(1, email);
            pstatement.setBytes(2, hash);
            pstatement.setBytes(3, salt);
            pstatement.execute();
        }
    }

    private byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }
}
