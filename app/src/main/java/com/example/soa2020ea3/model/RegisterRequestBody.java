package com.example.soa2020ea3.model;

public class RegisterRequestBody {
    private String env;
    private String name;
    private String lastname;
    private String dni;
    private String email;
    private String password;
    private Integer commission;

    public RegisterRequestBody(String env, String name, String lastname, String dni, String email, String password, Integer commission) {
        this.env = env;
        this.name = name;
        this.lastname = lastname;
        this.dni = dni;
        this.email = email;
        this.password = password;
        this.commission = commission;
    }

    public RegisterRequestBody(String env, Usuario nuevoUsuario) {
        this.env = env;
        this.name = nuevoUsuario.getName();
        this.lastname = nuevoUsuario.getLastname();
        this.dni = nuevoUsuario.getDni();
        this.email = nuevoUsuario.getEmail();
        this.password = nuevoUsuario.getPassword();
        this.commission = nuevoUsuario.getCommission();
    }

    public RegisterRequestBody(Usuario nuevoUsuario) {
        this.env = "PROD";
        this.name = nuevoUsuario.getName();
        this.lastname = nuevoUsuario.getLastname();
        this.dni = nuevoUsuario.getDni();
        this.email = nuevoUsuario.getEmail();
        this.password = nuevoUsuario.getPassword();
        this.commission = nuevoUsuario.getCommission();
    }
}
