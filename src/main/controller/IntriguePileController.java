package main.controller;

import java.awt.event.MouseEvent;

import ks.common.games.Solitaire;
import ks.common.model.Card;
import ks.common.model.Move;
import ks.common.model.Pile;
import ks.common.model.Column;
import ks.common.view.CardView;
import ks.common.view.ColumnView;
import ks.common.view.Container;
import ks.common.view.PileView;
import ks.common.view.Widget;
import main.model.*;

public class IntriguePileController extends java.awt.event.MouseAdapter {
	protected Solitaire theGame = null;
	
	PileView pileview;
	
	public IntriguePileController(Solitaire game, PileView pileview){
		super();
		
		theGame = game;
		this.pileview = pileview;
	}
	
	public void mousePressed(MouseEvent me) {
		// Ask PileView to retrieve the top card as a CardView Widget
		CardView cardView = pileview.getCardViewForTopCard(me);

		// no card present!
		if (cardView == null) { return; }
		
		// Have container track this object now. Record where it came from
		Container c = theGame.getContainer();
		c.setActiveDraggingObject (cardView, me);
		c.setDragSource(pileview);

		// we simply redraw our source pile to avoid flicker,
		// rather than refreshing all widgets...
		pileview.redraw();
	}
	
	public void mouseReleased(MouseEvent me) {
		Container c = theGame.getContainer();

		/** Return if there is no card being dragged chosen. */
		Widget w = c.getActiveDraggingObject();
		if (w == Container.getNothingBeingDragged()) return;

		/** Must be the CardView widget. */
		CardView cardView = (CardView) w;
		Card theCard = (Card) cardView.getModelElement();

		/** Recover the From Pile */
		ColumnView fromColumnView = (ColumnView) c.getDragSource();
		Column fromColumn = (Column) fromColumnView.getModelElement();

		// Determine the To Pile
		Pile toPile = (Pile) pileview.getModelElement();

		// Try to make the move
		Move tffm = new Tableau5FoundationMove(fromColumn, theCard, toPile);
		Move tsfm = new Tableau6FoundationMove(fromColumn, theCard, toPile);
		if (tffm.doMove(theGame)) theGame.pushMove (tffm);
		else if (tsfm.doMove(theGame)) theGame.pushMove (tsfm);
		else fromColumnView.returnWidget(cardView);

		// Since we could be released over a widget, or over the container, 
		// we must repaintAll() clipped to the region we are concerned about.
		// first refresh the last known position of the card being dragged.
		// then the widgets.
		theGame.refreshWidgets(); 

		// release the dragging object since the move is now complete (this 
		// will reset container's dragSource).
		c.releaseDraggingObject();
		c.repaint();
	}
}
