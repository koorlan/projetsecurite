package manager;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.SerializationUtils;

import model.PacketModel;

public class ClientHandler implements Runnable {
	private ServerHandler server;
	private Socket socket;
	ObjectInputStream reader;

	public ClientHandler(ServerHandler server, Socket socket) {
		super();
		this.server = server;
		this.socket = socket;
	}

	@Override
	public void run() {
		try {

			InputStream inputStream = socket.getInputStream();
			int count;
			byte[] packet = new byte[0];
			byte[] buffer = new byte[64];

			// TODO: rewrite
			while (true) {
				count = inputStream.read(buffer);
				packet = ArrayUtils.addAll(packet, buffer);
				if (count != buffer.length) {
					break;
				}
			}

			byte[] res = null;
			this.server.getCore().getLog().log(this, "New Packet arrived..processing");
			// maybe check integrity maybe after decoding...
			switch (this.server.getCore().getService()) {
			case "user":
				res = this.server.getCore().getPacket().processUser(packet);
				break;
			case "frontal":
				int portExt = this.server.getCore().getFrontal().getExternalserverManager().getModel().getPort();
				int portInt = this.server.getCore().getFrontal().getInternalserverManager().getModel().getPort();
				if (this.server.getSocket().getLocalPort() == portExt) {
					// external(from user)
					res = this.server.getCore().getPacket().processFrontal(packet, "EXTERNAL",this.socket);
				} else if (this.server.getSocket().getLocalPort() == portInt) {
					// internal(from central/frontal)
					
					res = this.server.getCore().getPacket().processFrontal(packet, "INTERNAL",this.socket);
				}
				break;
			case "central":
				res = this.server.getCore().getPacket().processCentral(packet);
				break;
			default:
				break;
			}
			if (res != null)
				socket.getOutputStream().write(res);
			this.socket.close();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
