import groovy.json.*

@NonCPS
create(String TeamName){
  def jsonBuilder = new groovy.json.JsonBuilder()
  def jsonSlurper = new JsonSlurper()
  def reader = new BufferedReader(new InputStreamReader(new FileInputStream("/var/lib/jenkins/workspace/${JOB_NAME}/jenkins.json"),"UTF-8"))
  def jsonObj = jsonSlurper.parse(reader)
  List<String> LIST = new ArrayList<String>();
  //def jsonObj = readJSON text: metrics
  print(TeamName)
  int score=0;
  def teamS=jsonObj.JENKINS.teamsuccessbuild_cnt
  if(teamS>10){
  score+=10;
  LIST.add(["metric":"Team Successfull Builds","Value":score,"Tool":"JENKINS"])
  }
  def teamF=jsonObj.JENKINS.teamfailurebuild_cnt
  if(teamF<5){
  score+=10;
  LIST.add(["metric":"Team Failure Builds","Value":score,"Tool":"JENKINS"])
  }
  
  
  jsonBuilder(
    "Teamname":TeamName,
    "Metrics" : LIST
    
    )
  File file = new File("/var/lib/jenkins/workspace/${JOB_NAME}/JenkinsScore.json")
  file.write(jsonBuilder.toPrettyString())
}


def call(jsondata){
def jsonString = jsondata
def jsonObj = readJSON text: jsonString

String a = jsonObj.riglet_info.name
String TeamName=a.replaceAll("\\[", "").replaceAll("\\]","");
  create(TeamName)
}
