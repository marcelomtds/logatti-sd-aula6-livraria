package run;

import model.Livraria;
import model.Livro;
import util.ConnectionDB;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {

    private static ConnectionDB connection;

    public static void main(final String[] args) {
        Livro livro = new Livro();
        livro.setIsbn(25452);
        livro.setTitulo("O Código Da Vinci");
        livro.setAutor("Dan Brown");
        livro.setAnoLancamento(2003);
        livro.setQuantidadePaginas(540);
        livro.setGenero("Romance Policial, Suspense");
        insertLivro(livro);

        Livraria livraria = new Livraria();
        livraria.setNome("Livraria Saraiva");
        insertLivraria(livraria, livro.getIsbn());

        findAll();
    }

    private static void insertLivro(final Livro livro) {
        try {
            connection = new ConnectionDB();
            connection.execute(String.format("insert into livro (isbn, titulo, autor, ano_lancamento, quantidade_paginas, genero) " +
                            "values ('%s', '%s', '%s', '%s', '%s', '%s')",
                    livro.getIsbn().toString(), livro.getTitulo(),
                    livro.getAutor(), livro.getAnoLancamento().toString(),
                    livro.getQuantidadePaginas().toString(), livro.getGenero()));
        } catch (final Exception e) {
            e.printStackTrace();
        } finally {
            connection.closeConnection();
        }
    }

    private static void insertLivraria(final Livraria livraria, final Integer isbn) {
        try {
            connection = new ConnectionDB();
            connection.execute(String.format("insert into livraria (nome, isbn_livro) " +
                    "values ('%s', '%s')", livraria.getNome(), isbn.toString()));
        } catch (final Exception e) {
            e.printStackTrace();
        } finally {
            connection.closeConnection();
        }
    }

    private static void findAll() {
        try {
            connection = new ConnectionDB();
            ResultSet rs = connection.executeWithResponse(
                    "select " +
                            "lvra.id, " +
                            "lvra.nome, " +
                            "lvr.isbn, " +
                            "lvr.titulo, " +
                            "lvr.autor, " +
                            "lvr.ano_lancamento, " +
                            "lvr.quantidade_paginas, " +
                            "lvr.genero " +
                            "from livraria as lvra " +
                            "inner join livro as lvr on lvr.isbn = lvra.isbn_livro " +
                            "order by lvra.nome asc");

            while (rs.next()) {
                System.out.println(String.format(
                        "\nLivraria - ID %d: , Nome: %s" +
                                "\nLivro - ISBN: %d, Título: %s, Autor: %s, Ano de Lançamento: %d, " +
                                "Quantidade de Páginas: %d, Gênero: %s",
                        rs.getInt("id"), rs.getString("nome"),
                        rs.getInt("isbn"), rs.getString("titulo"),
                        rs.getString("autor"), rs.getInt("ano_lancamento"),
                        rs.getInt("quantidade_paginas"), rs.getString("genero")));
            }
        } catch (final SQLException e) {
            e.printStackTrace();
        } finally {
            connection.closeConnection();
        }
    }

}
