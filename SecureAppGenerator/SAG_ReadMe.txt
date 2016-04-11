SAG_ReadMe.txt 

Enviornment Variables
=====================
SAG_ENV {dev, QA, Staging, Live}

S3_DOWNLOAD_BUCKET {qa-benetech-sag, staging-benetech-sag, benetech-sag, or Nothing which will use SAG_ENV}
GRADLE_HOME {where your gradle is}
ANDROID_HOME {where your Android SDK is}
AWS_KEY {Your key do not put in GitHub!}
AWS_SECRET {Your secret, Do not put in GitHub!}
FDROID_HOME {Where your FDroid is eg: /Users/charlesl/GitRepo/fdroidserver/}
INCLUDE_FDROID {true, false}

Alias (.bash_profile)
alias buildsag="gradle -p /Users/charlesl/EclipseMartus/Martus-Secure-App-Generator/SecureAppGenerator build"
alias cleansag="gradle -p /Users/charlesl/EclipseMartus/Martus-Secure-App-Generator/SecureAppGenerator clean"

------------
BUILDING SAG
============

To build a WAR file use the command above or set up an alias as I have done.
The WAR file will end up in SecureAppGenerator/built/libs/SAG-0.1.0.war
gradle.build is the build script to generate the WAR file

When you build from eclipse SecureApp preBuild.ant and postBuild.ant files gets called accordingly
which will bring in all the Android files needed from the martus-android project
NOTE: this is a relative class path so 

martus-android
SecureAppGenerator

both must be at the same directory level.
Also since minification of the Velum java script takes over 10 minutes to minify over 500 java script files that make up
Vellum that code has been commented out of the preBuild.ant file
So if you make changes to Velum you either have to uncomment out that code
or just run the minificaiton vellum script from the command line
ie: sh minify_Vellum.sh
NOTE: the un-minified Java script files can be found in 
SAG JavaScript files are
JavaScript/src
Vellum JavaScript files are
/thirdparty/Vellum/src

In both these directories there is a minified folder where the utility 
yuicompressor-2.4.7 puts the minified files.

So the minify_vellum.sh does two things
#1 compresses the java script and the results go in thirdparty/Vellum/minified
#2 copies these new minified files to 
/resources/static/Vellum

Then during the gradle build process these ultimately end up in 
/build/resources/main/static

----------
DEPLOYMENT
==========

When you push any changes to GitHub you must always bump the VERSION # located in
/src/main/java/appConfiguration.java
	private static final String VERSION = "SAG Beta 118";
 This will be displayed on the lower right corner of the cellphone 
 when you visit the webpage you you know which version of your files
 you are running on that instance of the Server.
 
QA 			https://sag-qa.benetech.org/
Staging 		https://sag-staging.benetech.org/
Production 	https://sag.benetech.org/

Once you push your changes then that will trigger a Jenkins build of the WAR file on the 
Jenkins Server.  You can see the status at
https://build.benetech.org/job/docker-martus-secure-app-generator/

Once that build is finished successfully you can push this to QA or Staging manuall
This can be done via this website
https://build.benetech.org/view/Deploy/job/Deploy-ECS/build?delay=0sec

Just select SecureAppGenerator and choose QA or Staging.
That will then generate the Docker container and push that to the corresponding server.
The Docker file configuration will be found in 
/DockerFile

Now to release to production first you must tag the version (See Ron for help on this)
Then ask Systems to Deploy the new Production version via a Jira Ticket

When you push to QA or Staging this may take some time to get deployed which is why updating 
the version # in AppConfiguration is critical to knowing when you can test your changges.

Also you need to update the Wiki so everyone knows which version is running on which server
and what changes that implys.
https://wiki.benetech.org/display/MTS/Secure+App+Release+Changelogs

------

LOGS
====

To see the logs you must have an AWS account and Ron will need to give you permission
to view the logs.
https://console.aws.amazon.com/cloudwatch/home?region=us-east-1#logs:

NOTE: logs are on the us-east-1 AWS cloud network.
Each log QA/Staging/Production/Dev is set using that environment SAG_ENV accordingly.

-------


 

