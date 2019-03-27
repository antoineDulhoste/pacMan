package application;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class ActionEchap implements EventHandler<KeyEvent> {
	private Menu m;
    private Group root;
    public ActionEchap(Menu m){
        this.m=m;
        this.root=m.getRoot();
    }
	@Override
    public void handle(KeyEvent event) {
    	if(event.getCode() == KeyCode.ESCAPE){
            if(m.getCompteur()==1) {
            	m.setCompteur(0);
            	m.menuRemove(3);
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
            	m.menu1();
            }
        }
    }
}
