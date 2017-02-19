package kkumar;

import java.awt.Dimension;
import java.util.Random;

import ks.client.gamefactory.GameWindow;
import ks.common.controller.SolitaireMouseMotionAdapter;
import ks.common.games.Solitaire;
import ks.common.games.SolitaireUndoAdapter;
import ks.common.model.BuildablePile;
import ks.common.model.Card;

import ks.common.model.Deck;
import ks.common.model.Pile;
import ks.common.view.BuildablePileView;
import ks.common.view.CardImages;

import ks.common.view.DeckView;
import ks.common.view.IntegerView;
import ks.common.view.PileView;
import ks.launcher.Main;

public class Stalactites extends Solitaire{
	
	Deck deck;
	DeckView deckView; //Not required in our game type
	Pile foundation[] = new Pile[5]; //extra index to avoid exception
	Pile foundationLastCard[] = new Pile[5]; //Pile for holding the last placed card

	Pile reservePile1, reservePile2;
	PileView foundationViews[] = new PileView [5];
	PileView foundationLastCardView[] = new PileView[5];
	PileView rp1, rp2;
		
	/**columns*/
	protected BuildablePile piles[] = new BuildablePile [9]; // in place of Column col1, col2, col3, col4, col5, col6, col7, col8;
	protected BuildablePileView pileViews[] = new BuildablePileView [9];// in place of ColumnView c1,c2,c3,c4,c5,c6,c7,c8;
	
	IntegerView scoreView, numLeftView;	
	int sequence = 0; //To hold the +1/+2 sequence of cards 

	
	@Override
	public String getName() {
		return "StalactitesKushagra";
	}

	@Override
	public boolean hasWon() {
		return getScore().getValue()==48;
	}
	
	public Dimension getPreferredSize(){
		return new Dimension (2000, 1000);
	}

	@Override
	public void initialize() {
		//	Initializing the model
		initializeModel(getSeed());
		initializeView();
		initializeControllers();
		
		
		/**Shuffling cards into the foundation pile*/
		for (int pileNum=1; pileNum <= 4; pileNum++) {
				Card c = deck.get();
				c.setFaceUp (true);
				foundation[pileNum].add (c);			
		}
		
		// prepare game by dealing facedown cards to all columns, then one face up
		for (int pileNum=1; pileNum <= 8; pileNum++) {
			for (int num = 1; num <= 6; num++) {
				Card c = deck.get();
				c.setFaceUp (true);
				piles[pileNum].add (c);
			}
		}
		
		
	}

	private void initializeControllers() {
	/*	deckView.setMouseAdapter(new StalactitesDeckController (this, deck, wastePile));
		deckView.setMouseMotionAdapter (new SolitaireMouseMotionAdapter(this));
		deckView.setUndoAdapter (new SolitaireUndoAdapter(this));*/
		
		// Now for each BuildablePile.
		for (int i = 1; i <= 8; i++) {
			pileViews[i].setMouseAdapter (new StalactitesBuildablePileController (this, pileViews[i]));
			pileViews[i].setMouseMotionAdapter (new SolitaireMouseMotionAdapter (this));
			pileViews[i].setUndoAdapter (new SolitaireUndoAdapter(this));
		}

		// Now for each Foundation.
		for (int i = 1; i <= 4; i++) {
			foundationViews[i].setMouseAdapter (new StalactitesFoundationController (this, foundationViews[i], foundationLastCard[i]));
			foundationViews[i].setMouseMotionAdapter (new SolitaireMouseMotionAdapter (this));
			foundationViews[i].setUndoAdapter (new SolitaireUndoAdapter(this));
		}
		
		
		rp1.setMouseAdapter (new StalactitesReservePileController (this, rp1));
		rp1.setMouseMotionAdapter (new SolitaireMouseMotionAdapter (this));
		rp1.setUndoAdapter (new SolitaireUndoAdapter(this));

		rp2.setMouseAdapter (new StalactitesReservePileController (this, rp2));
		rp2.setMouseMotionAdapter (new SolitaireMouseMotionAdapter (this));
		rp2.setUndoAdapter (new SolitaireUndoAdapter(this));
	}

