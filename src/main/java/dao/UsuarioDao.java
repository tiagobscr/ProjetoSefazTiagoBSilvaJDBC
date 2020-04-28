package dao;

import java.util.List;

import entidade.Usuario;

/**
 * 
 * @author Tiago Batista
 *
 *         UsuarioDAO é uma interface, onde compartilha a chamada dos metodos,
 *         mas não os implementam.
 */

public interface UsuarioDao {

	public boolean inserir(Usuario usuario);

	public void alterar(Usuario usuario);

	public void remover(Usuario usuario);

	public Usuario pesquisar(String email);

	public List<Usuario> listarTodos();

	public List<Usuario> listarNome(String nome);
}
