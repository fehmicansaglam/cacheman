package models;

import javax.persistence.Column;
import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class User extends Model {

    @Column(nullable = false, unique = true)
    public String username;

    public String fullname;

    public User(String username, String fullname) {

        this.username = username;
        this.fullname = fullname;
    }

    public static User findByUsername(String username) {

        return User.find("byUsername", username).first();
    }
}
