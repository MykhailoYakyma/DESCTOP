package kadammScreens;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import xml.NodosXML;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.GridLayout;
import java.awt.Color;
import javax.swing.BoxLayout;
import java.awt.FlowLayout;
import javax.swing.JPasswordField;

public class OngoingContest extends JFrame {

	private JPanel contentPane;
	private Timer tm;
	private JButton btnNewButton;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OngoingContest frame = new OngoingContest();
					frame.setVisible(true);
					frame.tm.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public OngoingContest() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Waiting Room");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(836, 645);
		setLocationRelativeTo(null);
		Image img = new ImageIcon(this.getClass().getResource("/icon_login.png")).getImage();
		setIconImage(img);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridLayout(2, 2, 20, 20));
		
		JLabel lblNewLabel_3 = new JLabel("Resuesta 1");
		lblNewLabel_3.setOpaque(true);
		lblNewLabel_3.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel_3.setBackground(Color.RED);
		panel.add(lblNewLabel_3);
		
		JLabel lblNewLabel_2 = new JLabel("Resuesta 2");
		lblNewLabel_2.setOpaque(true);
		lblNewLabel_2.setBackground(Color.BLUE);
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 15));
		panel.add(lblNewLabel_2);
		
		JLabel lblNewLabel_1 = new JLabel("Resuesta 3");
		lblNewLabel_1.setOpaque(true);
		lblNewLabel_1.setBackground(Color.YELLOW);
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		panel.add(lblNewLabel_1);
		
		JLabel lblNewLabel_4 = new JLabel("Resuesta 4");
		lblNewLabel_4.setOpaque(true);
		lblNewLabel_4.setBackground(Color.GREEN);
		lblNewLabel_4.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_4.setFont(new Font("Tahoma", Font.BOLD, 15));
		panel.add(lblNewLabel_4);
        
        JPanel panel_1 = new JPanel();
        contentPane.add(panel_1, BorderLayout.NORTH);
        panel_1.setLayout(new BorderLayout(0, 0));
        
        JLabel lblNewLabel = new JLabel("Question");
        panel_1.add(lblNewLabel);
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 41));
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        
        
        btnNewButton = new JButton("Next Question");
        btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 12));
        panel_1.add(btnNewButton, BorderLayout.EAST);
        btnNewButton.setForeground(Color.WHITE);
        btnNewButton.setBackground(new Color(102, 0, 204));
        btnNewButton.setEnabled(false);
        
        JPanel panel_2 = new JPanel();
        panel_1.add(panel_2, BorderLayout.WEST);
        panel_2.setLayout(new BorderLayout(0, 0));
        
        JLabel Contador = new JLabel(new NodosXML("config.xml").Timeout.getTextContent());
        panel_2.add(Contador);
        Contador.setHorizontalAlignment(SwingConstants.RIGHT);
        Contador.setFont(new Font("Showcard Gothic", Font.PLAIN, 20));
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                dispose();
            }
        });
        
        tm = new Timer(1000, new ActionListener() {

            NodosXML nodos = new NodosXML("config.xml");
            int i = Integer.valueOf(nodos.Timeout.getTextContent());
            @Override
            public void actionPerformed(ActionEvent e) {

                Contador.setText(Integer.toString(i));
                i--;
                if (i == -1) {
                    btnNewButton.setEnabled(true);
                    tm.stop();
                    
                }

            }

        });
	}

}
