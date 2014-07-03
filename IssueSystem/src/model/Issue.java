package model;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.DatabaseMetaData;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.DataFormatException;

import org.omg.CORBA.DATA_CONVERSION;

public class Issue {

	String titulo;
	String descricao;
	Date dataCriacao;
	Criticidade Criticidade;
	Tipo tipo;
	Status Status;
	
	public enum Criticidade {
		BLOCKER,CRITICAL,HIGH,MEDIUM,LOW;
	}
	
	public enum Tipo {
		BUG,ENHANCEMENT;
	}
	
	public enum Status {
		NOVO,ABERTO,EM_DEV, CLOSED, DUPLICADO, ATRIBUIDO, WONT_FIX;
	}

	//construtor
	public Issue(String titulo, String descricao, Date dataCriacao,
			model.Issue.Criticidade criticidade, Tipo tipo,
			model.Issue.Status status) {
		super();
		this.titulo = titulo;
		this.descricao = descricao;
		this.dataCriacao = dataCriacao;
		Criticidade = criticidade;
		this.tipo = tipo;
		Status = status;
	}
	
	//construtor
	public Issue(String titulo, String descricao, Date dataCriacao,
			String criticidade, String tipo, String status) {
		super(); 
		this.titulo = titulo;
		this.descricao = descricao;
		this.dataCriacao = dataCriacao;
		Criticidade = Criticidade.valueOf(criticidade);
		this.tipo = Tipo.valueOf(tipo);
		Status = Status.valueOf(status);
	}
	
	public String escreveNoTxt() {
		return titulo + ";" + descricao + ";" + dataCriacao + ";" + Criticidade + ";" + tipo + ";" + Status;
	}
	
	@Override
	public String toString() {
		return "Titulo=" + titulo + ", Descrição=" + descricao + ", Data Criacao=" + dataCriacao
				+ ", Criticidade=" + Criticidade + ", Tipo=" + tipo + ", Status=" + Status;
	}
	
	
	public static List<Issue> lerArquivo(String nome) {
		List<Issue> listaIssue = new LinkedList<Issue>()	;
		try {
		FileReader fileReader = new FileReader("dba_Issue" + nome +".txt");
		BufferedReader reader = new BufferedReader(fileReader);
		String linha;
		while((linha = reader.readLine()) != null){
			
			/*
			 * Divide a linha pelo ";" separador dos atributos
			 * 0 - Tituto, 1 - descricao, 2 - dataCriacao, 3 - Criticidade, 4 - Tipo, 5 - Status 
			 */
			java.sql.Date data;
		   String[] atributo = linha.split(";");
		   try
		   {
		   SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");  
		   data = new java.sql.Date(format.parse(atributo[2]).getTime()); 
		   }
		   catch (ParseException ex)
		   {
			   data = null;    
		   }
		   Issue user = new Issue(atributo[0], atributo[1], data, atributo[3], atributo[4],atributo[5]);
		   listaIssue.add(user);
		}
		fileReader.close();
		reader.close();
		}	
		catch(IOException e) {
			return listaIssue;
		}
		return listaIssue;
	}
	
	public static void gravaArquivo(List<Issue> listaIssue, String nome) throws IOException{
		FileWriter fileWritter = new FileWriter("dba_Issue" + nome + ".txt");
		BufferedWriter writter = new BufferedWriter(fileWritter);
		for	(Issue proj : listaIssue)
		{
			writter.write(proj.escreveNoTxt() + "\n");
		}
		writter.close();
		fileWritter.close();
	}
	
}
	

	