	private void initializeView() {
		CardImages ci = getCardImages();

		/*deckView = new DeckView (deck);
		deckView.setBounds (20,20, ci.getWidth(), ci.getHeight());
		container.addWidget (deckView);	*/
		/**DeckView*/
		rp1= new PileView(reservePile1);
		rp1.setBounds(20, 20, ci.getWidth(), ci.getHeight());
		container.addWidget(rp1);
		
		rp2= new PileView(reservePile2);
		rp2.setBounds(40+ci.getWidth(), 20, ci.getWidth(), ci.getHeight());
		container.addWidget(rp2);
		/*System.out.println("The length is");
		System.out.println(100+4*ci.getWidth());*/ //For debugging the display of entities
			
		/**PileView*/
		// create PileViews, one after the other.
		for (int pileNum = 1; pileNum <=4; pileNum++) {
			foundationViews[pileNum] = new PileView (foundation[pileNum]);
			foundationViews[pileNum].setBounds (20*(pileNum+3) + ci.getWidth()*(pileNum+2), 20, ci.getWidth(), ci.getHeight());
			container.addWidget (foundationViews[pileNum]);
		}
		
		for (int pileNum = 1; pileNum <=4; pileNum++) {
			foundationLastCardView[pileNum] = new PileView (foundationLastCard[pileNum]);
			foundationLastCardView[pileNum].setBounds (20*(pileNum+3) + ci.getWidth()*(pileNum+2), 40 + ci.getHeight(), ci.getWidth(), ci.getHeight());
			container.addWidget (foundationLastCardView[pileNum]);
		}
		  
		
		// create BuildablePileViews, one after the other (default to 13 full cards -- more than we'll need)
		for (int pileNum = 1; pileNum <=8; pileNum++) {
			pileViews[pileNum] = new BuildablePileView (piles[pileNum]);
			pileViews[pileNum].setBounds (20*pileNum + (pileNum-1)*ci.getWidth(), 2*ci.getHeight() + 60, ci.getWidth(), 13*ci.getHeight());
			container.addWidget (pileViews[pileNum]);
		}
		
		
		scoreView= new IntegerView(getScore());
		scoreView.setFontSize(20);
		//scoreView.setBounds(1800, 900, 100, 60);
		scoreView.setBounds(160+8*ci.getWidth(), 20, 100, 60); //Set the bounds
		/*System.out.println(ci.getWidth()); //For debugging and visualizing the location 
		System.out.println(ci.getHeight());
		System.out.println(160+8*ci.getWidth());*/
		container.addWidget(scoreView);
		
		numLeftView = new IntegerView (getNumLeft());
		numLeftView.setFontSize(20);
		numLeftView.setBounds (260 + 8*ci.getWidth(), 20, 100, 60);
		container.addWidget (numLeftView);
	}

	private void initializeModel(int seed) {
		deck = new Deck("deck");
		deck.create(seed);
		model.addElement (deck);   // added to the model (as defined within our superclass).
		

		
		/**Foundation Section**/
		for (int i = 1; i<=4; i++) {
			foundation[i] = new Pile ("foundation" + i);
			model.addElement (foundation[i]);
		}
		
		for (int i = 1; i<=4; i++) {
			foundationLastCard[i] = new Pile ("foundationLastCard" + i);
			model.addElement (foundationLastCard[i]);
		}
		
		/**columns*/
		for (int i = 1; i<=8; i++) {
			piles[i] = new BuildablePile ("pile" + i);
			model.addElement (piles[i]);
		} 
		

		reservePile1 = new Pile("ReservePile1");
		model.addElement(reservePile1);
		reservePile2= new Pile("ReservePile2");
		model.addElement(reservePile2);
		
		updateScore(0);
		updateNumberCardsLeft(48); 
		
		
		
	}
	
	public void setSequence(int newSequenceValue)
	{
		this.sequence= newSequenceValue;
	}
	
	public int getSequence()
	{
		return this.sequence;
	}
	
	public static void main (String []args) {
		// Seed is to ensure we get the same initial cards every time.
		// Here the seed is to "order by suit."
		GameWindow gw = Main.generateWindow(new Stalactites(), new Random().nextInt());
		//GameWindow gw = Main.generateWindow(new Stalactites(), Deck.OrderBySuit);
		//gw.setSkin(SkinCatalog.MULTIPLE_BOUNCING_BALLS);

	}

}
