import groovy.json.*
import groovy.json.JsonOutput
import groovy.json.JsonSlurper

	

def call(JSON)
{
def jsonString = JSON
def jsonObj = readJSON text: jsonString
def mailcount = jsonObj.config.emails.email.size()
	print(mailcount)

sh "curl -X GET -g http://52.14.229.175:8080/job/jenkins/api/json?tree=builds[id,result,changeSets[items[authorEmail]]] -u suneel:11035ac86f58bc32d03d8e873b7cc063a3 -o username.json"
	def jsonSlurper = new JsonSlurper()
def reader = new BufferedReader(new InputStreamReader(new FileInputStream("/var/lib/jenkins/workspace/${JOB_NAME}/username.json"),"UTF-8"))
def resultJson = jsonSlurper.parse(reader)
	def build=resultJson.builds[0].id
	print(build)


 


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
	   print(email)
  for(i=0;i<50;i++)
  {
 
   
   def state=resultJson.builds[i].result
   //def size=resultJson.builds[i].changeSets.items.size()
   //print(size)
  
   if(resultJson.builds[i].changeSets[0].items[0].authorEmail.equals(email) && state.equals("Successful"))
   {
   
    USERS.add(resultJson.builds[i])
	  
   }
   else if(resultJson.builds[i].changeSets[size-1].items[0].authorEmail.equals(email) && state.equals("Failed"))
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
	for(i=0;i<50;i++)
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
