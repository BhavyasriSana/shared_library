import groovy.json.*
import groovy.json.JsonOutput
import groovy.json.JsonSlurper

	

def call(JSON)
{
def jsonString = JSON
def jsonObj = readJSON text: jsonString
def mailcount = jsonObj.config.emails.email.size()
	print(mailcount)

sh "curl -X GET -g http://18.224.172.87:8080/job/jenkins/api/json?tree=builds[id,result,changeSets[items[authorEmail]]] -u BhavyasriSana:1185dbd72329ba8cbfc4f9179ed0d48c7a -o username.json"
	def jsonSlurper = new JsonSlurper()
def reader = new BufferedReader(new InputStreamReader(new FileInputStream("/var/lib/jenkins/workspace/${JOB_NAME}/username.json"),"UTF-8"))
def resultJson = jsonSlurper.parse(reader)
	def build=resultJson.builds[0].id
	print(build)
	int value = Integer.parseInt(build);
	print(value)


 


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
  for(i=1;i<value-1;i++)
  {
 
   
   def state=resultJson.builds[i].result
	  print (state)
   //def s=resultJson.builds[i].changeSets.size()
	  //print (s)
	  //int len = s-1;
  
   if(resultJson.builds[i].changeSets.items.authorEmail.equals(email) && state.equals("SUCCESS"))
   {
   
    USERS.add(resultJson.builds[i])
	  
   }
   else if(resultJson.builds[i].changeSets.items.authorEmail.equals(email) && state.equals("FAILURE"))
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
	for(i=1;i<value;i++)
  {
   //def date=resultJson.results.result[i].buildCompletedDate
   def state=resultJson.builds[i].result
	  print(state)

   
  if(state.equals("SUCCESS"))
  {
   
 
   SUCCESS.add(resultJson.builds[i])
     
  }
   else if(state.equals("FAILURE"))
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
