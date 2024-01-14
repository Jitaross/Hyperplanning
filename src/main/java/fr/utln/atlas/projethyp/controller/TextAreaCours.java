package fr.utln.atlas.projethyp.controller;

import fr.utln.atlas.projethyp.entities.Cours;
import javafx.scene.control.TextArea;
import lombok.Getter;

public class TextAreaCours extends TextArea {
	@Getter
	private Cours cours;

	public TextAreaCours(Cours cours, String s) {
		super(s);
		this.cours = cours;
	}
}
