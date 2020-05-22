import gov.nasa.jpf.vm.Verify;

//class Choice {

//	static void main(String[] args) {		
	while(true) {
		int number = Verify.getInt(0, 10);
		println number
		//println ""

		//boolean choice = args[0].toBoolean()
		boolean choice = Verify.getBoolean()
		if (choice == true)
			println "This time it is True"
		else
			println "This time it is False"
	}
	//}		
//}
