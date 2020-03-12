import groovy.json.*
	

def call(jsondata,jenkins){
def jsonString = jsondata
def jsonObj = readJSON text: jsonString
int ecount = jsonObj.riglet_info.auth_users.size()
	def team=jsonObj.riglet_info.name
List<String> jsonStringa= new ArrayList<String>();
  //jsonStringa.add(github)
   jsonStringa.add(jenkins)
	//jsonStringa.add(sonar)
   List<String> LIST = new ArrayList<String>();
   for(m=0;m<ecount;m++){
	   def mail=jsonObj.riglet_info.auth_users[m]
   
   for(i=0;i<jsonStringa.size();i++)
  { 
    int score=0
    String name="  "
	  String metric=" "
/*if(jsonStringa[i].contains("GITHUB"))
    {
    name="github"
    def jsonObj1 = readJSON text: jsonStringa[i]
  def count =jsonObj1.GITHUB.commits_count
  LIST.add(["toolName":name,"metricName":"commits","value":count])
   }*/
	  
   if(jsonStringa[i].contains("JENKINS"))
    {
      name="jenkins"
      def jsonObjb = readJSON text: jsonStringa[i]
	    def indCountS=jsonObjb.JENKINS.individualsuccess.size()
	    for(k=0;k<indCountS;k++){
       def emailS=jsonObjb.JENKINS.individualsuccess[k].email
	     print(emailS)
       if(mail.equals(emailS)){
         def countS=jsonObjb.JENKINS.individualsuccess[k].Success_cnt
	    LIST.add(["metricName":"successful_builds","toolName":name,"value":countS])
       }
	    }
	    
	    def indCountF=jsonObjb.JENKINS.individualfailure.size()
	    for(k=0;k<indCountF;k++){
       def emailF=jsonObjb.JENKINS.individualfailure[k].email
	     print(emailF)
       if(mail.equals(emailF)){
         def countF=jsonObjb.JENKINS.individualfailure[k].Failure_cnt
	    LIST.add(["metricName":"failure_builds","toolName":name,"value":countF])
       }
	    }
	    
	    def indCount=jsonObjb.JENKINS.individualbuilds.size()
	    for(k=0;k<indCount;k++){
       def emailT=jsonObjb.JENKINS.individualbuilds[k].email
	     print(emailT)
       if(mail.equals(emailT)){
         def countT=jsonObjb.JENKINS.individualbuilds[k].Total_cnt
	    LIST.add(["metricName":"total_builds","toolName":name,"value":countT])
       }
	    }
    }
      /*if(jsonStringa[i].contains("Sonar"))
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
    }*/
     
def jsonBuilder = new groovy.json.JsonBuilder()

jsonBuilder(
"teamMemberName":mail,
 "teamName":team,
  "metrics" : LIST
  
) 
  
  File file = new File("/var/lib/jenkins/workspace/${JOB_NAME}/Teamscore.json")
  file.write(jsonBuilder.toPrettyString())	
	//def metrics1 = jsonBuilder.toString()
}
}
}
