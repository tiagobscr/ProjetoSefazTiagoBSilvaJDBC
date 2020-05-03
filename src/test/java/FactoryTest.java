import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import dao.UsuarioDao;
import dao.UsuarioDaoImpl;
import entidade.Telefone;
import entidade.Usuario;

public class FactoryTest {

	UsuarioDaoImpl usuarioDao;

	List<Usuario> usuarios;

	@Test
	public void Test() {
		usuarioDao = new UsuarioDaoImpl();
		usuarios = UsuarioDaoImpl.getInstance().listarTodos();
		System.out.println(usuarios);
		for (Usuario usuario : usuarios) {
			System.out.println(usuario.getEmail());
			System.out.println(usuario.getTelefones());

		}
        
	    usuarios = UsuarioDaoImpl.getInstance().listarNome("Tiago");
		System.out.println(usuarios);

		for (Usuario usuario : usuarios) {
			System.out.println(usuario.getEmail());
			System.out.println(usuario.getTelefones());

		}
		Usuario usuario = UsuarioDaoImpl.getInstance().pesquisar("zenaide@gmail.com");
		System.out.println(usuario);
		System.out.println(usuario.getTelefones());
		Telefone telefone = new Telefone();
		telefone.setDdd(81);
		telefone.setNumero("98173-3022");
		telefone.setTipo("FIXO");
		//List<Telefone> telefones = usuario.getTelefones();
		//List<Telefone> novosTelefones = new ArrayList<Telefone>();
		usuario.getTelefones().remove(1);
		usuario.getTelefones().add(telefone);
		
		//usuarioDao.alterar(usuario);
		

	}

}
