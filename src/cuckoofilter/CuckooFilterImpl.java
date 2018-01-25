package cuckoofilter;

import java.util.Arrays;
import java.util.Random;

public class CuckooFilterImpl {
	//a table consists a series of buckets,one bucket contains four slots in usual.
	private int numOfBuckets;  
	private int numOfSlots;   
	private byte[][] table;

	public CuckooFilterImpl() {
		
		numOfBuckets = 1000000;
		numOfSlots = 4;
		table = new byte[numOfBuckets][numOfSlots];
	};
	
	public CuckooFilterImpl(int buckets, int slots) {
		numOfBuckets = buckets;
		numOfSlots = slots;
		table = new byte[numOfBuckets][numOfSlots];
	};
	
	public boolean insertItem(String item) {
		 byte[] hashkey = HashUtil.rawSHA256(item);
		 int firstIndex = countFirstIndex(hashkey);
		 int secondIndex = -1;
		 byte fingerprint = hashkey[0];
		 int slotIndex;
		 System.out.println("firstIndex="+firstIndex);
		 System.out.println("fingerprint="+fingerprint); 
		 //check first bucket
		 if((slotIndex = findSlot(firstIndex))!=-1) {
			 table[firstIndex][slotIndex] = fingerprint;
			 for(int i=0;i<numOfBuckets;i++) {
			 System.out.println(Arrays.toString(table[i]));
			 }
			 return true;
		 }
		 //check alternative bucket
		 else {
			 secondIndex = countSecondIndex(firstIndex,fingerprint);
			 System.out.println("secondIndex="+secondIndex); 
			 if((slotIndex = findSlot(secondIndex))!=-1) {
				 table[secondIndex][slotIndex] = fingerprint;
			 }else {
				 //insert the item by force 
				 if(alterPos(secondIndex,fingerprint,1)==true) {
					 return true;
				 }else {
					 System.out.println("The cuckoo filter is going to overflow, insert "+ item +" failed");
					 return false;
				 }
			 }
			 for(int i=0;i<numOfBuckets;i++) {
				 System.out.println(Arrays.toString(table[i]));
				 }
		 return true;
		 }
	};

	public boolean searchItem(String item) {
		 byte[] hashkey = HashUtil.rawSHA256(item);
		 int firstIndex = countFirstIndex(hashkey);
		 int secondIndex = -1;
		 byte fingerprint = hashkey[0];
		 if(containInBucket(firstIndex,fingerprint)!=-1) {
			 return true;
		 }else {
			 secondIndex = countSecondIndex(firstIndex,fingerprint);
			 if(containInBucket(secondIndex,fingerprint)!=-1) {
				 return true;
				 }
		 }
		 return false;
	};
	
	public void deleteItem(String item) {
		 byte[] hashkey = HashUtil.rawSHA256(item);
		 int firstIndex = countFirstIndex(hashkey);
		 byte fingerprint = hashkey[0];
		 int secondIndex = -1;
		 int slotNo;
		 if((slotNo = containInBucket(firstIndex,fingerprint))!=-1) {
			 table[firstIndex][slotNo]=0;
		 }else {
			 secondIndex = countSecondIndex(firstIndex,fingerprint);
			 if((slotNo = containInBucket(firstIndex,fingerprint))!=-1) {
				 table[secondIndex][slotNo]=0;
				 } 
		 }
	}
	
	//checking the item existed in the bucket or not
	private int containInBucket(int index,byte fingerprint) {
		 for(int i=0;i<numOfSlots;i++) {
			 if(table[index][i] == fingerprint) {
				 return i;
			 }
		 }return -1;
	};
	
	/*select a random slot for the new item, and choose another place for the evicted item; 
	 * if the eviction is more than 3 times, it means that the filter is going to overflow.
	 */
	private boolean alterPos(int index,byte fingerprint,int count) {
		Random random = new Random();
		int slotNo = random.nextInt(numOfSlots);
		byte temp = table[index][slotNo];
		table[index][slotNo] = fingerprint;
		int alterIndex = (index^indexByhash(HashUtil.rawSHA256(temp)))%numOfBuckets;
		int alterSlot = findSlot(alterIndex);
		//System.out.println("alterIndex= " +alterIndex + "  alterSlot= " + alterSlot);
		if(alterSlot!=-1) {
			table[alterIndex][alterSlot] = temp;
			return true;
		}else if(count<3){
			count++;
			if(alterPos(alterIndex,temp,count)==true) {
			return true;
			}
		}
		return false;
	};
	
	//select free slots in the bucket
	private int findSlot(int bucketIndex) {
		for(int i=0;i<numOfSlots;i++) {
			//System.out.println(i);
			if(table[bucketIndex][i]==(byte)0) {
				return i;
			}
		}
		return -1;
	};

	//count the item's first bucket number;
	private int countFirstIndex(byte[] hashkey) {
		int firstIndex = indexByhash(hashkey);
		return firstIndex;
	};
	//count the item's second bucket number;
	private int countSecondIndex(int firstIndex,byte fingerprint) {
		int secondIndex = (firstIndex^indexByhash(HashUtil.rawSHA256(fingerprint)))%numOfBuckets;
		return secondIndex;
	};
	
	
	private int indexByhash(byte[] hash) {
		// use lower 31 bits for that Java array supports up to 2^31(Integer.MAX_VALUE) size
    	int index = (hash[hash.length-4] & 0x7f) << 24;
        index |= (hash[hash.length-3] & 0xff) << 16;
        index |= (hash[hash.length-2] & 0xff) << 8;
        index |= (hash[hash.length-1] & 0xff) ;
        
        return index%numOfBuckets;
        }
	
}
