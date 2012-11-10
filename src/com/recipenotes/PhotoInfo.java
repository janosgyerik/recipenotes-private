package com.recipenotes;

import java.io.File;
import java.io.Serializable;

public class PhotoInfo implements Serializable {
	private static final long serialVersionUID = 8999960437754785025L;
	
	String recipeId;
	File photoFile;
}

