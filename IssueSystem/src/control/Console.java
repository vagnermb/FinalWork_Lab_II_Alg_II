package control;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;

import javax.net.ssl.SSLEngineResult.Status;

import model.Evento;
import model.Issue;
import model.Issue.Criticidade;
import model.Issue.Tipo;
import model.Projeto;
import model.Usuario;

public class Console {
	public List<Usuario> listaUsuarios;
	public List<Projeto> listaProjeto;
	public List<Issue> listaIssue;
	public List<Evento> listaEvento;
	private Usuario usuarioLogado;
	Usuario usuarioSistema = new Usuario(null, null, null);
	Scanner sc = new Scanner(System.in);

	public Console() {
		usuarioLogado = menuUsuario();
		menuProjeto();
	}

	public Usuario menuUsuario() {

		try {
			listaUsuarios = Usuario.lerArquivo();
			System.out.print("Digite o usuário:\n");
			String Login = sc.nextLine();

			boolean achou = false;
			for (Usuario user : listaUsuarios) {
				if (user.getUsername().equals(Login)) {
					usuarioSistema = user;
					achou = true;
				}
			}
			if (!achou) {
				System.out.print("Cadastrando usuario " + Login + "\nNome:\n");
				String Nome = sc.nextLine();
				System.out.print("Digite a senha:\n");
				String Senha = sc.nextLine();
				usuarioSistema = new Usuario(Login, Nome, Senha);
				listaUsuarios.add(usuarioSistema);
				Usuario.gravaArquivo(listaUsuarios);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.print("Erro ao ler arquivo");
		}

		return usuarioSistema;
	}

	public void menuProjeto() {
		listaProjeto = Projeto.lerArquivo(usuarioLogado.getUsername());

		int op = 0;
		do {
			System.out
					.println("\nMenu:\n1 - Listar projetos. \n2 - Adicionar Projeto. \n3 - "
							+ "Editar Projeto.\n4 - Excluir Projeto.\n0 - Para sair.");
			try {
				op = Integer.parseInt(sc.nextLine());
			} catch (NumberFormatException ex) {
				System.out.println("Valor inválido.");
				op = -1;
			}
			switch (op) {
			case 1:
				listarProjetos();
				break;
			case 2:
				adicionarProjeto();
				break;
			case 3:
				editarProjeto();
				break;
			case 4:
				excluirProjeto();

				break;
			}
		} while (op != 0);
	}

	public void listarProjetos() {
		if (listaProjeto.size() == 0) {
			System.out.println("Usuário sem Projetos.");
		}
		int i = 0;
		for (Projeto p : listaProjeto) {
			System.out.println(i + " - " + p.toString());
			i++;
		}
	}

	public void excluirProjeto() {
		listarProjetos();
		int codprojeto = -1;
		do {
			System.out.print("\nDigite o numero do projeto para excluir:(0 a "
					+ (listaProjeto.size() - 1) + ")\n");
			codprojeto = Integer.parseInt(sc.nextLine());
		} while (codprojeto < -1 || codprojeto >= listaProjeto.size());
		listaProjeto.remove(codprojeto);
		System.out.print("\nProjeto excluido com sucesso!");
		try {
			Projeto.gravaArquivo(listaProjeto, usuarioLogado.getUsername());
		} catch (IOException e) {
			System.out.println("problema ao salvar projeto!");
		}
	}

	public void adicionarProjeto() {
		System.out.print("\nAdicionando Projeto.\nNome do Projeto:\n");
		String nome = sc.nextLine();
		System.out.print("Descrição do Projeto:\n");
		String desc = sc.nextLine();
		System.out.print("Dono do Projeto:\n");
		String dono = sc.nextLine();
		System.out.print("Desenvolvedores do Projeto:\n");
		String desenvolvedor = sc.nextLine();

		Projeto p = new Projeto(nome, desc, dono, desenvolvedor);
		listaProjeto.add(p);
		try {
			Projeto.gravaArquivo(listaProjeto, usuarioLogado.getUsername());
		} catch (IOException e) {
			System.out.println("problema ao salvar projeto!");
		}
	}

	public void editarProjeto() {
		listarProjetos();
		// listaProjeto = Projeto.lerArquivo(usuarioLogado.getUsername());
		int codProjeto = 0;
		do {
			System.out.print("\nQual número do projeto que deseja editar:(0 à "
					+ (listaProjeto.size() - 1) + ")\n");
			codProjeto = Integer.parseInt(sc.nextLine());
		} while (codProjeto < 0 || codProjeto >= listaProjeto.size());
		System.out.print("\nEditando projeto "
				+ listaProjeto.get(codProjeto).getNome() + "\n");
		System.out.print("Nome do projeto: "
				+ listaProjeto.get(codProjeto).getNome() + "\n");
		System.out.print("Descrição do projeto: "
				+ listaProjeto.get(codProjeto).getDesc() + "\n");
		System.out.print("Dono do projeto: "
				+ listaProjeto.get(codProjeto).getDono() + "\n");
		System.out.print("Desenvolvedores do projeto: "
				+ listaProjeto.get(codProjeto).getDesenvolvedores() + "\n");
		int op = 0;
		Projeto p = null;
		do {
			System.out
					.println("\nMenu:\n1 - Alterar informações do projeto. \n2 - Listar issues do projeto."
							+ "\n3 - Lista evento.\n4 - Adicionar issue.\n5 - Adicionar evento."
							+ "\n6 - Excluir issue.\n7 - Excluir Evento.\n0 - Para sair");
			try {
				op = Integer.parseInt(sc.nextLine());
				p = listaProjeto.get(codProjeto);
			} catch (NumberFormatException ex) {
				System.out.println("Valor inválido.");
				op = 10;
			}
			switch (op) {
			case 1:// Alterar infomaçoes do projeto
				alteraInformacoesProjeto(p);
				break;
			case 2:// Lista issue
				System.out.println("Issues:\n" + p.listaIssue());
				break;
			case 3:// Lista evento
				break;
			case 4:// Adicionar issue
				adionarIssue(p);
				break;
			case 5:// Adicionar Evento
				break;
			case 6:// Excluir issue
				excluirIssue(p);
				break;
			case 7:// Excluir Evento
				break;
			}
		} while (op != 0);

		try {
			Projeto.gravaArquivo(listaProjeto, usuarioLogado.getUsername());
		} catch (IOException e) {
			System.out.println("problema ao salvar projeto!");
		}
	}

	public void alteraInformacoesProjeto(Projeto p) {
		String pular = "0";
		System.out
				.print("\nAlterando Informações do projeto.\nAlterar nome do Projeto:(Digite 0 para alterar e qualquer otra coisa para pular.)\n");
		pular = sc.nextLine();
		if (pular.equals("0")) {
			System.out.print("\nNome do Projeto:\n");
			p.setNome(sc.nextLine());
		}
		System.out
				.print("\nAlterar descrição do projeto:(Digite 0 para alterar e qualquer otra coisa para pular.)\n");
		pular = sc.nextLine();
		if (pular.equals("0")) {
			System.out.print("Descrição do Projeto:\n");
			p.setDesc(sc.nextLine());
		}
		System.out
				.print("\nAlterar dono do projeto:(Digite 0 para alterar e qualquer otra coisa para pular.)\n");
		if (pular.equals("0"))
			if (pular == "0") {
				System.out.print("Dono do Projeto:\n");
				p.setDono(sc.nextLine());
			}
		System.out
				.print("\nAlterar desenvolvedores do projeto:(Digite 0 para alterar e qualquer otra coisa para pular.)\n");
		if (pular.equals("0"))
			if (pular == "0") {
				System.out.print("Desenvolvedores do Projeto:\n");
				p.setDesenvolvedores(sc.nextLine());
			}
		System.out.print("\nProjeto alterado com sucesso.");
	}

	public void adionarIssue(Projeto p) {
		System.out.print("\nAdicionar o Issue ao projeto " + p.getNome()
				+ ".\nTitulo da issue:\n");
		String titulo = sc.nextLine();
		System.out.print("Descrição da issue:\n");
		String descricao = sc.nextLine();
		boolean informacaoValida = true;
		Criticidade criticidade = null;
		Tipo tipo = null;
		model.Issue.Status status = null;
		java.sql.Date data;
		do {
			System.out.print("Data Criação da issue:\n");
			String dataCriacao = sc.nextLine();
			try {
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
				data = new java.sql.Date(format.parse(dataCriacao).getTime());
				informacaoValida = true;
			} catch (ParseException ex) {
				System.out.print("FORMATO ERRADO!(FORMATO DD/MM/AAA)\n");
				informacaoValida = false;
				data = null;
			}
		} while (!informacaoValida);
		do {
			try {
				System.out.print("Criticidade da issue:\n");
				criticidade = Criticidade.valueOf(sc.nextLine().toUpperCase());
				informacaoValida = true;
			} catch (IllegalArgumentException ex) {
				System.out
						.print("Formato inválido!\nDeve ser: BLOCKER,CRITICAL,HIGH,MEDIUM,LOW\n");
				informacaoValida = false;
			}

		} while (!informacaoValida);
		do {
			try {
				System.out.print("Tipo da issue:\n");
				tipo = Tipo.valueOf(sc.nextLine().toUpperCase());
				informacaoValida = true;
			} catch (IllegalArgumentException ex) {
				System.out
						.print("Formato inválido!\nDeve ser: BUG,ENHANCEMENT\n");
				informacaoValida = false;
			}
		} while (!informacaoValida);
		do {
			try {
				System.out.print("Status da issue:\n");
				status = model.Issue.Status.valueOf(sc.nextLine().toUpperCase());
				informacaoValida = true;
			} catch (IllegalArgumentException ex) {
				System.out
						.print("Formato inválido!\nDeve ser: NOVO,ABERTO,EM DEV, CLOSED,"
								+ " DUPLICADO, ATRIBUIDO, WON'T FIX\n");
				informacaoValida = false;
			}
		} while (!informacaoValida);

		Issue i = new Issue(titulo, descricao, data, criticidade, tipo, status);
		p.adicionarIssue(i);
	}

	public void excluirIssue(Projeto p) {
		List<Issue> listaIssue = p.getIssue();
		System.out.println("Issues:\n" + p.listaIssue());
		int codIssue = -1;
		do {
			System.out.print("\nDigite o numero do projeto para excluir:(0 a "
					+ (listaProjeto.size() - 1) + ")\n");
			codIssue = Integer.parseInt(sc.nextLine());
		} while (codIssue < -1 || codIssue >= listaIssue.size());
		p.removeIssue(codIssue);
		System.out.print("\nIssue excluida com sucesso!");
	}

}
