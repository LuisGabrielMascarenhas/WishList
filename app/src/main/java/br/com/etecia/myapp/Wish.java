package br.com.etecia.myapp;

public class Wish {
        private int id;
        private String nome;
        private int valor;
        private int rating;
        public Wish(){

        }

        public Wish(int id, String nome, int valor,int rating){
            this.id = id;
            this.nome = nome;
            this.valor = valor;
            this.rating = rating;
        }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
