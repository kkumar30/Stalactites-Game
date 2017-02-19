package kkumar;

import ks.common.games.Solitaire;
import ks.common.model.Card;
import ks.common.model.Move;
import ks.common.model.Pile;

public class ReservePileToFoundationMove extends Move {

	Pile sourceReservePile;
	Pile targetFoundationPile; 
	Card cardBeingDragged;
	Pile foundationLastCard;
	Stalactites theGame;
	
	
	public ReservePileToFoundationMove(Pile from, Pile to, Card cardBeingDragged, Pile foundationLastCard, Stalactites theGame)
	{
	this.sourceReservePile= from;
	this.targetFoundationPile= to;
	this.cardBeingDragged=cardBeingDragged;
	this.foundationLastCard=foundationLastCard;
	this.theGame=theGame;
	}
	
	
	@Override
	public boolean doMove(Solitaire game) {
		if(!valid(game)){return false;}
		
		if(foundationLastCard.empty()) //To shift the card to show sequence
		{
		Card c=targetFoundationPile.peek();
		foundationLastCard.add(c);
		}
		
		targetFoundationPile.add(cardBeingDragged);		
		
		if(cardBeingDragged.getRank() + 1 ==targetFoundationPile.peek().getRank())
		{
			this.theGame.sequence=1;
		}
		
		else if(cardBeingDragged.getRank() + 2 ==targetFoundationPile.peek().getRank())
		{
			this.theGame.sequence=2;
		}
		
		

		System.out.print("SequenceVal=");
		System.out.println(this.theGame.getSequence());
		
		game.updateScore(+1);
		game.updateNumberCardsLeft(-1);
		
		return true;

	}

	@Override
	public boolean undo(Solitaire game) {
		sourceReservePile.add(targetFoundationPile.get());
		
		if(targetFoundationPile.count()==1)
		{
		foundationLastCard.get();
		}
		
		game.updateScore(-1);
		game.updateNumberCardsLeft(+1);
		return true;
	}

	@Override
	public boolean valid(Solitaire game) {
		Card c= cardBeingDragged;
		
		if(c==null) {
			return false;
			}
			/*System.out.print("SequenceVal=");
			System.out.println(this.theGame.getSequence());*/
		if(cardBeingDragged.getRank()==13 && targetFoundationPile.peek().getRank()==1 || cardBeingDragged.getRank()==12 && targetFoundationPile.peek().getRank()==1) //For wrapping up of card ranks
		{
		/*System.out.print("Rank=");
		System.out.println(cardBeingDragged.getRank());*/
		return true;
		}
		
		if(cardBeingDragged.getRank()==13 && targetFoundationPile.peek().getRank()==2) //For wrapping up of King and 2
		{
		/*System.out.print("Rank=");
		System.out.println(cardBeingDragged.getRank());*/
		return true;
		}
		
			//boolean draggingCardRed=true;
			if(this.theGame.getSequence()== 0)
			{
				if(((cardBeingDragged.getRank() + 1) == targetFoundationPile.peek().getRank()) || ((cardBeingDragged.getRank() + 2) == targetFoundationPile.peek().getRank()))
				{
					return true;
				}
				//else if((cardBeingDragged.getRank() + 2) == targetFoundationPile.peek().getRank())
			}
			/*else if(this.theGame.getSequence()==1)
			{
				if((cardBeingDragged.getRank() + 1) == targetFoundationPile.peek().getRank())
				{
					return true;
				}
				else 
					return false;
			}
			else if (this.theGame.getSequence()==2)
			{
				if((cardBeingDragged.getRank() + 2) == targetFoundationPile.peek().getRank())
				{
					return true;
				}
				else 
					return false;
			}
			else
				return false;*/
			
	return false;
		
	}

}
