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
	private Connection conn = null;

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

			conn = ConnectionBase1.getConncetion();
			String sql = "INSERT INTO USUARIO (nome,email,senha) VALUES (?,?,?)  ";
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

			System.out.println(usuario.getNome() + " Salvo com sucesso");

			return true;

		} catch (Exception e) {

			System.out.println(e.getMessage());
			Logger.getLogger(ConnectionBase1.class.getName()).log(Level.SEVERE, null, e);

			return false;

		} finally {
			try {
				conn.close();
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}

	}

	public void alterar(Usuario usuario) {
		// TODO Auto-generated method stub
		System.out.println(usuario);
		System.out.println(usuario.getEmail());
		System.out.println(usuario.getNome());

		try {
			conn = ConnectionBase1.getConncetion();
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
				novoTelefone.setUsuario(usuario);
				telefonesAtual.add(novoTelefone);
				System.out.println(novoTelefone);
				System.out.println(telefonesAtual);

			}

			if (telefonesAtual.equals(telefonesNovo))
				System.out.println("Iguais= 1");
			if (telefonesAtual.containsAll(telefonesNovo))
				System.out.println("Iguais= 2");
			if (telefonesAtual == telefonesNovo)
				System.out.println("Iguais= 3");
			if (telefonesNovo.containsAll(telefonesAtual))
				System.out.println("Iguais= 4");

			if (!telefonesAtual.equals(telefonesNovo)) {
				System.out.println("teste0");
				if (!telefonesNovo.containsAll(telefonesAtual))
					instrucaoSQL.executeUpdate("DELETE  TELEFONE WHERE email_usuario = '" + usuario.getEmail() + "'");
				List<Telefone> telefones = usuario.getTelefones();

				if (!telefonesAtual.containsAll(telefonesNovo))
					for (Telefone telefone : telefones) {
						PreparedStatement stmt1 = conn.prepareStatement(sql1);
						stmt1.setInt(1, telefone.getDdd());
						stmt1.setString(2, telefone.getNumero());
						stmt1.setString(3, telefone.getTipo());
						stmt1.setString(4, usuario.getEmail());
						stmt1.executeUpdate();
					}

			}

			System.out.println(usuario.getNome() + " Salvo com sucesso");
		} catch (Exception e) {

			System.out.println(e.getMessage());

			Logger.getLogger(ConnectionBase1.class.getName()).log(Level.SEVERE, null, e);

		} finally {
			try {
				conn.close();
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}

	}

	public void remover(Usuario usuario) {
		// TODO Auto-generated method stub
		System.out.println(usuario);
		try {

			conn = ConnectionBase1.getConncetion();
			Statement instrucaoSQL;
			instrucaoSQL = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			instrucaoSQL.executeUpdate("DELETE  TELEFONE WHERE email_usuario = '" + usuario.getEmail() + "'");
			instrucaoSQL.executeUpdate("DELETE USUARIO WHERE email = '" + usuario.getEmail() + "'");

		} catch (Exception e) {

			System.out.println(e.getMessage());
			Logger.getLogger(ConnectionBase1.class.getName()).log(Level.SEVERE, null, e);

		} finally {
			try {
				conn.close();
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}

	}

	/**
	 * Pesquisar, pesquisar pela chave primaria que é o email
	 */

	public Usuario pesquisar(String email) {

		Usuario usuario = new Usuario();
		List<Usuario> usuarios = new ArrayList<Usuario>();

		try {

			conn = ConnectionBase1.getConncetion();
			Statement instrucaoSQL;
			ResultSet resultados;

			instrucaoSQL = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			resultados = instrucaoSQL.executeQuery(
					"SELECT * FROM USUARIO  JOIN TELEFONE  ON USUARIO.email=TELEFONE.email_usuario WHERE USUARIO.email= '"
							+ email + "'");
			// "SELECT * FROM USUARIO WHERE email= '" + email + "'"
			List<Telefone> telefones = new ArrayList<Telefone>();

			while (resultados.next()) {

				Telefone telefone = new Telefone();

				usuario.setEmail(resultados.getString("email"));
				usuario.setNome(resultados.getString("nome"));
				usuario.setSenha(resultados.getString("senha"));
				telefone.setId(resultados.getInt("id_telefone"));
				telefone.setDdd(resultados.getInt("ddd"));
				telefone.setNumero(resultados.getString("numero"));
				telefone.setTipo(resultados.getString("tipo"));
				telefone.setUsuario(usuario);
				System.out.println(telefone);
				telefones.add(telefone);

				if (!usuarios.contains(usuario))
					usuarios.add(usuario);
			}

			List<Telefone> telefonesUsuario = new ArrayList<Telefone>();
			for (Telefone telefone : telefones) {

				if (telefone.getUsuario().getEmail().equals(usuario.getEmail()))
					telefonesUsuario.add(telefone);
			}
			usuario.setTelefones(telefonesUsuario);

			System.out.println(usuarios);

		} catch (SQLException ex) {

			Logger.getLogger(ConnectionBase1.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			try {
				conn.close();
				System.out.println(conn.isClosed());
			} catch (SQLException e) {

				e.printStackTrace();
			}
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

			conn = ConnectionBase1.getConncetion();
			Statement instrucaoSQL;
			ResultSet resultados;
			instrucaoSQL = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			resultados = instrucaoSQL.executeQuery(
					"SELECT * FROM USUARIO  JOIN TELEFONE  ON USUARIO.email=TELEFONE.email_usuario ORDER BY USUARIO.email ");

			List<Telefone> telefones = new ArrayList<Telefone>();

			while (resultados.next()) {
				Usuario usuario = new Usuario();
				Telefone telefone = new Telefone();

				usuario.setEmail(resultados.getString("email"));
				usuario.setNome(resultados.getString("nome"));
				usuario.setSenha(resultados.getString("senha"));
				telefone.setId(resultados.getInt("id_telefone"));
				telefone.setDdd(resultados.getInt("ddd"));
				telefone.setNumero(resultados.getString("numero"));
				telefone.setTipo(resultados.getString("tipo"));
				telefone.setUsuario(usuario);
				System.out.println(telefone);
				telefones.add(telefone);

				if (!usuarios.contains(usuario))
					usuarios.add(usuario);
			}

			for (Usuario usuario : usuarios) {
				List<Telefone> telefonesUsuario = new ArrayList<Telefone>();
				for (Telefone telefone : telefones) {

					if (telefone.getUsuario().getEmail().equals(usuario.getEmail()))
						telefonesUsuario.add(telefone);
				}
				usuario.setTelefones(telefonesUsuario);

			}

			System.out.println(usuarios);

		} catch (SQLException ex) {

			Logger.getLogger(ConnectionBase1.class.getName()).log(Level.SEVERE, null, ex);

		} finally {
			try {
				conn.close();
			} catch (SQLException e) {

				e.printStackTrace();
			}
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

			conn = ConnectionBase1.getConncetion();
			Statement instrucaoSQL;
			ResultSet resultados;

			instrucaoSQL = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			resultados = instrucaoSQL.executeQuery(
					"SELECT * FROM USUARIO  JOIN TELEFONE  ON USUARIO.email=TELEFONE.email_usuario WHERE USUARIO.nome like '%"
							+ nome + "%' ORDER BY USUARIO.nome ASC ");
			// "SELECT * FROM Usuario WHERE nome like '%" + nome + "%' order by nome ASC "

			List<Telefone> telefones = new ArrayList<Telefone>();

			while (resultados.next()) {
				Usuario usuario = new Usuario();
				Telefone telefone = new Telefone();

				usuario.setEmail(resultados.getString("email"));
				usuario.setNome(resultados.getString("nome"));
				usuario.setSenha(resultados.getString("senha"));
				telefone.setId(resultados.getInt("id_telefone"));
				telefone.setDdd(resultados.getInt("ddd"));
				telefone.setNumero(resultados.getString("numero"));
				telefone.setTipo(resultados.getString("tipo"));
				telefone.setUsuario(usuario);
				System.out.println(telefone);
				telefones.add(telefone);

				if (!usuarios.contains(usuario))
					usuarios.add(usuario);
			}

			for (Usuario usuario : usuarios) {
				List<Telefone> telefonesUsuario = new ArrayList<Telefone>();
				for (Telefone telefone : telefones) {

					if (telefone.getUsuario().getEmail().equals(usuario.getEmail()))
						telefonesUsuario.add(telefone);
				}
				usuario.setTelefones(telefonesUsuario);

			}

		} catch (SQLException ex) {

			Logger.getLogger(ConnectionBase1.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}

		return usuarios;
	}

}
