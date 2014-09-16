package com.kdk.imagesearch.util;

import java.io.Serializable;

public class Settings implements Serializable{

	private static final long serialVersionUID = -3167761091255695363L;
	public String size;
	public String color;
	public String type;
	public String site;
	
	public Settings(){
		size = "";
		color= "";
		type = "";
		site = "";
	}
	
	public String toString(){
		return "Size: ".concat(size).concat("\nColor: ").concat(color).concat("\nType: ").concat(type).concat("\n site: ").concat(site);
		
	}
}
