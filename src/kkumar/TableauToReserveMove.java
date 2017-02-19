package kkumar;

import ks.common.games.Solitaire;
import ks.common.model.BuildablePile;
import ks.common.model.Card;
import ks.common.model.Move;
import ks.common.model.Pile;

public class TableauToReserveMove extends Move{

	
	BuildablePile sourceTableauCol;
	Pile targetReservePile; 
	Card cardBeingDragged;
	//Stalactites theGame;

	

	public TableauToReserveMove(BuildablePile from, Pile to, Card cardBeingDragged)
	{
	this.targetReservePile=to;
	this.sourceTableauCol= from;
	this.cardBeingDragged=cardBeingDragged;
	}
	
	
	@Override
	public boolean doMove(Solitaire game) {
		
		if(!valid(game)){return false;}
		
		targetReservePile.add(cardBeingDragged);		
		
		return true;
}

	@Override
	public boolean undo(Solitaire game) {
		// sourceTableauCol.add(targetFoundationPile.get());
		
		Card c =  targetReservePile.get();
		sourceTableauCol.add(c);
		
		return true;
	}

	@Override
	public boolean valid(Solitaire game) {
		return (targetReservePile.empty());
		
	}

}
