package com.opethic.genivis.multitabs;


import javafx.scene.control.TabPane;

public abstract class DetachableTabPaneFactory {

	DetachableTabPane create(DetachableTabPane source) {
		final DetachableTabPane tabPane = new DetachableTabPane();
		tabPane.setSceneFactory(source.getSceneFactory());
		tabPane.setStageOwnerFactory(source.getStageOwnerFactory());
		tabPane.setScope(source.getScope());
		tabPane.setTabClosingPolicy(source.getTabClosingPolicy());
		tabPane.setTabDragPolicy(TabPane.TabDragPolicy.FIXED);
		tabPane.setCloseIfEmpty(true);
		tabPane.setDetachableTabPaneFactory(source.getDetachableTabPaneFactory());
		tabPane.setDropHint(source.getDropHint());
		init(tabPane);
		return tabPane;
	}

	/**
	 * Callback method to initialize newly created DetachableTabPane for the Tab
	 * that is being detached/docked.
	 */
	protected abstract void init(DetachableTabPane newTabPane);
}
