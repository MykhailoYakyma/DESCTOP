package kadammScreens;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import LIB.bbdd.dao.AnswersDao;
import LIB.bbdd.dao.KahootDao;
import LIB.bbdd.dao.ParticipantDao;
import LIB.bbdd.dao.QuestionsDao;
import LIB.bbdd.entity.Answers;
import LIB.bbdd.entity.Kahoot;
import LIB.bbdd.entity.Participant;
import LIB.bbdd.entity.Questions;
import exceptions.ErrorHandler;
import xml.NodosXML;

public class OngoingContest extends JFrame {

	private JPanel contentPane, panel;
	private JLabel redAnsLbl, blueAnsLbl, yellowAnsLbl, greenAnsLbl, questionLbl;
	private int timeOut = Integer.valueOf(new NodosXML().Timeout.getTextContent());
	private JLabel contador = new JLabel(Integer.toString(timeOut));
	private Timer tm;
	private JButton nextQuestionBtn;
	private QuestionsDao qDao = new QuestionsDao();
	private AnswersDao aDao = new AnswersDao();
	private List<Questions> questions;
	private List<Answers> answers;
	private KahootDao kdao = new KahootDao();
	private Kahoot currentKahoot = null;
	private JPanel panel_ranking;
	private JTable ranking;
	private JTable table_names;
	private List<Participant> participants = new ArrayList<>();// WaitingRoom.getParticipants();

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

		panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridLayout(2, 2, 20, 20));

		redAnsLbl = new JLabel();
		redAnsLbl.setOpaque(true);
		redAnsLbl.setHorizontalAlignment(SwingConstants.CENTER);
		redAnsLbl.setFont(new Font("Tahoma", Font.BOLD, 34));
		redAnsLbl.setBackground(Color.RED);

		blueAnsLbl = new JLabel();
		blueAnsLbl.setForeground(Color.BLACK);
		blueAnsLbl.setOpaque(true);
		blueAnsLbl.setBackground(Color.BLUE);
		blueAnsLbl.setHorizontalAlignment(SwingConstants.CENTER);
		blueAnsLbl.setFont(new Font("Tahoma", Font.BOLD, 34));

		yellowAnsLbl = new JLabel();
		yellowAnsLbl.setOpaque(true);
		yellowAnsLbl.setBackground(Color.YELLOW);
		yellowAnsLbl.setHorizontalAlignment(SwingConstants.CENTER);
		yellowAnsLbl.setFont(new Font("Tahoma", Font.BOLD, 34));

		greenAnsLbl = new JLabel();
		greenAnsLbl.setOpaque(true);
		greenAnsLbl.setBackground(Color.GREEN);
		greenAnsLbl.setHorizontalAlignment(SwingConstants.CENTER);
		greenAnsLbl.setFont(new Font("Tahoma", Font.BOLD, 34));

		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.NORTH);
		panel_1.setLayout(new BorderLayout(0, 0));

		questionLbl = new JLabel("Question");
		panel_1.add(questionLbl);
		questionLbl.setFont(new Font("Tahoma", Font.PLAIN, 20));
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

		timer();
		tm.start();

		setQuestion();

		panel_2.add(contador);
		contador.setHorizontalAlignment(SwingConstants.RIGHT);
		contador.setFont(new Font("Showcard Gothic", Font.PLAIN, 40));

		panel_ranking = new JPanel();
		contentPane.add(panel_ranking, BorderLayout.EAST);

		ranking = new JTable();
		ranking.setShowVerticalLines(false);
		ranking.setFont(new Font("Segoe UI", Font.PLAIN, 25));
		ranking.setForeground(Color.BLACK);
		// PRUEBA
		ParticipantDao pdao = new ParticipantDao();
		Participant p = new Participant("juan");
		p.setPoints(2030);
		pdao.saveParticipant(p);
		Participant p2 = new Participant("manuel");
		p2.setPoints(3730);
		pdao.saveParticipant(p2);
		Participant p3 = new Participant("aaaa");
		p3.setPoints(2000);
		pdao.saveParticipant(p3);
		participants.add(p);
		participants.add(p2);
		participants.add(p3);

		System.out.println("p" + p.getPoints() + "p2" + p2.getPoints() + "p3" + p3.getPoints());

		participants = participants.stream().sorted(Comparator.comparing(participant -> participant.getPoints()))
				.collect(Collectors.toList());

		ranking.setModel(new DefaultTableModel(

				new Object[][] { { participants.get(0).getAlias(), participants.get(0).getPoints() },
						{ participants.get(1).getAlias(), participants.get(1).getPoints() },
						{ participants.get(2).getAlias(), participants.get(2).getPoints() }, },

				new String[] { "name_column", "points_column" }) {
			Class[] columnTypes = new Class[] { String.class, Integer.class };

			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		ranking.getColumnModel().getColumn(0).setPreferredWidth(125);
		ranking.getColumnModel().getColumn(1).setPreferredWidth(100);

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		ranking.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		ranking.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
		TableRowSorter<TableModel> sorter = new TableRowSorter<>(ranking.getModel());
		ranking.setRowSorter(sorter);
		ranking.setRowHeight(30);
		List<RowSorter.SortKey> sortKeys = new ArrayList<>();

		sortKeys.add(new RowSorter.SortKey(1, SortOrder.DESCENDING));

		sorter.setSortKeys(sortKeys);
		sorter.sort();

		table_names = new JTable();
		table_names.setShowVerticalLines(false);
		table_names.setFont(new Font("SansSerif", Font.BOLD, 25));
		table_names.setModel(new DefaultTableModel(new Object[][] { { "Name", "Points" }, },
				new String[] { "New column", "New column" }));
		table_names.getColumnModel().getColumn(0).setPreferredWidth(125);
		table_names.getColumnModel().getColumn(1).setPreferredWidth(100);
		table_names.setRowHeight(30);
		table_names.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		table_names.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
		panel_ranking.setLayout(new BorderLayout(0, 0));

		panel_ranking.add(table_names, BorderLayout.NORTH);
		panel_ranking.add(ranking, BorderLayout.CENTER);
		nextQuestionBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				nextQuestionBtn.setEnabled(false);
				timeOut = Integer.valueOf(new NodosXML().Timeout.getTextContent());
				panel.removeAll();
				repaint();
				setQuestion();
			}

		});

	}

	private void timer() {

		tm = new Timer(1000, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				contador.setText(Integer.toString(timeOut));

				timeOut--;
				if (timeOut == -1) {

					((Timer) (e.getSource())).stop();
					nextQuestionBtn.setEnabled(true);
					showCorrect();

				}

			}

		});
	}

	protected void showCorrect() {
		try {
			if (!answers.get(0).isCorrect()) {
				redAnsLbl.setBackground(Color.GRAY);
			}
			if (!answers.get(1).isCorrect()) {
				blueAnsLbl.setBackground(Color.GRAY);
			}

			if (!answers.get(2).isCorrect()) {
				yellowAnsLbl.setBackground(Color.GRAY);

			}
			if (!answers.get(3).isCorrect()) {
				greenAnsLbl.setBackground(Color.GRAY);
			}
		} catch (Exception e) {

		}
		answers.clear();
	}

	private void setQuestion() {
		if (questions.size() > 0) {

			Questions currentQuestion = questions.get(0);
			questionLbl.setText(currentQuestion.getQuestion());
			answers = aDao.getAnswersByQuestion(currentQuestion);

			redAnsLbl.setText(answers.get(0).getAnswer());
			redAnsLbl.setBackground(Color.RED);
			panel.add(redAnsLbl);
			blueAnsLbl.setText(answers.get(1).getAnswer());
			blueAnsLbl.setBackground(Color.BLUE);
			panel.add(blueAnsLbl);
			if (answers.size() >= 3) {
				yellowAnsLbl.setText(answers.get(2).getAnswer());
				yellowAnsLbl.setBackground(Color.YELLOW);
				panel.add(yellowAnsLbl);
			}
			if (answers.size() == 4) {
				greenAnsLbl.setText(answers.get(3).getAnswer());
				greenAnsLbl.setBackground(Color.GREEN);
				panel.add(greenAnsLbl);
			}

			questions.remove(0);
			currentQuestion = null;
			tm.start();

		} else {
			new ErrorHandler("Game Over", "").setVisible(true);
		}

	}

	private void showRanking() {

	}

}
