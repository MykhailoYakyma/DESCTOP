package rmi;

//TestServer.java
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import org.hibernate.internal.build.AllowSysOut;

import LIB.bbdd.dao.AnswersDao;
import LIB.bbdd.dao.KahootDao;
import LIB.bbdd.dao.ParticipantDao;
import LIB.bbdd.dao.QuestionsDao;
import LIB.bbdd.entity.Answers;
import LIB.bbdd.entity.Kahoot;
import LIB.bbdd.entity.Participant;
import LIB.bbdd.entity.Questions;
import kadammScreens.OngoingContest;
import kadammScreens.WaitingRoom;
import lipermi.exception.LipeRMIException;
import lipermi.handler.CallHandler;
import lipermi.net.IServerListener;
import lipermi.net.Server;
import xml.NodosXML;

public class TestServer implements TestService, Runnable {
	private Thread worker;
	WaitingRoom waitingRoom;
	static ArrayList<String> waitingPlayers = new ArrayList<>();
	private QuestionsDao qDao = new QuestionsDao();
	private AnswersDao aDao = new AnswersDao();
	KahootDao kdao = new KahootDao();
	ParticipantDao pDao = new ParticipantDao();
	private List<Questions> questions;
	private List<Answers> answers;
	private Kahoot currentKahoot = null;
	public String kahootName;
	boolean isStarted = false;
	static boolean seguir = false;
	private final AtomicBoolean running = new AtomicBoolean(false);
	
	
	public TestServer(String kahootName) throws InterruptedException {
		this.kahootName = kahootName;
		try {
			waitingRoom = new WaitingRoom(kahootName);
			CallHandler callHandler = new CallHandler();
			callHandler.registerGlobal(TestService.class, this);
			Server server = new Server();
			server.bind(2324, callHandler);
			server.addServerListener(new IServerListener() {

				@Override
				public void clientDisconnected(Socket socket) {
					System.out.println("Client Disconnected: " + socket.getInetAddress());
				}

				@Override
				public void clientConnected(Socket socket) {
					System.out.println("Client Connected: " + socket.getInetAddress());
				}
			});
			System.out.println("Server Listening");
			start();
		} catch (LipeRMIException | IOException e) {
			e.printStackTrace();
		}		
	}
	
	@Override
	public String getResponse(String data) {
		System.out.println("getResponse called");
		return "Servidor disponible";
	}
	
	public void ComprovacionStarted() throws InterruptedException {
		while (isStarted == false) {
			isStarted = waitingRoom.isStarted();
			Thread.sleep(1000);
			System.out.println(isStarted + "sisoy");
		}
	}
	
	@Override
  public boolean getName(String nickname) {
      if (!waitingPlayers.contains(nickname)) {
          waitingPlayers.add(nickname);
          waitingRoom.nuevoConcursante(nickname);
          return true;
      } else {
          return false;
      }
  }
	
	public boolean isStarted() {
		return isStarted;
	}

	@Override
	public ArrayList<String> GetAnswers(int n) {
		seguir = false;
		currentKahoot = kdao.getKahoots().stream().filter(kahoot -> kahoot.getName().equals(kahootName)).findAny().get();
		questions = qDao.getQuestions().stream().filter(q -> q.getKahoot().getId() == currentKahoot.getId()).collect(Collectors.toList());
		Questions currentQuestion = questions.get(n);
		answers = aDao.getAnswersByQuestion(currentQuestion);
		ArrayList<String> myArray = new ArrayList<String>();
		for (Answers answer : answers) {
			myArray.add(answer.getAnswer());
		}
		return myArray;
	}
	
	public int getTime() {
		int timeOut;
		timeOut = Integer.valueOf(new NodosXML().Timeout.getTextContent());
		return timeOut;
	}
	
	public boolean getSeguir() {
		seguir = waitingRoom.getSeguir();
		boolean seg = seguir;
		seguir = false;
		return seg;
	}
	
	public void start() {
      worker = new Thread(this);
      worker.start();
  }
	
	public void stop() {
      running.set(false);
  }
	
	public void RespuestaJugador(String resp, String time, String name) {
//		Answers ans = new Answers();
//		for (Answers answer : answers) {
//			if (answer.getAnswer().equals(resp)) {
//				ans = answer;
//				break;
//			}
//		}
//
//		Participant participant = pDao.getParticipantByName(name);
//		List<Answers> listAnsw = participant.getAnswers();
//		listAnsw.add(ans);
//		participant.setAnswers(listAnsw);
//
//		if (ans.isCorrect()) {
//			int allPoints = participant.getPoints();
//			participant.setPoints(allPoints + calculatPoints(time));
//		}
//		else {
//			participant.setPoints(0);
//		}
//		
//		pDao.updateParticipant(participant);
		
		System.out.println("respuesta: "+resp);
		System.out.println("time: "+time);
		
	}
	
	
	
	@Override
	public void run() {
		while (isStarted == false) {
			
			isStarted = waitingRoom.isStarted;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	}
	
	public static ArrayList<String> getPlayers() {
		return waitingPlayers;
	}
	
	public int calculatPoints(String time){
		
		int points = 0;
		if (!time.equals(null)) {
			int ansTime = Integer.parseInt(time)/1000;
			points = 1000 - (ansTime*ansTime*4 + 10*ansTime);
		}
		return points;

	}
	

}

