import groovy.json.*
import groovy.json.JsonOutput
//import groovy.json.JsonSlurper
def call(JSON,rig)
{
def jsonString = JSON
def jsonObj = readJSON text: jsonString
def mailcount = jsonObj.riglet_info.auth_users.size()
	String pro = jsonObj.ci.jobs.job.job_name
	String ProjectName=pro.replaceAll("\\[", "").replaceAll("\\]","");
	
	def jsonObja = readJSON text: rig
	def IP=jsonObja.url
	def user=jsonObja.userName
	def pass=jsonObja.password
	def response = sh(script: """curl  -X GET -L -w '%{http_code}\\n' -u ${user}:${pass} '${IP}/job/${ProjectName}/api/json?tree=builds[id,result,changeSets[items[authorEmail]]]' -o outputjenkins.json """, returnStdout: true)
	println(response) 
	//sh "curl -X GET -g ${IP}/job/${ProjectName}/api/json?tree=builds[id,result,changeSets[items[authorEmail]]] -u ${user}:${pass} -o username.json"
	try
	{
	def resultJson = readJSON file: 'outputjenkins.json'
	def build=resultJson.builds[0].id
	//print(build)
	int val = Integer.parseInt(build);
	int value=val-1
		
  List<String> USERS = new ArrayList<String>()
	List<String> USERT = new ArrayList<String>()
	List<String> USERF = new ArrayList<String>()
 List<String>  LISTSUCCESS=new ArrayList<String>()
	List<String>  LISTTOTAL=new ArrayList<String>()
	 List<String>  LISS=new ArrayList<String>()
	 List<String>  LISF=new ArrayList<String>()
	List<String>  LIST=new ArrayList<String>()
	List<String> LISTFAILURE=new ArrayList<String>()
	List<String> SUCCESS = new ArrayList<String>()
    List<String> FAILURE = new ArrayList<String>()
	
	def jsonBuilder = new groovy.json.JsonBuilder()

   for(j=0;j<mailcount;j++)
   {
	   def cns=0
	   def cnf=0
	   def cnt=0
    def email=jsonObj.riglet_info.auth_users[j]
	   //print(email)
  for(i=1;i<value-1;i++)
  {
 
   
   def state=resultJson.builds[i].result
	  //print (state)
   def s=resultJson.builds[i].changeSets.size()
	  //print (s)
	  if (s>0){
		  if(resultJson.builds[i].changeSets[s-1].items[0].authorEmail.equals(email) && state.equals("SUCCESS"))
		  print("insidejjjjjjjjjjjjjjjjjjjjjjjjjjjjj")
   {
   
    USERS.add(resultJson.builds[i])
	   //print("insidejjjjjjjjjjjjjjjjjjjjjjjjjjjjj8888888888888888888888888888")
	  
   }
   else if(resultJson.builds[i].changeSets[s-1].items[0].authorEmail.equals(email) && state.equals("FAILURE"))
   {
	   
	   USERF.add(resultJson.builds[i])
   }
  if(resultJson.builds[i].changeSets[s-1].items[0].authorEmail.equals(email))
   {
	   
	   USERT.add(resultJson.builds[i])
	   //print("insidejjjjjjjjjjjjjjjjjjjjjjjjjjjjj8888888888888888888888888888")
	   
   }
		  	  
	  }
	  
   }
   cns=USERS.size()
	   cnt=USERT.size()
	   

	   LIST[j]=USERT.clone()
	   LISS[j]=USERS.clone()
	   LISF[j]=USERF.clone()
	   
   LISTTOTAL.add(["email":email,"Total":LIST[j],"Total_cnt":cnt])
   USERT.clear()
	   
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
	  //print(state)

   
  if(state.equals("SUCCESS"))
  {
   
 
   SUCCESS.add(resultJson.builds[i])
     
  }
   else if(state.equals("FAILURE"))
   {
    
       FAILURE.add(resultJson.builds[i])
     
   }
  }
	
		    jsonBuilder.JENKINS(
  
  "teambuild_cnt" : value,
  "teambuilds" : resultJson,		    
  "teamsuccess" : SUCCESS,
  "teamsuccessbuild_cnt" : SUCCESS.size(),
  "teamfailure" : FAILURE,
  "teamfailurebuild_cnt" :FAILURE.size(),
  "individualsuccess": LISTSUCCESS,
  "individualfailure": LISTFAILURE,
  "individualbuilds": LISTTOTAL
			    
  )
	
File file = new File("/var/lib/jenkins/workspace/${JOB_NAME}/jenkins.json")
file.write(jsonBuilder.toPrettyString())
	return jsonBuilder
	//def reader1 = new BufferedReader(new InputStreamReader(new FileInputStream("/var/lib/jenkins/workspace/${JOB_NAME}/bamboo.json"),"UTF-8"))
//def resu = jsonSlurper.parse(reader1)

	//println(resu.individualsuccess[2].Success_cnt)
				   //println(jsonBuilder)
	

}
	catch(Exception e)
{
	e.printStackTrace()
	
}
	 finally{
		
		if(response.contains("200"))
		println("data collected scuccesslfully")	
	if(response.contains("404"))
	println("Not found")
	if(response.contains("400"))
	println("Bad Request")
        if(response.contains("401"))
	println("Unauthorized")
	if(response.contains("403"))
		println("Forbidden")
	if(response.contains("500"))
		println("Internal Server Error")
		 }	
}
