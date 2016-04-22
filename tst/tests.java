
import java.awt.event.MouseEvent;

import main.application.*;
import main.controller.*;
import main.model.*;
import ks.client.gamefactory.GameWindow;
import ks.common.model.BuildablePile;
import ks.common.model.Card;
import ks.common.model.Column;
import ks.common.model.Pile;
import ks.common.view.CardView;
import ks.common.view.ColumnView;
import ks.common.view.Widget;
import ks.launcher.Main;
import ks.tests.KSTestCase;

public class tests extends KSTestCase{
	GameWindow window;
	Intrigue game;
	
	protected void setUp() throws Exception {
		game = new Intrigue();
		window = Main.generateWindow(game, 13);
	}
	
	public void testName() {
		assertEquals(game.getName(), "Intrigue Final");
	}
	
	Card cK = new Card(13, 1);
	Card cQ = new Card(12, 1);
	Card cJ = new Card(11, 1);
	Card c10 = new Card(10, 1);
	Card c9 = new Card(9, 1);
	Card c8 = new Card(8, 1);
	Card c7 = new Card(7, 1);
	Card c6 = new Card(6, 1);
	Card c5 = new Card(5, 1);
	Card c4 = new Card(4, 1);
	Card c3 = new Card(3, 1);
	Card c2 = new Card(2, 1);
	Card c1 = new Card(1, 1);
	
	public void testTableau5FoundationMove(){
		Tableau5FoundationMove m = new Tableau5FoundationMove(game.tableau[0], cQ, game.fFoundation[0]);
		assertEquals(m.valid(game), false);
		assertEquals(m.doMove(game), false);
		
		m = new Tableau5FoundationMove(game.tableau[0], c5, game.fFoundation[0]);
		assertEquals(m.valid(game), true);
		assertEquals(m.doMove(game), true);
		
		m = new Tableau5FoundationMove(game.tableau[0], c4, game.fFoundation[0]);
		assertEquals(m.valid(game), true);
		assertEquals(m.doMove(game), true);
		assertEquals(m.undo(game), true);
		
		m = new Tableau5FoundationMove(game.tableau[0], c5, game.fFoundation[0]);
		assertEquals(m.valid(game), false);
		assertEquals(m.doMove(game), false);
		
		m = new Tableau5FoundationMove(game.tableau[0], c6, game.fFoundation[0]);
		assertEquals(m.valid(game), false);
		assertEquals(m.doMove(game), false);
	}
	
	public void testTableau6FoundationMove(){
		Tableau6FoundationMove m = new Tableau6FoundationMove(game.tableau[0], cQ, game.fFoundation[0]);
		assertEquals(m.valid(game), false);
		assertEquals(m.doMove(game), false);
		
		m = new Tableau6FoundationMove(game.tableau[0], c6, game.fFoundation[0]);
		assertEquals(m.valid(game), true);
		assertEquals(m.doMove(game), true);
		
		m = new Tableau6FoundationMove(game.tableau[0], c7, game.fFoundation[0]);
		assertEquals(m.valid(game), true);
		assertEquals(m.doMove(game), true);
		assertEquals(m.undo(game), true);
		
		m = new Tableau6FoundationMove(game.tableau[0], c6, game.fFoundation[0]);
		assertEquals(m.valid(game), false);
		assertEquals(m.doMove(game), false);
		
		m = new Tableau6FoundationMove(game.tableau[0], c5, game.fFoundation[0]);
		assertEquals(m.valid(game), false);
		assertEquals(m.doMove(game), false);
	}
	
	public void testTableauTableauMove(){
		TableauTableauMove m = new TableauTableauMove(game.tableau[0], c7, game.tableau[1]);
		game.tableau[1].add(cQ);
		assertEquals(m.valid(game), true);
		assertEquals(m.doMove(game), true);
		assertEquals(m.undo(game), true);
		
		game.tableau[1].add(cK);
		assertEquals(m.valid(game), false);
		assertEquals(m.doMove(game), false);
	}
	
	public void testWin(){
		assertEquals(game.hasWon(), false);
		for(int i = 0; i < 8; i++){
			game.tableau[i].add(cQ);
		}
		assertEquals(game.hasWon(), true);
	}
}
