package kr.ac.goldcow.model;

import java.util.List;

public class MultiRowUser {
    private List<User> users;

    public List<User> getUsers() {
        return users;
    }
    public void setUsers(List<User> users) {
        this.users = users;
    }
}
