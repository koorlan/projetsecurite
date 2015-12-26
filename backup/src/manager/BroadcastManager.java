package manager;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;

import model.BroadcastModel;
import model.Front;
import model.FrontalModel;
import model.ServerModel;

public class BroadcastManager {
	private CoreManager core;
	private BroadcastModel model;

        public CoreManager getCore() {
            return core;
        }

        public void setCore(CoreManager core) {
            this.core = core;
        }

        public BroadcastModel getModel() {
            return model;
        }

        public void setModel(BroadcastModel model) {
            this.model = model;
        }
        
        public ServerModel getServer() {
            return this.model.getServer();
        }

        public void setServer(ServerModel server) {
            this.model.setServer(server);
        }
	public BroadcastManager( BroadcastModel model, CoreManager core) {
		super();
		this.core = core;
		this.model = model;
	}
	
	public void broadcast(byte[] packet){
		Socket socket;
		for(FrontalModel frontal :this.model.getFrontal()){			
			try {
				socket = new Socket(InetAddress.getByName(frontal.getExternalserverManager().getModel().getIpDest()),frontal.getExternalserverManager().getModel().getPort());		
				socket.getOutputStream().write(packet);
				socket.close();			
			}catch( ConnectException e) {
				this.core.getLog().err(this, "Connection refused");
			}
			catch (NumberFormatException | IOException e ) {
				e.printStackTrace();
			} 
		}
		
	}
	
	
}
