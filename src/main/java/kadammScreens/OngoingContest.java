package kadammScreens;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import LIB.bbdd.dao.KahootDao;
import LIB.bbdd.dao.QuestionsDao;
import LIB.bbdd.entity.Answers;
import LIB.bbdd.entity.Kahoot;
import LIB.bbdd.entity.Questions;
import xml.NodosXML;

public class OngoingContest extends JFrame {

	private JPanel contentPane;
	private Timer tm;
	private JButton nextQuestionBtn;
	QuestionsDao qDao = new QuestionsDao();
	List<Questions> questions;
	KahootDao kdao = new KahootDao();
	Kahoot currentKahoot = null;
	NodosXML nodos = new NodosXML("config.xml");
	int i = Integer.valueOf(nodos.Timeout.getTextContent());

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					KahootDao kdao = new KahootDao();

					OngoingContest frame = new OngoingContest(kdao.getKahoot(1).getName());
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public OngoingContest(String name) {

		currentKahoot = kdao.getKahoots().stream().filter(kahoot -> kahoot.getName().equals(name)).findAny().get();

		questions = qDao.getQuestions().stream().filter(q -> q.getKahoot().getId() == currentKahoot.getId())
				.collect(Collectors.toList());

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

		JLabel redAnsLbl = new JLabel();
		redAnsLbl.setOpaque(true);
		redAnsLbl.setHorizontalAlignment(SwingConstants.CENTER);
		redAnsLbl.setFont(new Font("Tahoma", Font.BOLD, 15));
		redAnsLbl.setBackground(Color.RED);
		panel.add(redAnsLbl);

		JLabel blueAnsLbl = new JLabel();
		blueAnsLbl.setOpaque(true);
		blueAnsLbl.setBackground(Color.BLUE);
		blueAnsLbl.setHorizontalAlignment(SwingConstants.CENTER);
		blueAnsLbl.setFont(new Font("Tahoma", Font.BOLD, 15));
		panel.add(blueAnsLbl);

		JLabel yellowAnsLbl = new JLabel();
		yellowAnsLbl.setOpaque(true);
		yellowAnsLbl.setBackground(Color.YELLOW);
		yellowAnsLbl.setHorizontalAlignment(SwingConstants.CENTER);
		yellowAnsLbl.setFont(new Font("Tahoma", Font.BOLD, 15));
		panel.add(yellowAnsLbl);

		JLabel greenAnsLbl = new JLabel();
		greenAnsLbl.setOpaque(true);
		greenAnsLbl.setBackground(Color.GREEN);
		greenAnsLbl.setHorizontalAlignment(SwingConstants.CENTER);
		greenAnsLbl.setFont(new Font("Tahoma", Font.BOLD, 15));
		panel.add(greenAnsLbl);

		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.NORTH);
		panel_1.setLayout(new BorderLayout(0, 0));

		JLabel questionLbl = new JLabel("Question");
		panel_1.add(questionLbl);
		questionLbl.setFont(new Font("Tahoma", Font.PLAIN, 41));
		questionLbl.setHorizontalAlignment(SwingConstants.CENTER);

		nextQuestionBtn = new JButton("Next Question");
		nextQuestionBtn.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel_1.add(nextQuestionBtn, BorderLayout.EAST);
		nextQuestionBtn.setForeground(Color.WHITE);
		nextQuestionBtn.setBackground(new Color(102, 0, 204));
		nextQuestionBtn.setEnabled(false);

		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2, BorderLayout.WEST);
		panel_2.setLayout(new BorderLayout(0, 0));

		JLabel Contador = new JLabel(new NodosXML("config.xml").Timeout.getTextContent());
		timer(Contador);
		tm.start();

		setQuestion(redAnsLbl, blueAnsLbl, yellowAnsLbl, greenAnsLbl, questionLbl);

		panel_2.add(Contador);
		Contador.setHorizontalAlignment(SwingConstants.RIGHT);
		Contador.setFont(new Font("Showcard Gothic", Font.PLAIN, 20));
		nextQuestionBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				nextQuestionBtn.setEnabled(false);
				int i = Integer.valueOf(nodos.Timeout.getTextContent());
				setQuestion(redAnsLbl, blueAnsLbl, yellowAnsLbl, greenAnsLbl, questionLbl);
				// dispose();
			}

		});

	}

	private void timer(JLabel Contador) {
		tm = new Timer(1000, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				Contador.setText(Integer.toString(i));
				i--;
				if (i == -1) {
					int i = Integer.valueOf(nodos.Timeout.getTextContent());
					((Timer) (e.getSource())).stop();
					nextQuestionBtn.setEnabled(true);

				}

			}

		});
	}

//	private void timer(JLabel Contador) {
//		tm = new Timer(1000, e -> {
//			NodosXML nodos = new NodosXML("config.xml");
//			int i = Integer.valueOf(nodos.Timeout.getTextContent());
//
//			if (i > -1) {
//				Contador.setText(Integer.toString(i));
//				i--;
//			}
//
//			else {
//				Contador.setText("0");
//				((Timer) (e.getSource())).stop();
//				nextQuestionBtn.setEnabled(true);
//
//			}
//
//		});
//	}

	private void setQuestion(JLabel redAnsLbl, JLabel blueAnsLbl, JLabel yellowAnsLbl, JLabel greenAnsLbl,
			JLabel questionLbl) {
		Questions currentQuestion = questions.get(0);
		questionLbl.setText(currentQuestion.getQuestion());
		List<Answers> answers = currentQuestion.getAnswers();

		redAnsLbl.setText(answers.get(0).getAnswer());
		blueAnsLbl.setText(answers.get(1).getAnswer());
		if (answers.size() >= 3) {
			yellowAnsLbl.setText(answers.get(2).getAnswer());
		}
		if (answers.size() == 4) {
			greenAnsLbl.setText(answers.get(3).getAnswer());
		}

		questions.remove(0);
		currentQuestion = null;
		answers.clear();
		tm.start();
	}

}
