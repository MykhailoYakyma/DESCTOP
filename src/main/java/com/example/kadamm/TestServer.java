package com.example.kadamm;

//TestServer.java
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import kadammScreens.WaitingRoom;
import lipermi.exception.LipeRMIException;
import lipermi.handler.CallHandler;
import lipermi.net.IServerListener;
import lipermi.net.Server;

public class TestServer implements TestService {
	WaitingRoom wr;
	ArrayList<String> wPlayers = new ArrayList<>();
	public TestServer(String kahootName) {
		
		try {
			wr = new WaitingRoom(kahootName);
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
		} catch (LipeRMIException | IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getResponse(String data) {
		System.out.println("getResponse called");
		return "Servidor disponible";
	}
	@Override
    public boolean getName(String nickname) {
        if (!wPlayers.contains(nickname)) {
            wPlayers.add(nickname);
            wr.nuevoConcursante(nickname);
            return true;
        } else {
            return false;
        }
    }

}
