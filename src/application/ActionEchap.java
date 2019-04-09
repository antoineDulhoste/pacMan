package application;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class ActionEchap implements EventHandler<KeyEvent> {
	private Menu m;
    public ActionEchap(Menu m){
        this.m=m;
        m.getRoot();
    }
	@Override
    public void handle(KeyEvent event) {
    	if(event.getCode() == KeyCode.ESCAPE){
            if(m.getCompteur()==1) {
            	m.setCompteur(0);
            	m.menuRemove(4);
            	m.t1();
            } 
            if(m.getCompteur()==2){
            	m.setCompteur(1);
            	m.menuRemove(2);
            	m.menu1();
            }
            if(m.getCompteur()==3){
            	m.setCompteur(2);
            	m.menuRemove(5);
            	m.menuMulti();
            }
            if(m.getCompteur()==4){
            	m.setCompteur(1);
            	m.menuRemove(3);
            	m.menu1();
            }
            if(m.getCompteur()==99){
            	m.setCompteur(3);
            	try {
            		Net.serverSocket.close();
                	Net.socket.close();
                	Net.dis.close();
                	Net.dos.close();
            	}catch(Exception ex) {
            		
            	}
            	m.menuRemove(5);
            	m.menuConfigServer();
            }
        }
    }
}
