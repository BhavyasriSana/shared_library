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
   for(m=0;m<ecount;m++){
	   def mail=jsonObj.riglet_info.auth_users[m]
   
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
	    def indCount=jsonObjb.JENKINS.individualsuccess.size()
	    for(k=0;k<indCount;k++){
       def email=jsonObjb.JENKINS.individualsuccess[k].email
	     print(email)
       if(mail.equals(email)){
         def countS=jsonObj.JENKINS.individualsuccess[k].Success_cnt
	   // print jsonObjb
      def total=jsonObjb.JENKINS.teambuild_cnt
  def scnt =jsonObjb.JENKINS.teamsuccessbuild_cnt
	    def fcnt=jsonObjb.JENKINS.teamfailurebuild_cnt
	    LIST.add(["toolName":name,"metricName":"total_builds","value":countS])
	    LIST.add(["toolName":name,"metricName":"successful_builds","value":scnt])
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
def jsonBuilder = new groovy.json.JsonBuilder()

jsonBuilder(
 "teamName":team,
  "metrics" : LIST
  
) 
  
  File file = new File("/var/lib/jenkins/workspace/${JOB_NAME}/Teamscore.json")
  file.write(jsonBuilder.toPrettyString())	
	//def metrics1 = jsonBuilder.toString()
}
}
