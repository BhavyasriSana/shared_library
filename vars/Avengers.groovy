import groovy.json.*
def call(jsondata,github,jenkins,sonar){
def jsonString = jsondata
def jsonObj = readJSON text: jsonString
int ecount = jsonObj.riglet_info.auth_users.size()
	def team=jsonObj.riglet_info.name
List<String> jsonStringa= new ArrayList<String>();
  jsonStringa.add(github)
   jsonStringa.add(jenkins)
	jsonStringa.add(sonar)
   List<String> LIST = new ArrayList<String>();
   for(i=0;i<jsonStringa.size();i++)
  { 
    int score=0
    String name="  "
	  String metric=" "
if(jsonStringa[i].contains("GITHUB"))
    {
    name="github"
    def jsonObj1 = readJSON text: jsonStringa[i]
  def count =jsonObj1.GITHUB.commits_count
  LIST.add(["toolName":name,"metricName":"commits","value":count])
   }
   if(jsonStringa[i].contains("JENKINS"))
    {
      name="jenkins"
      def jsonObjb = readJSON text: jsonStringa[i]
	   // print jsonObjb
      def total=jsonObjb.JENKINS.teambuild_cnt
  def scnt =jsonObjb.JENKINS.teamsuccessbuild_cnt
	    def fcnt=jsonObjb.JENKINS.teamfailurebuild_cnt
	    LIST.add(["toolName":name,"metricName":"total_builds","value":total])
	    LIST.add(["toolName":name,"metricName":"success_builds","value":scnt])
	    LIST.add(["toolName":name,"metricName":"failure_builds","value":fcnt])
      }
      if(jsonStringa[i].contains("Sonar"))
    {
	    name="sonar"
	    def jsonObjc = readJSON text: jsonStringa[i]
	    //print jsonObjc
	    for(i=0;i<jsonObjc.Sonar.Metrics.component.measures.size();i++){
		    //print jsonObjc.Sonar.Metrics.component.measures
    		def sonar_metric=jsonObjc.Sonar.Metrics.component.measures[i].metric
		def d=jsonObjc.Sonar.Metrics.component.measures[i].value
    		double data = Double.parseDouble(d); 
       		LIST.add(["toolName":name,"metricName":sonar_metric,"value":data])
	    }
    }
     }
	//print(LIST)
def jsonBuilder = new groovy.json.JsonBuilder()

jsonBuilder(
 "teamName":team,
  "metrics" : LIST
  
) 
	return jsonBuilder
  
  File file = new File("/var/lib/jenkins/workspace/${JOB_NAME}/Teamscore.json")
  file.write(jsonBuilder.toPrettyString())
	
List<String> JSON = new ArrayList<String>();
  List<String> LIST2 = new ArrayList<String>();
  List<String> JSON1 = new ArrayList<String>();

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
	   def jsonObjj = readJSON text: jsonStringa[i]

  //println(jsonObja)
  def scnt =jsonObjj.JENKINS.individualsuccess[j].Success_cnt
  def fcnt =jsonObjj.JENKINS.individualfailure[j].Failure_cnt
  def tcnt =jsonObjj.JENKINS.individualbuilds[j].Total_cnt
 def email1=jsonObjj.JENKINS.individualsuccess[j].email
      
 if(email==email1)
  {
	   LIST2.add(["toolName":name,"metricName":"success_builds","value":scnt])
	    LIST2.add(["toolName":name,"metricName":"failure_builds","value":fcnt])
	  LIST2.add(["toolName":name,"metricName":"total_builds","value":tcnt])
	  
  }
   }
	   
	  if(jsonStringa[i].contains("GITHUB"))
      {
        name="gitlab"
        def jsonObjg= readJSON text: jsonStringa[i]
  //println(jsonObj)
  def cnt =jsonObjg.GITHUB.individual_commit_Details[j].User_Commits_count
	       def email1=jsonObjg.GITHUB.individual_commit_Details[j].User_email
	       if(email==email1)
  {
    LIST2.add(["toolName":name,"metricName":"commits","value":cnt])
  }
      }
    }
	   JSON1[j]=LIST2.clone()
	   
   JSON.add(["teamMemberName":email,"teamName":team,"metrics":JSON1[j]])
    LIST2.clear()
	//reward=0    
	   
    }
	
     def jsonBuilder = new groovy.json.JsonBuilder()

jsonBuilder(
   JSON
  
) 
  
  File file = new File("/var/lib/jenkins/workspace/${JOB_NAME}/game.json")
file.write(jsonBuilder.toPrettyString())
	
}
