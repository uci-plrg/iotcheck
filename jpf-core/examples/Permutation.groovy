//import gov.nasa.jpf.vm.Verify

//int count1 = Verify.getIntFromList(1,2,3)
//int count2 = Verify.getIntFromList(4,5,6)
//int count3 = Verify.getIntFromList(0)
//println "Count1: " + count1
//println "Count2:   " + count2
//println "Count3:     " + count3

/*def list = [[1,2,3],
            [1,3,2],
            [2,1,3],
            [2,3,1],
            [3,1,2],
            [3,2,1]]
int count = Verify.getInt(0,5)

list[count].each {
  switch(it) {
    case 1:
      //appObject.setValue([name: "Touched", value: "Touched", deviceId: 0, descriptionText: "",
			//		   displayed: true, linkText: "", isStateChange: false, unit: "", data: []])
      println "1";
      break;
    case 2:
      //lockObject.setValue([name: "lock0", value: "locked", deviceId: 0, descriptionText: "",
			//		   displayed: true, linkText: "", isStateChange: false, unit: "", data: []])
      println "   2";
			break;
    case 3:
      //lockObject.setValue([name: "lock0", value: "unlocked", deviceId: 0, descriptionText: "",
			//		   displayed: true, linkText: "", isStateChange: false, unit: "", data: []])
      println "      3";
      break;
    default:
      break;
  }
}*/

def permuteHelper(nums, curr, list) {
  if (curr == nums.size() - 1) {
    list.add(nums)
  } else {
    for(int i=curr; i<nums.size(); i++) {
      swap(nums, i, curr)
      permuteHelper(nums, curr + 1, list)
      swap(nums, curr, i)
    }
  }
}
    
def swap(nums, src, dest) {
  int temp = nums[dest];
  nums[dest] = nums[src];
  nums[src] = temp;
}

def nums = [1,2,3,4]
def list = [[]]
permuteHelper(nums, 0, list)

println list

