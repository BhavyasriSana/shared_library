 import groovy.json.*

@NonCPS
create(){
def jsonSlurper = new JsonSlurper()
def reader = new BufferedReader(new InputStreamReader(new FileInputStream("/var/lib/jenkins/workspace/normalpro/username.json"),"UTF-8"))
def resultJson = jsonSlurper.parse(reader)
    int countS=0;
   int countF=0;
    int total=resultJson.users[0].project.builds.length
   for(int i=0;i<total;i++){
      if(resultJson.users[0].project.builds[i].actions[0].causes[0].userName=="suneelpunnana"){
         countS++;
      }    
   }
    println("total number of builds by suneel:"+countS) 
}    
     

def call()
{
    sh "curl -XGET -g http://52.14.229.175:8080/asynchPeople/api/json?depth=3 -o username.json"
    create()
}
 
