package com.example.demo.model;

import javax.persistence.*;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @OneToOne
    @JoinColumn(name = "walletId", referencedColumnName = "id")
    private Wallet wallet;

    public Wallet getWallet () {
        return wallet;
    }

    public void setWallet (Wallet wallet) {
        this.wallet = wallet;
    }

    public void setWalletNull () {
        this.wallet = null;
    }

    public User (Long id) {
        this.id = id;
    }

    public User () {
    }

    public Long getId () {
        return id;
    }

    public void setId (Long id) {
        this.id = id;
    }

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public String getLogin () {
        return login;
    }

    public void setLogin (String login) {
        this.login = login;
    }

    public String getPassword () {
        return password;
    }

    public void setPassword (String password) {
        this.password = password;
    }

    public void printUser () {
        System.out.println("ID: "+this.id);
        System.out.println("Name: "+this.name);
        System.out.println("PASS: "+this.password);
        System.out.println("LOGIN: "+this.login);
        System.out.println("WALLET: "+this.wallet);
    }
}