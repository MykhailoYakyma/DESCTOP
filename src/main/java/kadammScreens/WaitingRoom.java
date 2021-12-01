package kadammScreens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import LIB.bbdd.dao.ContestDao;
import LIB.bbdd.dao.KahootDao;
import LIB.bbdd.dao.ParticipantDao;
import LIB.bbdd.entity.Contest;
import LIB.bbdd.entity.Kahoot;
import LIB.bbdd.entity.Participant;
import exceptions.ErrorHandler;
import login.LoginFrame;
import rmi.TestServer;

public class WaitingRoom extends JFrame {

	private JTextArea connectedPlayersTxt;
	private String localhost = setLocalhost();
	private JPanel contentPane;
	private JPanel waitingroomPanel;
	private Timer tm = null;
	private JLabel countdownLabel = new JLabel("");
	private static Contest contest = new Contest();
	public static List<Participant> participants = new ArrayList<Participant>();
	private OngoingContest contestFrame;
	private boolean seguir = false;
	public boolean isStarted = false;

	/**
	 * Create the frame.
	 */
	public WaitingRoom(String name) {
		setVisible(true);
		setResizable(false);
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle(name);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(836, 645);
		setLocationRelativeTo(null);
		Image img = new ImageIcon(this.getClass().getResource("/icon_login.png")).getImage();
		setIconImage(img);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(102, 0, 204));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JLabel lblNewLabel = new JLabel(name);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setForeground(new Color(255, 255, 255));
		lblNewLabel.setFont(new Font("Verdana", Font.BOLD, 30));

		localhost = setLocalhost();
		JLabel ipLabel = new JLabel(String.valueOf(localhost));
		ipLabel.setHorizontalAlignment(SwingConstants.CENTER);
		ipLabel.setForeground(new Color(255, 255, 255));
		ipLabel.setFont(new Font("Verdana", Font.BOLD, 30));
		ipLabel.setBackground(new Color(255, 255, 255));

		JLabel lblNewLabel_2 = new JLabel("Waiting for players...");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblNewLabel_2.setForeground(new Color(255, 255, 255));

		JButton btnNewButton = new JButton("START ");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				setContest(name);
				ParticipantDao participantDao = new ParticipantDao();

				for (String participantName : TestServer.getPlayers()) {
					Participant participant = new Participant(participantName, contest);
					participantDao.saveParticipant(participant);
					participants.add(participant);
				}

				tm.start();

			}

		});
		btnNewButton.setForeground(new Color(102, 0, 204));
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnNewButton.setBackground(new Color(255, 255, 255));

		connectedPlayersTxt = new JTextArea();
		connectedPlayersTxt.setEditable(false);
		connectedPlayersTxt.setLineWrap(true);
		connectedPlayersTxt.setForeground(new Color(0, 0, 0));
		connectedPlayersTxt.setFont(new Font("Courier New", Font.BOLD, 18));
		connectedPlayersTxt.setBackground(new Color(102, 0, 204));
		countdownLabel.setForeground(Color.WHITE);
		countdownLabel.setFont(new Font("Tahoma", Font.BOLD, 99));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane
				.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPane.createSequentialGroup().addGap(10)
								.addComponent(ipLabel, GroupLayout.DEFAULT_SIZE, 802, Short.MAX_VALUE).addGap(10))
						.addGroup(gl_contentPane.createSequentialGroup().addGap(272)
								.addComponent(btnNewButton, GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE).addGap(279))
						.addGroup(gl_contentPane.createSequentialGroup().addGap(212)
								.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 383, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
								.addComponent(countdownLabel, GroupLayout.PREFERRED_SIZE, 160,
										GroupLayout.PREFERRED_SIZE)
								.addGap(19))
						.addGroup(gl_contentPane.createSequentialGroup().addGap(33)
								.addComponent(connectedPlayersTxt, GroupLayout.PREFERRED_SIZE, 727,
										GroupLayout.PREFERRED_SIZE)
								.addContainerGap(62, Short.MAX_VALUE))
						.addGroup(gl_contentPane.createSequentialGroup().addContainerGap()
								.addComponent(lblNewLabel_2, GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE)
								.addGap(560)));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane
				.createSequentialGroup().addGap(12)
				.addComponent(ipLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(countdownLabel, GroupLayout.PREFERRED_SIZE, 125, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE))
				.addGap(26)
				.addComponent(lblNewLabel_2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addPreferredGap(ComponentPlacement.UNRELATED)
				.addComponent(connectedPlayersTxt, GroupLayout.PREFERRED_SIZE, 292, GroupLayout.PREFERRED_SIZE)
				.addGap(18)
				.addComponent(btnNewButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGap(52)));
		contentPane.setLayout(gl_contentPane);

		tm = new Timer(1000, new ActionListener() {

			int i = Integer.valueOf(LoginFrame.getNodos().Timeout.getTextContent());

			@Override
			public void actionPerformed(ActionEvent e) {

				countdownLabel.setText(Integer.toString(i));
				i--;
				if (i == -2) {
					dispose();
					isStarted = true;
					contestFrame = new OngoingContest(name);
					contestFrame.setVisible(true);

				}

			}

		});

	}

	private String setLocalhost() {
		try {
			localhost = String.valueOf(Inet4Address.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			ErrorHandler error = new ErrorHandler("CONNEXION ERROR", "Could not get local ip, please try again");
			e.printStackTrace();
		}
		return localhost;
	}

	public void nuevoConcursante(String nick) {
		connectedPlayersTxt.append(nick + "  ");
	}

	public boolean getSeguir() {
		seguir = contestFrame.getKahootSeguir();
		return seguir;
	}
	
	public boolean isStarted() {
		return isStarted;
	}
	
	private void setContest(String name) {
		ContestDao contestDao = new ContestDao();

		KahootDao kDao = new KahootDao();
		Kahoot currentKahoot = kDao.getKahoots().stream().filter(kahoot -> kahoot.getName().equals(name)).findAny()
				.get();
		contest.setAdmin(LoginFrame.getAdmin());
		contest.setKahoot(currentKahoot);
		contestDao.saveContest(contest);
	}

	public static List<Participant> getParticipants() {
		return participants;
	}
	
	public static Contest getContest(){
		return contest;
	}
}
