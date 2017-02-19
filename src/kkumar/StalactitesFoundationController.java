package kkumar;

import java.awt.event.MouseEvent;

import heineman.Klondike;
import heineman.klondike.MoveCardToFoundationMove;
import heineman.klondike.MoveWasteToFoundationMove;
import ks.common.model.BuildablePile;
import ks.common.model.Card;
import ks.common.model.Column;
import ks.common.model.Move;
import ks.common.model.Pile;
import ks.common.view.BuildablePileView;
import ks.common.view.CardView;
import ks.common.view.ColumnView;
import ks.common.view.Container;
import ks.common.view.PileView;
import ks.common.view.Widget;

public class StalactitesFoundationController extends java.awt.event.MouseAdapter{
	/** The Stalactites Game. */
	protected Stalactites theGame;

	/** The specific Foundation pileView being controlled. */
	protected PileView src;
	/**
	 * FoundationController constructor comment.
	 */
	
	
	/**To set the last card to the lastcard holder in foundation pile*/
	protected Pile foundationLastCard;
	
	public StalactitesFoundationController(Stalactites theGame, PileView foundation, Pile foundationLastCard) {
		this.theGame = theGame;
		this.src = foundation;
		this.foundationLastCard= foundationLastCard;
	}
	/**
	 * Coordinate reaction to the completion of a Drag Event.
	 * <p>
	 * A bit of a challenge to construct the appropriate move, because cards
	 * can be dragged both from the WastePile (as a CardView object) and the 
	 * BuildablePileView (as a ColumnView).
	 * @param me java.awt.event.MouseEvent
	 */
	public void mouseReleased(MouseEvent me) {
	
		Container c = theGame.getContainer();
		
		/** Return if there is no card being dragged chosen. */
		Widget draggingWidget = c.getActiveDraggingObject();
		
		if (draggingWidget == Container.getNothingBeingDragged()) {
			System.err.println ("FoundationController::mouseReleased() unexpectedly found nothing being dragged.");
			c.releaseDraggingObject();		
			return;
		}

		/** Recover the from BuildablePile OR waste Pile */
		Widget fromWidget = c.getDragSource();
		if (fromWidget == null) {
			System.err.println ("FoundationController::mouseReleased(): somehow no dragSource in container.");
			c.releaseDraggingObject();
			return;
		}

		
		// Determine the To Pile
				Pile foundation = (Pile) src.getModelElement();

				if (fromWidget instanceof BuildablePileView) {
					// coming from a buildable pile [user may be trying to move multiple cards]
					BuildablePile fromPile = (BuildablePile) fromWidget.getModelElement();

					/** Must be the ColumnView widget being dragged. */
					ColumnView columnView = (ColumnView) draggingWidget;
					Column col = (Column) columnView.getModelElement();
					if (col == null) {
						System.err.println ("FoundationController::mouseReleased(): somehow ColumnView model element is null.");
						c.releaseDraggingObject();			
						return;
					}

					// must use peek() so we don't modify col prematurely. Here is a HACK! Presumably
					// we only want the Move object to know things about the move, but we have to put
					// in a check to verify that Column is of size one. NO good solution that I can
					// see right now.
					if (col.count() != 1) {
						fromWidget.returnWidget (draggingWidget);  // return home
					} else {
						Move m = new TableauToFoundationMove (fromPile, foundation, col.peek(), foundationLastCard, this.theGame);

						if (m.doMove (theGame)) {
							// Success
							theGame.pushMove (m);
						} else {
							fromWidget.returnWidget (draggingWidget);
						}
					}
				} else {
					// Coming from the waste [number of cards being dragged must be one]
					Pile piles = (Pile) fromWidget.getModelElement();

					/** Must be the CardView widget being dragged. */
					CardView cardView = (CardView) draggingWidget;
					Card theCard = (Card) cardView.getModelElement();
					if (theCard == null) {
						System.err.println ("FoundationController::mouseReleased(): somehow CardView model element is null.");
						c.releaseDraggingObject();
						return;
					}

					// must use peek() so we don't modify col prematurely
					Move m = new ReservePileToFoundationMove (piles, foundation, theCard, foundationLastCard, this.theGame);
					if (m.doMove (theGame)) {
						// Success
						theGame.pushMove (m);
					} else {
						fromWidget.returnWidget (draggingWidget);
					}
				}

		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		// Determine the To Pile
		/*Pile foundation = (Pile) src.getModelElement();
		BuildablePile piles= (BuildablePile) fromWidget.getModelElement(); //ONly will work if the src is from the Columns
		
		CardView cardView = (CardView) draggingWidget; //Sets the cardview as to which card is being dragged
		Card theCard= (Card) cardView.getModelElement();
		
		
		Move move= new TableauToFoundationMove(piles, foundation, theCard, isRed); //Calls the move function
		
		if(move.doMove(theGame))
		{
			theGame.pushMove(move);
			theGame.refreshWidgets();
		}
		else
		{
			fromWidget.returnWidget(draggingWidget);
		}*/
		
		
		
		
		//release the dragging object (this will reset dragSource)
		c.releaseDraggingObject();
		
		//Finally repaint
		c.repaint();
	}
}
