package AntColonySim;

public class AntInLLArray {
	AntType antType;
	
	int LLIndex;
	
	int uniqueID;
	
	AntInLLArray (AntType antTypeParam, int LLIndexParam, int uniqueIDParam) {
		
		antType = antTypeParam;

//		LLIndex = LLIndexParam;
		
		uniqueID = uniqueIDParam;
	}

	public AntType getAntType () {
		
		return antType;
	}

/*
	public int getLLIndex () {
		
		return LLIndex;
	}
*/

	public int getUniqueID () {
		
		return uniqueID;
	}

}


