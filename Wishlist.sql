CREATE DATABASE Wishlist;
Use Wishlist;

CREATE TABLE produtos(
    id INT NOT NULL AUTO_INCREMENT,
    nome VARCHAR (50) NOT NULL,
    valor decimal (6,2) NOT NULL,
    rating INT NOT NULL,
    PRIMARY KEY (id)


);


