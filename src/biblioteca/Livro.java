package biblioteca;

public class Livro {
    private String autor;
    private String nome;
    private String genero;
    private int numeroDeExemplares;

    public Livro(String autor, String nome, String genero, int numeroDeExemplares) {
        this.autor = autor;
        this.nome = nome;
        this.genero = genero;
        this.numeroDeExemplares = numeroDeExemplares;
    }

    // Getters e Setters

    @Override
    public String toString() {
        return String.format("Autor: %s, Nome: %s, Gênero: %s, Exemplares: %d", 
                              autor, nome, genero, numeroDeExemplares);
    }

	public int getNumeroDeExemplares() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getNome() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setNumeroDeExemplares(int i) {
		// TODO Auto-generated method stub
		
	}
}
