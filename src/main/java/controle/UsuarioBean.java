package controle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import dao.UsuarioDaoImpl;
import entidade.Telefone;
import entidade.Usuario;

/**
 * 
 * @author Tiago Batista
 *
 *         Classe responsavel por controlar as telas de consultar e manter
 *
 */
@ManagedBean(name = "UsuarioBean")
@ViewScoped
public class UsuarioBean {

	private Usuario usuario;
	private Telefone telefone;
	private List<Usuario> usuarios;
	private UsuarioDaoImpl usuarioDao;
	private String emailSelecionado;
	private String confirmaSenha;
	private String nomePesquisa;
	private String emailPesquisa;
	private String foneMask;
	private int quantPesquisada;

	private static final String PESQUISAR = "pesquisarUsuario.xhtml";

	private static final String Manter = "manterUsuario.xhtml";

	private static final String Login = "index.xhtml";

	public UsuarioBean() {
		this.usuario = new Usuario();
		this.telefone = new Telefone();
		this.usuarios = new ArrayList<Usuario>();
		// Instanciando a interface com a implementa��o, passando a conex�o
		this.usuarioDao = new UsuarioDaoImpl();
		// Recupera a lista de todos os usuarios
		this.usuarios = this.usuarioDao.listarTodos();
		this.quantPesquisada = this.usuarios.size();
		this.usuario = new Usuario();
		this.usuario.setTelefones(new ArrayList<Telefone>());
		this.foneMask = "99999-9999";

	}

	/**
	 * Metodo pesquisar, vai realizar a consulta na base e retornar a lista completa
	 */
	public void pesquisar() {
		this.usuarios = this.usuarioDao.listarTodos();
		this.quantPesquisada = this.usuarios.size();
		System.out.println("Teste");

	}

	/**
	 * Metodo pesquisarNome, vai realizar a consulta na base e retornar a lista por
	 * nome
	 */

	public void pesquisarNome() {
		System.out.println(this.nomePesquisa);
		this.usuarios = this.usuarioDao.listarNome(this.nomePesquisa);
		this.quantPesquisada = this.usuarios.size();
	}

	/**
	 * Metodo pesquisarEmail, vai realizar a consulta na base e retornar a lista por
	 * email
	 */

	public void pesquisarEmail() {
		if (this.usuarioDao.pesquisar(this.emailPesquisa) != null) {
			Usuario usuarioAtual = this.usuarioDao.pesquisar(this.emailPesquisa);
			this.usuarios.clear();
			this.usuarios.add(usuarioAtual);
			this.quantPesquisada = this.usuarios.size();
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "", " email inválido "));
		}
	}

	public void cadastrar() {
		System.out.println(usuario);
		usuarioDao.inserir(usuario);

		System.out.println("Cadastrado");
	}

	public void editar() {
		Usuario usuarioAtual = this.usuarioDao.pesquisar(this.usuario.getEmail());
		System.out.println(this.usuario.getNome());
		//this.usuario = usuarioAtual;
		usuarioAtual = this.usuario;
		usuarioDao.alterar(usuarioAtual);
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "", usuarioAtual.getNome() + " alterado com sucesso."));

		System.out.println("alterado");
	}

	public void remover() {
		System.out.println(usuario);
		Usuario usuarioAtual = this.usuarioDao.pesquisar(this.usuario.getEmail());
		this.usuario = usuarioAtual;

		usuarioDao.remover(usuarioAtual);
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "", usuarioAtual.getNome() + " Excluido com sucesso."));
		System.out.println("removido");
		pesquisar();
	}

	/**
	 * Metodo setarMask, vai setar a m�scara do telefone para celular ou fixo (9 ou
	 * 8 digitos)
	 */

	public void setarMask() {

		System.out.println(this.telefone.getTipo());
		if (this.telefone.getTipo().equals("CELULAR")) {
			this.foneMask = "99999-9999";
		} else {
			this.foneMask = "9999-9999";
		}

	}

	public void adicionarTelefone() {
		Telefone telefoneNovo = new Telefone();
		telefoneNovo.setDdd(this.telefone.getDdd());
		telefoneNovo.setNumero(this.telefone.getNumero());
		telefoneNovo.setTipo(this.telefone.getTipo());
		telefoneNovo.setUsuario(this.usuario);

		if (!this.existeTelefone(telefoneNovo)) {
			this.usuario.getTelefones().add(telefoneNovo);
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
					" Telefone já cadastrado para " + this.usuario.getNome()));

		}

		this.telefone = new Telefone();

	}

	private boolean existeTelefone(Telefone telefone) {
		boolean resultado = false;

		for (Telefone telefoneAtual : this.usuario.getTelefones()) {
			if (telefoneAtual.getNumero().equals(telefone.getNumero()) && telefoneAtual.getDdd() == telefone.getDdd()) {
				resultado = true;
			}
		}

		return resultado;
	}

	public void removerTelefone() {

		int i = 0;
		while (i < this.usuario.getTelefones().size()) {

			if (this.usuario.getTelefones().get(i).getId() == this.telefone.getId()) {
				this.usuario.getTelefones().remove(i);
			}
			i = i + 1;
		}

		System.out.println(this.usuario.getTelefones());

	}

	public void salvar() throws IOException {
		Usuario usuarioAtual = this.usuarioDao.pesquisar(this.usuario.getEmail());
		System.out.println(usuarioAtual);
		System.out.println(usuarioAtual!=null);
		if (usuarioAtual.getEmail() != null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "",
					"Usuário " + usuarioAtual.getEmail() + " já cadastrado"));
		} else {
			if (usuarioDao.inserir(this.usuario)) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "",
						this.usuario.getEmail() + " salvo com sucesso."));
				System.out.println("=======salvou ======");
				this.usuario = new Usuario();
				this.telefone = new Telefone();
				abrirLogin();

			} else {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Erro ao inserir."));
			}
		}

	}

	public void abrirManterUsuario() throws IOException {

		FacesContext.getCurrentInstance().getExternalContext().redirect(Manter);
	}

	public void abrirPesquisarUsuario() throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect(PESQUISAR);

	}

	public void abrirLogin() throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect(Login);

	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Telefone getTelefone() {
		return telefone;
	}

	public void setTelefone(Telefone telefone) {
		this.telefone = telefone;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public String getEmailSelecionado() {
		return emailSelecionado;
	}

	public void setEmailSelecionado(String emailSelecionado) {
		this.emailSelecionado = emailSelecionado;
	}

	public String getConfirmaSenha() {
		return confirmaSenha;
	}

	public void setConfirmaSenha(String confirmaSenha) {
		this.confirmaSenha = confirmaSenha;
	}

	public String getNomePesquisa() {
		return nomePesquisa;
	}

	public void setNomePesquisa(String nomePesquisa) {
		this.nomePesquisa = nomePesquisa;
	}

	public String getEmailPesquisa() {
		return emailPesquisa;
	}

	public void setEmailPesquisa(String emailPesquisa) {
		this.emailPesquisa = emailPesquisa;
	}

	public String getFoneMask() {
		return foneMask;
	}

	public void setFoneMask(String foneMask) {
		this.foneMask = foneMask;
	}

	public int getQuantPesquisada() {
		return quantPesquisada;
	}

	public void setQuantPesquisada(int quantPesquisada) {
		this.quantPesquisada = quantPesquisada;
	}

}
