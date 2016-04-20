package main.model;

import ks.common.games.Solitaire;
import ks.common.model.*;

public class TableauTableauMove extends Move {
	protected Column source, target;
	protected Card card;
	
	public TableauTableauMove(Column source, Card card, Column target){
		super();
		
		this.source = source;
		this.target = target;
		this.card = card;
	}
	
	public boolean doMove(Solitaire theGame){
		if(valid(theGame)){
			target.add(card);
			return true;
		} else return false;
	}
	
	public boolean valid(Solitaire theGame){
		if(card.getRank() == 12 || target.peek().getRank() != 12) return false;
		else return true;
	}
	
	public boolean undo(Solitaire theGame){
		if(target.peek().getRank() == 12) return false;
		else {
			source.add(source.get());
			return true;
		}
	}
}
