package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class Projeto {
	
	String nome;
	String desc;
	String dono;
	String desenvolvedores;
	List<Issue> listaIssue;
	
	public Projeto(String nome, String desc, String dono, String desenvolvedores) {
		super();
		this.nome = nome;
		this.desc = desc;
		this.dono = dono;
		this.desenvolvedores = desenvolvedores;
		listaIssue = new ArrayList<Issue>();
	}
	public Projeto(String nome, String desc, String dono, String desenvolvedores,
			List<Issue> listIssues) {
		super();
		this.nome = nome;
		this.desc = desc;
		this.dono = dono;
		this.desenvolvedores = desenvolvedores;
		listaIssue = listIssues;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getDono() {
		return dono;
	}
	public void setDono(String dono) {
		this.dono = dono;
	}
	public String getDesenvolvedores() {
		return desenvolvedores;
	}
	public void setDesenvolvedores(String desenvolvedores) {
		this.desenvolvedores = desenvolvedores;
	}
	
	public List<Issue> getIssue()
	{
		return listaIssue;
	}
	
	/*
	 *  Salva por padrao Username;Nome;Senha no txt
	 */
	
	public String escreveNoTxt() {
		return nome + ";" + desc + ";" + dono + ";" + desenvolvedores;
	}
	
	public String listaIssue()
	{
		String aux = "";
		int count = 0;
		if(listaIssue.size() == 0)
			return "Não existe nenhum Issue nesse projeto.";
		for	(Issue i: listaIssue){
			aux +=  count + " - " + i.toString();
		}
		return aux;
	}
	
	public void adicionarIssue(Issue i){
		listaIssue.add(i);		
	}
	
	public Issue removeIssue(int index){
		return listaIssue.remove(index);
	}
	
	@Override
	public String toString() {
		return "Projeto: Nome=" + nome + ", Descrição=" + desc + ", Dono=" + dono
				+ ", Desenvolvedores=" + desenvolvedores ;
	}
	public static List<Projeto> lerArquivo(String login) {
		List<Projeto> listaProjetos = new LinkedList<Projeto>()	;
		try {
		FileReader fileReader = new FileReader("dba_Projeto"+ login+".txt");
		BufferedReader reader = new BufferedReader(fileReader);
		String linha;
		while((linha = reader.readLine()) != null){
			
			/*
			 * Divide a linha pelo ";" separador dos atributos
			 * 0 - nome, 1 - desc, 2 - dono, 3 - desenvolvedores 
			 */
		   String[] atributo = linha.split(";");
		   List<Issue> listIssue = Issue.lerArquivo(atributo[0]);
		   Projeto user = new Projeto(atributo[0], atributo[1], atributo[2], atributo[3], listIssue);
		   listaProjetos.add(user);
		}
		fileReader.close();
		reader.close();
		}	
		catch(IOException e) {
			return listaProjetos;
		}
		return listaProjetos;
	}
	
	public static void gravaArquivo(List<Projeto> listaProjetos, String login) throws IOException{
		FileWriter fileWritter = new FileWriter("dba_Projeto" + login + ".txt");
		BufferedWriter writter = new BufferedWriter(fileWritter);
		for	(Projeto proj : listaProjetos)
		{
			writter.write(proj.escreveNoTxt() + "\n");
			Issue.gravaArquivo(proj.getIssue(), proj.getNome());
		}
		writter.close();
		fileWritter.close();
	}
}
