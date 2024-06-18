package biblioteca;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Biblioteca {
    private List<Livro> livros;
    private static final String JSON_FILE = "src/main/resources/livros.json";

    public Biblioteca() {
        this.livros = carregarLivros();
    }

    public List<Livro> listarLivros() {
        return livros;
    }

    public boolean cadastrarLivro(Livro livro) {
        livros.add(livro);
        return salvarLivros();
    }

    public boolean alugarLivro(String nome) {
        for (Livro livro : livros) {
            if (livro.getNome().equalsIgnoreCase(nome) && livro.getNumeroDeExemplares() > 0) {
                livro.setNumeroDeExemplares(livro.getNumeroDeExemplares() - 1);
                return salvarLivros();
            }
        }
        return false;
    }

    public boolean devolverLivro(String nome) {
        for (Livro livro : livros) {
            if (livro.getNome().equalsIgnoreCase(nome)) {
                livro.setNumeroDeExemplares(livro.getNumeroDeExemplares() + 1);
                return salvarLivros();
            }
        }
        return false;
    }

    private List<Livro> carregarLivros() {
        List<Livro> livros = new ArrayList<>();
        JSONParser parser = new JSONParser();
        File file = new File(JSON_FILE);
        if (!file.exists()) {
            try (FileWriter writer = new FileWriter(JSON_FILE)) {
                writer.write(new JSONArray().toJSONString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (FileReader reader = new FileReader(JSON_FILE)) {
            JSONArray livrosArray = (JSONArray) parser.parse(reader);
            for (Object obj : livrosArray) {
                JSONObject livroJson = (JSONObject) obj;
                String autor = (String) livroJson.get("autor");
                String nome = (String) livroJson.get("nome");
                String genero = (String) livroJson.get("genero");
                int numeroDeExemplares = ((Long) livroJson.get("numeroDeExemplares")).intValue();
                livros.add(new Livro(autor, nome, genero, numeroDeExemplares));
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return livros;
    }

    private synchronized boolean salvarLivros() {
        JSONArray livrosArray = new JSONArray();
        for (Livro livro : livros) {
            JSONObject livroJson = new JSONObject();
            livroJson.put("autor", livro.getAutor());
            livroJson.put("nome", livro.getNome());
            livroJson.put("genero", livro.getGenero());
            livroJson.put("numeroDeExemplares", livro.getNumeroDeExemplares());
            livrosArray.add(livroJson);
        }
        try (FileWriter writer = new FileWriter(JSON_FILE)) {
            writer.write(livrosArray.toJSONString());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
