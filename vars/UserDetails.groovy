import groovy.json.*
import groovy.json.JsonOutput

	

def call(JSON,IP)
{
def jsonString = JSON
def jsonObj = readJSON text: jsonString
def mailcount = jsonObj.config.emails.email.size()

sh "curl -XGET -g http://52.14.229.175:8080/job/jenkins/api/json?tree=builds[id,result,changeSets[items[authorEmail]]] -o username.json"
	def jsonSlurper = new JsonSlurper()
def reader = new BufferedReader(new InputStreamReader(new FileInputStream("/var/lib/jenkins/workspace/normalpro/username.json"),"UTF-8"))
def resultJson = jsonSlurper.parse(reader)
	def build=resultJson.builds[0].id


 


  List<String> USERS = new ArrayList<String>()
	List<String> USERF = new ArrayList<String>()
 List<String>  LISTSUCCESS=new ArrayList<String>()
	 List<String>  LISS=new ArrayList<String>()
	 List<String>  LISF=new ArrayList<String>()
	List<String> LISTFAILURE=new ArrayList<String>()
	List<String> SUCCESS = new ArrayList<String>()
    List<String> FAILURE = new ArrayList<String>()
	


 


	def jsonBuilder = new groovy.json.JsonBuilder()

   for(j=0;j<mailcount;j++)
   {
	   def cns=0
	   def cnf=0
    def email=jsonObj.config.emails.email[j] 
  for(i=0;i<build;i++)
  {
 
   
   def state=resultJson.builds[i].result
  
   if(resultJson.builds[i].changeSets.contains(email) && state.equals("Successful"))
   {
   
    USERS.add(resultJson.builds[i])
	  
   }
   else if(resultJson.builds[i].changeSets.contains(email) && state.equals("Failed"))
   {
	   
	   USERF.add(resultJson.builds[i])
   }
   }
   cns=USERS.size()

	
	   LISS[j]=USERS.clone()
	   LISF[j]=USERF.clone()
	   
   LISTSUCCESS.add(["email":email,"success":LISS[j],"Success_cnt":cns])
   USERS.clear()
	 
   cnf=USERF.size()
   LISTFAILURE.add(["email":email,"failure":LISF[j],"Failure_cnt":cnf])
   USERF.clear()
   }
	for(i=0;i<build;i++)
  {
   //def date=resultJson.results.result[i].buildCompletedDate
   def state=resultJson.builds[i].result

   
  if(state.equals("Successful"))
  {
   
 
   SUCCESS.add(resultJson.builds[i])
     
  }
   else if(state.equals("Failed"))
   {
    
       FAILURE.add(resultJson.builds[i])
     
   }
  }
	
		    jsonBuilder.Jenkins(
  "teamsuccess" : SUCCESS,
  "teamsuccessbuild_cnt" : SUCCESS.size(),
  "teamfailure" : FAILURE,
  "teamfailurebuild_cnt" :FAILURE.size(),
  "individualsuccess": LISTSUCCESS,
  "individualfailure": LISTFAILURE
  )
	
File file = new File("/var/lib/jenkins/workspace/${JOB_NAME}/jenkins.json")
file.write(jsonBuilder.toPrettyString())
	//def reader1 = new BufferedReader(new InputStreamReader(new FileInputStream("/var/lib/jenkins/workspace/${JOB_NAME}/bamboo.json"),"UTF-8"))
//def resu = jsonSlurper.parse(reader1)

	//println(resu.individualsuccess[2].Success_cnt)
				   //println(jsonBuilder)
	

}