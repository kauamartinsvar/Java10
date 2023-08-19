package com.br.var.solutions;

public class ValidaUsuario {
    public enum ValidaUser{
        USER_1("var", "12345678"),

        USER_2("kaua.martins", "Teste01&"),
        USER_3("var", "var");

            private String usuario;
            private String senha;

        ValidaUser(String username, String password){
                this.usuario = username;
                this.senha = password;
            }

        public String getUsuario() {
            return usuario;
        }

        public String getSenha() {
            return senha;
        }
    }
public static Boolean validaUsuario(String username, String password)
{
    for(ValidaUser validaUser : ValidaUser.values()){
        if (validaUser.getUsuario().equalsIgnoreCase(username) && validaUser.getSenha().equals(password))
        {
            return true;
        }
    }
    return false;
}
}
