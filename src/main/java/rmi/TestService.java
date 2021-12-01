package rmi;

import java.util.ArrayList;

public interface TestService {

  public String getResponse(String data);
  public boolean getName(String nickname); 
  public ArrayList<String> GetAnswers(int n);
  public boolean isStarted();
  public int getTime();
  public boolean getSeguir();
  public void RespuestaJugador(String resp, String time, String name);
}