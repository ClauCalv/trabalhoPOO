package br.edu.ufabc.poo.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

import br.edu.ufabc.poo.model.Status;
import br.edu.ufabc.poo.model.Vector2D;
import br.edu.ufabc.poo.players.Player;

public class PlayPanel extends JPanel {

	private JTable playerTable;
	private JTextField txtCoord;
	private JTextField txtUnkBox, txtMissBox, txtHitBox, txtSunkBox;
	private JLabel lblUnkText, lblMissText, lblHitText, lblSunkText;
	private JButton btnAtirar, btnDica;
	private JLabel lblMapaDeBatalha, lblClique, lblLegenda;
	
	private Player player;
	
	public PlayPanel(int mapSize, Player player) {
		this.player = player;
		
		initComponents(mapSize);		
	}
	
	private void initComponents(int mapSize) {
		this.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setLayout(null);
		
		lblMapaDeBatalha = new JLabel("Mapa de batalha:");
		lblMapaDeBatalha.setBounds(15, 15, 101, 14);
		this.add(lblMapaDeBatalha);
		
		playerTable = createBattleMapTable(mapSize);
		playerTable.setBounds(15, 35, 200, 200);
		this.add(playerTable);
		
		txtCoord = new JTextField();
		txtCoord.setColumns(10);
		txtCoord.setBounds(225, 55, 60, 20);
		this.add(txtCoord);
		
		btnAtirar = new JButton("Atirar");
		btnAtirar.setBounds(225, 85, 100, 23);
		this.add(btnAtirar);
		
		lblClique = new JLabel("Clique no mapa:");
		lblClique.setBounds(225, 35, 100, 14);
		this.add(lblClique);
		
		lblLegenda = new JLabel("Legenda:");
		lblLegenda.setBounds(225, 120, 100, 14);
		this.add(lblLegenda);
		
		btnDica = new JButton("?");
		btnDica.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnDica.setToolTipText("Dica");
		btnDica.setBounds(295, 55, 30, 23);
		this.add(btnDica);
		
		txtUnkBox = new JTextField();
		txtUnkBox.setEditable(false);
		txtUnkBox.setBackground(Color.LIGHT_GRAY);
		txtUnkBox.setBounds(225, 140, 25, 20);
		this.add(txtUnkBox);
		txtUnkBox.setColumns(10);
		
		txtMissBox = new JTextField();
		txtMissBox.setEditable(false);
		txtMissBox.setColumns(10);
		txtMissBox.setBackground(Color.RED);
		txtMissBox.setBounds(225, 165, 25, 20);
		this.add(txtMissBox);
		
		txtHitBox = new JTextField();
		txtHitBox.setEditable(false);
		txtHitBox.setColumns(10);
		txtHitBox.setBackground(Color.GREEN);
		txtHitBox.setBounds(225, 190, 25, 20);
		this.add(txtHitBox);
		
		txtSunkBox = new JTextField();
		txtSunkBox.setEditable(false);
		txtSunkBox.setColumns(10);
		txtSunkBox.setBackground(Color.BLUE);
		txtSunkBox.setBounds(225, 215, 25, 20);
		this.add(txtSunkBox);
		
		lblUnkText = new JLabel("Desconhecido");
		lblUnkText.setBounds(255, 143, 86, 14);
		this.add(lblUnkText);
		
		lblMissText = new JLabel("Erro");
		lblMissText.setBounds(255, 168, 46, 14);
		this.add(lblMissText);
		
		lblHitText = new JLabel("Acerto");
		lblHitText.setBounds(255, 193, 46, 14);
		this.add(lblHitText);
		
		lblSunkText = new JLabel("Navio Afundado");
		lblSunkText.setBounds(255, 218, 109, 14);
		this.add(lblSunkText);
	}
	
	private JTable createBattleMapTable(int mapSize) {
		
		String[][] cellData = new String[mapSize][mapSize];
		for (int i = 0; i < cellData.length; i++)
			for (int j = 0; j < cellData.length; j++)
				cellData[i][j] = ('A' + i) + "" + (1 + j);
		
		Object[] columnNames = new Object[mapSize];
		for (int i = 0; i < columnNames.length; i++)
			columnNames[i] = "C" + i;
		
		JTable table = new JTable(cellData, columnNames);
		
		@SuppressWarnings("serial")
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
			
			public Component getTableCellRendererComponent (JTable table, Object value,
					boolean isSelected,boolean hasFocus, int row, int column) { 
					
				Component cell = super.getTableCellRendererComponent (table, value,
						isSelected, hasFocus, row, column);
	  
				cell.setBackground( getCellColor(row, column) );
	      
				return cell;
			}
		};		
		table.setDefaultRenderer(Object.class, renderer);
		
		MouseAdapter listener = new MouseAdapter() {
			
			public void mouseClicked(MouseEvent event) {
				
				int row = table.rowAtPoint(event.getPoint());
				int column = table.columnAtPoint(event.getPoint());
				
				txtCoord.setText((String) table.getValueAt(row, column));
			}
		};
		table.addMouseListener(listener);
		
		return table;
		
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	private Color getCellColor(int row, int column) {
		
		Status status = player.getBattleMap().getStatusAtPoint(row, column);
		
		return status == Status.MISS ? Color.RED : status == Status.HIT ? Color.GREEN :
				status == Status.SUNK ? Color.BLUE : Color.LIGHT_GRAY;
	}
	
	private Vector2D cellCoordToMapCoord(String cellCoord) {
		int x = cellCoord.charAt(0) - 'A';
		int y = Integer.parseInt(cellCoord.substring(1, cellCoord.length()));
		return new Vector2D(x, y);
	}

}
