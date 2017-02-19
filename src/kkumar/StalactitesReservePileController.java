package kkumar;

import java.awt.event.MouseEvent;

import heineman.Klondike;
import ks.common.controller.SolitaireReleasedAdapter;
import ks.common.games.Solitaire;
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

public class StalactitesReservePileController extends java.awt.event.MouseAdapter {
	
	/** The Klondike Game. */
	protected Stalactites theGame;

	/** The specific WastePileView being controlled. */
	protected PileView src;
	
	public StalactitesReservePileController(Stalactites theGame, PileView reservePileView) {
		this.theGame=theGame;
		this.src=reservePileView;
	}
	
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
		BuildablePile reservePile = (BuildablePile) fromWidget.getModelElement();
		
		ColumnView columnView = (ColumnView) draggingWidget;
		Column col = (Column) columnView.getModelElement();
		/*CardView cardView = (CardView) draggingWidget;
		Card theCard = (Card) cardView.getModelElement();*/
		
		Move move = new TableauToReserveMove(reservePile, foundation, col.peek());
		if (move.doMove(theGame)) {
			theGame.pushMove (move);     // Successful Move has been Move
		} else {
			fromWidget.returnWidget (draggingWidget);
		}
		
		// release the dragging object, (this will reset dragSource)
		c.releaseDraggingObject();
		
		// finally repaint
		c.repaint();
	
	
}



public void mousePressed(MouseEvent me) {
	 
	// The container manages several critical pieces of information; namely, it
	// is responsible for the draggingObject; in our case, this would be a CardView
	// Widget managing the card we are trying to drag between two piles.
	Container c = theGame.getContainer();
	
	/** Return if there is no card to be chosen. */
	Pile wastePile = (Pile) src.getModelElement();
	if (wastePile.count() == 0) {
		c.releaseDraggingObject();
		return;
	}

	// Get a card to move from PileView. Note: this returns a CardView.
	// Note that this method will alter the model for PileView if the condition is met.
	CardView cardView = src.getCardViewForTopCard (me);
	
	// an invalid selection of some sort.
	if (cardView == null) {
		c.releaseDraggingObject();
		return;
	}
	
	// If we get here, then the user has indeed clicked on the top card in the PileView and
	// we are able to now move it on the screen at will. For smooth action, the bounds for the
	// cardView widget reflect the original card location on the screen.
	Widget w = c.getActiveDraggingObject();
	if (w != Container.getNothingBeingDragged()) {
		System.err.println ("WastePileController::mousePressed(): Unexpectedly encountered a Dragging Object during a Mouse press.");
		return;
	}

	// Tell container which object is being dragged, and where in that widget the user clicked.
	c.setActiveDraggingObject (cardView, me);
	
	// Tell container which source widget initiated the drag
	c.setDragSource (src);

	// The only widget that could have changed is ourselves. If we called refresh, there
	// would be a flicker, because the dragged widget would not be redrawn. We simply
	// force the WastePile's image to be updated, but nothing is refreshed on the screen.
	// This is patently OK because the card has not yet been dragged away to reveal the
	// card beneath it.  A bit tricky and I like it!
	src.redraw();
}

	
}