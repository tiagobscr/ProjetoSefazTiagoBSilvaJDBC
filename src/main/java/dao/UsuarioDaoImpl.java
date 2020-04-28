package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import entidade.Telefone;
import entidade.Usuario;
import util.ConnectionBase1;

/**
 * 
 * @author Tiago Batista
 *
 * 
 *
 */
public class UsuarioDaoImpl {

	private static UsuarioDaoImpl instance;

	// Singleton
	public static UsuarioDaoImpl getInstance() {
		if (instance == null)
			instance = new UsuarioDaoImpl();
		return instance;
	}

	public boolean inserir(Usuario usuario) {
		// TODO Auto-generated method stub
		List<Telefone> telefones = new ArrayList<Telefone>();
		try {

			Connection conn = ConnectionBase1.getConncetion();
			String sql = "INSERT INTO USUARIO (nome,email,senha) VALUES (?,?,?)";
			String sql1 = "INSERT INTO TELEFONE (ddd,numero,tipo,email_usuario) VALUES (?,?,?,?)";

			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, usuario.getNome());
			stmt.setString(2, usuario.getEmail());
			stmt.setString(3, usuario.getSenha());

			stmt.executeUpdate();
			stmt.close();

			telefones = usuario.getTelefones();

			for (Telefone telefone : telefones) {
				PreparedStatement stmt1 = conn.prepareStatement(sql1);
				stmt1.setInt(1, telefone.getDdd());
				stmt1.setString(2, telefone.getNumero());
				stmt1.setString(3, telefone.getTipo());
				stmt1.setString(4, usuario.getEmail());
				stmt1.executeUpdate();
			}

			conn.close();
			System.out.println(usuario.getNome() + " Salvo com sucesso");

			return true;

		} catch (Exception e) {

			System.out.println(e.getMessage());
			Logger.getLogger(ConnectionBase1.class.getName()).log(Level.SEVERE, null, e);
			return false;

		}

	}

	public void alterar(Usuario usuario) {
		// TODO Auto-generated method stub
		System.out.println(usuario);
		System.out.println(usuario.getEmail());
        System.out.println(usuario.getNome());
		try {
			Connection conn = ConnectionBase1.getConncetion();
			String sql = "UPDATE  USUARIO  SET nome=?,senha=? WHERE email = '" + usuario.getEmail() + "'";
			String sql1 = "INSERT INTO TELEFONE (ddd,numero,tipo,email_usuario) VALUES (?,?,?,?)";
			Statement instrucaoSQL;
			instrucaoSQL = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			// ResultSet resultados;
			ResultSet resultados1;
			resultados1 = instrucaoSQL
					.executeQuery("SELECT * FROM TELEFONE WHERE email_usuario= '" + usuario.getEmail() + "'");

			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, usuario.getNome());
			stmt.setString(2, usuario.getSenha());

			stmt.executeUpdate();
			stmt.close();

			List<Telefone> telefonesNovo = usuario.getTelefones();
			List<Telefone> telefonesAtual = new ArrayList<Telefone>();

			while (resultados1.next()) {
				Telefone novoTelefone = new Telefone();
				novoTelefone.setId(resultados1.getInt("id_telefone"));
				novoTelefone.setDdd(resultados1.getInt("ddd"));
				novoTelefone.setNumero(resultados1.getString("numero"));
				novoTelefone.setTipo(resultados1.getString("tipo"));
				telefonesAtual.add(novoTelefone);
				System.out.println(novoTelefone);
				System.out.println(telefonesAtual);

			}

			if (!telefonesAtual.equals(telefonesNovo)) {
				System.out.println("teste0");
				instrucaoSQL.executeUpdate("DELETE  TELEFONE WHERE email_usuario = '" + usuario.getEmail() + "'");
				List<Telefone> telefones = usuario.getTelefones();

				for (Telefone telefone : telefones) {
					PreparedStatement stmt1 = conn.prepareStatement(sql1);
					stmt1.setInt(1, telefone.getDdd());
					stmt1.setString(2, telefone.getNumero());
					stmt1.setString(3, telefone.getTipo());
					stmt1.setString(4, usuario.getEmail());
					stmt1.executeUpdate();
				}

			}

			conn.close();
			System.out.println(usuario.getNome() + " Salvo com sucesso");
		} catch (Exception e) {

			System.out.println(e.getMessage());

			Logger.getLogger(ConnectionBase1.class.getName()).log(Level.SEVERE, null, e);

		}

	}

	public void remover(Usuario usuario) {
		// TODO Auto-generated method stub
		System.out.println(usuario);
		try {

			Connection conn = ConnectionBase1.getConncetion();
			Statement instrucaoSQL;
			instrucaoSQL = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			instrucaoSQL.executeUpdate("DELETE  TELEFONE WHERE email_usuario = '" + usuario.getEmail() + "'");
			instrucaoSQL.executeUpdate("DELETE USUARIO WHERE email = '" + usuario.getEmail() + "'");
			

		} catch (Exception e) {

			System.out.println(e.getMessage());
			Logger.getLogger(ConnectionBase1.class.getName()).log(Level.SEVERE, null, e);

		}

	}

	/**
	 * Pesquisar, pesquisar pela chave primaria que é o email
	 */

	public Usuario pesquisar(String email) {

		Usuario usuario = new Usuario();
		List<Usuario> usuarios = new ArrayList<Usuario>();

		try {

			Connection conn = ConnectionBase1.getConncetion();
			Statement instrucaoSQL;
			ResultSet resultados;
			ResultSet resultados1;
			instrucaoSQL = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			resultados = instrucaoSQL.executeQuery("SELECT * FROM USUARIO WHERE email= '" + email + "'");

			while (resultados.next()) {

				usuario.setEmail(resultados.getString("email"));
				usuario.setNome(resultados.getString("nome"));
				usuario.setSenha(resultados.getString("senha"));
				usuarios.add(usuario);
				System.out.println(usuario);

			}

			for (Usuario usuario1 : usuarios) {

				resultados1 = instrucaoSQL.executeQuery("SELECT * FROM TELEFONE WHERE email_usuario= '" + email + "'");
				// SELECT * FROM TELEFONE WHERE email_usuario= '%" + usuario.getEmail() + "%'
				List<Telefone> telefones = new ArrayList<Telefone>();
				usuario.setTelefones(telefones);
				while (resultados1.next()) {
					Telefone novoTelefone = new Telefone();
					novoTelefone.setId(resultados1.getInt("id_telefone"));
					novoTelefone.setDdd(resultados1.getInt("ddd"));
					novoTelefone.setNumero(resultados1.getString("numero"));
					novoTelefone.setTipo(resultados1.getString("tipo"));
					telefones.add(novoTelefone);
					System.out.println(usuario1);
					System.out.println(novoTelefone);
					System.out.println(telefones);

				}
			}
			conn.close();

		} catch (SQLException ex) {

			Logger.getLogger(ConnectionBase1.class.getName()).log(Level.SEVERE, null, ex);
		}

		System.out.println(usuario);

		return usuario;
	}

	/**
	 * O metodo listar todos, faz um select * from, porém com o JPA, vc faz a
	 * consulta pelo objeto direto assim from Usuario, que isso é o objeto usuario e
	 * não a tabela
	 */

	public List<Usuario> listarTodos() {

		List<Usuario> usuarios = new ArrayList<Usuario>();

		try {

			Connection conn = ConnectionBase1.getConncetion();
			Statement instrucaoSQL;
			ResultSet resultados;
			ResultSet resultados1;
			instrucaoSQL = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			resultados = instrucaoSQL.executeQuery("SELECT * FROM USUARIO order by nome ASC ");

			while (resultados.next()) {
				Usuario usuario = new Usuario();
				usuario.setEmail(resultados.getString("email"));
				usuario.setNome(resultados.getString("nome"));
				usuario.setSenha(resultados.getString("senha"));

				usuarios.add(usuario);

				System.out.println(usuarios);

			}

			for (Usuario usuario : usuarios) {

				resultados1 = instrucaoSQL
						.executeQuery("SELECT * FROM TELEFONE WHERE email_usuario= '" + usuario.getEmail() + "'");
				// SELECT * FROM TELEFONE WHERE email_usuario= '%" + usuario.getEmail() + "%'
				List<Telefone> telefones = new ArrayList<Telefone>();
				usuario.setTelefones(telefones);
				while (resultados1.next()) {
					Telefone novoTelefone = new Telefone();
					novoTelefone.setId(resultados1.getInt("id_telefone"));
					novoTelefone.setDdd(resultados1.getInt("ddd"));
					novoTelefone.setNumero(resultados1.getString("numero"));
					novoTelefone.setTipo(resultados1.getString("tipo"));
					telefones.add(novoTelefone);
					System.out.println(novoTelefone);
					System.out.println(telefones);

				}
			}
			conn.close();

		} catch (SQLException ex) {

			Logger.getLogger(ConnectionBase1.class.getName()).log(Level.SEVERE, null, ex);
		}

		return usuarios;
	}

	/**
	 * O metodo listar nome, faz um select por nome usando Where e LIKE para procura
	 * 
	 */

	public List<Usuario> listarNome(String nome) {
		// TODO Auto-generated method stub

		List<Usuario> usuarios = new ArrayList<Usuario>();

		try {

			Connection conn = ConnectionBase1.getConncetion();
			Statement instrucaoSQL;
			ResultSet resultados;
			ResultSet resultados1;
			instrucaoSQL = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			resultados = instrucaoSQL
					.executeQuery("SELECT * FROM Usuario WHERE nome like '%" + nome + "%' order by nome ASC ");

			while (resultados.next()) {
				Usuario usuario = new Usuario();
				usuario.setEmail(resultados.getString("email"));
				usuario.setNome(resultados.getString("nome"));
				usuario.setSenha(resultados.getString("senha"));

				usuarios.add(usuario);

				System.out.println(usuarios);

			}

			for (Usuario usuario : usuarios) {

				resultados1 = instrucaoSQL
						.executeQuery("SELECT * FROM TELEFONE WHERE email_usuario= '" + usuario.getEmail() + "'");
				List<Telefone> telefones = new ArrayList<Telefone>();
				usuario.setTelefones(telefones);

				while (resultados1.next()) {
					Telefone novoTelefone = new Telefone();
					novoTelefone.setId(resultados1.getInt("id_telefone"));
					novoTelefone.setDdd(resultados1.getInt("ddd"));
					novoTelefone.setNumero(resultados1.getString("numero"));
					novoTelefone.setTipo(resultados1.getString("tipo"));
					usuario.getTelefones().add(novoTelefone);

					System.out.println(usuarios);

				}
			}
			conn.close();

		} catch (SQLException ex) {

			Logger.getLogger(ConnectionBase1.class.getName()).log(Level.SEVERE, null, ex);
		}

		return usuarios;
	}

}
