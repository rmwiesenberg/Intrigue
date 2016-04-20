package main.model;

import ks.common.games.Solitaire;
import ks.common.model.*;

public class Tableau6FoundationMove extends Move {
	protected Column tableau;
	protected Pile foundation;
	protected Card card;
	
	public Tableau6FoundationMove(Column tableau, Card card, Pile foundation){
		super();
		
		this.tableau = tableau;
		this.foundation = foundation;
		this.card = card;
	}
	
	public boolean doMove(Solitaire theGame){
		if(valid(theGame)){
			foundation.add(card); 
			theGame.updateScore(1);
			return true;
		} else return false;
	}
	
	public boolean valid(Solitaire theGame){
		if(foundation.empty() && card.getRank() == 6) return true;
		else if(card.getRank() > 6 && card.getRank() < 12){
			if((foundation.peek().getRank() + 1) == card.getRank()) return true;
			else return false;
		} else return false;
	}
	
	public boolean undo(Solitaire theGame){
		if(foundation.empty() || tableau.empty()) return false;
		
		tableau.add(foundation.get());
		theGame.updateScore(-1);
		return true;
	}
}