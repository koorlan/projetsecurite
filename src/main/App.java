package main;

import manager.*;
import manager.SecurityManager;
import model.*;

public class App {
	public static void main(String[] args) throws Exception {
		switch (args[0]) {
		case "server":
			CentralServer server = new CentralServer(args[1]);
			server.getCore().start();
			break;
		case "user":
			User user = new User();
			user.fill(args[1],args[2]);
			user.getCore().start();
			break;
		case "frontal":
			Frontal frontal = new Frontal(args[1]);
			frontal.getCore().start();
			break;
		default:
			System.out.println("Usage java -jar app.jar [server|user|frontal] <DBnameSEC>.sqlite <DBnameDATA>.sqlite");
		}

	}
}
