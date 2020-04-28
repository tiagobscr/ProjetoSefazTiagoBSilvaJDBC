package controle;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManagerFactory;
import dao.UsuarioDaoImpl;
import entidade.Usuario;


/**
 * 
 * @author Tiago Batista
 *
 *         LoginBean, classe responsavel pela logica de logar no sistema
 *
 */

@ManagedBean(name = "LoginBean")
@ViewScoped
public class LoginBean {

	private String usuarioTXT;
	private String senhaTXT;
	UsuarioDaoImpl usuarioDao;
	private String mensagem;
	EntityManagerFactory factory;
	private Usuario usuarioLogado;
	Usuario usuario;
	private String confirmaSenha;

	private static final String PESQUISAR = "pesquisarUsuario.xhtml";

	private static final String Manter = "manterUsuario.xhtml";

	private static final String Login = "index.xhtml";

	private static final String Senha = "novaSenha.xhtml";

	public LoginBean() {
		this.usuario = new Usuario();
		this.usuarioDao = new UsuarioDaoImpl();

	}

	public void entrar() throws IOException {

		if (this.usuarioTXT.equals("admin") && this.senhaTXT.equals("admin")) {
			FacesContext.getCurrentInstance().getExternalContext().redirect(PESQUISAR);
		} else {
			//this.usuario=this.usuarioDao.pesquisar(this.usuarioTXT);
			if (this.usuarioDao.pesquisar(this.usuarioTXT).getEmail() != null) {
				if (this.usuarioDao.pesquisar(this.usuarioTXT).getSenha().equals(this.senhaTXT)) {
					this.usuarioLogado = usuarioDao.pesquisar(this.usuarioTXT);

					this.usuario = this.usuarioLogado;

					FacesContext.getCurrentInstance().getExternalContext().redirect(PESQUISAR);
				} else {
					this.mensagem = " Senha inválida";
				}
			} else {

				this.mensagem = " Usuario não cadastrado";
			}

		}
	}

	public void abrirNovaSenha() throws IOException {

		if (this.usuarioDao.pesquisar(this.usuarioTXT).getEmail() != null) {
			this.usuario = new Usuario();
			this.usuario.setEmail(this.usuarioTXT);

		} else {
			this.usuario.setEmail("");

		}

		FacesContext.getCurrentInstance().getExternalContext().redirect(Senha);
	}

	public void novaSenha() throws IOException {

		if (this.usuarioDao.pesquisar(this.usuario.getEmail()).getEmail() != null) {

			Usuario usuarioAtual = this.usuarioDao.pesquisar(this.usuario.getEmail());
			System.out.println(usuarioAtual.getNome());
			System.out.println(this.usuario.getNome());

			if (this.usuario.getNome().equals(usuarioAtual.getNome())
					&& this.usuario.getEmail().equals(usuarioAtual.getEmail())) {
				usuarioAtual.setSenha(this.usuario.getSenha());
				usuarioDao.alterar(usuarioAtual);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "",
						usuarioAtual.getNome() + " senha alterada com sucesso."));
				this.mensagem = " Senha alterada com sucesso.";
				usuarioAtual = new Usuario();
				System.out.println("alterado");
				abrirLogin();
			} else {

				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "", " dados inválidos."));
				usuarioAtual = new Usuario();
				this.usuario = new Usuario();

			}
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "", " Usuário inválido."));

			this.mensagem = " Usuário inválido.";

		}

	}

	public void abrirLogin() throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect(Login);

	}

	public void abrirManterUsuario() throws IOException {

		FacesContext.getCurrentInstance().getExternalContext().redirect(Manter);
	}

	public String getUsuarioTXT() {
		return usuarioTXT;
	}

	public void setUsuarioTXT(String usuarioTXT) {
		this.usuarioTXT = usuarioTXT;
	}

	public String getSenhaTXT() {
		return senhaTXT;
	}

	public void setSenhaTXT(String senhaTXT) {
		this.senhaTXT = senhaTXT;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public Usuario getUsuarioLogado() {
		return usuarioLogado;
	}

	public void setUsuarioLogado(Usuario usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getConfirmaSenha() {
		return confirmaSenha;
	}

	public void setConfirmaSenha(String confirmaSenha) {
		this.confirmaSenha = confirmaSenha;
	}

}
