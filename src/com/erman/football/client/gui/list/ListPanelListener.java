package com.erman.football.client.gui.list;

public interface ListPanelListener {
	
	public void CellClicked(DataCell dataCell);
	public void removeClicked(DataCell dataCell);
	public void modifyClicked(DataCell dataCell);
	public void endClicked(DataCell dataCell,int x, int y);

}
