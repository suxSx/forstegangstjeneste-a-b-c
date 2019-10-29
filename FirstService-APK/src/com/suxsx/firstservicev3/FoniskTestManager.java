package com.suxsx.firstservicev3;

public class FoniskTestManager {
	
	private static String[] names = { 
		"A", 
		"B",
		"C", 
        "D",
        "E",
        "F", 
        "G",
        "H",
        "I",
        "J",
        "K",
        "L",
        "M",
        "N",
        "O",
        "P",
        "Q",
        "R",
        "S",
        "T",
        "U",
        "V",
        "W",
        "X",
        "Y",
        "Z",
        "Æ",
        "Ø",
        "Å",
    };
	
	private static String[] namesRes = { 
		"Alfa", 
		"Bravo",
		"Charlie", 
        "Delta",
        "Echo",
        "Foxtrot", 
        "Golf",
        "Hotel",
        "India",
        "Juliet",
        "Kilo",
        "Lima",
        "Mike",
        "November",
        "Oscar",
        "Papa",
        "Quebec",
        "Romeo",
        "Sierra",
        "Tango",
        "Uniform",
        "Victor",
        "Whiskey",
        "Xray",
        "Yankee",
        "Zulu",
        "Ærlig",
        "Østen",
        "Åse",
    };
	
private String name;
private String nameSvar;
private String wrongNames = "";
private Boolean testStarted = false;
private Boolean nullFeil = true;
public int right = 0;
public int total = 0;

private static int id = 0;


public Boolean isStarted()
	{ return testStarted; }

public Boolean isFeil()
{ return nullFeil; }

public String getWrongName()
{ return wrongNames; }

public void setWrongName(String wrong)
{ wrongNames += wrong + "\n"; }

public int currentID()
{ return id; }

public void Started()
{ testStarted = true; }

public void enFeil()
{ nullFeil = false; }

public void nullStill()
{ 
	id = 0;
	right = 0;
	total = 0;
	wrongNames = "";
	testStarted = false;
}

public void getNext(){
   name = names[id];
   nameSvar = namesRes[id];
   
   id++;
   total++;
}


public String getCurrent(int i)
{
	if(i == 0) return name;
	else return nameSvar;
}
	
	public FoniskTestManager()
	{
		
	}

}
