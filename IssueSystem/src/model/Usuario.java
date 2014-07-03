package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Usuario {
	
	String username;
	String nome;
	String senha;	
	
	public Usuario(String username, String nome, String senha) {
		super();
		this.username = username;
		this.nome = nome;
		this.senha = senha;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	/*
	 *  Salva por padrao Username;Nome;Senha no txt
	 */
	public String escreveNoTxt() {
		return username + ";" + nome + ";" + senha;
	}
	
	public static List<Usuario> lerArquivo() throws IOException{
		FileReader fileReader = new FileReader("dba_Usuario.txt");
		BufferedReader reader = new BufferedReader(fileReader);
		List<Usuario> listaUsuario = new LinkedList<Usuario>()	;
		String linha;
		while((linha = reader.readLine()) != null){
			/*
			 * Divide a linha pelo ";" separador dos atributos
			 * 0 - UserName, 1 - Nome, 2 - Senha
			 */
		   String[] atributo = linha.split(";");
		   Usuario user = new Usuario(atributo[0], atributo[1], atributo[2]);
		   listaUsuario.add(user);
		}
		fileReader.close();
		reader.close();
		return listaUsuario;
	}
	
	public static void gravaArquivo(List<Usuario> listaUsuarios) throws IOException{
		FileWriter fileWritter = new FileWriter("dba_Usuario.txt");
		BufferedWriter writter = new BufferedWriter(fileWritter);
		for	(Usuario user : listaUsuarios)
		{
			writter.write(user.escreveNoTxt() + "\n");
		}
		writter.close();
		fileWritter.close();
	}
	
}
