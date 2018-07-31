package br.edu.ufabc.poo.gui;

import java.awt.CardLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainFrame extends JFrame {

	private JPanel cards;
	private JPanel configPanelCard, loginPanelCard, playPanelCard;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MainFrame() {
		this.setTitle("Batalha Naval");
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100, 100, 380, 300);
		
		cards = new JPanel();
		setContentPane(cards);
		cards.setLayout(new CardLayout());
		
		configPanelCard = new ConfigPanel();
		cards.add(configPanelCard);
		
		this.setVisible(true);
	}
}
