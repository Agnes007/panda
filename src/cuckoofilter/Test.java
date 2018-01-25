package cuckoofilter;

public class Test {
	public static void main(String[] args) {
		CuckooFilterImpl cf = new CuckooFilterImpl(6,2);
		cf.insertItem("055");
		cf.insertItem("177");
		cf.insertItem("1845");
		cf.insertItem("3@445");
		
		if(cf.searchItem("3@445")==true) {
			System.out.println("find 3@445 succeed");
		}else{
			System.out.println("could not find 3@4945");
		};
		

		cf.insertItem("4@445");
		cf.insertItem("51845");
		cf.insertItem("6@45");
		cf.insertItem("7845");
		
		if(cf.searchItem("4@445")==true) {
			System.out.println("find 4@494161845 succeed");
		}else{
			System.out.println("could not find 4@494161845");
		};
		
		/*cf.deleteItem("4@494161845");
	
		if(cf.searchItem("4@494161845")==true) {
			System.out.println("find 4@494161845 succeed");
		}else{
			System.out.println("could not find 4@494161845");
		};
		*/
		
		cf.insertItem("8@4941");
		cf.insertItem("9@4941615");
		cf.insertItem("10@49845");


	}
}
