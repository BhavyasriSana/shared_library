import groovy.json.*
import groovy.json.JsonSlurper 
//int ids1;

def call(jsondata){
      def jsonString = jsondata
      def jsonObj = readJSON text: jsonString
      int ecnt = jsonObj.riglet_info.auth_users.size()
         println("No of users "+ ecnt)
      String a=jsonObj.scm1.repositories.repository.repo_name[0]
String repoName=a.replaceAll("\\[", "").replaceAll("\\]","");

 println(repoName)
     //withCredentials([usernamePassword(credentialsId: 'github', passwordVariable: 'password', usernameVariable:'username')]) {
      sh "curl -X GET    -u SumaVarshitha:sumasuji268 https://api.github.com/repos/SumaVarshitha/${repoName}/commits -o commits.json"
    // }
   def jsonSlurper = new JsonSlurper()
 def reader = new BufferedReader(new InputStreamReader(new FileInputStream("/var/lib/jenkins/workspace/${JOB_NAME}/commits.json"),"UTF-8"))
def resultJson = jsonSlurper.parse(reader)
def totalcommits = resultJson.size()
      println(totalcommits)
	println(ecnt)
      println(JsonOutput.toJson(resultJson))
      List<String> JSON = new ArrayList<String>();
   	 List<String> COMMIT = new ArrayList<String>();
	 List<String> LIST = new ArrayList<String>();

	 def jsonBuilder = new groovy.json.JsonBuilder()
for(i=0;i<ecnt;i++)
 {
	def email=jsonObj.riglet_info.auth_users[i] 
  for(j=0;j<totalcommits;j++)
  {
	 // println(jsonObj.config.emails.email[i])
	 // println(resultJson[j].commit.author.email)
   if(email==resultJson[j].commit.author.email)
   {
	   JSON.add(resultJson[j])
     }
     }
	// println(jsonObj.config.emails.email[i])
	 cnt=JSON.size()
	 LIST[i]=JSON.clone()
	 COMMIT.add(["User_email":email,"User_Commits": LIST[i],"User_Commits_count":cnt])
	//LIST.add(["email":email,"Commit":JSON,"Commit_cnt":cnt])
	 //JCOPY[i]=(JsonOutput.toJson(JSON))
	// println(JCOPY[i])
	 JSON.clear()
	 
	   
     }
 /* for(i=0;i<JCOPY.size();i++)
	{
		println(JCOPY[i])
	}
    */
 jsonBuilder.GITHUB(
  "total_commits" : resultJson,
  "commits_count" : resultJson.size(),
	 "individual_commit_Details":COMMIT
  
  )
File file = new File("/var/lib/jenkins/workspace/${JOB_NAME}/output.json")
	file.write(jsonBuilder.toPrettyString())
return jsonBuilder
}
