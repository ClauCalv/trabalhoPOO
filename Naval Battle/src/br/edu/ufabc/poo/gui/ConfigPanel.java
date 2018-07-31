package br.edu.ufabc.poo.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerListModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ConfigPanel extends JPanel {

	protected static final String[] shipConfigExamples = 
		{"2,2,3,4","2,3,3,4,5","2,3,3,4,4,5","2,2,3,3,3,4,4,5,6","2,2,3,3,3,4,4,4,5,5,6","2,2,3,3,4,4,4,5,5,5,6,6,7"};
	
	private JTextField txtShips;
	private JLabel lblConfigure, lblJogarContra, lblTamanhoDoMapa, lblAdicionarNavios;
	private JRadioButton rdbtnOComputador, rdbtnOutroJogador;
	private ButtonGroup btngrpMultiplayer;
	private JSpinner spnMapSize;
	private JButton btnGenShips, btnValidarNavios, btnComecar;
	
	private int mapSize = 6;
	private int[] ships;

	public ConfigPanel() {

		initComponents();
	}
	
	private void initComponents() {
		
		this.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setLayout(null);
		
		lblConfigure = new JLabel("Configure o jogo:");
		lblConfigure.setBounds(10, 11, 101, 14);
		this.add(lblConfigure);
		
		lblJogarContra = new JLabel("Jogar contra:");
		lblJogarContra.setBounds(10, 51, 86, 14);
		this.add(lblJogarContra);
		
		rdbtnOComputador = new JRadioButton("o computador");
		rdbtnOComputador.setBounds(10, 71, 109, 23);
		this.add(rdbtnOComputador);
		
		rdbtnOutroJogador = new JRadioButton("outro jogador");
		rdbtnOutroJogador.setBounds(10, 97, 109, 23);
		this.add(rdbtnOutroJogador);
		
		btngrpMultiplayer = new ButtonGroup();
		btngrpMultiplayer.add(rdbtnOutroJogador);
		btngrpMultiplayer.add(rdbtnOComputador);
		
		lblTamanhoDoMapa = new JLabel("Tamanho do Mapa:");
		lblTamanhoDoMapa.setBounds(10, 162, 109, 14);
		this.add(lblTamanhoDoMapa);
		
		spnMapSize = new JSpinner();
		spnMapSize.setModel(new SpinnerListModel(new String[] {"6x6", "8x8", "10x10", "12x12", "14x14", "16x16"}));
		spnMapSize.setBounds(10, 187, 86, 20);
		spnMapSize.addChangeListener(new ChangeListener() {
			
			public void stateChanged(ChangeEvent event) {
				
				String value = (String) ((JSpinner) event.getSource()).getValue();
				switch (value) {
					case "6x6":
						mapSize = 6;
						break;
					case "8x8":
						mapSize = 8;
						break;
					case "10x10":
						mapSize = 10;
						break;
					case "12x12":
						mapSize = 12;
						break;
					case "14x14":
						mapSize = 14;
						break;
					case "16x16":
						mapSize = 16;
						break;
				}
			}
		});
		this.add(spnMapSize);
		
		lblAdicionarNavios = new JLabel("Adicionar navios:");
		lblAdicionarNavios.setBounds(182, 51, 101, 14);
		this.add(lblAdicionarNavios);
		
		txtShips = new JTextField();
		txtShips.setBounds(182, 72, 158, 20);
		this.add(txtShips);
		
		btnGenShips = new JButton("?");
		btnGenShips.setToolTipText("Sugest\u00E3o de Navios");
		btnGenShips.setBounds(310, 97, 30, 23);
		btnGenShips.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent event) {
				
				txtShips.setText(shipConfigExamples[(mapSize-6)/2]);
			}
		});
		this.add(btnGenShips);
		
		
		btnValidarNavios = new JButton("Validar navios");
		btnValidarNavios.setBounds(182, 97, 118, 23);
		btnValidarNavios.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent event) {
				
				String[] ships = txtShips.getText().split(",");
				int[] shipSizes = new int[ships.length];
				boolean valid = false;
				try {
					for (int i = 0; i < ships.length; i++)
						shipSizes[i] = Integer.parseInt(ships[i]);
					
					valid = isShipConfigValid(shipSizes);
				} catch (Exception e2) { }
				
				if(valid) {
					btnComecar.setEnabled(true);
					setShips(shipSizes);
				}
				else {
					String message = "A configuração usada é inválida.\n"
								   + " - Use somente vírgulas para separar os valores;\n"
								   + " - Os navios devem ter tamanho maior ou igual a 2;\n"
								   + " - Os navios não podem ser maiores que a lateral do mapa.";
					JOptionPane.showMessageDialog(null, message, "Configuração Inválida", JOptionPane.ERROR_MESSAGE);
					btnComecar.setEnabled(false);
				}
			}
		});
		this.add(btnValidarNavios);
		
		btnComecar = new JButton("Come\u00E7ar Jogo");
		btnComecar.setEnabled(false);
		btnComecar.setBounds(182, 186, 158, 23);
		btnComecar.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent event) {
				// TODO Auto-generated method stub
				
			}
		});
		this.add(btnComecar);
	}

	protected void setShips(int[] ships) {
		
		this.ships = ships;
	}

	protected boolean isShipConfigValid(int[] shipSizes) {
		
		int count = 0;
		
		for (int size : shipSizes) {
			if(size < 2 || size > mapSize)
				return false;
			count += size;
		}
		
		return count <= mapSize * mapSize / 2;
	}

}
