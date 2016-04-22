package main.application;

import ks.client.gamefactory.*;
import ks.common.controller.SolitaireMouseMotionAdapter;
import ks.common.controller.SolitaireReleasedAdapter;
import ks.common.games.Solitaire;
import ks.common.games.SolitaireUndoAdapter;
import ks.common.model.*;
import ks.common.view.*;
import ks.launcher.Main;
import main.model.*;
import main.controller.*;

import java.awt.Dimension;


public class Intrigue extends Solitaire{
	protected MultiDeck deck;
	
	public Column tableau[] = new Column[8];
	
	public Pile fFoundation[] = new Pile[8];
	
	public Pile sFoundation[] = new Pile[8];
	
	protected Pile tempPile;
	
	protected ColumnView tv[] = new ColumnView[8];
	
	protected PileView ffv[] = new PileView[8];
	
	protected PileView sfv[] = new PileView[8];
	
	protected IntegerView scoreView;
	
	private void initializeController(){
		// Init Column and Pile Controllers
		for(int i = 0; i < 8; i++){
			tv[i].setMouseAdapter(new IntrigueColumnController (this, tv[i]));
			tv[i].setMouseMotionAdapter (new SolitaireMouseMotionAdapter (this));
			tv[i].setUndoAdapter (new SolitaireUndoAdapter(this));
			
			ffv[i].setMouseAdapter(new IntriguePileController (this, ffv[i]));
			ffv[i].setMouseMotionAdapter (new SolitaireMouseMotionAdapter (this));
			ffv[i].setUndoAdapter (new SolitaireUndoAdapter(this));
			
			sfv[i].setMouseAdapter(new IntriguePileController (this, sfv[i]));
			sfv[i].setMouseMotionAdapter (new SolitaireMouseMotionAdapter (this));
			sfv[i].setUndoAdapter (new SolitaireUndoAdapter(this));
		}
		
		// Init scoreView
		scoreView.setMouseMotionAdapter (new SolitaireMouseMotionAdapter(this));
		scoreView.setMouseAdapter (new SolitaireReleasedAdapter(this));
		scoreView.setUndoAdapter (new SolitaireUndoAdapter(this));
		
		getContainer().setMouseMotionAdapter(new SolitaireMouseMotionAdapter(this));
		getContainer().setMouseAdapter (new SolitaireReleasedAdapter(this));
		getContainer().setUndoAdapter (new SolitaireUndoAdapter(this));
	}
	
	@Override
	public String getName() {
		return "Intrigue Final";
	}
	
	private void initializeModel(int seed){
		score = getScore();
		score.setValue(0);
		
		deck = new MultiDeck("deck", 2);
		deck.create(seed);
		addModelElement(deck);
		
		tempPile = new Pile("tempPile");
		addModelElement(tempPile);
		
		for(int i = 0; i < 8; i++){
			int numName = i + 1;
			tableau[i] = new Column("tableau" + numName);
			fFoundation[i] = new Pile("fFoundation" + numName);
			sFoundation[i] = new Pile("sFoundation" + numName);
			
			addModelElement(tableau[i]);
			addModelElement(fFoundation[i]);
			addModelElement(sFoundation[i]);
		}
	}
	
	private void initializeView(){
		CardImages ci = getCardImages();
		
		scoreView = new IntegerView(getScore());
		scoreView.setBounds(50 + 4*ci.getWidth(), -20, 100, 65);
		container.addWidget(scoreView);
		
		int n = 0;
		
		for(int i = 0; i < 8; i++){
			ffv[i] = new PileView(fFoundation[i]);
			ffv[i].setBounds((20 * (i+1) + (i * ci.getWidth())), 175, ci.getWidth(), ci.getHeight());
			container.addWidget(ffv[i]);			
			n++;
		
			tv[i] = new ColumnView(tableau[i]);
			tv[i].setBounds((20 * (i+1) + (i * ci.getWidth())), 275, ci.getWidth(), 1000);
			container.addWidget(tv[i]);			
			n++;
		
			sfv[i] = new PileView(sFoundation[i]);
			sfv[i].setBounds((20 * (i+1) + (i * ci.getWidth())), 75, ci.getWidth(), ci.getHeight());
			container.addWidget(sfv[i]);
			n++;
		}
	}
	
	public boolean hasWon(){
		return 	(tableau[0].peek().getRank() == 12) &&
				(tableau[1].peek().getRank() == 12) &&
				(tableau[2].peek().getRank() == 12) &&
				(tableau[3].peek().getRank() == 12) &&
				(tableau[4].peek().getRank() == 12) &&
				(tableau[5].peek().getRank() == 12) &&
				(tableau[6].peek().getRank() == 12) &&
				(tableau[7].peek().getRank() == 12);
	}
	
	public void initialize(){
		initializeModel(getSeed());
		initializeView();
		initializeController();
		distributeCards();
	}
	
	private void distributeCards(){
		int curTableau = 0;
		boolean firstQueen = false;
		while (!firstQueen) {
			if (deck.peek().getRank() == 12) {
				tableau[curTableau].add(deck.get());
				deck.push(tempPile);
				firstQueen = true;
			} else {
				tempPile.add(deck.get());
			}
		}

		while (!deck.empty()) {
			Card curCard = deck.get();
			int rank = curCard.getRank();
			if (rank == 12)
				curTableau++;

			tableau[curTableau].add(curCard);
			rank = curCard.getRank();

			if (rank == 6 || rank == 7 || rank == 8 || rank == 9 || rank == 10 || rank == 11) {
				for (int i = 0; i < 8; i++) {
					if (sFoundation[i].empty() && (rank == 6)) {
						sFoundation[i].add(tableau[curTableau].get());
						this.updateScore(1);
						break;
					} else if (!sFoundation[i].empty()) {
						int frank = sFoundation[i].peek().getRank() + 1;
						if (frank == rank) {
							sFoundation[i].add(tableau[curTableau].get());
							this.updateScore(1);
							break;
						}
					}
				}
			} else if (rank == 5 || rank == 4 || rank == 3 || rank == 2 || rank == 1 || rank == 13) {
				for (int i = 0; i < 8; i++) {
					if (fFoundation[i].empty() && rank == 5) {
						fFoundation[i].add(tableau[curTableau].get());
						this.updateScore(1);
						break;
					} else if (!fFoundation[i].empty()) {
						int frank = fFoundation[i].peek().getRank() - 1;
						if (frank == rank || (frank == 0 && rank == 13)) {
							fFoundation[i].add(tableau[curTableau].get());
							this.updateScore(1);
							break;
						}
					}

				}
			}
//			inum.increment(1);
		}
	}
	
	@Override
	public Dimension getPreferredSize() {
	  return new Dimension (780, 1040);
	}
	
	public static void main(String []args){
		GameWindow gw = Main.generateWindow(new Intrigue(), Deck.OrderByRank);
		gw.setVisible(true);
	}
}
