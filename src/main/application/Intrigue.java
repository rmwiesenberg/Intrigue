package main.application;

import ks.client.gamefactory.GameWindow;
import ks.common.controller.SolitaireReleasedAdapter;
import ks.common.games.Solitaire;
import ks.common.model.*;
import ks.common.view.*;
import ks.launcher.Main;
import main.model.*;
import main.controller.*;
import java.util.ArrayList;


public class Intrigue extends Solitaire{
	protected MultiDeck deck;
	
	protected ArrayList<Column> tableau = new ArrayList<Column>(8);
	
	protected ArrayList<Pile> fFoundation = new ArrayList<Pile>(8);
	
	protected ArrayList<Pile> sFoundation = new ArrayList<Pile>(8);
	
	protected Pile tempPile;
	
	protected ArrayList<ColumnView> tv = new ArrayList<ColumnView>(8);
	
	protected ArrayList<PileView> ffv = new ArrayList<PileView>(8);
	
	protected ArrayList<PileView> sfv = new ArrayList<PileView>(8);
	
	protected IntegerView scoreView;
	
	private void initializeController(){
		// Init Column and Pile Controllers
		for(int i = 0; i < 8; i++){
			tv.get(i).setMouseAdapter(new IntrigueColumnController (this, tv.get(i)));
			ffv.get(i).setMouseAdapter(new IntriguePileController (this, ffv.get(i)));
			sfv.get(i).setMouseAdapter(new IntriguePileController (this, sfv.get(i)));
		}
		
		// Init scoreView
		scoreView.setMouseAdapter(new SolitaireReleasedAdapter(this));
	}
	
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
		
		for(int i = 0; i < 8; i++){
			int numName = i + 1;
			tableau.add(i, new Column("tableau" + numName));
			fFoundation.add(i, new Pile("fFoundation" + numName));
			sFoundation.add(i, new Pile("sFoundation" + numName));
			
			addModelElement(tableau.get(i));
			addModelElement(fFoundation.get(i));
			addModelElement(sFoundation.get(i));
		}
	}
	
	private void initializeView(){
		CardImages ci = getCardImages();
		
		scoreView = new IntegerView(getScore());
		scoreView.setBounds(100+5*ci.getWidth(), 0, 100, 65);
		addViewWidget(scoreView);
		
		int i = 0;
		int n = 0;
		
		while(i < 8){
			ffv.add(i, new PileView(fFoundation.get(i)));
			ffv.get(i).setBounds((((n+1) * 20) + (n * ci.getWidth())), 80, ci.getWidth(), ci.getHeight());
			addViewWidget(ffv.get(i));			
			n++;
		
			tv.add(i, new ColumnView(tableau.get(i)));
			tv.get(i).setBounds((((n+1) * 20) + (n * ci.getWidth())), 80, ci.getWidth(), ci.getHeight());
			addViewWidget(tv.get(i));			
			n++;
		
			sfv.add(i, new PileView(sFoundation.get(i)));
			sfv.get(i).setBounds((((n+1) * 20) + (n * ci.getWidth())), 80, ci.getWidth(), ci.getHeight());
			addViewWidget(sfv.get(i));
			n++;
			
			i++;
		}
		/*
		n = 0;
		while(i < 8){
			ffv.add(i, new PileView(fFoundation.get(i)));
			ffv.get(i).setBounds((((n+1) * 20) + (n * ci.getWidth())), 400, ci.getWidth(), ci.getHeight());
			addViewWidget(ffv.get(i));			
			n++;
		
			tv.add(i, new ColumnView(tableau.get(i)));
			tv.get(i).setBounds((((n+1) * 20) + (n * ci.getWidth())), 400, ci.getWidth(), ci.getHeight());
			addViewWidget(tv.get(1));			
			n++;
		
			sfv.add(i, new PileView(sFoundation.get(i)));
			sfv.get(i).setBounds((((n+1) * 20) + (n * ci.getWidth())), 400, ci.getWidth(), ci.getHeight());
			addViewWidget(sfv.get(i));
			n++;
			
			i++;
		}
		*/
	}
	
	public boolean hasWon(){
		return 	(tableau.get(0).peek().getRank() == 12) &&
				(tableau.get(1).peek().getRank() == 12) &&
				(tableau.get(2).peek().getRank() == 12) &&
				(tableau.get(3).peek().getRank() == 12) &&
				(tableau.get(4).peek().getRank() == 12) &&
				(tableau.get(5).peek().getRank() == 12) &&
				(tableau.get(6).peek().getRank() == 12) &&
				(tableau.get(7).peek().getRank() == 12);
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
		while(!firstQueen){
			if (deck.peek().getRank() == 12){
				tableau.get(curTableau).add(deck.get());
				deck.push(tempPile);
				firstQueen = true;
			} else {
				tempPile.add(deck.get());
			}
		}
		
		while(!deck.empty()){
			boolean sorted = false;
			if (deck.peek().getRank() == 12) curTableau++;
			tableau.get(curTableau).add(deck.get());
			
			for(int i = 0; i < 8; i++){
				Move tffm = new Tableau5FoundationMove(tableau.get(curTableau), tableau.get(curTableau).get(), fFoundation.get(i));
				Move tsfm = new Tableau6FoundationMove(tableau.get(curTableau), tableau.get(curTableau).get(), sFoundation.get(i));
				if(!sorted) sorted = tffm.doMove(this);
				if(!sorted) sorted = tsfm.doMove(this);
			}
		}
	}
	
	public static void main(String []args){
		GameWindow gw = Main.generateWindow(new Intrigue(), Deck.OrderByRank);
		gw.setVisible(true);
	}
}
