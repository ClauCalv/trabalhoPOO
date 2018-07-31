package br.edu.ufabc.poo.gui;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

public class LoginPanel extends JPanel {

	private JLabel lblTurnoDoJogador, lblColoqueASenha;
	private JButton btnEntrar;
	private JPasswordField passwordField;

	public LoginPanel() {
		
		initComponents();
	}
	
	private void initComponents() {
		
		this.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setLayout(null);
		
		lblTurnoDoJogador = new JLabel("Turno do jogador 1:");
		lblTurnoDoJogador.setBounds(10, 11, 117, 14);
		this.add(lblTurnoDoJogador);
		
		lblColoqueASenha = new JLabel("Coloque a senha para jogar:");
		lblColoqueASenha.setBounds(10, 50, 168, 14);
		this.add(lblColoqueASenha);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(10, 75, 161, 20);
		this.add(passwordField);
		
		btnEntrar = new JButton("Entrar");
		btnEntrar.setBounds(181, 74, 89, 23);
		this.add(btnEntrar);
	}

}
