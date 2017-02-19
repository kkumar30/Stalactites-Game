package kkumar;

import java.awt.event.MouseEvent;

import heineman.klondike.FlipCardMove;
import ks.common.controller.SolitaireReleasedAdapter;
import ks.common.games.Solitaire;
import ks.common.model.BuildablePile;
import ks.common.model.Column;
import ks.common.model.Move;
import ks.common.view.BuildablePileView;
import ks.common.view.ColumnView;
import ks.common.view.Container;
import ks.common.view.Widget;

public class StalactitesBuildablePileController extends SolitaireReleasedAdapter{

	
	BuildablePileView src;
	
	public StalactitesBuildablePileController(Solitaire theGame, BuildablePileView src ) {
		super(theGame);
		this.src=src;
	}

	
	public void mousePressed(MouseEvent me)
	{
		
		// The container manages several critical pieces of information; namely, it
		// is responsible for the draggingObject; in our case, this would be a CardView
		// Widget managing the card we are trying to drag between two piles.
		Container c = theGame.getContainer();

		/** Return if there is no card to be chosen. */
		BuildablePile theBP = (BuildablePile) src.getModelElement();
		if (theBP.count() == 0) {
			return;
		}

		// No Face Up cards means that we must be requesting to flip a card.
		// If we get here, we must have some cards in the BuildablePile
		if (theBP.getNumFaceUp() == 0) {
			Move m = new FlipCardMove (theBP);
			if (m.doMove(theGame)) {
				theGame.pushMove (m);
				theGame.refreshWidgets();
			} else {
				// error in flip card. Not sure what to do
				System.err.println ("BuildablePileController::mousePressed(). Unexpected failure in flip card.");
			}
			return;
		}

		// Get a column of cards to move from the BuildablePileView
		// Note that this method will alter the model for BuildablePileView if the condition is met.
		ColumnView colView = src.getColumnView (me);

		// an invalid selection (either all facedown, or not in faceup region)
		if (colView == null) {
			return;
		}

		// Check conditions
		Column col = (Column) colView.getModelElement();
		if (col == null) {
			System.err.println ("BuildablePileController::mousePressed(): Unexpectedly encountered a ColumnView with no Column.");
			return; // sanity check, but should never happen.
		}

		// verify that Column has desired Klondike Properties to move
		if ((!col.descending()) || (!col.alternatingColors())) {
			theBP.push (col);
			java.awt.Toolkit.getDefaultToolkit().beep();
			return; // announce our displeasure
		}

		// If we get here, then the user has indeed clicked on the top card in the PileView and
		// we are able to now move it on the screen at will. For smooth action, the bounds for the
		// cardView widget reflect the original card location on the screen.
		Widget w = c.getActiveDraggingObject();
		if (w != Container.getNothingBeingDragged()) {
			System.err.println ("BuildablePileController::mousePressed(): Unexpectedly encountered a Dragging Object during a Mouse press.");
			return;
		}

		// Tell container which object is being dragged, and where in that widget the user clicked.
		c.setActiveDraggingObject (colView, me);

		// Tell container which BuildablePileView is the source for this drag event.
		c.setDragSource (src);

		// we simply redraw our source pile to avoid flicker,
		// rather than refreshing all widgets...
		src.redraw();
		
		
		
		
	}
	
}
