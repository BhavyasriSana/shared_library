import groovy.json.*
import groovy.json.JsonOutput
def call(jsondata,jenkins,github)
{
def jsonString = jsondata
def jsonObj = readJSON text: jsonString
int ecount = jsonObj.riglet_info.auth_users.size()
	def team=jsonObj.riglet_info.name

 
 List<String> JSON = new ArrayList<String>();
  List<String> LIST = new ArrayList<String>();
  List<String> JSON1 = new ArrayList<String>();
	List<String> jsonStringa= new ArrayList<String>();
	jsonStringa.add(jenkins)
   jsonStringa.add(github)
	 for(j=0;j<ecount;j++)
   {
	 def email=jsonObj.riglet_info.auth_users[j]
	   int score=0
    int reward=0
    String name="  "
	 for(i=0;i<jsonStringa.size();i++)
  { 
   
	  
  if(jsonStringa[i].contains("JENKINS"))
    {
 
	   
	    
     name="jenkins"
    //  def jsonStringb = bamboo
	   // def jsonString1 = jsonStringa[i]
	   def jsonObja = readJSON text: jsonStringa[i]

  //println(jsonObja)
  def scnt =jsonObja.JENKINS.individualsuccess[j].Success_cnt
  def fcnt =jsonObja.JENKINS.individualfailure[j].Failure_cnt
  def tcnt =jsonObja.JENKINS.individualbuilds[j].Total_cnt
 def email1=jsonObja.JENKINS.individualsuccess[j].email
      
 // def res=bamboo1.bamboo.teamsuccessbuild_cnt
 // def obj = JSON.parse(bamboo1)
 //println(scnt)
 //int score=0
	  
 if(email==email1)
  {
   // LIST.add(["toolName":name,"metricName":"total_builds","value":total])
	    
 
	   LIST.add(["toolName":name,"metricName":"success_builds","value":scnt])
	    LIST.add(["toolName":name,"metricName":"failure_builds","value":fcnt])
	  LIST.add(["toolName":name,"metricName":"total_builds","value":tcnt])
	  
  }
   }
	   
	  if(jsonStringa[i].contains("GITHUB"))
      {
        name="gitlab"
	      //metric="commits"
        def jsonObjc= readJSON text: jsonStringa[i]
  //println(jsonObj)
  def cnt =jsonObjc.GITHUB.individual_commit_Details[j].User_Commits_count
	       def email1=jsonObjc.GITHUB.individual_commit_Details[j].User_email
	       if(email==email1)
  {
    LIST.add(["toolName":name,"metricName":"commits","value":cnt])
  }
      }
    }
	   JSON1[j]=LIST.clone()
	   
   JSON.add(["teamMemberName":email,"teamName":team,"metrics":JSON1[j]])
    LIST.clear()
	//reward=0    
	   
    }
	
     def jsonBuilder = new groovy.json.JsonBuilder()

jsonBuilder(
   JSON
  
) 
  
  File file = new File("/var/lib/jenkins/workspace/${JOB_NAME}/game.json")
file.write(jsonBuilder.toPrettyString())
    
  //println(JSON)
}
