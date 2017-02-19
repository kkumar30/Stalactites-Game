package kkumar;

import java.awt.event.MouseEvent;
import java.util.Random;

import junit.framework.TestCase;
import ks.client.gamefactory.GameWindow;
import ks.common.games.Solitaire;
import ks.common.model.BuildablePile;
import ks.common.model.Card;
import ks.common.model.Deck;
import ks.common.model.Move;
import ks.common.model.Pile;
import ks.common.view.Widget;
import ks.launcher.Main;
import ks.tests.model.ModelFactory;
import ks.tests.KSTestCase;

public class TestStalactites extends TestCase {

	Stalactites game;
	GameWindow gw;
	
	
	protected void SetUp()
	{
		game = new Stalactites();
		gw = Main.generateWindow(game, Deck.OrderBySuit);
	}
	
	protected void tearDown()
	{
		gw.dispose();
	}
	
	public void testSimple(){

		game = new Stalactites();
		gw = Main.generateWindow(game, Deck.OrderBySuit);
		
		assertEquals(0, this.game.deck.count());
		assertEquals(0, this.game.getScoreValue());
		assertTrue(!this.game.hasWon());
		assertEquals(0, this.game.getSequence());
		
		this.game.setSequence(1);
		assertEquals(1, this.game.getSequence());
		
		}
	
	public MouseEvent createPressed (Solitaire game, Widget view, int dx, int dy) {
		MouseEvent me = new MouseEvent(game.getContainer(), MouseEvent.MOUSE_PRESSED, 
				System.currentTimeMillis(), 0, 
				view.getX()+dx, view.getY()+dy, 0, false);
		return me;
	}
	
	public MouseEvent createReleased (Solitaire game, Widget view, int dx, int dy) {
		MouseEvent me = new MouseEvent(game.getContainer(), MouseEvent.MOUSE_RELEASED, 
				System.currentTimeMillis(), 0, 
				view.getX()+dx, view.getY()+dy, 0, false);
		return me;
	}
	
	public void testSimple2()
	{

		game = new Stalactites();
		gw = Main.generateWindow(game, Deck.OrderBySuit);
		
		ModelFactory.init(game.reservePile1, "4S");
		ModelFactory.init(game.reservePile2, "5S");
		ModelFactory.init(game.piles[1], "4H 2S");
		ModelFactory.init(game.piles[2], "6H 7S");
		
		ModelFactory.init(game.foundationLastCard[1], "6S");
		ModelFactory.init(game.foundation[1], "5S");
		
		Move rf = new ReservePileToFoundationMove(game.reservePile1, game.foundation[1], game.reservePile1.peek(), game.foundationLastCard[1], game );
		rf.doMove(game);
		assertEquals ("4S", game.foundation[1].peek().toString());
		boolean b= rf.undo(game);
		assertTrue(b);

		
		
		Move tf=new TableauToFoundationMove(game.piles[1], game.foundation[1], game.piles[1].peek(), game.foundationLastCard[1], game);
		tf.doMove(game);
		assertEquals ("5S", game.foundation[1].peek().toString());
		assertTrue(tf.undo(game));
		
		Move tr=new TableauToReserveMove(game.piles[1], game.reservePile2, game.piles[1].peek());
		tr.doMove(game);
		assertEquals ("5S", game.reservePile2.peek().toString());
		assertTrue(tr.undo(game));
		
	
		/*
		// create mouse press at (0,0) within the deckview; should deal card
		MouseEvent press = this.createReleased(game, game.rp1, 0, 0);
		game.deckView.getMouseManager().handleMouseEvent(press);
		
		// what do we know about the game after press on deck? Card dealt!
		assertEquals ("KS", game.rp1.getModelElement().toString()); 
		
		
		// press a bit offset into the widget.
		/*MouseEvent press = createPressed (redBlack, redBlack.wastePileView, 0, 0);
		redBlack.wastePileView.getMouseManager().handleMouseEvent(press);
				
		assertEquals ("4S", redBlack.wastePile.peek().toString());*/
		
		/*BuildablePile sourceTableauCol = this.game.piles[1];
		Pile targetReservePile=this.game.reservePile1; 
		Card cardBeingDragged=this.game.piles[1].peek();
		
		Move m = new TableauToReserveMove(sourceTableauCol, targetReservePile, cardBeingDragged);
		
		this.game.pushMove(m);
		//ModelFactory.init(game.pile, "5H");
		
		assertEquals ("5H", game.reservePile1.peek().toString());*/
		
	}

	
	
}
