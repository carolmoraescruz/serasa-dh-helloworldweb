package br.com.hello.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.hello.adapter.ConnectionFactory;
import br.com.hello.entity.Contato;

public class ContatoRepository implements IRepository<Contato>{
	private Connection connection;
	
	//jdbc
	
	public ContatoRepository() {
		try {
			connection = ConnectionFactory.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	@Override
	public void save(Contato contato) {
		String sql = "INSERT INTO pessoas(nome, email) values(?,?)"; //evitar ataques de SQL injection
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, contato.getNome());
			stmt.setString(2, contato.getEmail());
			stmt.execute();
			stmt.close();
			
		} catch (SQLException ex) {
			throw new RuntimeException("Erro 500");
		}
	}

	@Override
	public void delete(Integer id) {
		String sql = "DELETE FROM pessoas WHERE id = ?"; //evitar ataques de SQL injection
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setInt(1, id);;
			stmt.execute();
			stmt.close();
			
		} catch (SQLException ex) {
			throw new RuntimeException("Erro 500");
		}
		
	}

	@Override
	public List<Contato> findAll() {
		List<Contato> contatos = new ArrayList<>();
		String sql = "SELECT * FROM pessoas"; //evitar ataques de SQL injection
		try {
			PreparedStatement stmt = connection.prepareStatement(sql); // Leva info pro banco
			ResultSet result = stmt.executeQuery(); // Retorna info do banco
			
			while(result.next()) {
				Contato contato = new Contato();
				contato.setId(result.getInt("id"));
				contato.setNome(result.getString("nome"));
				contato.setEmail(result.getString("email"));
				contatos.add(contato);
			}
			result.close();
			stmt.close();
			
		} catch (SQLException ex) {
			throw new RuntimeException("Erro 500");
		}
		return contatos;
	}

	@Override
	public Contato findById(Integer id) {
		String sql = "SELECT * FROM pessoas WHERE id=?"; //evitar ataques de SQL injection
		Contato contato = new Contato();
		
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setInt(1, id);
			ResultSet result = stmt.executeQuery(); // Retorna info do banco
			
			if(result.first()) {
				contato.setNome(result.getString("nome"));
				contato.setEmail(result.getString("email"));
			}
			result.close();
			stmt.close();
			
		} catch (SQLException ex) {
			throw new RuntimeException("Erro 500");
		}
		
		return contato;
	}

	@Override
	public void update(Contato contato) {
		String sql = "UPDATE pessoas SET nome=?,email=? WHERE id=?"; //evitar ataques de SQL injection
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, contato.getNome());
			stmt.setString(2, contato.getEmail());
			stmt.setInt(3, contato.getId());
			stmt.execute();
			stmt.close();
			
		} catch (SQLException ex) {
			throw new RuntimeException("Erro 500");
		}
		
	}

}